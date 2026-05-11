package ua.nure.petshelter.ui.animals.details

import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.dto.AnimalDto

object Details {
    sealed interface Event {
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBackClick : Action
        data object OnAdoptClick : Action
    }

    data class State(
        val animal: AnimalDto? = null,
        val isLoading: Boolean = true,
        val error: String? = null
    )
}