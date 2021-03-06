package com.example.android.notas

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        val adapter = NotaAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        NotaViewModel.allNotas.observe(this, Observer { Notas ->
            Notas?.let { adapter.submitList(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@Ecra, NovaNotaActivity::class.java)
            startActivityForResult(intent, newNotaActivityRequestCode)
        }
        adapter.setOnItemClick(object : NotaAdapter.onItemclick {
            override fun onEditClick(position: Int, nota: String, problema: String) {
                val intent = Intent(this@Ecra, NovaNotaActivity::class.java)
                intent.putExtra("ID", position)
                intent.putExtra("nota",nota)
                intent.putExtra("problema",problema)
                startActivityForResult(intent, 2)

            }

            override fun onDeleteClick(position: Int) {
                NotaViewModel.deleteByNota(position)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newNotaActivityRequestCode && resultCode == Activity.RESULT_OK) {

            val pnota = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Nota)
            val pProblema = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Problema)

            if (pnota != null && pProblema != null) {
                val nota = Nota(nota = pnota, problema = pProblema)
                NotaViewModel.insert(nota)
            }
        }else if (requestCode == 1) {
            Toast.makeText(applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show()
        }

        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            val pnota = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Nota)
            val pProblema = data?.getStringExtra(NovaNotaActivity.EXTRA_REPLY_Problema)
            val id = data?.getIntExtra(NovaNotaActivity.EXTRA_REPLY_ID, 0)
            if (pnota != null && pProblema != null && id != null) {
                NotaViewModel.updateNota(pnota, id, pProblema)
            }
        }else if (requestCode == 2) {
            Toast.makeText(applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_SHORT).show()
        }
    }

}
