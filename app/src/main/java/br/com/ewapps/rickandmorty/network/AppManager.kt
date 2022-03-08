package br.com.ewapps.rickandmorty.network

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import br.com.ewapps.rickandmorty.models.CharacterResponse
import com.squareup.moshi.Json
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppManager {
    private val _characterResponse = mutableStateOf(CharacterResponse())
    val characterResponse: State<CharacterResponse>
    @Composable get() = remember {
        _characterResponse
    }

    init {
        getCharacters()
    }


    fun getCharacters() {
        val service = ApiClient.apiService.fetchCharacters()
        service.enqueue(object : Callback<CharacterResponse> {
            override fun onResponse(
                call: Call<CharacterResponse>,
                response: Response<CharacterResponse>
            ) {
                if (response.isSuccessful) {
                    _characterResponse.value = response.body()!!
                    Log.d("Personagens", "${_characterResponse}")
                }else {
                    Log.d("erro: ", "${response.errorBody()}")
                }

            }

            override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                Log.e("erro: ", "${t.printStackTrace()}")
            }

        })
    }
}