package com.example.android.notas.api

data class Pontos(

        val id: Int,
        val latitude: String,
        val longitude: String,
        val nome: String,
        val foto: String,
        val descricao: String,
        val user_id: Int,
)