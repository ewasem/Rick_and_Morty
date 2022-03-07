package br.com.ewapps.rickandmorty.ui

data class CharacterModel(
    val episode: List<String>?,
    val gender: String?,
    val id: Int,
    val image: Int,
    val location: Location?,
    val name: String,
    val origin: Origin?,
    val species: String?,
    val status: String?
)

data class Location(
    val name: String?
)

data class Origin(
    val name: String?
)