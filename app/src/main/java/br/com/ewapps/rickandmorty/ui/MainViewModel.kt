package br.com.ewapps.rickandmorty.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.ewapps.rickandmorty.MainApp
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.InfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


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

    //Atualiza se os dados estão sendo baixados
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    //Função que pega os dados de páginas e total de personagens
    fun getInfo() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _infoResponse.value = repository.getInfo()
            _isLoading.value = false
            _infoResponse.value.info?.pages?.let { getCharacters(it) }
            Log.d("Pages: ", "${_infoResponse.value.info!!.pages}")
        }
    }

    //Armazena a Lista com todos os personagens
    private val _characterList = MutableStateFlow(CharacterResponse())
    val characterList: StateFlow<CharacterResponse>
        get() = _characterList

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
    private var charList = mutableListOf<CharacterModel>()

    //Função que coleta todos os personagens da API
    fun getCharacters(pages: Int) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if (_characterList.value.result.isNullOrEmpty()) {
                var i = 1
                while (i <= pages) {
                    _characterResponse = repository.getCharacters(i)
                    _characterResponse.result?.let { charList.addAll(it) }

                    _isLoading.value = false
                    Log.d("Valor de i: ", "$i")
                    i++
                }
                _characterList.update { CharacterResponse(charList) }
                Log.d("Teste:", "${charList.size}")
            }
        }
    }
}