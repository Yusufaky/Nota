package com.example.android.notas.api

import retrofit2.Call
import retrofit2.http.*

interface EndPoints {

    @GET("myslim/api/utilizadores")
    fun getUsers(): Call<List<User>>

    @GET("myslim/api/utilizador/{id}")
    fun getUsersByName(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("myslim/api/utilizador/login")
    fun postLogin(@Field("email") first: String, @Field ("password") second: String): Call<OutputPost>

    @GET("myslim/api/pontos")
    fun getPontos(): Call<List<Pontos>>


}