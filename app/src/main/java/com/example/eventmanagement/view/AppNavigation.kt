package com.example.eventmanagement.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eventmanagement.repository.SessionManager
import androidx.navigation.NavType

@Composable
fun AppNavigation(navController: NavHostController) {

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    NavHost(
        navController = navController,
        startDestination = if (sessionManager.isLoggedIn()) "home" else "login"
    ) {
        composable("signup") { SignupPage(navController) }
        composable("home") { HomePage(navController) }
        composable("hosting") { HostingPage(navController) }
        composable("attend") { AttendPage(navController) }
        composable("login") { LoginPage(navController) }
        composable(
            "register/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "Default"
            RegisterPage(navController, id)
        }
        composable("dashboard") { UserHackathonPage(navController) }
    }
}