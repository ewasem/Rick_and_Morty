package br.com.ewapps.rickandmorty.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.ewapps.rickandmorty.MainApp
import br.com.ewapps.rickandmorty.models.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = getApplication<MainApp>().repository

    //Variável que recebe a lista de personagens
    private var _characterResponse = CharacterResponse()

    //Variável que recebe a lista de episódios
    private var _episodeResponse = EpisodeResponse()

    //Variável que recebe a quantidade de páginas e o total de personagens
    //Já que a API não possui uma rota que receba todos os personagens de uma vez
    //o número de páginas se torna necessário
    private var _infoResponse = MutableStateFlow(InfoResponse())
    val infoResponse: StateFlow<InfoResponse>
        get() = _infoResponse

    //Recebe as informações do total de episódios e do número de páginas de episódios
    private var _infoEpisodesResponse = MutableStateFlow(InfoResponse())
    val infoEpisodesResponse: StateFlow<InfoResponse>
        get() = _infoEpisodesResponse

    //Atualiza se os dados estão sendo baixados
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    //Variável de controle em caso de erro
    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean>
        get() = _isError

    //variável que em caso de erro, atualiza a variável de controle _isError para verdadeiro
    private val errorHandler = CoroutineExceptionHandler { _, error ->
        if (error is Exception) {
            _isError.value = true
        }
    }

    //Função que pega os dados de páginas e total de personagens
    private fun getInfo() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _infoResponse.value = repository.getInfo()
            _isLoading.value = false
            _infoResponse.value.info?.pages?.let { getCharacters(it) }
            Log.d("Pages: ", "${_infoResponse.value.info!!.pages}")
        }
    }

    private fun getInfoEpisodes() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _infoEpisodesResponse.value = repository.getInfoEpisodes()
            _isLoading.value = false
            _infoEpisodesResponse.value.info?.pages?.let { getEpisodes(it) }
        }
    }

    //Coleta as informações ao inicializar a viewModel
    init {
        getInfo()
        getInfoEpisodes()
    }

    //Armazena a Lista com todos os personagens
    private val _characterList = MutableStateFlow(CharacterResponse())
    val characterList: StateFlow<CharacterResponse>
        get() = _characterList

    //Armazena a Lista com todos os episódios
    private val _episodeList = MutableStateFlow(EpisodeResponse())
    val episodeList: StateFlow<EpisodeResponse>
        get() = _episodeList

    //Armazena o index da peimeira linha que aparece na tela
    private val _visibleItemIndex = MutableStateFlow(0)

    //Armazena a posição na linha
    private val _visibleItemOffset = MutableStateFlow(0)

    //Armazena a orientação atual da tela
    private val _orientation = MutableStateFlow(0)
    val orientation: StateFlow<Int>
        get() = _orientation

    //Atualiza a orientação em caso de mudança
    fun updateOrientation(orientation: Int) {
        _orientation.value = orientation
    }

    //Atualiza a posição
    fun updateOffset(offset: Int) {
        _visibleItemOffset.update { offset }
    }

    //retorna o valor da posição para o compose
    fun getOffset(): Int {
        return _visibleItemOffset.value
    }

    //apualiza a linha
    fun updateIndex(index: Int) {
        _visibleItemIndex.update { index }
    }

    //retorna a linha atual para o compose
    fun getIndex(orientation: Int): Int {
        if (orientation == 2) {
            return _visibleItemIndex.value / 2
        }
        return _visibleItemIndex.value
    }

    //armazena todos os personagens para depois atualizar a lista de personagens que irá aparecer na tela
    private var charList = mutableListOf<Character>()

    private var epList = mutableListOf<Episode>()

    val resultCharacterList = snapshotFlow { _characterList }


    //Função que coleta todos os personagens da API
    private fun getCharacters(pages: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            if (_characterList.value.result.isNullOrEmpty()) {
                var i = 1
                while (i <= pages) {
                    _characterResponse = repository.getCharacters(i)
                    _characterResponse.result?.let { charList.addAll(it) }
                    _characterList.update { CharacterResponse(charList) }
                    _isLoading.value = false
                    i++
                }
            }
        }
    }

    //Função que coleta todos os episódios da API
    private fun getEpisodes(pages: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            if (_episodeList.value.result.isNullOrEmpty()) {
                var i = 1
                while (i <= pages) {
                    _episodeResponse = repository.getEpisodes(i)
                    _episodeResponse.result?.let { epList.addAll(it) }
                    _episodeList.update { EpisodeResponse(epList) }
                    i++
                }
                _isLoading.value = false
                println("episódios: ${_episodeList.value.result}")
                season(eplist = _episodeList)
            }
        }
    }

    private val _allSeasons = MutableStateFlow(mutableListOf<Season>())

    private val allSeasons = arrayListOf<Season>()

    val resultEpisodeList = snapshotFlow { _allSeasons }

    private fun season(eplist: MutableStateFlow<EpisodeResponse>) {

        val episodeList = eplist.value.result
        var temAnt = "01"
        var list = mutableListOf<Epi>()
        var count = 0

        if (episodeList != null) {
            for (i in episodeList.indices) {
                val temp = episodeList[i].episode.substringAfter("S").substringBefore("E")
                val epi = episodeList[i].episode.substringAfter("E")
                val name = episodeList[i].name
                val id = episodeList[i].id
                if (temp == temAnt) {
                    list.add(Epi(epi,name,id))
                } else {
                    if (list.isEmpty()) {
                        temAnt = temp
                        list.add(Epi(epi,name,id))
                    } else {
                        allSeasons.add(count,Season(temAnt, list))
                        allSeasons.forEach { it ->
                            println("Temporadaww: ${it.season}")
                            it.episodes.forEach {
                                println("Epidódioww: ${it.episodeNumber}, Nome ${it.episodeName}")
                            }
                        }
                        count++
                        temAnt = temp
                        list = mutableListOf<Epi>()
                        list.add(Epi(epi,name,id))
                    }
                }
                if (episodeList.lastIndex == i) {
                    allSeasons.add(count, Season(temAnt, list))
                    allSeasons.forEach { it ->
                        println("Temporada: ${it.season}")
                        it.episodes.forEach {
                            println("Epidódio: ${it.episodeNumber}, Nome ${it.episodeName}")
                        }
                    }
                    _allSeasons.update { allSeasons }

                }
            }
        }
    }

    // private val _allCharacterSeasons = MutableStateFlow(mutableListOf(Season()))

    private val allCharacterSeasons = mutableListOf<Season>()

    //val resultCharacterEpisodeList = snapshotFlow { _allCharacterSeasons }

/*
    fun getEpisode(episode: MutableList<Int>) {
        episode.forEach { epi ->
            _allSeasons.value.forEach { season ->
                season.episodeId?.forEach { epiInSeason ->
                    if (epiInSeason == epi) {

                    }
                }
            }
        }
    }*/

}