package cl.duoc.visso.data.model

// Asegúrate de que los nombres de variables coincidan con el JSON del backend
data class Cotizacion(
    val id: Long? = null,
    val usuario: Usuario? = null, // Necesitas enviar el ID del usuario
    val producto: Producto? = null, // Necesitas enviar el ID del producto
    val nombrePaciente: String,
    val fechaReceta: String, // Enviaremos fecha como String "YYYY-MM-DD"
    val gradoOd: Double,
    val gradoOi: Double,
    val tipoLente: String,
    val tipoCristal: String,
    val antirreflejo: Boolean,
    val filtroAzul: Boolean,
    val despachoDomicilio: Boolean,
    val valorAprox: Double? = null
)