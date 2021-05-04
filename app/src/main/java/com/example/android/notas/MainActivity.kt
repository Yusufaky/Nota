package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.aplicacao.android.notas.R

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressure: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        val bntNotas = findViewById<Button>(R.id.pagNotas)
        val bntMapa = findViewById<Button>(R.id.pagMapa)
        bntNotas.setOnClickListener {
            val intent = Intent(this@MainActivity, Ecra::class.java)
            startActivity(intent)
            finish()
        }
        bntMapa.setOnClickListener {
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        return
    }
//sensor
    override fun onSensorChanged(event: SensorEvent) {
        val value = event.values[0]
        val teste = findViewById<Button>(R.id.pagNotas)
        // Do something with this sensor data.
        if(value <50){
            teste.setBackgroundColor(Color.MAGENTA)
        }else{
            teste.setBackgroundColor(Color.BLUE)
        }


    }

    override fun onResume() {
        // Register a listener for the sensor.
        super.onResume()
        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}