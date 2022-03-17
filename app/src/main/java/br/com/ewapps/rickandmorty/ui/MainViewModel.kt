package br.com.ewapps.rickandmorty.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.ewapps.rickandmorty.MainApp
import br.com.ewapps.rickandmorty.models.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = getApplication<MainApp>().repository

    //Variável que recebe a lista de personagens
    private var _characterResponse = CharacterResponse()

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

    private val _numberOfSeasons = MutableStateFlow(TmdbSeasonsInfo())

    private fun getSeasons() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _numberOfSeasons.value = repository.getSeasons()
            _isLoading.value = false
            _numberOfSeasons.value.numberOfSeasons?.let { getEpisodesFromTmdb(it) }
        }

    }

    private val _allEpisodes = MutableStateFlow(mutableListOf<SeasonTmdb>())
    val allEpisodes = snapshotFlow { _allEpisodes }
    private val _episodes = mutableListOf<SeasonTmdb>()

    private fun getEpisodesFromTmdb(seasons: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            var i = 1
            while (i <= seasons) {
                _episodes.add(repository.getEpisodesFromTmdb(i))
                i++
            }
            var count = 0
            _episodes.forEach {season ->
                season.episodes?.forEach {episode ->
                    count ++
                    episode.episodeId = count
                }
            }
            _allEpisodes.value = _episodes
            _isLoading.value = false
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

    //Coleta as informações ao inicializar a viewModel
    init {
        getInfo()
        getSeasons()
    }

    //Armazena a Lista com todos os personagens
    private val _characterList = MutableStateFlow(CharacterResponse())


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
            println("CharacterList $charList")
        }
    }

    private var _characterEpisodeString = MutableStateFlow(CharactersEpisode())


    //Retorna a lista de personagens por episódio
    fun getCharactersFromEpisodeStringList(episode: Int): List<Character>{
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _characterEpisodeString.value = repository.getCharactersFromEpisode(episode)
            _isLoading.value = false
        }
        val char = mutableListOf<Character>()
        val list = _characterEpisodeString.value.characters
        if (list != null) {
            list.forEach {
                val character = it.substringAfterLast("/").toInt()
                _characterList.value.result?.forEach {
                    if (character == it.id) {
                        char.add(it)
                    }
                }
            }
        }
        return char
    }

    //Retorna a data para o formato correto
    fun dateFormatter(date: String): String {

        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        return formatter.format(parser.parse(date))
    }

}