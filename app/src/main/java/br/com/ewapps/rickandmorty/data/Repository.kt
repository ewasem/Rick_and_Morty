package br.com.ewapps.rickandmorty.data

import br.com.ewapps.rickandmorty.network.AppManager


class Repository (private val manager: AppManager) {
    suspend fun getCharacters(page: Int) = manager.getCharacters(pages = page)
    suspend fun getInfo() = manager.getInfo()
}