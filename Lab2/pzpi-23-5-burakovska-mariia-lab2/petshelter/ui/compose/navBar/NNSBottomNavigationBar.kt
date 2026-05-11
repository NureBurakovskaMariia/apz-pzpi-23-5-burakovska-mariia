package ua.nure.petshelter.ui.compose.navBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import ua.nure.petshelter.navigation.NestedGraph
import ua.nure.petshelter.navigation.TopLevelRoute

@Composable
fun NNSBottomNavigationBar(
    modifier: Modifier = Modifier,
    currentDestination: NavDestination?,
    items: List<TopLevelRoute<NestedGraph>>,
    onItemSelect: (NestedGraph) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { topLevelRoute ->
            NNSNavigationBarItem(
                item = topLevelRoute,
                isSelected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        route = topLevelRoute.route::class
                    )
                } == true,
            ) {
                onItemSelect(topLevelRoute.route)
            }
        }
    }
}