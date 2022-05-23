package com.example.famaapp

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.famaapp.data.database.AppDatabase
import com.example.famaapp.model.Clientes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import kotlinx.android.synthetic.main.activity_nuevos_clientes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.*


class NuevosClientesActivity : AppCompatActivity() {
     lateinit var lacationRequest: com.google.android.gms.location.LocationRequest
     lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevos_clientes)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        btn_ubicacion.setOnClickListener {
            permisosUbicacion()
        }

        var idClientes: Int? = null

        if (intent.hasExtra("clientes")) {
            val clientes = intent.extras?.getSerializable("clientes") as Clientes

            ed_nombre.setText(clientes.nombre)
            ed_departamento.setText(clientes.departamentoNegocio)
            ed_municipio.setText(clientes.municipioNegocio)
            ed_barrio.setText(clientes.barrioNegocio)
            ed_departamento_dom.setText(clientes.departamentoDomicilio)
            ed_municipio.setText(clientes.municipioDomicilio)
            ed_barrio_dom.setText(clientes.barrioDomicilio)
            ed_iden.setText(clientes.identeficacion).toString()
            ed_estado.setText(clientes.estado)
            idClientes = clientes.idClientes
        }

        val database = AppDatabase.getDatabase(this)

        btn_guardar.setOnClickListener {
            val nombre = ed_nombre.text.toString()
            val departamentoNegocio = ed_departamento.text.toString()
            val municipioNegocio = ed_municipio.text.toString()
            val barrioNegocio = ed_barrio.text.toString()
            val departamentoDomicilio = ed_departamento_dom.text.toString()
            val municipioDomicilio = ed_municipio_dom.text.toString()
            val barrioDomicilio = ed_barrio_dom.text.toString()
            val identeficacion = ed_iden.text.toString()
            val estado = ed_estado.text.toString()
            val numero = ed_numero.text.toString()
            val descripcionDireccion = tv_ubicacion.text.toString()

            val clientes = Clientes(
                nombre,
                departamentoNegocio,
                municipioNegocio,
                barrioNegocio,
                departamentoDomicilio,
                municipioDomicilio,
                barrioDomicilio,
                numero,
                descripcionDireccion,
                edad = null,
                identeficacion,
                estado,
                )

            if (idClientes != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    clientes.idClientes = idClientes
                    database.clientes().update(clientes)
                    this@NuevosClientesActivity.finish()
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    database.clientes().insertAll(clientes)
                    this@NuevosClientesActivity.finish()
                }
            }
        }
    }

    private fun permisosUbicacion() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            gps()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 777)
        }
    }

    private fun gps() {
        lacationRequest = create()
        lacationRequest.priority = PRIORITY_HIGH_ACCURACY
        lacationRequest.interval = 5000
        lacationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(lacationRequest)

        builder.setAlwaysShow(true)

        val result = LocationServices.getSettingsClient(this.applicationContext).checkLocationSettings(builder.build())

        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                getClientLocation()

            }catch (e:ApiException){
                e.printStackTrace()
                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this, 200)
                    }catch (sendIntenteException:IntentSender.SendIntentException){
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }

            }
        }
    }

    private fun getClientLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            val location = task.getResult()
            if (location != null){
                try {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val address = geocoder.getFromLocation(location.latitude,location.longitude, 1)

                    val address_line = address[0].getAddressLine(0)
                    tv_ubicacion.setText(address_line)
                    val address_location = address[0].getAddressLine(0)
                    
                    openLocation(address_location.toString())

                }catch (e:IOException){

                }
            }
        }
    }

    private fun openLocation(location: String) {
        tv_ubicacion.setOnClickListener {
            if (!tv_ubicacion.text.isEmpty()){
                val uri = Uri.parse("geo:0, 0?q=$location")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }

        }
    }
}
