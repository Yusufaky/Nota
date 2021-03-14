package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
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
        val adapter = NotaAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        NotaViewModel.allNotas.observe(this, Observer { Notas ->
            // Update the cached copy of the words in the adapter.
            Notas?.let { adapter.setNota(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@Ecra, NovaNotaActivity::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }
/*
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            NotaViewModel.deleteByNota()
            true
        }*/
    }

    fun deleteNota(){
        Toast.makeText(this, "MArcos", Toast.LENGTH_SHORT).show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val pnota = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Nota)

            if (pnota!= null) {
                val nota = Nota(nota = pnota)
                NotaViewModel.insert(nota)
            }

        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }

}
