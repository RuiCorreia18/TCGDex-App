package com.example.tcgdex_app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgdex_app.domain.usecase.SearchCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class SearchCardsViewModel @Inject constructor(
    private val searchCardsUseCase: SearchCardsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchCardUiState())
    val uiState: StateFlow<SearchCardUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        searchCards(null)
    }

    fun searchCards(name: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
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