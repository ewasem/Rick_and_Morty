package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class Episode(
    @Json(name = "air_date")
    val air_date: String,
    @Json(name = "characters")
    val characters: List<String>,
    @Json(name = "episode")
    val episode: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String
)
