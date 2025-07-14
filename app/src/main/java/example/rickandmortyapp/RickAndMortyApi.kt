package example.rickandmortyapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("name") name: String? = null,
        @Query("status") status: String? = null,
        @Query("species") species: String? = null,
        @Query("gender") gender: String? = null
    ): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterDetail(@Path("id") id: Int): RickMortyCharacter
    companion object {
        fun create(): RickAndMortyApi {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RickAndMortyApi::class.java)
        }
    }
}