package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class Location (
    @Json(name="name")
    val name: String
)
