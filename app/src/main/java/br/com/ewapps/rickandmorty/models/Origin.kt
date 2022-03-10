package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json


data class Origin (
    @Json(name = "name")
    val name: String
)
