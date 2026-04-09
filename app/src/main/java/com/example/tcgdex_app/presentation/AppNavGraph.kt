package com.example.tcgdex_app.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tcgdex_app.presentation.search.SearchCardsRoute

object Routes {
    const val SEARCH = "search"
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
                onCardClick = {
                }
            )
        }
    }
}