package br.com.ewapps.rickandmorty.ui

import br.com.ewapps.rickandmorty.R
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.models.Location
import br.com.ewapps.rickandmorty.models.Origin

object MockData {
    val characterList = listOf<CharacterModel>(
         )


    fun getCharacterData(characterId: Int): CharacterModel {
        return characterList.first{it.id == characterId}
    }
}