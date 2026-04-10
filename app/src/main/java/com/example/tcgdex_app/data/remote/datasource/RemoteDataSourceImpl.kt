package com.example.tcgdex_app.data.remote.datasource

import com.example.tcgdex_app.data.remote.api.TCGDexApiService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: TCGDexApiService
) : RemoteDataSource {
    override suspend fun searchCards(name: String?) = api.searchCards(name)
    override suspend fun getCardDetails(id: String) = api.getCardDetails(id)
}