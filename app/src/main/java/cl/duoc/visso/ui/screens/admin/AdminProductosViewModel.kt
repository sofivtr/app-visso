package cl.duoc.visso.ui.screens.admin

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.visso.data.model.Categoria
import cl.duoc.visso.data.model.Marca
import cl.duoc.visso.data.model.Producto
import cl.duoc.visso.data.remote.ApiService
import cl.duoc.visso.data.repository.CategoriaRepository
import cl.duoc.visso.data.repository.MarcaRepository
import cl.duoc.visso.data.repository.ProductoRepository
import cl.duoc.visso.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class AdminProductosViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val marcaRepository: MarcaRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _productos = MutableStateFlow<Resource<List<Producto>>>(Resource.Loading())
    val productos: StateFlow<Resource<List<Producto>>> = _productos

    private val _categorias = MutableStateFlow<Resource<List<Categoria>>>(Resource.Loading())
    val categorias: StateFlow<Resource<List<Categoria>>> = _categorias

    private val _marcas = MutableStateFlow<Resource<List<Marca>>>(Resource.Loading())
    val marcas: StateFlow<Resource<List<Marca>>> = _marcas

    private val _operationState = MutableStateFlow<Resource<String>?>(null)
    val operationState: StateFlow<Resource<String>?> = _operationState

    init {
        cargarProductos()
        cargarCategorias()
        cargarMarcas()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _productos.value = Resource.Loading()
            _productos.value = productoRepository.listarProductos()
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
        imagenUriStr: String,
        categoria: Categoria,
        marca: Marca,
        context: Context
    ) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            
            Log.d("AdminProductos", "Crear producto - imagenUriStr recibido: $imagenUriStr")
            
            // Subir imagen si hay una seleccionada
            val imagenUrl = if (imagenUriStr.isNotEmpty() && imagenUriStr.startsWith("content://")) {
                Log.d("AdminProductos", "Subiendo imagen nueva desde URI")
                val uri = Uri.parse(imagenUriStr)
                val urlSubida = subirImagen(context, uri)
                Log.d("AdminProductos", "Resultado subida: $urlSubida")
                urlSubida ?: "/images/optico_rayban_1.webp"
            } else {
                Log.d("AdminProductos", "Usando URL existente o default")
                imagenUriStr.ifEmpty { "/images/optico_rayban_1.webp" }
            }
            
            Log.d("AdminProductos", "ImagenUrl final para crear: $imagenUrl")
            
            // Usar formato ISO para la fecha (YYYY-MM-DD)
            val fechaActual = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date())
            
            val producto = Producto(
                codigoProducto = codigo,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = stock,
                fechaCreacion = fechaActual,
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
        imagenUriStr: String,
        categoria: Categoria,
        marca: Marca,
        fechaCreacion: String,
        context: Context
    ) {
        viewModelScope.launch {
            _operationState.value = Resource.Loading()
            
            Log.d("AdminProductos", "Actualizar producto ID: $id - imagenUriStr: $imagenUriStr")
            
            // Subir imagen solo si se seleccionó una nueva
            val imagenUrl = if (imagenUriStr.isNotEmpty() && imagenUriStr.startsWith("content://")) {
                Log.d("AdminProductos", "Subiendo imagen nueva desde URI en actualización")
                val uri = Uri.parse(imagenUriStr)
                val urlSubida = subirImagen(context, uri)
                Log.d("AdminProductos", "Resultado subida actualización: $urlSubida")
                urlSubida ?: imagenUriStr
            } else {
                Log.d("AdminProductos", "Manteniendo URL existente: $imagenUriStr")
                imagenUriStr
            }
            
            Log.d("AdminProductos", "ImagenUrl final para actualizar: $imagenUrl")
            
            val producto = Producto(
                id = id,
                codigoProducto = codigo,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                stock = stock,
                fechaCreacion = fechaCreacion,
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
        _operationState.value = null
    }

    suspend fun subirImagen(context: Context, uri: Uri): String? {
        return try {
            Log.d("AdminProductos", "Iniciando subida de imagen: $uri")
            
            // Copiar el archivo de la URI a un archivo temporal
            val inputStream = context.contentResolver.openInputStream(uri)
            if (inputStream == null) {
                Log.e("AdminProductos", "No se pudo abrir InputStream de la URI")
                return null
            }
            
            val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            Log.d("AdminProductos", "Archivo temporal creado: ${tempFile.absolutePath}")
            
            inputStream.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            
            Log.d("AdminProductos", "Archivo copiado, tamaño: ${tempFile.length()} bytes")

            // Crear el RequestBody y MultipartBody.Part
            val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagenPart = MultipartBody.Part.createFormData("imagen", tempFile.name, requestBody)
            
            Log.d("AdminProductos", "Enviando imagen al servidor...")

            // Subir la imagen al servidor
            val response = apiService.subirImagen(imagenPart)
            
            Log.d("AdminProductos", "Respuesta recibida - Código: ${response.code()}, Exitosa: ${response.isSuccessful}")
            
            // Eliminar el archivo temporal
            tempFile.delete()

            if (response.isSuccessful && response.body() != null) {
                val imagenUrl = response.body()!!["imagenUrl"]
                Log.d("AdminProductos", "Imagen subida exitosamente: $imagenUrl")
                imagenUrl
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AdminProductos", "Error en respuesta del servidor: $errorBody")
                null
            }
        } catch (e: Exception) {
            Log.e("AdminProductos", "Error subiendo imagen: ${e.message}", e)
            e.printStackTrace()
            null
        }
    }

    fun procesarImagen(context: Context, uri: Uri): String {
        // Este método ahora solo retorna una ruta temporal
        // La subida real se hace de forma asíncrona en crearProducto/actualizarProducto
        return uri.toString()
    }
}
