package cl.duoc.visso.data.model

import com.google.gson.annotations.SerializedName

data class Marca(
    @SerializedName("id") val id: Long,
    @SerializedName("nombre") val nombre: String
)