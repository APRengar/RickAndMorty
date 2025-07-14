package example.rickandmortyapp

import example.rickandmortyapp.CharacterViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.clickable
import androidx.navigation.NavController
import example.rickandmortyapp.ui.components.FilterBar
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import example.rickandmortyapp.ui.components.DropdownField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(navController: NavController, viewModel: CharacterViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }

    var selectedStatus by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    Column {
        // 🔍 Поиск и фильтры
        SearchBar(
            query = searchQuery,
            onQueryChange = {
                searchQuery = it
                viewModel.searchCharacters(it) // Обновить список по поиску
            },
            onSearch = {
                viewModel.searchCharacters(searchQuery)
                searchActive = false
            },
            active = searchActive,
            onActiveChange = { searchActive = it },
            placeholder = { Text("Search characters...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Пока ничего не выводим внутри
        }
        @Composable
        fun FilterBar(
            selectedStatus: String,
            onStatusChange: (String) -> Unit,
            selectedSpecies: String,
            onSpeciesChange: (String) -> Unit,
            selectedGender: String,
            onGenderChange: (String) -> Unit
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                DropdownField(
                    label = "Status",
                    options = listOf("Alive", "Dead", "unknown"),
                    selectedOption = selectedStatus,
                    onOptionSelected = onStatusChange
                )
                DropdownField(
                    label = "Species",
                    options = listOf("Human", "Alien", "Robot", "Animal", "Mythological", "unknown"),
                    selectedOption = selectedSpecies,
                    onOptionSelected = onSpeciesChange
                )
                DropdownField(
                    label = "Gender",
                    options = listOf("Male", "Female", "Genderless", "unknown"),
                    selectedOption = selectedGender,
                    onOptionSelected = onGenderChange
                )
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(8.dp)) {
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("detail/${character.id}")
                        }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(character.image),
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = character.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "${character.species}, ${character.status}, ${character.gender}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: RickMortyCharacter) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberAsyncImagePainter(character.image),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = character.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${character.species}, ${character.status}, ${character.gender}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}