package com.example.tcgdex_app.presentation.details

import com.example.tcgdex_app.domain.model.CardDetails

data class CardDetailsUiState(
    val isLoading: Boolean = false,
    val data: CardDetails? = null,
    val error: String? = null
)