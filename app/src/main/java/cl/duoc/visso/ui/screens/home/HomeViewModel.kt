package cl.duoc.visso.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.visso.data.model.Categoria
import cl.duoc.visso.data.model.Producto
import cl.duoc.visso.data.model.SolicitudCarrito
import cl.duoc.visso.data.repository.CarritoRepository
import cl.duoc.visso.data.repository.ProductoRepository
import cl.duoc.visso.utils.Resource
import cl.duoc.visso.utils.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val productoRepository = ProductoRepository()
    private val carritoRepository = CarritoRepository()
    private val sessionManager = SessionManager(application)

    private val _productos = MutableStateFlow<Resource<List<Producto>>>(Resource.Loading())
    val productos: StateFlow<Resource<List<Producto>>> = _productos

    private val _categorias = MutableStateFlow<Resource<List<Categoria>>>(Resource.Loading())
    val categorias: StateFlow<Resource<List<Categoria>>> = _categorias

    private val _selectedCategory = MutableStateFlow<Long?>(null)
    val selectedCategory: StateFlow<Long?> = _selectedCategory

    private val _addToCartState = MutableStateFlow<Resource<String>?>(null)
    val addToCartState: StateFlow<Resource<String>?> = _addToCartState

    val filteredProducts: StateFlow<List<Producto>> = combine(
        _productos,
        _selectedCategory
    ) { productosResource, categoryId ->
        when (productosResource) {
            is Resource.Success -> {
                val productos = productosResource.data ?: emptyList()
                if (categoryId == null) {
                    productos
                } else {
                    productos.filter { it.categoria.id == categoryId }
                }
            }
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadProductos()
        loadCategorias()
    }

    private fun loadProductos() {
        viewModelScope.launch {
            _productos.value = productoRepository.listarProductos()
        }
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            _categorias.value = productoRepository.listarCategorias()
        }
    }

    fun selectCategory(categoryId: Long?) {
        _selectedCategory.value = categoryId
    }

    fun agregarAlCarrito(productoId: Long) {
        viewModelScope.launch {
            _addToCartState.value = Resource.Loading()
            val userId = sessionManager.userId.first()
            val solicitud = SolicitudCarrito(
                usuarioId = userId,
                productoId = productoId,
                cantidad = 1
            )
            _addToCartState.value = carritoRepository.agregarProducto(solicitud)
        }
    }

    fun resetAddToCartState() {
        _addToCartState.value = null
    }
}
