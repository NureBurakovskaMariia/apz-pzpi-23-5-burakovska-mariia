package ua.nure.petshelter.ui.donations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.petshelter.repository.donation.DonationRepository
import ua.nure.petshelter.repository.onError
import ua.nure.petshelter.repository.onSuccess
import javax.inject.Inject

@HiltViewModel
class DonationsViewModel @Inject constructor(
    private val repository: DonationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(Donations.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Donations.Event>()
    val event = _event.asSharedFlow()

    fun onAction(action: Donations.Action) {
        when (action) {
            is Donations.Action.OnTypeSelected -> {
                _state.update { it.copy(selectedType = action.type) }
            }
            is Donations.Action.OnAmountChanged -> {
                val validNumber = action.amount.filter { it.isDigit() || it == '.' }
                _state.update { it.copy(amount = validNumber) }
            }
            is Donations.Action.OnNoteChanged -> {
                _state.update { it.copy(note = action.note) }
            }
            Donations.Action.OnDismissDialog -> {
                _state.update { it.copy(showSuccessDialog = false, amount = "", note = "") }
            }
            Donations.Action.OnSubmitClick -> submitDonation()
        }
    }

    private fun submitDonation() = viewModelScope.launch {
        val currentState = state.value
        val amountDouble = currentState.amount.toDoubleOrNull()

        if (amountDouble == null || amountDouble <= 0) {
            _event.emit(Donations.Event.ShowSnackbar("Please enter a valid amount"))
            return@launch
        }

        _state.update { it.copy(isLoading = true) }

        repository.makeDonation(
            amount = amountDouble,
            type = currentState.selectedType.apiValue,
            note = currentState.note.ifBlank { null }
        ).onSuccess {
            _state.update { it.copy(isLoading = false, amount = "", note = "") }
            _event.emit(Donations.Event.OnDonationSuccess)
            _state.update { it.copy(isLoading = false, showSuccessDialog = true) }
        }.onError {
            _state.update { it.copy(isLoading = false) }
            _event.emit(Donations.Event.ShowSnackbar("Failed to send donation"))
        }
    }
}