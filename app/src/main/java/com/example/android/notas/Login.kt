package com.example.android.notas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.aplicacao.android.notas.R
import com.example.android.notas.api.EndPoints
import com.example.android.notas.api.OutputPost
import com.example.android.notas.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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

            }
        })


    }


}