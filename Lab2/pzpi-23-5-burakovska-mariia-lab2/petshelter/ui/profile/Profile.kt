package ua.nure.petshelter.ui.profile

import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.dto.TaskDto // Не забудь імпортувати свій DTO

object Profile {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data class OnTaskStatusChange(val taskId: Int, val isCompleted: Boolean) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val name: String = "",
        val email: String = "",
        val role: String = "",
        val tasks: List<TaskDto> = emptyList()
    )
}