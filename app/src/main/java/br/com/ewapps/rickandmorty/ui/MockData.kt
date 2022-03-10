package br.com.ewapps.rickandmorty.ui

import br.com.ewapps.rickandmorty.models.Character

object MockData {
    val characterList = listOf<Character>(
         )


    fun getCharacterData(characterId: Int): Character {
        return characterList.first{it.id == characterId}
    }
}