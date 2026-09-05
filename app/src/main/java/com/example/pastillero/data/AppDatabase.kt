package com.example.pastillero.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pastillero.utils.PasswordHasher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Medicamento::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicamentoDao(): MedicamentoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pastillero_database"
                )
                .addCallback(AppDatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        //Uso de override dentro de la funcion onCreate de la clase 'RoomDatabase.Callback'
        private class AppDatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val userDao = database.userDao()
                        //Usamos listOf ya que creamos una coleccion inmutable y ordenada de los cinco usuarios iniciales con contraseñas encriptadas con SHA-256
                        val passwordEncriptada = PasswordHasher.hash("123")
                        val usuariosIniciales = listOf(
                            User(nombre = "Juan Pérez", email = "juan@gmail.com", password = passwordEncriptada),
                            User(nombre = "María López", email = "maria@gmail.com", password = passwordEncriptada),
                            User(nombre = "Carlos Gómez", email = "carlos@gmail.com", password = passwordEncriptada),
                            User(nombre = "Ana Torres", email = "ana@gmail.com", password = passwordEncriptada),
                            User(nombre = "Pedro Silva", email = "pedro@gmail.com", password = passwordEncriptada)
                        )
                        //Bucle forEach para la insercion de usuarios iniciales
                        usuariosIniciales.forEach{
                            userDao.insertUser(it)
                        }
                    }
                }
            }
        }
    }
}
