package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class EpisodeResponse(
    @Json(name = "results")
    var result: MutableList<Episode>? = null
)