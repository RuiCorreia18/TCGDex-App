package com.example.tcgdex_app.presentation.search

import app.cash.turbine.test
import com.example.tcgdex_app.domain.model.Card
import com.example.tcgdex_app.domain.usecase.SearchCardsUseCase
import com.example.tcgdex_app.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchCardsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchCardsUseCase: SearchCardsUseCase = mockk()

    private val fakeCards = listOf(
        Card(id = "xy1-1", name = "Bulbasaur", imageUrl = "https://example.com/bulbasaur.png"),
        Card(id = "xy1-2", name = "Ivysaur", imageUrl = "https://example.com/ivysaur.png"),
    )

    private fun createViewModel() = SearchCardsViewModel(searchCardsUseCase)

    @Test
    fun `init triggers searchCards with null and emits loaded state`() = runTest {
        coEvery { searchCardsUseCase(null) } returns fakeCards

        val viewModel = createViewModel()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(fakeCards, state.data)
            assertTrue(!state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchCards with query updates state with results`() = runTest {
        coEvery { searchCardsUseCase(null) } returns emptyList()
        coEvery { searchCardsUseCase("Bulba") } returns listOf(fakeCards[0])

        val viewModel = createViewModel()

        viewModel.searchCards("Bulba")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(fakeCards[0]), state.data)
            assertTrue(!state.isLoading)
            assertNull(state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `new search cancels previous in-flight request`() = runTest {
        coEvery { searchCardsUseCase(null) } returns emptyList()
        coEvery { searchCardsUseCase("Bulba") } coAnswers {
            delay(1000)
            listOf(fakeCards[0])
        }
        coEvery { searchCardsUseCase("Ivy") } returns listOf(fakeCards[1])

        val viewModel = createViewModel()

        viewModel.searchCards("Bulba")
        viewModel.searchCards("Ivy")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(fakeCards[1]), state.data)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { searchCardsUseCase("Ivy") }
    }

    @Test
    fun `searchCards emits error state when use case throws`() = runTest {
        coEvery { searchCardsUseCase(null) } returns emptyList()
        coEvery { searchCardsUseCase("fail") } throws RuntimeException("Network error")

        val viewModel = createViewModel()

        viewModel.searchCards("fail")

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(!state.isLoading)
            assertEquals("Network error", state.error)
            assertTrue(state.data.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchCards uses fallback message when exception has no message`() = runTest {
        coEvery { searchCardsUseCase(null) } returns emptyList()
        coEvery { searchCardsUseCase(any()) } throws RuntimeException()

        val viewModel = createViewModel()
        viewModel.searchCards("anything")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Something went wrong", state.error)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchCards with null query calls use case with null`() = runTest {
        coEvery { searchCardsUseCase(null) } returns fakeCards

        val viewModel = createViewModel()
        viewModel.searchCards(null)

        coVerify(atLeast = 1) { searchCardsUseCase(null) }
    }

    @Test
    fun `searchCards clears previous error on new search`() = runTest {
        coEvery { searchCardsUseCase(null) } returns emptyList()
        coEvery { searchCardsUseCase("bad") } throws RuntimeException("oops")
        coEvery { searchCardsUseCase("good") } returns fakeCards

        val viewModel = createViewModel()
        viewModel.searchCards("bad")

        viewModel.searchCards("good")

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
            assertEquals(fakeCards, state.data)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
