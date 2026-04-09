package com.example.tcgdex_app.domain.usecase

import com.example.tcgdex_app.domain.TCGDexRepository
import com.example.tcgdex_app.domain.model.Card
import javax.inject.Inject

class SearchCardsUseCase @Inject constructor(
    private val repository: TCGDexRepository
){
    suspend operator fun invoke(name: String?): List<Card>{
        return repository.searchCards(name)
    }
}