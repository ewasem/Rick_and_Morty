package br.com.ewapps.rickandmorty

import android.app.Application
import br.com.ewapps.rickandmorty.data.Repository
import br.com.ewapps.rickandmorty.network.ApiClient
import br.com.ewapps.rickandmorty.network.AppManager
import br.com.ewapps.rickandmorty.network.TmdbClient

class MainApp: Application() {
    private val manager by lazy {
        AppManager(ApiClient.apiService, TmdbClient.tmdbService)
    }

    val repository by lazy {
        Repository(manager = manager)
    }
}