package com.example.tcgdex_app.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tcgdex_app.presentation.details.CardDetailsRoute
import com.example.tcgdex_app.presentation.search.SearchCardsRoute

object Routes {
    const val SEARCH = "search"
    const val DETAILS = "details"
    const val DETAILS_ROUTE = "$DETAILS/{id}"

    fun details(id: String) = "$DETAILS/${id}"
}


@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Routes.SEARCH
    ) {
        composable(Routes.SEARCH) {
            SearchCardsRoute(
                onCardClick = { id ->
                    navController.navigate(Routes.details(id)){
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Routes.DETAILS_ROUTE,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
            )
        ) {
            CardDetailsRoute(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
