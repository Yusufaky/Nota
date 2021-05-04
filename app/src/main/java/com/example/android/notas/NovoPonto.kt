package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.Pontos
import com.example.android.notas.api.ServiceBuilder
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NovoPonto : AppCompatActivity() {
    private lateinit var nome: EditText

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback
    private lateinit var locationRequest : LocationRequest
    private lateinit var lastLocation : Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_ponto)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                //Log.d("pontos", "coordenadas" + loc.latitude + "- " + loc.longitude)
            }
        }
        createLocationRequest()



        nome = findViewById<EditText>(R.id.pontoproblema)
        var tipo = 0
        val tipoacidente = findViewById<View>(R.id.AcidenteN) as RadioButton
        val tipoobras = findViewById<View>(R.id.obrasN) as RadioButton
        tipoacidente.setOnClickListener{
            tipo=2
            Toast.makeText(this, R.string.acidentes, Toast.LENGTH_SHORT).show()
        }
        tipoobras.setOnClickListener{
            tipo=1
            Toast.makeText(this, R.string.obras, Toast.LENGTH_SHORT).show()
        }


        val user_id = sharedPref.all[getString(R.string.user_id)].toString()


        val button = findViewById<Button>(R.id.Novoponto)
        button.setOnClickListener {
            if (TextUtils.isEmpty(nome.text) || tipo == 0) {
                Toast.makeText(this@NovoPonto, getString(R.string.Campos_vazios), Toast.LENGTH_SHORT).show()
            } else {
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                Log.d("dados", nome.text.toString() + lastLocation.latitude.toString() + lastLocation.longitude.toString() + "----" + user_id + "----" + tipo)
                val call = request.postNovoPonto(nome.text.toString(), lastLocation.latitude, lastLocation.longitude, user_id.toInt(), tipo)

                call.enqueue(object : Callback<Pontos> {
                    override fun onResponse(call: Call<Pontos>, response: Response<Pontos>) {

                        if (response.isSuccessful) {
                            val c: Pontos = response.body()!!

                            val intent = Intent(this@NovoPonto, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Pontos>, t: Throwable) {
                        Toast.makeText(this@NovoPonto, getString(R.string.fail), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NovoPonto, MapsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                })


            }
        }
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }




    private  fun createLocationRequest(){
        locationRequest = LocationRequest()

        locationRequest.interval=1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }



    public override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }




}