package com.example.tcgdex_app.domain.usecase

import com.example.tcgdex_app.domain.TCGDexRepository
import com.example.tcgdex_app.domain.model.Card
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchCardsUseCase @Inject constructor(
    private val repository: TCGDexRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    /**
     * Only cards with an image are displayed.
     * Filtering runs on Dispatchers.Default to avoid competing with UI rendering.
     */
    suspend operator fun invoke(name: String?): List<Card> =
        withContext(defaultDispatcher) {
            repository.searchCards(name)
                .filter { it.imageUrl.isNotEmpty() }
        }
}