package cl.duoc.visso.ui.screens.carrito

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.visso.data.model.Carrito
import cl.duoc.visso.data.repository.CarritoRepository
import cl.duoc.visso.utils.Resource
import cl.duoc.visso.utils.SessionManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CarritoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CarritoRepository()
    private val sessionManager = SessionManager(application)

    private val _carrito = MutableStateFlow<Resource<Carrito>>(Resource.Loading())
    val carrito: StateFlow<Resource<Carrito>> = _carrito

    private val _checkoutState = MutableStateFlow<Resource<String>?>(null)
    val checkoutState: StateFlow<Resource<String>?> = _checkoutState

    init {
        loadCarrito()
    }

    fun loadCarrito() {
        viewModelScope.launch {
            _carrito.value = Resource.Loading()
            val userId = sessionManager.userId.first()
            _carrito.value = repository.obtenerCarrito(userId)
        }
    }

    fun finalizarCompra() {
        viewModelScope.launch {
            _checkoutState.value = Resource.Loading()
            val userId = sessionManager.userId.first()
            _checkoutState.value = repository.cerrarCarrito(userId)
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = null
    }
}
