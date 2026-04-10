package com.example.tcgdex_app.domain

import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.usecase.SearchCardsUseCase
import com.example.tcgdex_app.util.MainDispatcherRule
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCardsUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: TCGDexRepository = mockk()
    private val useCase = SearchCardsUseCase(repository, mainDispatcherRule.testDispatcher)

    private val fakeCards = listOf(
        Card(id = "xy1-1", name = "Bulbasaur", imageUrl = "https://example.com/bulbasaur.png"),
        Card(id = "xy1-2", name = "Ivysaur", imageUrl = ""),
        Card(id = "xy1-3", name = "Venusaur", imageUrl = "https://example.com/venusaur.png"),
    )

    @Test
    fun `filters out cards without image`() = runTest {
        coEvery { repository.searchCards(any()) } returns fakeCards

        val result = useCase(null)

        assertEquals(2, result.size)
        assertTrue(result.none { it.imageUrl.isEmpty() })
    }

    @Test
    fun `passes query to repository`() = runTest {
        coEvery { repository.searchCards("Bulba") } returns listOf(fakeCards[0])

        useCase("Bulba")

        coVerify(exactly = 1) { repository.searchCards("Bulba") }
    }

    @Test
    fun `returns empty list when all cards have no image`() = runTest {
        val noImageCards = fakeCards.map { it.copy(imageUrl = "") }
        coEvery { repository.searchCards(any()) } returns noImageCards

        val result = useCase(null)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `passes null query to repository`() = runTest {
        coEvery { repository.searchCards(null) } returns fakeCards

        useCase(null)

        coVerify(exactly = 1) { repository.searchCards(null) }
    }

    @Test
    fun `returns all cards when all have images`() = runTest {
        val allWithImages = fakeCards.filter { it.imageUrl.isNotEmpty() }
        coEvery { repository.searchCards(any()) } returns allWithImages

        val result = useCase(null)

        assertEquals(allWithImages.size, result.size)
    }
}