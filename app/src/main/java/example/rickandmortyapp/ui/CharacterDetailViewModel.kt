package example.rickandmortyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import example.rickandmortyapp.RetrofitInstance
import example.rickandmortyapp.RickMortyCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel : ViewModel() {

    private val _character = MutableStateFlow<RickMortyCharacter?>(null)
    val character: StateFlow<RickMortyCharacter?> = _character

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacterDetail(id)
                _character.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}