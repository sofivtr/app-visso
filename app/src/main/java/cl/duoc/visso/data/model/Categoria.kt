package cl.duoc.visso.data.model

import com.google.gson.annotations.SerializedName

data class Categoria(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String
)