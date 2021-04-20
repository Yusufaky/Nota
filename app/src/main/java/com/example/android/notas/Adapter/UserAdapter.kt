package com.example.android.notas.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aplicacao.android.notas.R
import com.example.android.notas.api.User

class UserAdapter(val users: List<User>): RecyclerView.Adapter<UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview2, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        return holder.bind(users[position])
    }
}

class UsersViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val email: TextView = itemView.findViewById(R.id.nomeEdit)
    private val password:TextView = itemView.findViewById(R.id.passEdit)

    fun bind(user: User) {
        email.text = user.email
        password.text = user.password

    }

}