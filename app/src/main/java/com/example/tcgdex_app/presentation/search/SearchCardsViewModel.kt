package com.example.tcgdex_app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgdex_app.domain.usecase.SearchCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SearchCardsViewModel @Inject constructor(
    private val searchCardsUseCase: SearchCardsUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchCardUiState())
    val uiState: StateFlow<SearchCardUiState> = _uiState.asStateFlow()

    init {
        searchCards(null)
    }

    fun searchCards(name: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val cards = searchCardsUseCase(name)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = cards,
                        error = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }
}