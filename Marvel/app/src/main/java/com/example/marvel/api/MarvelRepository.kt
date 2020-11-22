package com.example.marvel.api

import android.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.marvel.models.MarvelResponse
import com.example.marvel.utils.ApiResource
import com.example.marvel.utils.md5
import com.example.marvel.utils.toHex
import com.example.marvel.utils.ts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MarvelRepository {

    private val apiKey = "0c1e60f9250a017182d63084d7c09cb2"
    private val privateKey = "6594010e6f49719b84a21cf982dee4b7ef33e51c"
    private val apiRepository: ApiInterface

    init {
        apiRepository = getRetrofit().create(ApiInterface::class.java)
    }

    private val _characters = MutableLiveData<ApiResource<MarvelResponse>>()
    val characters: LiveData<ApiResource<MarvelResponse>> = _characters

    private fun getHash(ts: String): String = md5(ts + privateKey + apiKey).toHex()

    fun getCharacters(offset: Int, limit: Int = 20) {
        _characters.value = ApiResource.Loading()
        val ts = ts()
        apiRepository.getCharacters(ts, getHash(ts), apiKey, limit, offset).enqueue(object : Callback<MarvelResponse> {
            override fun onResponse(call: Call<MarvelResponse>?, response: Response<MarvelResponse>?) {
                response?.let {
                    _characters.value = ApiResource.Success(it.body())
                } ?: run {
                    _characters.value = ApiResource.Error("")
                }
            }

            override fun onFailure(call: Call<MarvelResponse>?, t: Throwable?) {
                t?.let {
                    _characters.value = ApiResource.Error(it.localizedMessage)
                } ?: run {
                    _characters.value = ApiResource.Error("")
                }
            }
        })
    }

    fun getCharacterInfo(characterId: String): LiveData<ApiResource<MarvelResponse>> {
        val data = MutableLiveData<ApiResource<MarvelResponse>>()
        apiRepository.getCharacterInfo(characterId, apiKey).enqueue(object : Callback<MarvelResponse> {
            override fun onResponse(call: Call<MarvelResponse>?, response: Response<MarvelResponse>?) {
                response?.let {
                    data.value = ApiResource.Success(it.body())
                } ?: run {
                    data.value = ApiResource.Error("")
                }
            }

            override fun onFailure(call: Call<MarvelResponse>?, t: Throwable?) {
                t?.let {
                    data.value = ApiResource.Error(it.localizedMessage)
                } ?: run {
                    data.value = ApiResource.Error("")
                }
            }
        })
        data.value = ApiResource.Loading()
        return data
    }


    companion object {
        private var newsRepository: MarvelRepository? = null

        fun getInstance(): MarvelRepository {
            newsRepository?.let {
                return it
            }?: run {
                newsRepository = MarvelRepository()
                return newsRepository!!
            }
        }

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://gateway.marvel.com:443/v1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}