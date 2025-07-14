package example.rickandmortyapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import example.rickandmortyapp.ui.components.DropdownField
import example.rickandmortyapp.ui.components.FilterBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import example.rickandmortyapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(navController: NavController, viewModel: CharacterViewModel = viewModel()) {
    val characters by viewModel.characters.collectAsState()
    val gridState = rememberLazyGridState()

    var searchQuery by remember { mutableStateOf("") }
    var searchActive by remember { mutableStateOf(false) }

    var selectedStatus by remember { mutableStateOf("") }
    var selectedSpecies by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    // Паджинация при скролле вниз
    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo }
            .map { it.visibleItemsInfo }
            .distinctUntilChanged()
            .collectLatest { visibleItems ->
                val lastVisible = visibleItems.lastOrNull()?.index ?: 0
                val total = characters.size
                if (lastVisible >= total - 4) {
                    viewModel.loadCharacters(
                        page = viewModel.getCurrentPage(),
                        name = searchQuery.ifBlank { null },
                        status = selectedStatus.ifBlank { null },
                        species = selectedSpecies.ifBlank { null },
                        gender = selectedGender.ifBlank { null }
                    )
                }
            }
    }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
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
        ) {}

        FilterBar(
            selectedStatus = selectedStatus,
            onStatusChange = {
                selectedStatus = it
                viewModel.applyFilters(
                    CharacterFilter(
                        //name = searchQuery,
                        status = selectedStatus,
                        species = selectedSpecies,
                        gender = selectedGender
                    )
                )
            },
            selectedSpecies = selectedSpecies,
            onSpeciesChange = {
                selectedSpecies = it
                viewModel.applyFilters(
                    CharacterFilter(
                        //name = searchQuery,
                        status = selectedStatus,
                        species = selectedSpecies,
                        gender = selectedGender
                    )
                )
            },
            selectedGender = selectedGender,
            onGenderChange = {
                selectedGender = it
                viewModel.applyFilters(
                    CharacterFilter(
                        //name = searchQuery,
                        status = selectedStatus,
                        species = selectedSpecies,
                        gender = selectedGender
                    )
                )
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = gridState,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(characters) { character ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(Screen.CharacterDetail.createRoute(character.id))
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

            item(span = { GridItemSpan(2) }) {
                if (viewModel.isLoading()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}