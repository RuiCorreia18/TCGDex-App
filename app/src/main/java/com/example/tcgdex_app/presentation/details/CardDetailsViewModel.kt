package com.example.tcgdex_app.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tcgdex_app.domain.usecase.GetCardDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class CardDetailsViewModel @Inject constructor(
    private val getCardDetailsUseCase: GetCardDetailsUseCase,
    savedStateHandle: SavedStateHandle,
): ViewModel() {


    private val cardId: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(CardDetailsUiState())
    val uiState: StateFlow<CardDetailsUiState> = _uiState.asStateFlow()

    init {
        if (cardId.isBlank()) {
            _uiState.update {
                it.copy(error = "Missing card id")
            }
        } else {
            loadCardDetails(cardId)
        }
    }

    fun retry() {
        cardId.let(::loadCardDetails)
    }

    private fun loadCardDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy( isLoading = true, error = null) }

            try {
                val cardDetails = getCardDetailsUseCase(id)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        data = cardDetails,
                        error = null
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load card details"
                    )
                }
            }
        }
    }
}