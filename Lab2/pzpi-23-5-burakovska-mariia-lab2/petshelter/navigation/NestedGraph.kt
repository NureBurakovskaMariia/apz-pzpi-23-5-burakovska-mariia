package ua.nure.petshelter.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NestedGraph {
    @Serializable data object Profile : NestedGraph()
    @Serializable data object Animals : NestedGraph()

    @Serializable data object Donations : NestedGraph()
}