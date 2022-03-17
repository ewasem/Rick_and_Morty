package br.com.ewapps.rickandmorty.models

import com.squareup.moshi.Json

data class SeasonTmdb(
    @Json(name = "air_date")
    val air_date: String? = null,
    @Json(name = "episodes")
    val episodes: List<EpisodeTmdb>? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "overview")
    val overview: String? = null,
    @Json(name = "poster_path")
    val posterPath: String? = null,
    @Json(name = "season_number")
    val seasonNumber: Int? = null
)

data class EpisodeTmdb(
    @Json(name = "air_date")
    val air_date: String? = null,
    @Json(name = "episode_number")
    val episodeNumber: Int? = null,
    var episodeId: Int? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "overview")
    val overview: String? = null,
    @Json(name = "still_path")
    val still_path: String? = null,
    @Json(name = "season_number")
    val seasonNumber: Int? = null
)

