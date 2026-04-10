package com.example.tcgdex_app.presentation.details

import androidx.lifecycle.*
import com.example.tcgdex_app.domain.usecase.GetCardDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class CardDetailsViewModel @Inject constructor(
    private val getCardDetailsUseCase: GetCardDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val cardId: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(CardDetailsUiState())
    val uiState: StateFlow<CardDetailsUiState> = _uiState.asStateFlow()

    init {
        loadCardDetails(cardId)
    }

    fun retry() {
        cardId.let(::loadCardDetails)
    }

    private fun loadCardDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

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