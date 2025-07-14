package example.rickandmortyapp.ui.Navigation

sealed class Screen(val route: String) {
    object CharacterList : Screen("character_list")
    object CharacterDetail : Screen("character_detail/{characterId}") {
        fun createRoute(characterId: Int): String = "character_detail/$characterId"
    }
}