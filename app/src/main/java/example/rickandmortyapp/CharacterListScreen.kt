package example.rickandmortyapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import example.rickandmortyapp.ui.components.DropdownField
import example.rickandmortyapp.ui.components.FilterBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterViewModel = viewModel()
) {
    val characters by viewModel.characters.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    var selectedStatus by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    Column {
        // 🔍 Поиск
        SearchBar(
            inputField = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.loadCharacters(
                            name = it,
                            status = selectedStatus.takeIf { it.isNotEmpty() },
                            species = selectedSpecies.takeIf { it.isNotEmpty() },
                            gender = selectedGender.takeIf { it.isNotEmpty() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Search characters...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                )
            },
            expanded = false,
            onExpandedChange = {}
        ) {}

        // 🎛 Фильтры
        FilterBar(
            selectedStatus = selectedStatus,
            onStatusChange = {
                selectedStatus = it
                viewModel.loadCharacters(
                    name = searchQuery.takeIf { it.isNotEmpty() },
                    status = it.takeIf { it.isNotEmpty() },
                    species = selectedSpecies.takeIf { it.isNotEmpty() },
                    gender = selectedGender.takeIf { it.isNotEmpty() }
                )
            },
            selectedSpecies = selectedSpecies,
            onSpeciesChange = {
                selectedSpecies = it
                viewModel.loadCharacters(
                    name = searchQuery.takeIf { it.isNotEmpty() },
                    status = selectedStatus.takeIf { it.isNotEmpty() },
                    species = it.takeIf { it.isNotEmpty() },
                    gender = selectedGender.takeIf { it.isNotEmpty() }
                )
            },
            selectedGender = selectedGender,
            onGenderChange = {
                selectedGender = it
                viewModel.loadCharacters(
                    name = searchQuery.takeIf { it.isNotEmpty() },
                    status = selectedStatus.takeIf { it.isNotEmpty() },
                    species = selectedSpecies.takeIf { it.isNotEmpty() },
                    gender = it.takeIf { it.isNotEmpty() }
                )
            }
        )

        // 📦 Список персонажей
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