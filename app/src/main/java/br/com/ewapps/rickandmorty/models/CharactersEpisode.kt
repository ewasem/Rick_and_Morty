package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class CharactersEpisode(
    @Json(name = "characters")
    var characters: List<String>? = null
)