package example.rickandmortyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import example.rickandmortyapp.ui.CharacterDetailScreen
import example.rickandmortyapp.ui.Navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.CharacterList.route) {
                    composable(route = Screen.CharacterList.route) {
                        CharacterListScreen(navController)
                    }
                    composable(route = Screen.CharacterDetail.route) { backStackEntry ->
                        val characterId = backStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                        characterId?.let {
                            CharacterDetailScreen(characterId, navController)
                        }
                    }
                }
            }
        }
    }
}