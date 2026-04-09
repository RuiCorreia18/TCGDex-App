package com.example.tcgdex_app.data.remote.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonPrimitive

@Serializable
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
    @SerialName("abilities") val abilities: List<AbilityDto> = emptyList(),
    @SerialName("attacks") val attacks: List<AttackDto> = emptyList(),
    @SerialName("weaknesses") val weaknesses: List<WeaknessDto> = emptyList(),
    @SerialName("retreat") val retreat: Int? = null,
    )

@Serializable
data class SetDetailsDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
)

@Serializable
data class AbilityDto(
    @SerialName("type") val type: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("effect") val effect: String? = null,
)

@Serializable
data class AttackDto(
    @SerialName("name") val name: String,
    @SerialName("cost") val cost: List<String> = emptyList(),
    @Serializable(with = FlexibleDamageSerializer::class)
    @SerialName("damage") val damage: String? = null,
    @SerialName("effect") val effect: String? = null,
)

@Serializable
data class WeaknessDto(
    @SerialName("type") val type: String? = null,
    @SerialName("value") val value: String? = null,
)

object FlexibleDamageSerializer : KSerializer<String?> {
    override val descriptor = PrimitiveSerialDescriptor("FlexibleDamage", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String?) {
        if (value != null) encoder.encodeString(value) else encoder.encodeNull()
    }

    override fun deserialize(decoder: Decoder): String? {
        val json = (decoder as JsonDecoder).decodeJsonElement().jsonPrimitive
        return json.content
    }
}