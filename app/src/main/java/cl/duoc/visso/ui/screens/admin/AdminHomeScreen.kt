package cl.duoc.visso.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cl.duoc.visso.data.model.Carrito
import cl.duoc.visso.ui.theme.BluePrimary
import cl.duoc.visso.utils.Resource
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavController,
    viewModel: AdminViewModel = hiltViewModel()
) {
    val ventasState by viewModel.ventas.collectAsState()
    var showActiveOnly by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Pedidos") },
                actions = {
                    IconButton(onClick = { viewModel.cargarVentas() }) {
                        Icon(Icons.Default.Refresh, "Actualizar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BluePrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            AdminBottomNavigationBar(navController = navController, currentRoute = "admin/home")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filtro de estado
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = !showActiveOnly,
                    onClick = { showActiveOnly = false },
                    label = { Text("Todos") }
                )
                FilterChip(
                    selected = showActiveOnly,
                    onClick = { showActiveOnly = true },
                    label = { Text("Solo Activos") }
                )
            }

            when (val state = ventasState) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Resource.Success -> {
                    val ventas = state.data?.filter {
                        if (showActiveOnly) it.estado == "A" else true
                    } ?: emptyList()

                    if (ventas.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (showActiveOnly) "No hay pedidos activos" else "No hay pedidos",
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(ventas) { venta ->
                                PedidoCard(venta = venta)
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = state.message ?: "Error al cargar pedidos",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.cargarVentas() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PedidoCard(venta: Carrito) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${venta.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = if (venta.estado == "A")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (venta.estado == "A") "ACTIVO" else "CERRADO",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider()

            // Cliente
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Cliente",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${venta.usuario.nombre} ${venta.usuario.apellido}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Fecha
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "Fecha",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = formatearFecha(venta.fechaCreacion),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Productos
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ShoppingBag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${venta.detalles.size} producto(s)",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Divider()

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = venta.getFormattedTotal(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }
        }
    }
}

@Composable
fun AdminBottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text("Pedidos") },
            selected = currentRoute == "admin/home",
            onClick = {
                if (currentRoute != "admin/home") {
                    navController.navigate("admin/home") {
                        popUpTo("admin/home") { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
            label = { Text("Productos") },
            selected = currentRoute == "admin/productos",
            onClick = {
                if (currentRoute != "admin/productos") {
                    navController.navigate("admin/productos")
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.People, contentDescription = null) },
            label = { Text("Usuarios") },
            selected = currentRoute == "admin/usuarios",
            onClick = {
                if (currentRoute != "admin/usuarios") {
                    navController.navigate("admin/usuarios")
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            label = { Text("Salir") },
            selected = false,
            onClick = {
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        )
    }
}

fun formatearFecha(fechaString: String): String {
    return try {
        val fecha = LocalDateTime.parse(fechaString)
        fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
    } catch (e: Exception) {
        fechaString
    }
}