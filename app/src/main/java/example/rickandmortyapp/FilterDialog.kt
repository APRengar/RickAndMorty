package example.rickandmortyapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.dp
import example.rickandmortyapp.ui.components.DropdownField

@Composable
fun FilterDialog(
    initialFilter: CharacterFilter,
    onDismiss: () -> Unit,
    onApply: (CharacterFilter) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(initialFilter.status ?: "") }
    var selectedSpecies by remember { mutableStateOf(initialFilter.species ?: "") }
    var selectedGender by remember { mutableStateOf(initialFilter.gender ?: "") }

    val statusOptions = listOf("", "alive", "dead", "unknown")
    val genderOptions = listOf("", "female", "male", "genderless", "unknown")
    val speciesOptions = listOf("", "human", "alien", "robot", "mythological", "animal", "cronenberg", "disease")

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onApply(
                    CharacterFilter(
                        status = selectedStatus.ifEmpty { null },
                        species = selectedSpecies.ifEmpty { null },
                        gender = selectedGender.ifEmpty { null }
                    )
                )
            }) { Text("Apply") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Filters") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DropdownField("Status", selectedStatus, statusOptions) { selectedStatus = it }
                DropdownField("Species", selectedSpecies, speciesOptions) { selectedSpecies = it }
                DropdownField("Gender", selectedGender, genderOptions) { selectedGender = it }
            }
        }
    )
}