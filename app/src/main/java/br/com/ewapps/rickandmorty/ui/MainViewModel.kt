package br.com.ewapps.rickandmorty.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.ewapps.rickandmorty.MainApp
import br.com.ewapps.rickandmorty.models.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = getApplication<MainApp>().repository

    //Variável que após a tela de splash, muda para falso para alterar o startDestination. Feito através da função
    //delayForSplash
    private val _splash = MutableStateFlow(true)
    val splash: StateFlow<Boolean>
        get() = _splash

    //Variável que recebe a lista de personagens
    private var _characterResponse = CharacterResponse()

    //Variável que recebe a quantidade de páginas e o total de personagens
    //Já que a API não possui uma rota que receba todos os personagens de uma vez
    //o número de páginas se torna necessário
    private var _infoResponse = MutableStateFlow(InfoResponse())
    val infoResponse: StateFlow<InfoResponse>
        get() = _infoResponse

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

    //Armazena os dados do personagem selecionado
    private val _selectedCharacter = MutableStateFlow(Character())
    val selectedCharacter: StateFlow<Character>
        get() = _selectedCharacter

    //Armazena a lista de episódios do personagem selecionado
    private val _characterEpisodes = MutableStateFlow(mutableListOf<SeasonTmdb>())
    val characterEpisodes: StateFlow<MutableList<SeasonTmdb>>
        get() = _characterEpisodes

    //Retorna os dados do personagem, assim como a lista de episódios que ele participa
    fun selectedCharacter(id: Int) {
        val list = mutableListOf<EpisodeTmdb>()
        val seasonEpisodes = mutableListOf<SeasonTmdb>()
        id.let {

            //Procura o personagem pelo id
            _characterList.value.result?.forEach {
                if (it.id == id) {
                    _selectedCharacter.value = it
                    val characterEpisodes = _selectedCharacter.value.episode

                    //pega a lista de episódios do personagem
                    characterEpisodes?.forEach {
                        val episodeId = it.substringAfterLast("/").toInt()
                        _allEpisodes.value.forEach {
                            it.episodes?.forEach {
                                if (it.episodeId == episodeId) {
                                    list.add(it)
                                }
                            }
                        }
                    }
                    var season = 0
                    var listEpisodesPerSeason = mutableListOf<EpisodeTmdb>()
                    for (i in list.indices) {

                        if (season == list[i].seasonNumber) {
                            listEpisodesPerSeason.add(list[i])
                        } else {
                            if (listEpisodesPerSeason.isNotEmpty()) {
                                _allEpisodes.value.forEach {
                                    if (it.seasonNumber == season) {
                                        seasonEpisodes.add(
                                            SeasonTmdb(
                                                it.air_date,
                                                listEpisodesPerSeason,
                                                it.name,
                                                it.overview,
                                                it.posterPath,
                                                it.seasonNumber
                                            )
                                        )
                                    }
                                }
                            }
                            season = list[i].seasonNumber!!
                            listEpisodesPerSeason = mutableListOf<EpisodeTmdb>()
                            listEpisodesPerSeason.add(list[i])
                        }
                        if (list.lastIndex == i) {
                            if (listEpisodesPerSeason.isNotEmpty()) {
                                _allEpisodes.value.forEach {
                                    if (it.seasonNumber == season) {
                                        seasonEpisodes.add(
                                            SeasonTmdb(
                                                it.air_date,
                                                listEpisodesPerSeason,
                                                it.name,
                                                it.overview,
                                                it.posterPath,
                                                it.seasonNumber
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        _characterEpisodes.value = seasonEpisodes
    }

    //Armazena a lista de personagens que participam no episódio
    private var _episodeCharacters = MutableStateFlow(mutableListOf<Character>())
    var episodeCharacters: StateFlow<MutableList<Character>> = _episodeCharacters

    //Armazena os dados do episódio selecionado
    private val _episodeSelectedData = MutableStateFlow(EpisodeTmdb())
    val episodeSelectedData: StateFlow<EpisodeTmdb>
    get() = _episodeSelectedData

    //Retorna os dados do episódio selecionado
    fun selectedEpisode(id: Int) {
        getCharactersFromEpisodeStringList(id)
        var episodeTmdb = EpisodeTmdb()
        _allEpisodes.value.forEach {
            it.episodes!!.forEach {
                if (it.episodeId == id) {
                    episodeTmdb = it
                }
            }
        }
        _episodeSelectedData.value = episodeTmdb
    }

    private val _numberOfSeasons = MutableStateFlow(TmdbSeasonsInfo())

    //Esta função cria um temporizador para alterar a startDestination, pois apenas o popBackStack apenas eliminava a
    //tela anterior, porém as outras telas não funcionavam corretamente, ele abria sempre uma nova tela, uma acima da outra
    //deixando várias telas character, por exemplo.
    private fun delayForSplash() {
        viewModelScope.launch {
            delay(3200L)
            _splash.value = false
        }
    }

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
            _episodes.forEach { season ->
                season.episodes?.forEach { episode ->
                    count++
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
        delayForSplash()
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
    private fun getCharactersFromEpisodeStringList(episode: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            _characterEpisodeString.value = repository.getCharactersFromEpisode(episode)
            _isLoading.value = false
        }
        val char = mutableListOf<Character>()
        _characterEpisodeString.value.characters?.forEach {
            val character = it.substringAfterLast("/").toInt()
            _characterList.value.result?.forEach {
                if (character == it.id) {
                    char.add(it)
                }
            }
        }
        _episodeCharacters.value = char
    }

    //Retorna a data para o formato correto
    fun dateFormatter(date: String): String {

        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        return formatter.format(parser.parse(date))
    }

}