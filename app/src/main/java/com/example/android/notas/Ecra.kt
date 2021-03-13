package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplicacao.android.notas.R
import com.example.android.notas.Adapter.NotaAdapter
import com.example.android.notas.ViewModel.NotaViewModel
import com.example.android.notas.ViewModel.NotaViewModelFactory
import com.example.android.notas.entidade.Nota
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Ecra : AppCompatActivity() {

    private val newNotaActivityRequestCode = 1
    private val NotaViewModel: NotaViewModel by viewModels {
        NotaViewModelFactory((application as NotasApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecra_notas)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotaAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        NotaViewModel.allNotas.observe(owner = this) { Notas ->
            Notas.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@Ecra, NovaNotaActivity::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }

        val btnApagar = findViewById<Button>(R.id.btnDelete)
/*
        btnApagar.setOnClickListener{
            NotaViewModel.deleteNota()
            true
        }
*/
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NovaNotaActivity.EXTRA_REPLY)?.let { reply ->
                val Nota = Nota(reply)
                NotaViewModel.insert(Nota)
            }
        } else {
            Toast.makeText(this, R.string.empty_not_saved, Toast.LENGTH_SHORT).show()
        }

    }


}
