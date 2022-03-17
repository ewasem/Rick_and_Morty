package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class TmdbSeasonsInfo(
    @Json(name = "number_of_seasons")
    val numberOfSeasons: Int? = null
)
