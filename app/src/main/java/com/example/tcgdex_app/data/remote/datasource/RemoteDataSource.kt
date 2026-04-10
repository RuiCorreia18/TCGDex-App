package com.example.tcgdex_app.data.remote.datasource

import com.example.tcgdex_app.data.remote.dto.CardDetailsDto
import com.example.tcgdex_app.data.remote.dto.CardDto

/**
 * Abstracts the network layer from the repository.
 * The repository depends on this interface, not on Retrofit directly,
 * making it easy to swap the HTTP client without touching business logic.
 */
interface RemoteDataSource {
    suspend fun searchCards(name: String?): List<CardDto>
    suspend fun getCardDetails(id: String): CardDetailsDto
}