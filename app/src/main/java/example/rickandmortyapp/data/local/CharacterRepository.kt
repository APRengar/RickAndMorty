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
        return try {
            val apiCharacter = api.getCharacterDetail(id)
            dao.insertCharacter(apiCharacter.toEntity())
            apiCharacter
        } catch (e: Exception) {
            dao.getCharacterById(id)?.toDomain()
        }
    }
}