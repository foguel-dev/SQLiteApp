package com.example.famaapp.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.famaapp.R
import com.example.famaapp.model.Clientes
import kotlinx.android.synthetic.main.item_clientes.view.*

/**
 * Created by Yamil Vogl on 21/5/2022
 */

class Adapter (private val mContext: Context, private val listaClientes:List<Clientes>):ArrayAdapter<Clientes>(mContext,0,listaClientes){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContext).inflate(R.layout.item_clientes, parent, false)

        val clientes = listaClientes[position]

        val firstLetter = clientes.nombre!!.substring( 0 , 1 ).toUpperCase()
        layout.tv_avatar.text = firstLetter

        layout.tv_nombre_item.text = clientes.nombre
        layout.tv_departamento_item.text = clientes.departamentoNegocio
        layout.tv_municipio_item.text = clientes.municipioNegocio
        layout.tv_barrio_item.text = clientes.barrioNegocio
        layout.tv_direccion_item.text = clientes.descripcionDireccion
        layout.tv_numero_item.text = clientes.numero

        return layout
    }
}