package com.gems.yandexdo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gems.yandexdo.screen.MainScreen
import com.gems.yandexdo.screen.TaskScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.Main.route
) {
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationItem.Main.route) {
            MainScreen(navController)
        }

        composable(NavigationItem.Task.route) {
            TaskScreen(navController)
        }

    }
}

