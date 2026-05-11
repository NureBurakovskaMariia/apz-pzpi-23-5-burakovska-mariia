package ua.nure.petshelter.ui.animals.list

import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.dto.AnimalDto

object AnimalList {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
    }

    sealed interface Action {
        data class OnAnimalClick(val animalId: Int) : Action
        data object OnRefresh : Action
    }

    data class State(
        val animals: List<AnimalDto> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null
    )
}