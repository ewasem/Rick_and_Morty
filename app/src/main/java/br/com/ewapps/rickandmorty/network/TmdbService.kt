package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.models.SeasonTmdb
import br.com.ewapps.rickandmorty.models.TmdbModel
import br.com.ewapps.rickandmorty.models.TmdbSeasonsInfo
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET("60625/season/{season}/episode/{episode}")
    suspend fun getTmdbData(
        @Path("season") season: String,
        @Path("episode") episode: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : TmdbModel

    @GET("60625")
    suspend fun getSeasons(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : TmdbSeasonsInfo

    @GET("60625/season/{season}")
    suspend fun getEpisodes(
        @Path("season") season: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ) : SeasonTmdb
}