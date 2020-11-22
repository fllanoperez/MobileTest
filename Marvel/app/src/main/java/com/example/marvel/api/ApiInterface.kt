package com.example.marvel.api

import com.example.marvel.models.MarvelResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("characters")
    fun getCharacters(@Query("ts") ts: String,
                      @Query("hash") hash: String,
                      @Query("apikey") apiKey: String,
                      @Query("limit") limit: Int,
                      @Query("offset") offset: Int): Call<MarvelResponse>

    @GET("characters/{Id}")
    fun getCharacterInfo(@Path("Id") characterId: String, @Query("apikey") apiKey: String): Call<MarvelResponse>
}