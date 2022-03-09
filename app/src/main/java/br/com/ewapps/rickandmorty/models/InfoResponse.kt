package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

class InfoResponse (@Json(name="info")
                    val info: Info? = null)
