package com.example.tcgdex_app.presentation.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.tcgdex_app.domain.model.*
import com.example.tcgdex_app.domain.usecase.GetCardDetailsUseCase
import com.example.tcgdex_app.util.MainDispatcherRule
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CardDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCardDetailsUseCase: GetCardDetailsUseCase = mockk()

    private val fakeCardDetails = CardDetails(
        id = "xy1-1",
        name = "Bulbasaur",
        imageUrl = "https://example.com/bulbasaur.png",
        category = "Pokemon",
        illustrator = "Ken Sugimori",
        rarity = "Common",
        set = "XY Base Set",
        hp = 60,
        types = listOf("Grass"),
        evolveFrom = null,
        description = "A strange seed was planted on its back at birth.",
        stage = "Basic",
        abilities = emptyList(),
        attacks = listOf(
            Attack(name = "Tackle", cost = listOf("Colorless"), damage = "10")
        ),
        weaknesses = listOf(Weakness(type = "Fire", value = "×2")),
        retreat = 1,
    )

    private fun createViewModel(cardId: String = "xy1-1") = CardDetailsViewModel(
        getCardDetailsUseCase = getCardDetailsUseCase,
        savedStateHandle = SavedStateHandle(mapOf("id" to cardId)),
    )

    @Test
    fun `init loads card details successfully`() = runTest {
        coEvery { getCardDetailsUseCase("xy1-1") } returns fakeCardDetails

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(fakeCardDetails, state.data)
            assertTrue(!state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits error state when use case throws`() = runTest {
        coEvery { getCardDetailsUseCase("xy1-1") } throws RuntimeException("Network error")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(!state.isLoading)
            assertEquals("Network error", state.error)
            assertNull(state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `init emits fallback error message when exception has no message`() = runTest {
        coEvery { getCardDetailsUseCase("xy1-1") } throws RuntimeException()

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Failed to load card details", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry reloads card details after error`() = runTest {
        coEvery { getCardDetailsUseCase("xy1-1") } throws RuntimeException("Network error")

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertEquals("Network error", errorState.error)

            coEvery { getCardDetailsUseCase("xy1-1") } returns fakeCardDetails
            viewModel.retry()

            val successState = awaitItem()
            assertEquals(fakeCardDetails, successState.data)
            assertNull(successState.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `retry calls use case with correct id`() = runTest {
        coEvery { getCardDetailsUseCase("xy1-1") } returns fakeCardDetails

        val viewModel = createViewModel()
        viewModel.retry()

        coVerify(atLeast = 2) { getCardDetailsUseCase("xy1-1") }
    }
}