package cl.duoc.visso.ui.screens.admin

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.visso.data.model.Categoria
import cl.duoc.visso.data.model.Marca
import cl.duoc.visso.data.model.Producto
import cl.duoc.visso.data.repository.CategoriaRepository
import cl.duoc.visso.data.repository.MarcaRepository
import cl.duoc.visso.data.repository.ProductoRepository
import cl.duoc.visso.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AdminProductosViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val marcaRepository: MarcaRepository
) : ViewModel() {

    private val _productos = MutableStateFlow<Resource<List<Producto>>>(Resource.Loading())
    val productos: StateFlow<Resource<List<Producto>>> = _productos

    private val _categorias = MutableStateFlow<Resource<List<Categoria>>>(Resource.Loading())
    val categorias: StateFlow<Resource<List<Categoria>>> = _categorias

    private val _marcas = MutableStateFlow<Resource<List<Marca>>>(Resource.Loading())
    val marcas: StateFlow<Resource<List<Marca>>> = _marcas

    private val _operationState = MutableStateFlow<Resource<String>>(Resource.Idle())
    val operationState: StateFlow<Resource<String>> = _operationState

    init {
        cargarProductos()
        cargarCategorias()
        cargarMarcas()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = Resource.Loading()
            _productos.value = productoRepository.obtenerProductos()
        }
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            _categorias.value = Resource.Loading()
            _categorias.value = categoriaRepository.obtenerCategorias()
        }
    }

    private fun cargarMarcas() {
        viewModelScope.launch {
            _marcas.value = Resource.Loading()
            _marcas.value = marcaRepository.obtenerMarcas()
        }
    }

    fun crearProducto(
        codigo: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        tipo: String,
        imagenUrl: String,
        categoria: Categoria,
        marca: Marca
    ) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            val producto = Producto(
                codigoProducto = codigo,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = stock,
                tipo = tipo,
                fechaCreacion = "",
                imagenUrl = imagenUrl,
                categoria = categoria,
                marca = marca
            )
            val resultado = productoRepository.crearProducto(producto)
            _operationState.value = when (resultado) {
                is Resource.Success -> {
                    cargarProductos()
                    Resource.Success("Producto creado exitosamente")
                }
                is Resource.Error -> Resource.Error(resultado.message ?: "Error al crear producto")
                else -> Resource.Error("Error desconocido")
            }
        }
    }

    fun actualizarProducto(
        id: Long,
        codigo: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int,
        tipo: String,
        imagenUrl: String,
        categoria: Categoria,
        marca: Marca
    ) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            val producto = Producto(
                id = id,
                codigoProducto = codigo,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = stock,
                tipo = tipo,
                fechaCreacion = "",
                imagenUrl = imagenUrl,
                categoria = categoria,
                marca = marca
            )
            val resultado = productoRepository.actualizarProducto(id, producto)
            _operationState.value = when (resultado) {
                is Resource.Success -> {
                    cargarProductos()
                    Resource.Success("Producto actualizado exitosamente")
                }
                is Resource.Error -> Resource.Error(resultado.message ?: "Error al actualizar producto")
                else -> Resource.Error("Error desconocido")
            }
        }
    }

    fun eliminarProducto(id: Long) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            val resultado = productoRepository.eliminarProducto(id)
            _operationState.value = when (resultado) {
                is Resource.Success -> {
                    cargarProductos()
                    Resource.Success("Producto eliminado exitosamente")
                }
                is Resource.Error -> Resource.Error(resultado.message ?: "Error al eliminar producto")
                else -> Resource.Error("Error desconocido")
            }
        }
    }

    fun resetOperationState() {
        _operationState.value = Resource.Idle()
    }

    fun procesarImagen(context: Context, uri: Uri): String {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "producto_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            
            "/images/$fileName"
        } catch (e: Exception) {
            ""
        }
    }
}
