package cl.duoc.visso.ui.screens.perfil

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.visso.data.model.Usuario
import cl.duoc.visso.data.repository.UsuarioRepository
import cl.duoc.visso.utils.Resource
import cl.duoc.visso.utils.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UsuarioRepository()
    private val sessionManager = SessionManager(application)

    private val _usuario = MutableStateFlow<Resource<Usuario>>(Resource.Loading())
    val usuario: StateFlow<Resource<Usuario>> = _usuario

    private val _updateState = MutableStateFlow<Resource<Usuario>?>(null)
    val updateState: StateFlow<Resource<Usuario>?> = _updateState

    init {
        loadPerfil()
    }

    private fun loadPerfil() {
        viewModelScope.launch {
            _usuario.value = Resource.Loading()
            val userId = sessionManager.userId.first()
            _usuario.value = repository.obtenerPerfil(userId)
        }
    }

    fun actualizarPerfil(usuario: Usuario) {
        viewModelScope.launch {
            _updateState.value = Resource.Loading()
            val userId = sessionManager.userId.first()
            _updateState.value = repository.actualizarPerfil(userId, usuario)
        }
    }

    suspend fun cerrarSesion() {
        sessionManager.clearSession()
    }

    fun resetUpdateState() {
        _updateState.value = null
    }
}