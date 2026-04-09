package com.example.tcgdex_app.data.remote.api

import com.example.tcgdex_app.data.remote.dto.CardDetailsDto
import com.example.tcgdex_app.data.remote.dto.CardDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TCGDexApiService {

    @GET("cards")
    suspend fun searchCards(
        @Query("name") name: String?,
    ): List<CardDto>

    /**
     * Get full details for a single card by its ID.
     */
    @GET("cards/{id}")
    suspend fun getCardDetails(
        @Path("id") id: String,
    ): CardDetailsDto
}