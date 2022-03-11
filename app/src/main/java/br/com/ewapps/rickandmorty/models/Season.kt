package br.com.ewapps.rickandmorty.models

data class Season(
    val season: String? = null,
    val episodes : List<Epi>

)

data class Epi(
    var episodeNumber: String? = null,
    var episodeName: String? = null,
    var episodeId: Int? = null
)
