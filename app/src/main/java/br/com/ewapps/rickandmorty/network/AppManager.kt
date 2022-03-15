package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.BuildConfig
import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.EpisodeResponse
import br.com.ewapps.rickandmorty.models.InfoResponse
import br.com.ewapps.rickandmorty.models.TmdbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppManager(private val service: ApiService, private val tmdbService: TmdbService) {

    suspend fun getInfo(): InfoResponse = withContext(Dispatchers.IO) {
        service.getInfo()
    }

    suspend fun getCharacters(pages: Int): CharacterResponse = withContext(Dispatchers.IO) {
        service.fetchCharacters(pages)
    }

    suspend fun getInfoEpisodes(): InfoResponse = withContext(Dispatchers.IO) {
        service.getInfoEpisodes()
    }

    suspend fun getEpisodes(pages: Int): EpisodeResponse = withContext(Dispatchers.IO) {
        service.fetchEpisodes(pages)
    }

    suspend fun getTmdbData(season: String, episode: String): TmdbModel = withContext(Dispatchers.IO) {
        tmdbService.getTmdbData(season, episode, BuildConfig.API_KEY, "pt-BR")
    }
}