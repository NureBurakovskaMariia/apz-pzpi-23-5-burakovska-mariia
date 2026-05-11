package ua.nure.petshelter.ui.auth.register

import androidx.annotation.StringRes
import ua.nure.petshelter.BuildConfig
import ua.nure.petshelter.navigation.Screen

public object Register {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnRegister : Action

        data class OnNameChange(val name: String) : Action
        data class OnEmailChange(val email: String) : Action
        data class OnPasswordChange(val password: String) : Action

        data class OnPrivacyPolicyAgreementChange(val isAgreed: Boolean) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val name: String = "",
        val email: String = if (BuildConfig.DEBUG) "mariia.burakovska@nure.ua" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret123" else "",
        val isPrivacyPolicyAgreed: Boolean = false,
        @StringRes val nameError: Int? = null,
        @StringRes val emailError: Int? = null,
        @StringRes val passwordError: Int? = null,
    )
}