package com.example.tcgdex_app.domain.usecase

import com.example.tcgdex_app.domain.TCGDexRepository
import com.example.tcgdex_app.domain.model.CardDetails
import javax.inject.Inject

class GetCardDetailsUseCase @Inject constructor(
    private val repository: TCGDexRepository
){
    suspend operator fun invoke(id: String): CardDetails{
        return repository.getCardDetails(id)
    }
}