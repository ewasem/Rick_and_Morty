package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class Character(
    @Json(name="id")
    val id: Int? = null,
    @Json(name="name")
    val name: String? = null,
    @Json(name="status")
    val status: String? = null,
    @Json(name="species")
    val species: String? = null,
    @Json(name="gender")
    val gender: String? = null,
    @Json(name = "origin")
    val origin: Origin? = null,
    @Json(name="location")
    val location: Location? = null,
    @Json(name="image")
    val image: String? = null,
    @Json(name = "episode")
    val episode: List<String>? = null
)


