package example.rickandmortyapp

data class CharacterResponse(
    val info: Info,
    val results: List<RickMortyCharacter>
)

data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

data class RickMortyCharacter(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)