package example.rickandmortyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<RickMortyCharacter>>(emptyList())
    val characters = _characters.asStateFlow()

    private var currentPage = 1
    private var isLoading = false
    private var endReached = false

    private var currentFilter = CharacterFilter()

    init {
        loadCharacters()
    }

    fun loadCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(
                    page = page,
                    name = name,
                    status = status,
                    species = species,
                    gender = gender
                )
                _characters.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetPagination() {
        currentPage = 1
        endReached = false
        _characters.value = emptyList()
    }

    fun searchCharacters(name: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(name = name)
                _characters.value = response.results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun applyFilters(filter: CharacterFilter) {
        currentFilter = filter
        currentPage = 1
        endReached = false
        _characters.value = emptyList()
        loadCharacters()
    }

    suspend fun loadCharacterDetail(id: Int): RickMortyCharacter? {
        return try {
            RetrofitInstance.api.getCharacterDetail(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}