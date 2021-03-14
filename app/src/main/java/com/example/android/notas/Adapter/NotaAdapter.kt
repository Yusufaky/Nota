
package com.example.android.notas.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aplicacao.android.notas.R
import com.example.android.notas.ViewModel.NotaViewModel
import com.example.android.notas.entidade.Nota

class NotaAdapter internal constructor(
        context: Context
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var nota = emptyList<Nota>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NotaViewHolder(itemView)


    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = nota[position]
        holder.notaItemView.text = current.nota
    }

    internal fun setNota(notas: List<Nota>) {
        this.nota = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = nota.size
}
