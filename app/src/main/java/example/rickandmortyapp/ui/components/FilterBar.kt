package example.rickandmortyapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            options = listOf("") + listOf("Alive", "Dead", "unknown"),
            selectedOption = selectedStatus,
            onOptionSelected = onStatusChange
        )
        DropdownField(
            label = "Species",
            options = listOf("") + listOf("Human", "Alien", "Robot", "Animal", "Mythological Creature", "unknown"),
            selectedOption = selectedSpecies,
            onOptionSelected = onSpeciesChange
        )
        DropdownField(
            label = "Gender",
            options = listOf("") + listOf("Male", "Female", "Genderless", "unknown"),
            selectedOption = selectedGender,
            onOptionSelected = onGenderChange
        )
    }
}
