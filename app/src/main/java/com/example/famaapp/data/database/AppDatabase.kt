package com.example.famaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.famaapp.model.Clientes
import com.example.famaapp.ClientesDao

/**
 * Created by Yamil Vogl on 20/5/2022
 */
@Database(entities = [Clientes::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clientes(): ClientesDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "clientes_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}