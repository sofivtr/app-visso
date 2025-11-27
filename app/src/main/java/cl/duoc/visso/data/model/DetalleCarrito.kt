package cl.duoc.visso.data.model

import cl.duoc.visso.utils.formatPrice
import com.google.gson.annotations.SerializedName

data class DetalleCarrito(
    @SerializedName("id") val id: Long,
    @SerializedName("producto") val producto: Producto,
    @SerializedName("cotizacion") val cotizacion: Any? = null,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precioUnitario") val precioUnitario: Double
) {
    fun getSubtotal(): Double = precioUnitario * cantidad
    fun getFormattedSubtotal(): String = getSubtotal().formatPrice()
}