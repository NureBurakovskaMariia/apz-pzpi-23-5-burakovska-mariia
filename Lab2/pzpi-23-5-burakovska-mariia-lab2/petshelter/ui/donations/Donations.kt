package ua.nure.petshelter.ui.donations

object Donations {

    enum class Type(val title: String, val apiValue: String) {
        MONEY("Money", "money"),
        FOOD("Food", "food"),
        MEDICINES("Medicines", "medicines")
    }

    sealed interface Event {
        data class ShowSnackbar(val message: String) : Event
        data object OnDonationSuccess : Event
    }

    sealed interface Action {
        data class OnTypeSelected(val type: Type) : Action
        data class OnAmountChanged(val amount: String) : Action
        data class OnNoteChanged(val note: String) : Action
        data object OnSubmitClick : Action
        data object OnDismissDialog : Action
    }

    data class State(
        val selectedType: Type = Type.MONEY,
        val amount: String = "",
        val note: String = "",
        val isLoading: Boolean = false,
        val showSuccessDialog: Boolean = false
    )
}