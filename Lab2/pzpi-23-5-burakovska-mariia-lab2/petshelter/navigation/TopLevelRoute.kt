package ua.nure.petshelter.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ua.nure.petshelter.R

data class TopLevelRoute<T: Any>(
    val route: T,
    @param:StringRes val title: Int,
    @param:DrawableRes val selectedIcon: Int,
    @param:DrawableRes val unselectedIcon: Int,
)

val topLevelRoutes = listOf<TopLevelRoute<NestedGraph>>(

    TopLevelRoute(
        route = NestedGraph.Animals,
        title = R.string.pets,
        selectedIcon = R.drawable.animals_active,
        unselectedIcon = R.drawable.animals_passive

    ),
    TopLevelRoute(
        route = NestedGraph.Donations,
        title = R.string.donations,
        selectedIcon = R.drawable.donations_active,
        unselectedIcon = R.drawable.donations_passive
    ),
    TopLevelRoute(
        route = NestedGraph.Profile,
        title = R.string.profile,
        selectedIcon = R.drawable.profile_active,
        unselectedIcon = R.drawable.profile_passive
    ),

)