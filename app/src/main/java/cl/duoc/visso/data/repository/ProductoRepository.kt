package cl.duoc.visso.data.repository

import cl.duoc.visso.data.model.*
import cl.duoc.visso.data.remote.RetrofitClient
import cl.duoc.visso.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductoRepository {
    private val api = RetrofitClient.apiService

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
}
