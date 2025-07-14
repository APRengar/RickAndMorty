package example.rickandmortyapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import example.rickandmortyapp.data.AppDatabase
import example.rickandmortyapp.data.local.CharacterRepository
import example.rickandmortyapp.RickAndMortyApi
import example.rickandmortyapp.RickMortyCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CharacterRepository(
        api = RickAndMortyApi.create(),
        dao = AppDatabase.getDatabase(application).characterDao()
    )

    private val _character = MutableStateFlow<RickMortyCharacter?>(null)
    val character: StateFlow<RickMortyCharacter?> = _character

    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            val result = repository.getCharacterDetail(id)
            _character.value = result
        }
    }
}