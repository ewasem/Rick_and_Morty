package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.models.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
Criada a interface, que define como o retrofit conversa
com o serviço usando o método Get
 */
interface ApiService {
    @GET("character")
    fun fetchCharacters(): Call<CharacterResponse>
}