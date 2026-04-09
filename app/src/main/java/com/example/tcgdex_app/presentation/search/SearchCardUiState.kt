package com.example.tcgdex_app.presentation.search

import com.example.tcgdex_app.domain.model.Card

data class SearchCardUiState(
    val isLoading: Boolean = false,
    val data: List<Card> = emptyList(),
    val error: String? = null
)