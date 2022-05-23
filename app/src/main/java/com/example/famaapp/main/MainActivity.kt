package com.example.famaapp.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.famaapp.*
import com.example.famaapp.data.database.AppDatabase
import com.example.famaapp.model.Clientes
import com.example.famaapp.ui.view.Adapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var listaClientes = emptyList<Clientes>()
        val database = AppDatabase.getDatabase(this)

        database.clientes().getAll().observe(this, Observer {
            listaClientes = it
            val adapter = Adapter(this, listaClientes)
            listView.adapter = adapter
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, ClientesActivity::class.java)
            intent.putExtra("id", listaClientes[position].idClientes)
            startActivity(intent)
        }

        floating_guardar.setOnClickListener {
            val intent = Intent(this, NuevosClientesActivity::class.java)
            startActivity(intent)
        }
    }
}
