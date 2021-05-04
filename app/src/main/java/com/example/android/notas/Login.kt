package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.OutputPost
import com.example.android.notas.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var prox: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        prox = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )
        if (sharedPref != null) {
            if (sharedPref.all[getString(R.string.loginshared)] == true) {
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.nomeEdit)
        val passwordInserida = findViewById<EditText>(R.id.passEdit)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postLogin(email.text.toString(), passwordInserida.text.toString())
        var intent = Intent(this, MapsActivity::class.java)

        if (email.text.isNullOrEmpty() || passwordInserida.text.isNullOrEmpty()) {

            if (email.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()) {
                Log.d("**TESTE", "Aqui sem nada")
                Toast.makeText(this@Login, R.string.noData, Toast.LENGTH_SHORT).show()
            }
            if (email.text.isNullOrEmpty() && !passwordInserida.text.isNullOrEmpty()) {
                Toast.makeText(this@Login, R.string.emailLogin, Toast.LENGTH_SHORT).show()
            }
            if (!email.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()) {
                Toast.makeText(this@Login, R.string.pass_login, Toast.LENGTH_SHORT).show()
            }

        }

        call.enqueue(object : Callback<OutputPost> {
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful) {
                    val dados: OutputPost = response.body()!!


                    if (email.text.toString() == (dados.email) && (passwordInserida.text.toString() == (dados.password))) {
                        startActivity(intent)
                        finish()


                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()) {
                            putBoolean(getString(R.string.loginshared), true)
                            putString(getString(R.string.novoEmail), "${email.text}")
                            putInt(getString(R.string.user_id), dados.id)
                            commit()
                        }
                    } else if (!(email.text.toString() == (dados.email) && (passwordInserida.text.toString() == (dados.password)))) {

                        Toast.makeText(this@Login, R.string.Error_login, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@Login, R.string.Error_login, Toast.LENGTH_SHORT).show()
            }
        })


    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        return
    }

    override fun onSensorChanged(event: SensorEvent) {
        val value = event.values[0]
        Log.d("prox", value.toString())
        val teste = findViewById<Button>(R.id.login)
        // Do something with this sensor data.
        if (value < 15) {
            teste.setBackgroundColor(Color.GREEN)
        } else {
            teste.setBackgroundColor(Color.BLUE)
        }


    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, prox, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}