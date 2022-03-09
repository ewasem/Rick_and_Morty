package br.com.ewapps.rickandmorty.network

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.InfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppManager(private val service: ApiService) {

    suspend fun getInfo(): InfoResponse = withContext(Dispatchers.IO) {
        service.getInfo()
    }

    suspend fun getCharacters(pages: Int): CharacterResponse = withContext(Dispatchers.IO) {

            service.fetchCharacters(pages)

    }
}