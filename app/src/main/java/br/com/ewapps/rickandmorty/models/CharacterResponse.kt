package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class CharacterResponse(@Json(name="results")
                             var result: MutableList<CharacterModel>? = null)