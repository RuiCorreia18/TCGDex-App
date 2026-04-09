package com.example.tcgdex_app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardDto(
    @SerialName("id") val id: String,
    @SerialName("localId") val localId: String,
    @SerialName("name") val name: String,
    @SerialName("image") val image: String? = null,
)
