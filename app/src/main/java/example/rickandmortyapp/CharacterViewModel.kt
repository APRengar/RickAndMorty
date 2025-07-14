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
        page: Int = currentPage,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        if (isLoading || endReached) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters(
                    page = page,
                    name = name,
                    status = status,
                    species = species,
                    gender = gender
                )

                if (response.results.isEmpty()) {
                    endReached = true
                } else {
                    _characters.value = _characters.value + response.results
                    currentPage++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPagination() {
        currentPage = 1
        endReached = false
        _characters.value = emptyList()
    }

    fun searchCharacters(name: String) {
        resetPagination()
        loadCharacters(name = name)
    }

    fun applyFilters(filter: CharacterFilter) {
        currentFilter = filter
        resetPagination()
        loadCharacters(
            //name = filter.name,
            status = filter.status,
            species = filter.species,
            gender = filter.gender
        )
    }

    fun getCurrentPage(): Int = currentPage
    fun isLoading(): Boolean = isLoading
}