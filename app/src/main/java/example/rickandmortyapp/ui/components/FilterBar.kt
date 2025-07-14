package example.rickandmortyapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable

    @Composable
    fun FilterBar(
        selectedStatus: String,
        onStatusChange: (String) -> Unit,
        selectedSpecies: String,
        onSpeciesChange: (String) -> Unit,
        selectedGender: String,
        onGenderChange: (String) -> Unit
    ) {
        Column {
            DropdownField(
                label = "Status",
                options = listOf("Alive", "Dead", "unknown"),
                selectedOption = selectedStatus,
                onOptionSelected = onStatusChange
            )
            DropdownField(
                label = "Species",
                options = listOf("Human", "Alien", "Robot", "Animal", "Mythological Creature", "unknown"),
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
