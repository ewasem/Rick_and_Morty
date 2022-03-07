package br.com.ewapps.rickandmorty.ui

data class EpisodeModel(
    val id: Int,
    val season: String,
    val episodes: String
)

object EpisodeData {
    private val episodeList = listOf<EpisodeModel>(
        EpisodeModel(1, "01", "01"),
        EpisodeModel(2, "01", "02"),
        EpisodeModel(3, "01", "03"),
        EpisodeModel(4, "01", "04"),
        EpisodeModel(5, "01", "05"),
        EpisodeModel(6, "01", "06"),
        EpisodeModel(7, "01", "07"),
        EpisodeModel(8, "01", "08"),
        EpisodeModel(9, "01", "09"),
        EpisodeModel(10, "01", "10"),
        EpisodeModel(11, "01", "11"),
        EpisodeModel(12, "02", "01"),
        EpisodeModel(13, "02", "02"),
        EpisodeModel(14, "02", "03"),
        EpisodeModel(15, "02", "04"),
        EpisodeModel(16, "02", "05"),
        EpisodeModel(17, "02", "06"),
        EpisodeModel(18, "02", "07"),
        EpisodeModel(19, "02", "08"),
        EpisodeModel(20, "02", "09"),
        EpisodeModel(21, "02", "10"),
        EpisodeModel(22, "03", "01"),
        EpisodeModel(23, "03", "02"),
        EpisodeModel(24, "03", "03"),
        EpisodeModel(25, "03", "04"),
        EpisodeModel(26, "03", "05"),
        EpisodeModel(27, "03", "06"),
        EpisodeModel(28, "03", "07"),
        EpisodeModel(29, "03", "08"),
        EpisodeModel(30, "03", "09"),
        EpisodeModel(31, "03", "10"),
        EpisodeModel(32, "04", "01"),
        EpisodeModel(33, "04", "02"),
        EpisodeModel(34, "04", "03"),
        EpisodeModel(35, "04", "04"),
        EpisodeModel(36, "04", "05"),
        EpisodeModel(37, "04", "06"),
        EpisodeModel(38, "04", "07"),
        EpisodeModel(39, "04", "08"),
        EpisodeModel(40, "04", "09"),
        EpisodeModel(41, "04", "10"),
        EpisodeModel(42, "05", "01"),
        EpisodeModel(43, "05", "02"),
        EpisodeModel(44, "05", "03"),
        EpisodeModel(45, "05", "04"),
        EpisodeModel(46, "05", "05"),
        EpisodeModel(47, "05", "06"),
        EpisodeModel(48, "05", "07"),
        EpisodeModel(49, "05", "08"),
        EpisodeModel(50, "05", "09"),
        EpisodeModel(51, "05", "10"),
        EpisodeModel(52, "06", "01"),
        EpisodeModel(53, "06", "02"),
        EpisodeModel(54, "06", "03"),
        EpisodeModel(55, "06", "04"),
        EpisodeModel(56, "06", "05"),
        EpisodeModel(57, "06", "06"),
        EpisodeModel(58, "06", "07"),
        EpisodeModel(59, "06", "08"),
        EpisodeModel(60, "06", "09"),
        EpisodeModel(61, "06", "10")
    )

    fun getEpisode(id: Int): EpisodeModel{
        return episodeList.first{it.id == id}
    }
}
