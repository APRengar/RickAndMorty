package example.rickandmortyapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import example.rickandmortyapp.data.AppDatabase
import example.rickandmortyapp.data.local.CharacterRepository
import example.rickandmortyapp.data.local.toEntity
import example.rickandmortyapp.data.local.toDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).characterDao()
    private val context = application.applicationContext
    private val database = AppDatabase.getDatabase(application)
    private val characterRepository = CharacterRepository(
        api = RetrofitInstance.api,
        dao = dao
    )

    private val _characters = MutableStateFlow<List<RickMortyCharacter>>(emptyList())
    val characters = _characters.asStateFlow()

    private var currentPage = 1
    private var isLoading = false
    private var endReached = false

    private var currentFilter = CharacterFilter()

    init {
        viewModelScope.launch {
            characterRepository.preloadAllCharactersIfNeeded()
            loadCharactersSafely()
            preloadAllCharacters()
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun preloadAllCharacters() {
        viewModelScope.launch {
            if (!isNetworkAvailable()) return@launch

            var page = 1
            var isLastPage = false

            while (!isLastPage) {
                try {
                    val response = RetrofitInstance.api.getCharacters(page = page)
                    val entities = response.results.map { it.toEntity() }
                    dao.insertAll(entities)

                    // Последняя страница, если количество полученных персонажей < 20
                    if (response.results.size < 20) {
                        isLastPage = true
                    } else {
                        page++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    break // если ошибка — останавливаем цикл
                }
            }
        }
    }

    private fun loadCharactersSafely() {
        viewModelScope.launch {
            if (isNetworkAvailable()) {
                try {
                    val response = RetrofitInstance.api.getCharacters()
                    val entities = response.results.map { it.toEntity() }
                    dao.insertAll(entities)
                    _characters.value = response.results
                } catch (e: Exception) {
                    val cached = dao.getAllCharacters()
                    _characters.value = cached.map { it.toDomain() }
                }
            } else {
                val cached = dao.getAllCharacters()
                _characters.value = cached.map { it.toDomain() }
            }
        }
    }

    fun loadCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            if (isNetworkAvailable()) {
                try {
                    val response = RetrofitInstance.api.getCharacters(
                        page = page,
                        name = name,
                        status = status,
                        species = species,
                        gender = gender
                    )
                    val entities = response.results.map { it.toEntity() }
                    dao.insertAll(entities)
                    _characters.value = response.results
                } catch (e: Exception) {
                    e.printStackTrace()
                    val cached = dao.getAllCharacters()
                    _characters.value = cached.map { it.toDomain() }
                }
            } else {
                val cached = dao.getAllCharacters()
                _characters.value = cached.map { it.toDomain() }
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
            status = filter.status,
            species = filter.species,
            gender = filter.gender
        )
    }

    fun getCurrentPage(): Int = currentPage
    fun isLoading(): Boolean = isLoading
}