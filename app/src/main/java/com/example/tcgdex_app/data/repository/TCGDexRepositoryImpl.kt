package com.example.tcgdex_app.data.repository

import com.example.tcgdex_app.data.remote.api.TCGDexApiService
import com.example.tcgdex_app.domain.TCGDexRepository
import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.model.CardDetails
import javax.inject.Inject

class TCGDexRepositoryImpl @Inject constructor(
    private val api: TCGDexApiService
): TCGDexRepository{
    override suspend fun searchCards(name: String?): List<Card> {
        TODO("Not yet implemented")
    }

    override suspend fun getCardDetails(
        id: String
    ): CardDetails {
        TODO("Not yet implemented")
    }
}