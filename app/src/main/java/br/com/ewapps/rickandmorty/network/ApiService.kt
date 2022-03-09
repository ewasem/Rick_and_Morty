package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.Info
import br.com.ewapps.rickandmorty.models.InfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
Criada a interface, que define como o retrofit conversa
com o serviço usando o método Get
 */
interface ApiService {
    @GET("character")
    suspend fun fetchCharacters(@Query("page") page: Int): CharacterResponse

    @GET("character")
    suspend fun getInfo(): InfoResponse

}