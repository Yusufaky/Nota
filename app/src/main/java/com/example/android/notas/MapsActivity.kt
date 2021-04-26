package com.example.android.notas

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.Pontos
import com.example.android.notas.api.ServiceBuilder
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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var anomalia: List<Pontos>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getPontos()
        var position: LatLng
        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )

        //verificar se os utilizadores sao os mesmo para ver se a cor dos pontos e igual
        call.enqueue(object : Callback<List<Pontos>>{
            override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                if (response.isSuccessful){
                    anomalia = response.body()!!
                    for(anomalia1 in anomalia){
                        position = LatLng(anomalia1.latitude.toDouble(), anomalia1.longitude.toDouble())

                        if (anomalia1.user_id == sharedPref.all[getString(R.string.user_id)]){

                            mMap.addMarker(MarkerOptions()
                                    .position(position)
                                    .title(anomalia1.nome)
                                    .snippet(anomalia1.descricao + " " + anomalia1.foto + " " + anomalia1.user_id + " " + sharedPref.all[getString(R.string.user_id)].toString())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))

                            )
                        }else {
                            mMap.addMarker(
                                    MarkerOptions()
                                            .position(position)
                                            .title(anomalia1.nome)
                                            .snippet(anomalia1.descricao + " " + anomalia1.foto + " " + anomalia1.user_id.toString())
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })


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

        val zone = LatLng(41.637682, -8.697163)
        val zoomLevel = 15f

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zone, zoomLevel))
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
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        Toast.makeText(this, R.string.logout, Toast.LENGTH_SHORT).show()
    }

}