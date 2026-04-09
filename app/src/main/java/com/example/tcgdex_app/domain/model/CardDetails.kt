package com.example.tcgdex_app.domain.model

data class CardDetails(
    val id: String,
    val name: String,
    val imageUrl: String,
    val category: String,
    val illustrator: String,
    val rarity: String,
    val set: String,
    val hp: Int?,
    val types: List<String>,
    val evolveFrom: String? = null,
    val description: String? = null,
    val stage: String? = null,
    val attacks: List<Attack> = emptyList(),
    val weaknesses: List<Weakness> = emptyList(),
    val retreat: Int? = null,
)

data class Attack(
    val name: String,
    val cost: List<String> = emptyList(),
    val damage: String = "",
    val effect: String? = null,
)

data class Weakness(
    val type: String,
    val value: String = "",
)