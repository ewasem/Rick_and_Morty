package br.com.ewapps.rickandmorty.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.ewapps.rickandmorty.MainApp
import br.com.ewapps.rickandmorty.models.CharacterModel
import br.com.ewapps.rickandmorty.models.CharacterResponse
import br.com.ewapps.rickandmorty.models.Info
import br.com.ewapps.rickandmorty.models.InfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = getApplication<MainApp>().repository

    private var _characterResponse = CharacterResponse()

    private val _infoResponse = MutableStateFlow(InfoResponse())
    val infoResponse: StateFlow<InfoResponse>
    get() = _infoResponse

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean>
    get() = _isLoading

    fun getInfo() {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _infoResponse.value = repository.getInfo()
            _isLoading.value = false
            Log.d("Pages: ", "${_infoResponse.value.info?.pages}")
        }
    }

    private val _characterList = mutableStateListOf<CharacterModel>()
    val characterList: SnapshotStateList<CharacterModel>
    get() = _characterList

    fun getCharacters(pages: Int){
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            var i = 1
            while (i <= pages) {
                _characterResponse = repository.getCharacters(i)
                _characterResponse.result?.let { characterList.addAll(it) }
                _isLoading.value = false
                i++
            }
        }
    }
}