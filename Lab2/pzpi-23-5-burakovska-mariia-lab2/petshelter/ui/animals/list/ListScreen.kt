package ua.nure.petshelter.ui.animals.list

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.petshelter.repository.dto.AnimalDto
import ua.nure.petshelter.ui.compose.PSAnimalCard
import ua.nure.petshelter.ui.compose.PScreen
import ua.nure.petshelter.ui.compose.PSTitle
import ua.nure.petshelter.ui.theme.AppTheme

@Composable
fun ListScreen(
    viewModel: ListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AnimalList.Event.OnNavigate -> navController.navigate(event.route)
            }
        }
    }

    ListScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ListScreenContent(
    state: AnimalList.State,
    onAction: (AnimalList.Action) -> Unit
) {
    PScreen {
        PSTitle(title = "Our Pets")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = AppTheme.dimension.normal,
                vertical = AppTheme.dimension.small
            )
        ) {
            items(state.animals) { animal ->
                PSAnimalCard(
                    animal = animal,
                    onClick = { onAction(AnimalList.Action.OnAnimalClick(animal.id)) }
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ListScreenPreview() {
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.color.background)) {
            ListScreenContent(
                state = AnimalList.State(
                    animals = listOf(
                        AnimalDto(
                            id = 1,
                            name = "Buddy",
                            species = "Dog",
                            breed = "Golden Retriever",
                            gender = "MALE",
                            birthDate = "2022-05-10",
                            description = "Very friendly dog",
                            status = "Available"
                        ),
                        AnimalDto(
                            id = 2,
                            name = "Luna",
                            species = "Cat",
                            breed = "Siamese",
                            gender = "FEMALE",
                            birthDate = "2023-01-15",
                            description = "Loves to play",
                            status = "Available"
                        ),
                        AnimalDto(
                            id = 3,
                            name = "Max",
                            species = "Dog",
                            breed = "Beagle",
                            gender = "MALE",
                            birthDate = "2021-11-20",
                            description = "Energetic and loyal",
                            status = "Adopted"
                        )
                    )
                ),
                onAction = {}
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ListScreenDarkPreview() {
    ListScreenPreview()
}