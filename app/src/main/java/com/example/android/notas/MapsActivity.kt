package com.example.android.notas

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.Pontos
import com.example.android.notas.api.ServiceBuilder
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var anomalia: List<Pontos>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var lastLocation: Location
    private val LOCATION_PERMISSION_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                Log.d("pontos", "coordenadas" + loc.latitude + "- " + loc.longitude)
            }
        }
        createLocationRequest()

        val fabponto = findViewById<FloatingActionButton>(R.id.btnAdd)
        fabponto.setOnClickListener {

            val intent = Intent(this, NovoPonto::class.java)
            startActivity(intent)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getPontos()
        var posicao: LatLng
        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )

        //verificar se os utilizadores sao os mesmo para ver se a cor dos pontos e igual
        call.enqueue(object : Callback<List<Pontos>> {
            override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                if (response.isSuccessful) {
                    anomalia = response.body()!!
                    for (anomalia1 in anomalia) {
                        posicao = LatLng(anomalia1.latitude.toDouble(), anomalia1.longitude.toDouble())

                        if (anomalia1.user_id == sharedPref.all[getString(R.string.user_id)]) {

                            mMap.addMarker(MarkerOptions()
                                    .position(posicao)
                                    .title(anomalia1.nome)
                                    .snippet("Descrição: " + anomalia1.nome)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                            )
                        } else {
                            mMap.addMarker(
                                    MarkerOptions()
                                            .position(posicao)
                                            .title(anomalia1.nome)
                                            .snippet("Descrição: " + anomalia1.nome)
                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))

                            )
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(posicao))
                    }
                }
            }

            override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, R.string.Error_Error, Toast.LENGTH_SHORT).show()
            }
        })
        val zoomLevel = 15f
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return
            }
            return
        }
        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currenteLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currenteLatLng, zoomLevel))
            }
        }
    }

    fun sair(view: View) {

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putBoolean(getString(R.string.loginshared), false)
            putString(getString(R.string.novoEmail), "")
            commit()
        }
        var intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show()
    }

    private  fun createLocationRequest(){
        locationRequest = LocationRequest()

        locationRequest.interval=1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

}