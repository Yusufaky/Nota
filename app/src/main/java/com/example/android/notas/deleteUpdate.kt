package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.Pontos
import com.example.android.notas.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class deleteUpdate : AppCompatActivity() {

    private lateinit var editponto: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_update)

        val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
        )
        editponto = findViewById(R.id.UpdatePontoEdit)

        var nome: String? =intent.getStringExtra("nome")

        val tudo = intent.getStringExtra("Tudo")
        val tudoaray = tudo!!.split("_").toTypedArray()
        val id = tudoaray[0].toInt()
        val id_tipo = tudoaray[5].toInt()
        val id_user = tudoaray[4].toInt()
        val id_userShare = tudoaray[6].toInt()

        val tipoacidente = findViewById<View>(R.id.AcidenteE) as RadioButton
        val tipoobras = findViewById<View>(R.id.ObrasE) as RadioButton
        tipoacidente.setOnClickListener{
            Toast.makeText(this, R.string.acidentes, Toast.LENGTH_SHORT).show()
        }
        tipoobras.setOnClickListener{
            Toast.makeText(this, R.string.obras, Toast.LENGTH_SHORT).show()
        }


        findViewById<EditText>(R.id.UpdatePontoEdit).setText(nome)

        val buttondelete = findViewById<Button>(R.id.DeletePonto)

        buttondelete.setOnClickListener{

            if(id_user == id_userShare) {
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                Log.d("ads", id.toString())
                val call = request.delete(id)

                call.enqueue(object : Callback<Pontos> {
                    override fun onResponse(call: Call<Pontos>, response: Response<Pontos>) {

                        if (response.isSuccessful) {
                            val c: Pontos = response.body()!!

                            val intent = Intent(this@deleteUpdate, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    override fun onFailure(call: Call<Pontos>, t: Throwable) {
                        val intent = Intent(this@deleteUpdate, MapsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            } else{
                Toast.makeText(this@deleteUpdate, getString(R.string.permissoes), Toast.LENGTH_SHORT).show()
            }
        }
        val buttonupdate = findViewById<Button>(R.id.UpdatePonto)

        buttonupdate.setOnClickListener{
            if(id_user == id_userShare) {
                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.update(id, editponto.text.toString(), 2)

                call.enqueue(object : Callback<Pontos> {
                    override fun onResponse(call: Call<Pontos>, response: Response<Pontos>) {

                        if (response.isSuccessful) {
                            val c: Pontos = response.body()!!

                            val intent = Intent(this@deleteUpdate, MapsActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<Pontos>, t: Throwable) {
                        val intent = Intent(this@deleteUpdate, MapsActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                })
            }else{
                Toast.makeText(this@deleteUpdate, getString(R.string.permissoes), Toast.LENGTH_SHORT).show()
            }
        }
    }




}
