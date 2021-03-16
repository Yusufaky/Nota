package com.example.android.notas

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.aplicacao.android.notas.R

class EditarNota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_nota)
        val editnotaView = findViewById<EditText>(R.id.edit_nota)
        var ss: String = intent.getStringExtra("id").toString()
        val button = findViewById<Button>(R.id.guardar)
        button.setOnClickListener {
            val replyIntent = Intent()

            if (TextUtils.isEmpty(editnotaView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nota = editnotaView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY_NOTAS, nota)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object {
        const val EXTRA_REPLY_NOTAS = "com.example.android.notas"
    }
}
