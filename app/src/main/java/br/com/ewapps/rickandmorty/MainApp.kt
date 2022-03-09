package br.com.ewapps.rickandmorty

import android.app.Application
import br.com.ewapps.rickandmorty.data.Repository
import br.com.ewapps.rickandmorty.network.ApiClient
import br.com.ewapps.rickandmorty.network.AppManager

class MainApp: Application() {
    private val manager by lazy {
        AppManager(ApiClient.apiService)
    }

    val repository by lazy {
        Repository(manager = manager)
    }
}