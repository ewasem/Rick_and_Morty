package br.com.ewapps.rickandmorty.ui

import br.com.ewapps.rickandmorty.R

object MockData {
    val characterList = listOf<CharacterModel>(
        CharacterModel(listOf("10", "23", "31"), "Masculino", 1,R.drawable.im1, Location("terra"),"Rick Sanchez", Origin("Terra"), "Humano", "Vivo"),
        CharacterModel(listOf("1", "2", "34"), "Masculino", 2,R.drawable.im2, Location("terra"),"Morty Smith", Origin("Terra"), "Humano", "Vivo"),
        CharacterModel(listOf("1", "2", "43"), "Feminino", 3,R.drawable.im3, Location("terra"),"Summer Smith", Origin("Terra"), "E.T.", "Morto"),
        CharacterModel(listOf("1", "2", "3"), "Masculino", 4,R.drawable.im4, Location("Plumbum"),"Beth Smith", Origin("Mola"), "Humano", "Vivo"),
        CharacterModel(listOf("1", "2", "3"), "Masculino", 5,R.drawable.im5, Location("Russia"),"Jerry Smith", Origin("Terra"), "Mol", "Desconhecido")

    )


    fun getCharacterData(characterId: Int): CharacterModel {
        return characterList.first{it.id == characterId}
    }
}