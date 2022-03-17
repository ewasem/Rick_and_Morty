package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.BuildConfig
import br.com.ewapps.rickandmorty.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppManager(private val service: ApiService, private val tmdbService: TmdbService) {

    suspend fun getInfo(): InfoResponse = withContext(Dispatchers.IO) {
        service.getInfo()
    }

    suspend fun getCharacters(pages: Int): CharacterResponse = withContext(Dispatchers.IO) {
        service.fetchCharacters(pages)
    }

    suspend fun getCharactersFromEpisode(episodeId: Int): CharactersEpisode = withContext(Dispatchers.IO) {
        service.getCharacterFromEpisode(episodeId)
    }

    suspend fun getTmdbData(season: String, episode: String): TmdbModel = withContext(Dispatchers.IO) {
        tmdbService.getTmdbData(season, episode, BuildConfig.API_KEY, "pt-BR")
    }

    suspend fun getSeasons(): TmdbSeasonsInfo = withContext(Dispatchers.IO) {
        tmdbService.getSeasons(BuildConfig.API_KEY, "pt-BR")
    }

    suspend fun getEpisodesFromTmdb(season: Int): SeasonTmdb= withContext(Dispatchers.IO) {
        tmdbService.getEpisodes(season, BuildConfig.API_KEY, "pt-BR")
    }
}