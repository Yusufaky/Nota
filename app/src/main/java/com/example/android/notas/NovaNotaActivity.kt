
package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class NovaNotaActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_nota)
        val editnotaView = findViewById<EditText>(R.id.edit_nota)

        val button = findViewById<Button>(R.id.guardar)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editnotaView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val nota = editnotaView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, nota)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.android.notalistsql.REPLY"
    }
}
