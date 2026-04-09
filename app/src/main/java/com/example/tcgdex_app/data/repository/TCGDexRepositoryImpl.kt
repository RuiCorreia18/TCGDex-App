package com.example.tcgdex_app.data.repository

import com.example.tcgdex_app.data.remote.api.TCGDexApiService
import com.example.tcgdex_app.data.toDomainModel
import com.example.tcgdex_app.domain.TCGDexRepository
import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.model.CardDetails
import javax.inject.Inject

class TCGDexRepositoryImpl @Inject constructor(
    private val api: TCGDexApiService
): TCGDexRepository{
    override suspend fun searchCards(name: String?): List<Card> {
        return api.searchCards(name = name)
            .map { it.toDomainModel() }
    }

    override suspend fun getCardDetails(
        id: String
    ): CardDetails {
        return api.getCardDetails(id = id).toDomainModel()
    }
}