package ua.nure.petshelter.ui.auth.login

import androidx.annotation.StringRes
import ua.nure.petshelter.BuildConfig
import ua.nure.petshelter.navigation.Screen

object Login {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnLogIn : Action
        data class OnEmailChange(val email: String) : Action
        data class OnPasswordChange(val password: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val email: String = if (BuildConfig.DEBUG) "mariia.burakovska@nure.ua" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret123" else "",
        @StringRes val loginError: Int? = null,
    )
}