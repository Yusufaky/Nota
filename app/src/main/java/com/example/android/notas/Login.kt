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
        if (sharedPref != null){
            if(sharedPref.all[getString(R.string.loginshared)]==true){
                var intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)

            }
        }
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.nomeEdit)
        val passwordInserida = findViewById<EditText>(R.id.passEdit)

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postTest(email.text.toString(),passwordInserida.text.toString())
        var intent = Intent(this, MapsActivity::class.java)

        if(email.text.isNullOrEmpty() || passwordInserida.text.isNullOrEmpty()){

            if(email.text.isNullOrEmpty() && !passwordInserida.text.isNullOrEmpty()){
                Toast.makeText(this@Login, R.string.emailLogin, Toast.LENGTH_SHORT).show()
            }
            if(!email.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()){
                Toast.makeText(this@Login, R.string.pass_login, Toast.LENGTH_SHORT).show()
            }
            if(email.text.isNullOrEmpty() && passwordInserida.text.isNullOrEmpty()){
                Toast.makeText(this@Login, R.string.pass_login + R.string.emailLogin, Toast.LENGTH_SHORT).show()
            }
        }

        call.enqueue(object : Callback<OutputPost>{
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful){
                    val e: OutputPost = response.body()!!

                    //Confirmação login
                    if(email.text.toString().equals(e.email) && (passwordInserida.text.toString().equals(e.password))){
                        startActivity(intent)

                        //Shared Preferences Login
                        val sharedPref: SharedPreferences = getSharedPreferences(
                                getString(R.string.loginSharePreferences), Context.MODE_PRIVATE
                        )
                        with(sharedPref.edit()){
                            putBoolean(getString(R.string.loginshared), true)
                            putString(getString(R.string.novoEmail), "${email.text}")
                            putInt(getString(R.string.user_id), e.id)
                            commit()
                            Log.d("**TESTE","${e.id}" )
                        }
                    }else if (!(email.text.toString().equals(e.email) && (passwordInserida.text.toString().equals(e.password)))){

                        Toast.makeText(this@Login, R.string.Error_login, Toast.LENGTH_SHORT).show()
                    }

                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@Login, R.string.Error_Error, Toast.LENGTH_SHORT).show()

            }
        })




    }


}