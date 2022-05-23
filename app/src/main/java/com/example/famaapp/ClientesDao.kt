package com.example.famaapp

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.famaapp.model.Clientes

/**
 * Created by Yamil Vogl on 22/5/2022
 */
@Dao
interface ClientesDao {
    @Query("SELECT * FROM clientes")
    fun getAll(): LiveData<List<Clientes>>
    @Query("SELECT * FROM clientes WHERE idClientes = :id")
    fun get(id: Int): LiveData<Clientes>
    @Insert
    fun insertAll(vararg clientes: Clientes)
    @Update
    fun update(clientes: Clientes)
    @Delete
    fun delete(clientes: Clientes)
}