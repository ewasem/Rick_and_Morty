package br.com.ewapps.rickandmorty.models

data class Episode(
    val id: Int,
    val season: String,
    val episodes: String
)

object EpisodeData {
    private val episodeList = listOf<Episode>(
        Episode(1, "01", "01"),
        Episode(2, "01", "02"),
        Episode(3, "01", "03"),
        Episode(4, "01", "04"),
        Episode(5, "01", "05"),
        Episode(6, "01", "06"),
        Episode(7, "01", "07"),
        Episode(8, "01", "08"),
        Episode(9, "01", "09"),
        Episode(10, "01", "10"),
        Episode(11, "01", "11"),
        Episode(12, "02", "01"),
        Episode(13, "02", "02"),
        Episode(14, "02", "03"),
        Episode(15, "02", "04"),
        Episode(16, "02", "05"),
        Episode(17, "02", "06"),
        Episode(18, "02", "07"),
        Episode(19, "02", "08"),
        Episode(20, "02", "09"),
        Episode(21, "02", "10"),
        Episode(22, "03", "01"),
        Episode(23, "03", "02"),
        Episode(24, "03", "03"),
        Episode(25, "03", "04"),
        Episode(26, "03", "05"),
        Episode(27, "03", "06"),
        Episode(28, "03", "07"),
        Episode(29, "03", "08"),
        Episode(30, "03", "09"),
        Episode(31, "03", "10"),
        Episode(32, "04", "01"),
        Episode(33, "04", "02"),
        Episode(34, "04", "03"),
        Episode(35, "04", "04"),
        Episode(36, "04", "05"),
        Episode(37, "04", "06"),
        Episode(38, "04", "07"),
        Episode(39, "04", "08"),
        Episode(40, "04", "09"),
        Episode(41, "04", "10"),
        Episode(42, "05", "01"),
        Episode(43, "05", "02"),
        Episode(44, "05", "03"),
        Episode(45, "05", "04"),
        Episode(46, "05", "05"),
        Episode(47, "05", "06"),
        Episode(48, "05", "07"),
        Episode(49, "05", "08"),
        Episode(50, "05", "09"),
        Episode(51, "05", "10"),
        Episode(52, "06", "01"),
        Episode(53, "06", "02"),
        Episode(54, "06", "03"),
        Episode(55, "06", "04"),
        Episode(56, "06", "05"),
        Episode(57, "06", "06"),
        Episode(58, "06", "07"),
        Episode(59, "06", "08"),
        Episode(60, "06", "09"),
        Episode(61, "06", "10")
    )

    fun getEpisode(id: Int): Episode {
        return episodeList.first{it.id == id}
    }

    fun getAllEpisodes(): List<Episode> {
        return episodeList
    }
}
