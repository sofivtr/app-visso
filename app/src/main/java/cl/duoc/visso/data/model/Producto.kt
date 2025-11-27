package cl.duoc.visso.data.model

import cl.duoc.visso.utils.formatPrice
import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id") val id: Long,
    @SerializedName("codigoProducto") val codigoProducto: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int,
    @SerializedName("tipo") val tipo: String,
    @SerializedName("fechaCreacion") val fechaCreacion: String,
    @SerializedName("imagenUrl") val imagenUrl: String?,
    @SerializedName("categoria") val categoria: Categoria,
    @SerializedName("marca") val marca: Marca
) {
    fun getFormattedPrice(): String = precio.formatPrice()
    fun getFullImageUrl(): String = "http://10.0.2.2:8081${imagenUrl ?: ""}"
}
