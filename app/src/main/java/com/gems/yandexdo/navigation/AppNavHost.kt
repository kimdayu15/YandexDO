package com.gems.yandexdo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gems.yandexdo.data.TodoItemsRepository
import com.gems.yandexdo.screen.MainScreen
import com.gems.yandexdo.screen.TaskScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = NavigationItem.Main.route
) {
    val repository = TodoItemsRepository(context = LocalContext.current)
    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        composable(NavigationItem.Main.route) {
            MainScreen(navController, repository)
        }

        composable(NavigationItem.TaskScreen.route) {
            TaskScreen(navController = navController, repository = repository)
        }

        composable("${NavigationItem.TaskScreen.route}/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            if (taskId != null) {
                TaskScreen(navController = navController, repository = repository, taskId = taskId)
            }
        }

    }
}

