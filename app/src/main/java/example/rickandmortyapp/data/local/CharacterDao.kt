package example.rickandmortyapp.data.local

import androidx.room.*
import example.rickandmortyapp.data.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Int): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters")
    suspend fun clearAll()

    @Query("SELECT * FROM characters WHERE name LIKE '%' || :name || '%'")
    suspend fun searchByName(name: String): List<CharacterEntity>

    @Query("""
        SELECT * FROM characters 
        WHERE (:status IS NULL OR status = :status)
        AND (:species IS NULL OR species = :species)
        AND (:gender IS NULL OR gender = :gender)
    """)
    suspend fun filterCharacters(
        status: String?,
        species: String?,
        gender: String?
    ): List<CharacterEntity>
}