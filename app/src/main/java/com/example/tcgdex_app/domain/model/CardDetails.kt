package com.example.tcgdex_app.domain.model

data class CardDetails(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val category: String?,
    val illustrator: String?,
    val rarity: String?,
    val hp: Int?,
    val types: List<String>
)
