package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json


data class Info (
    @Json(name = "count")
    val count: Int? = null,
    @Json(name = "pages")
    val pages: Int? = null
)

