package cl.duoc.visso.data.repository

import cl.duoc.visso.data.model.Usuario
import cl.duoc.visso.data.remote.RetrofitClient
import cl.duoc.visso.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsuarioRepository {
    private val api = RetrofitClient.apiService

    suspend fun obtenerPerfil(usuarioId: Long): Resource<Usuario> = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerPerfil(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al cargar perfil")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }

    suspend fun actualizarPerfil(usuarioId: Long, usuario: Usuario): Resource<Usuario> = withContext(Dispatchers.IO) {
        try {
            val response = api.actualizarPerfil(usuarioId, usuario)
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Error al actualizar perfil")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Error de conexión")
        }
    }
}