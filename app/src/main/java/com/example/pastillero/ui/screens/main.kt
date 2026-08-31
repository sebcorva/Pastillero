package com.example.pastillero.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pastillero.data.Medicamento
import com.example.pastillero.data.MomentoDia
import com.example.pastillero.ui.viewmodel.AppViewModelFactory
import com.example.pastillero.ui.viewmodel.MainViewModel
import java.time.LocalTime

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
            item { 
                SeccionMomento(
                    momento = MomentoDia.MANANA, 
                    medicamentos = listaMedicamentos,
                    onDelete = { med -> viewModel.eliminarMedicamento(med) }
                ) 
            }
            item { 
                SeccionMomento(
                    momento = MomentoDia.TARDE, 
                    medicamentos = listaMedicamentos,
                    onDelete = { med -> viewModel.eliminarMedicamento(med) }
                ) 
            }
            item { 
                SeccionMomento(
                    momento = MomentoDia.NOCHE, 
                    medicamentos = listaMedicamentos,
                    onDelete = { med -> viewModel.eliminarMedicamento(med) }
                ) 
            }
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                FormularioMedicamento(
                    onGuardar = { nuevosMed ->
                        viewModel.agregarMedicamentos(nuevosMed) {
                            showSheet = false
                            Toast.makeText(context, "Guardado con éxito", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMedicamento(onGuardar: (List<Medicamento>) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var momentosSeleccionados by remember { mutableStateOf(setOf(MomentoDia.MANANA)) }
    
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

        // Segmented Buttons para Momento del Día (Multiselección)
        MultiChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().height(64.dp)) {
            MomentoDia.entries.forEachIndexed { index, momento ->
                SegmentedButton(
                    checked = momentosSeleccionados.contains(momento),
                    onCheckedChange = { isChecked ->
                        momentosSeleccionados = if (isChecked) {
                            momentosSeleccionados + momento
                        } else {
                            momentosSeleccionados - momento
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = MomentoDia.entries.size),
                    modifier = Modifier.fillMaxHeight()
                ) {
                    Text(
                        momento.etiqueta,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
                    "Dosis (ej: 500mg, 1 tableta)", 
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
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            MomentoDia.entries.forEach { momento ->
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
                    val listaNuevos = momentosSeleccionados.map { momento ->
                        val state = when(momento) {
                            MomentoDia.MANANA -> morningTimeState
                            MomentoDia.TARDE -> afternoonTimeState
                            MomentoDia.NOCHE -> nightTimeState
                        }
                        Medicamento(
                            nombre = nombre,
                            dosis = dosis,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SeccionMomento(
    momento: MomentoDia, 
    medicamentos: List<Medicamento>,
    onDelete: (Medicamento) -> Unit
) {
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
                    onClick = { /* Próximamente: Lógica de audio */ },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Escuchar medicamentos",
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
                        headlineContent = { 
                            Text(
                                med.nombre, 
                                style = MaterialTheme.typography.bodyLarge
                            ) 
                        },
                        supportingContent = { 
                            Text(
                                med.dosis, 
                                style = MaterialTheme.typography.bodyLarge
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
