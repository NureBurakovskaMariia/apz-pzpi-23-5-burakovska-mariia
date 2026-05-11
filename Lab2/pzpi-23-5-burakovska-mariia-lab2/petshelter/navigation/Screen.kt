package ua.nure.petshelter.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object EmptyScreen : Screen()

    @Serializable
    sealed class Auth : Screen() {
        @Serializable
        data object Registration : Auth()
        @Serializable
        data object SignIn : Auth()
    }

    @Serializable
    data object Profile : Screen()

    @Serializable
    sealed class Animals : Screen() {
        @Serializable
        data object List : Animals()

        @Serializable
        data class Details(val animalId: Int) : Animals()
    }

    @Serializable
    data object Donations : Screen()
}