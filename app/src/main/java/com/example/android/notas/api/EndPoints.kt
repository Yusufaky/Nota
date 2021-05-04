package com.example.android.notas.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @FormUrlEncoded
    @POST("myslim/api/utilizador/login")
    fun postLogin(@Field("email") first: String, @Field ("password") second: String): Call<OutputPost>

    @GET("myslim/api/pontos")
    fun getPontos(): Call<List<Pontos>>

    @FormUrlEncoded
    @POST("myslim/api/pontos/novo")
    fun postNovoPonto(@Field("nome") nome: String,@Field("latitude") latitude: Double,@Field("longitude") longitude: Double,@Field("user_id") user_id: Int,@Field("id_Tipo") id_Tipo: Int): Call<Pontos>

    @FormUrlEncoded
    @POST("myslim/api/pontos/delete")
    fun delete(@Field("id") id: Int): Call<Pontos>

    @FormUrlEncoded
    @POST("myslim/api/pontos/update")
    fun update(@Field("id") id: Int,@Field("nome") nome: String,@Field("id_Tipo") id_Tipo: Int): Call<Pontos>

}
