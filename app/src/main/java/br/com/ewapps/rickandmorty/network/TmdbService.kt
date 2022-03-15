package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.models.EpisodeResponse
import br.com.ewapps.rickandmorty.models.TmdbModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("season/{season}/episode/{episode}")
    suspend fun getTmdbData(
        @Path("season") season: String,
        @Path("episode") episode: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : TmdbModel
}