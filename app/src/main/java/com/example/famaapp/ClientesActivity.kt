package com.example.famaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.famaapp.data.database.AppDatabase
import com.example.famaapp.model.Clientes
import kotlinx.android.synthetic.main.activity_clientes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientesActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private lateinit var clientes: Clientes
    private lateinit var clientesLiveData: LiveData<Clientes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        database = AppDatabase.getDatabase(this)

        val idClientes = intent.getIntExtra("id", 0)

        clientesLiveData = database.clientes().get(idClientes)

        clientesLiveData.observe(this, Observer {
            clientes = it

            tv_nombreCom.text = clientes.nombre
            tv_departamento_nego.text = clientes.departamentoNegocio
            tv_municipio_nego.text = clientes.municipioNegocio
            tv_barrio_nego.text = clientes.barrioNegocio
            tv_departamento_domi.text = clientes.departamentoDomicilio
            tv_municipio_domi.text = clientes.municipioDomicilio
            tv_barrio_domi.text = clientes.barrioDomicilio
            tv_direccion.text = clientes.descripcionDireccion
            tv_numero.text = clientes.numero
            tv_edad.text = clientes.edad
            tv_estado.text = clientes.estado
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_item -> {
                clientesLiveData.removeObservers(this)
                CoroutineScope(Dispatchers.IO).launch {
                    database.clientes().delete(clientes)
                    this@ClientesActivity.finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
