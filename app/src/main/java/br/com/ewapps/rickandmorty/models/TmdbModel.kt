package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class TmdbModel(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "overview")
    val overview: String? = null
)