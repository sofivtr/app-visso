package cl.duoc.visso.data.remote

import cl.duoc.visso.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/registro")
    suspend fun registrar(@Body usuario: Usuario): Response<Usuario>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<Usuario>

    @POST("api/auth/recuperar-password")
    suspend fun recuperarPassword(@Body request: RecuperarPasswordRequest): Response<Map<String, String>>

    @GET("api/productos")
    suspend fun listarProductos(): Response<List<Producto>>

    @GET("api/categorias")
    suspend fun listarCategorias(): Response<List<Categoria>>

    @GET("api/carrito/{usuarioId}")
    suspend fun obtenerCarrito(@Path("usuarioId") usuarioId: Long): Response<Carrito>

    @POST("api/carrito/agregar")
    suspend fun agregarAlCarrito(@Body solicitud: SolicitudCarrito): Response<Unit>

    @POST("api/carrito/cerrar/{usuarioId}")
    suspend fun cerrarCarrito(@Path("usuarioId") usuarioId: Long): Response<Unit>

    @GET("api/usuarios/{id}")
    suspend fun obtenerPerfil(@Path("id") id: Long): Response<Usuario>

    @PUT("api/usuarios/{id}")
    suspend fun actualizarPerfil(@Path("id") id: Long, @Body usuario: Usuario): Response<Usuario>
}