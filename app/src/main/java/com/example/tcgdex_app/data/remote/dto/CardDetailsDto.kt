package com.example.tcgdex_app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CardDetailsDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("image") val image: String? = null,
    @SerialName("category") val category: String = "",
    @SerialName("illustrator") val illustrator: String? = null,
    @SerialName("rarity") val rarity: String? = null,
    @SerialName("set") val set: SetDetailsDto? = null,
    @SerialName("hp") val hp: Int? = null,
    @SerialName("types") val types: List<String> = emptyList(),
    @SerialName("evolveFrom") val evolveFrom: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("stage") val stage: String? = null,
    @SerialName("attacks") val attacks: List<AttackDto> = emptyList(),
    @SerialName("weaknesses") val weaknesses: List<WeaknessDto> = emptyList(),
    @SerialName("retreat") val retreat: Int? = null,
    )

data class SetDetailsDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
)

@Serializable
data class AttackDto(
    @SerialName("name") val name: String,
    @SerialName("cost") val cost: List<String> = emptyList(),
    @SerialName("damage") val damage: Int? = null,
    @SerialName("effect") val effect: String? = null,
)

@Serializable
data class WeaknessDto(
    @SerialName("type") val type: String,
    @SerialName("value") val value: String,
)