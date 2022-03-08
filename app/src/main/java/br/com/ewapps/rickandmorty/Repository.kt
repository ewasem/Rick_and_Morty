package br.com.ewapps.rickandmorty

import br.com.ewapps.rickandmorty.network.ApiService


class Repository (private val apiService: ApiService) {
    fun getCharacters(page: String) = apiService.fetchCharacters()
}