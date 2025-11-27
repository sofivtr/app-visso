package cl.duoc.visso.data.repository

import cl.duoc.visso.data.model.*
import cl.duoc.visso.data.remote.ApiService
import cl.duoc.visso.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun listarProductos(): Resource<List<Producto>> = withContext(Dispatchers.IO) {
        try {
            val response = api.listarProductos()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al cargar productos")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun listarCategorias(): Resource<List<Categoria>> = withContext(Dispatchers.IO) {
        try {
            val response = api.listarCategorias()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al cargar categorías")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun crearProducto(producto: Producto): Resource<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = api.crearProducto(producto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al crear producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun actualizarProducto(id: Long, producto: Producto): Resource<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = api.actualizarProducto(id, producto)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al actualizar producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun eliminarProducto(id: Long): Resource<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.eliminarProducto(id)
            if (response.isSuccessful) {
                Resource.Success("Producto eliminado")
            } else {
                Resource.Error("Error al eliminar producto")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun listarMarcas(): Resource<List<Marca>> = withContext(Dispatchers.IO) {
        try {
            val response = api.listarMarcas()
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al cargar marcas")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun obtenerProductoPorId(id: Long): Resource<Producto> = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerProducto(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Producto no encontrado")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }
}