package cl.duoc.visso.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cl.duoc.visso.data.model.Producto
import cl.duoc.visso.ui.screens.auth.*
import cl.duoc.visso.ui.screens.home.HomeScreen
import cl.duoc.visso.ui.screens.carrito.CarritoScreen
import cl.duoc.visso.ui.screens.perfil.PerfilScreen
import cl.duoc.visso.ui.screens.cotizacion.CotizacionScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ===== AUTH FLOW =====
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ===== MAIN FLOW =====
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(Screen.Carrito.route) {
            CarritoScreen(navController)
        }

        composable(Screen.Perfil.route) {
            PerfilScreen(navController)
        }


    }
}