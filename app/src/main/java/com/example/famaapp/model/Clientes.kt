package com.example.famaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
/**
 * Created by Yamil Vogl on 20/5/2022
 */
@Entity(tableName = "clientes")
class Clientes(
    val nombre:String?,
    val departamentoNegocio: String?,
    val municipioNegocio: String?,
    val barrioNegocio: String?,
    val departamentoDomicilio: String?,
    val municipioDomicilio: String?,
    val barrioDomicilio: String?,
    val numero: String?,
    val descripcionDireccion: String?,
    val edad: String?,
    val estado: String?,
    val identeficacion: String?,
    @PrimaryKey(autoGenerate = true)
    var idClientes: Int = 0,
) : Serializable