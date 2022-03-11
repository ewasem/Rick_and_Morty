package br.com.ewapps.rickandmorty.network

import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.EpisodeResponse
import br.com.ewapps.rickandmorty.models.InfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppManager(private val service: ApiService) {

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
}