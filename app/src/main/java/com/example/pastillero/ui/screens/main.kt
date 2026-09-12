package com.example.pastillero.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pastillero.data.FormatoMedicamento
import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.MomentoDia
import com.example.pastillero.ui.viewmodel.AppViewModelFactory
import com.example.pastillero.ui.viewmodel.MainViewModel
import java.time.LocalTime

/**
 * Pantalla principal del pastillero ("MainScreen").
 *
 * Muestra el listado de medicamentos organizados por momentos del día (Mañana, Tarde, Noche),
 * permite registrar la toma de remedios, eliminar registros y lanzar pruebas de vibración/alarma.
 *
 * @param onLogout Callback que se ejecuta cuando el usuario presiona el botón de cerrar sesión.
 * @param viewModel Instancia del [MainViewModel] encargada de gestionar el estado reactivo y operaciones de base de datos.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit = {},
    viewModel: MainViewModel = viewModel(factory = AppViewModelFactory(LocalContext.current))
) {
    val context = LocalContext.current
    val listaMedicamentos by viewModel.listaMedicamentos.collectAsState()

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    // Solicitar permiso de notificaciones automáticamente en Android 13+ (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { }
        )
        LaunchedEffect(Unit) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Uso de Array (arrayOf)
    val momentosArray = arrayOf(
        MomentoDia.MANANA,
        MomentoDia.TARDE,
        MomentoDia.NOCHE
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Mi Pastillero", 
                        style = MaterialTheme.typography.headlineMedium 
                    ) 
                },
                actions = {
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PowerSettingsNew,
                            contentDescription = "Cerrar Sesión",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            // Función Lambda Callback del botón '+': Activa el estado 'showSheet = true' para abrir el modal del formulario
            LargeFloatingActionButton(onClick = { showSheet = true }) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Agregar",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            momentosArray.forEach { momento ->
                item { 
                    SeccionMomento(
                        momento = momento, 
                        medicamentos = listaMedicamentos,
                        onToggleTomado = { med -> viewModel.alternarEstadoTomado(med) },
                        onProbarAlarma = { med ->
                            Toast.makeText(context, "Esto es una vibración de prueba", Toast.LENGTH_SHORT).show()
                            viewModel.probarNotificacionYVibracion(context, med)
                        },
                        onDelete = { med -> viewModel.eliminarMedicamento(context, med) }
                    ) 
                }
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                FormularioMedicamento(
                    onGuardar = { nuevosMed ->
                        viewModel.agregarMedicamentos(context, nuevosMed) {
                            showSheet = false
                            Toast.makeText(context, "Guardado con éxito", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

/**
 * Componente modal (Bottom Sheet) que despliega el formulario para agregar nuevos medicamentos.
 *
 * Permite seleccionar los momentos del día (multiselección), el formato de medicamento (Pastilla, Jarabe, etc.),
 * ingresar el nombre, la dosis y configurar las horas exactas mediante un selector numérico.
 *
 * @param onGuardar Callback que recibe la lista de instancias de [Medicamento] generadas para guardarlas en la base de datos.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMedicamento(onGuardar: (List<Medicamento>) -> Unit) {
    // Uso de Array (arrayOf) para definir los momentos del día
    val momentosArray = arrayOf(
        MomentoDia.MANANA,
        MomentoDia.TARDE,
        MomentoDia.NOCHE
    )

    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var formatoSeleccionado by remember { mutableStateOf(FormatoMedicamento.PASTILLA) }
    // Usamos setOf inicializado con el primer elemento del arreglo momentosArray
    var momentosSeleccionados by remember { mutableStateOf(setOf(momentosArray[0])) }
    
    // Estados independientes para cada momento
    val morningTimeState = rememberTimePickerState(initialHour = 8, is24Hour = false)
    val afternoonTimeState = rememberTimePickerState(initialHour = 14, is24Hour = false)
    val nightTimeState = rememberTimePickerState(initialHour = 20, is24Hour = false)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Agregar Medicamento", 
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        // Segmented Buttons para Momento del Día (Multiselección usando momentosArray)
        Text(
            "Momento del día:", 
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().height(64.dp)) {
            momentosArray.forEachIndexed { index, momento ->
                SegmentedButton(
                    checked = momentosSeleccionados.contains(momento),
                    onCheckedChange = { isChecked ->
                        momentosSeleccionados = if (isChecked) {
                            momentosSeleccionados + momento
                        } else {
                            momentosSeleccionados - momento
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = momentosArray.size),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        momento.etiqueta,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Segmented Buttons para Formato del Medicamento (Selección única)
        Text(
            "Tipo de medicamento:", 
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().height(64.dp)) {
            FormatoMedicamento.entries.forEachIndexed { index, formato ->
                SegmentedButton(
                    selected = formatoSeleccionado == formato,
                    onClick = { formatoSeleccionado = formato },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = FormatoMedicamento.entries.size),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        formato.etiqueta,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { 
                Text(
                    "Nombre del medicamento", 
                    style = MaterialTheme.typography.bodyLarge 
                ) 
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = dosis,
            onValueChange = { dosis = it },
            label = { 
                Text(
                    "Cantidad o Dosis (ej: 1, 500, 10)", 
                    style = MaterialTheme.typography.bodyLarge 
                ) 
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (momentosSeleccionados.isNotEmpty()) {
            Text(
                "Configura las horas:", 
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(16.dp))

            momentosArray.forEach { momento ->
                if (momentosSeleccionados.contains(momento)) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                momento.etiqueta, 
                                style = MaterialTheme.typography.bodyLarge, 
                                color = MaterialTheme.colorScheme.primary
                            )
                            val state = when(momento) {
                                MomentoDia.MANANA -> morningTimeState
                                MomentoDia.TARDE -> afternoonTimeState
                                MomentoDia.NOCHE -> nightTimeState
                            }
                            TimeInput(state = state)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (nombre.isNotEmpty() && momentosSeleccionados.isNotEmpty()) {
                    // Operación de colección .map: Transforma cada horario seleccionado del Set en un nuevo objeto 'Medicamento'
                    val listaNuevos = momentosSeleccionados.map { momento ->
                        val state = when(momento) {
                            MomentoDia.MANANA -> morningTimeState
                            MomentoDia.TARDE -> afternoonTimeState
                            MomentoDia.NOCHE -> nightTimeState
                        }
                        Medicamento(
                            nombre = nombre,
                            dosis = dosis,
                            formato = formatoSeleccionado,
                            momentoDia = momento,
                            horaExacta = LocalTime.of(state.hour, state.minute)
                        )
                    }
                    onGuardar(listaNuevos)
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp)
        ) {
            Text(
                "Guardar Medicamento",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Tarjeta contenedora que agrupa y despliega los medicamentos filtrados para un [MomentoDia] específico.
 *
 * @param momento El bloque horario a representar ([MomentoDia.MANANA], [MomentoDia.TARDE] o [MomentoDia.NOCHE]).
 * @param medicamentos Lista completa de medicamentos traídos desde el estado del ViewModel.
 * @param onToggleTomado Callback para conmutar el estado de confirmación visual (tomado / no tomado).
 * @param onProbarAlarma Callback para probar manualmente la notificación visible y el patrón de vibración.
 * @param onDelete Callback para eliminar un medicamento específico de la base de datos.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeccionMomento(
    momento: MomentoDia, 
    medicamentos: List<Medicamento>,
    onToggleTomado: (Medicamento) -> Unit,
    onProbarAlarma: (Medicamento?) -> Unit,
    onDelete: (Medicamento) -> Unit
) {
    // Operación de colección .filter: Filtra la lista completa conservando solo los medicamentos del momento actual
    val filtrados = medicamentos.filter { it.momentoDia == momento }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = momento.etiqueta,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = { onProbarAlarma(filtrados.firstOrNull()) },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Vibration,
                        contentDescription = "Probar vibración",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (filtrados.isEmpty()) {
                Text(
                    "No hay medicamentos programados", 
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                filtrados.forEach { med ->
                    ListItem(
                        leadingContent = {
                            // Función Lambda Callback del check de tomado: Notifica el cambio de estado pasando la entidad 'med' seleccionada
                            IconButton(
                                onClick = { onToggleTomado(med) },
                                modifier = Modifier.size(48.dp)
                            ) {
                                if (med.tomado) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Marcar como no tomado",
                                        modifier = Modifier.size(36.dp),
                                        tint = Color(0xFF2E7D32)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        contentDescription = "Marcar como tomado",
                                        modifier = Modifier.size(36.dp),
                                        tint = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        },
                        headlineContent = { 
                            Text(
                                med.nombre, 
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (med.tomado) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                            ) 
                        },
                        supportingContent = { 
                            Text(
                                med.obtenerInstruccionDosis(), 
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (med.tomado) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        },
                        trailingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    med.obtenerHoraFormateada(),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                IconButton(
                                    onClick = { onDelete(med) },
                                    modifier = Modifier.size(64.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    )
                    if (filtrados.last() != med) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}
