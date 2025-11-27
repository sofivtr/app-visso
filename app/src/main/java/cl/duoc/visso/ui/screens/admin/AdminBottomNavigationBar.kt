package cl.duoc.visso.ui.screens.admin

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import cl.duoc.visso.ui.theme.BluePrimary

@Composable
fun AdminBottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Inventory, "Productos") },
            label = { Text("Productos") },
            selected = currentRoute == "admin/productos",
            onClick = {
                if (currentRoute != "admin/productos") {
                    navController.navigate("admin/productos") {
                        popUpTo("admin/productos") { inclusive = true }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                indicatorColor = BluePrimary.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.People, "Usuarios") },
            label = { Text("Usuarios") },
            selected = currentRoute == "admin/usuarios",
            onClick = {
                if (currentRoute != "admin/usuarios") {
                    navController.navigate("admin/usuarios") {
                        popUpTo("admin/productos") { inclusive = false }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                indicatorColor = BluePrimary.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Category, "Categorías") },
            label = { Text("Categorías") },
            selected = currentRoute == "admin/categorias",
            onClick = {
                if (currentRoute != "admin/categorias") {
                    navController.navigate("admin/categorias") {
                        popUpTo("admin/productos") { inclusive = false }
                    }
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = BluePrimary,
                selectedTextColor = BluePrimary,
                indicatorColor = BluePrimary.copy(alpha = 0.1f)
            )
        )
    }
}
