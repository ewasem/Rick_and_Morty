package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class Character(
    @Json(name="id")
    val id: Int,
    @Json(name="name")
    val name: String,
    @Json(name="status")
    val status: String,
    @Json(name="species")
    val species: String,
    @Json(name="gender")
    val gender: String,
    @Json(name = "origin")
    val origin: Origin,
    @Json(name="location")
    val location: Location,
    @Json(name="image")
    val image: String,
    @Json(name = "episode")
    val episode: List<String>
)


