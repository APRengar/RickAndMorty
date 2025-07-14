package example.rickandmortyapp.data.local

import example.rickandmortyapp.RickAndMortyApi
import example.rickandmortyapp.RickMortyCharacter
import example.rickandmortyapp.data.local.toEntity
import example.rickandmortyapp.data.local.toDomain

class CharacterRepository(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) {

    suspend fun getCharacters(
        forceRefresh: Boolean = false
    ): List<RickMortyCharacter> {
        return try {
            if (forceRefresh) {
                val fromApi = api.getCharacters().results
                dao.clearAll()
                dao.insertAll(fromApi.map { it.toEntity() })
                fromApi
            } else {
                val cached = dao.getAllCharacters()
                if (cached.isNotEmpty()) {
                    cached.map { it.toDomain() }
                } else {
                    val fromApi = api.getCharacters().results
                    dao.insertAll(fromApi.map { it.toEntity() })
                    fromApi
                }
            }
        } catch (e: Exception) {
            // fallback to cache on error
            dao.getAllCharacters().map { it.toDomain() }
        }
    }

    suspend fun getCharacterDetail(id: Int): RickMortyCharacter? {
        // Сначала пробуем получить данные из локальной базы
        val cached = dao.getCharacterById(id)?.toDomain()
        if (cached != null) {
            return cached
        }

        // Если в БД нет — пробуем запросить из API
        return try {
            val apiCharacter = api.getCharacterDetail(id)
            dao.insertCharacter(apiCharacter.toEntity())
            apiCharacter
        } catch (e: Exception) {
            null // не удалось получить даже из API
        }
    }

    suspend fun preloadAllCharactersIfNeeded() {
        try {
            // Если база уже заполнена — пропускаем
            val cached = dao.getAllCharacters()
            if (cached.isNotEmpty()) return

            // Загружаем всех с API и сохраняем
            var page = 1
            var allCharacters = mutableListOf<RickMortyCharacter>()

            while (true) {
                val response = api.getCharacters(page = page)
                allCharacters.addAll(response.results)

                // Если дошли до последней страницы — выходим
                if (response.info.next == null) break

                page++
            }

            // Сохраняем всех
            dao.insertAll(allCharacters.map { it.toEntity() })

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}