package com.example.tcgdex_app.domain

import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.model.CardDetails

interface TCGDexRepository {
    suspend fun searchCards(name: String?): List<Card>
    suspend fun getCardDetails(id: String): CardDetails
}