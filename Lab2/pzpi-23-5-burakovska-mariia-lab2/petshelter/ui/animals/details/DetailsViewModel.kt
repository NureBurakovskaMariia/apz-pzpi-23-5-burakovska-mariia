package ua.nure.petshelter.ui.animals.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.petshelter.repository.animal.AnimalRepository
import ua.nure.petshelter.repository.onError
import ua.nure.petshelter.repository.onSuccess
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val animalRepository: AnimalRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "DetailsViewModel"

    private val _state = MutableStateFlow(Details.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Details.Event>()
    val event = _event.asSharedFlow()

    init {
        val animalId: Int? = savedStateHandle.get<Int>("animalId")
        if (animalId != null) {
            loadAnimal(animalId)
        } else {
            _state.update { it.copy(isLoading = false, error = "ID тварини не знайдено") }
        }
    }

    fun onAction(action: Details.Action) = viewModelScope.launch {
        when (action) {
            Details.Action.OnBackClick -> _event.emit(Details.Event.OnBack)
            Details.Action.OnAdoptClick -> {
                Log.d(TAG, "Клік на усиновлення тварини: ${state.value.animal?.name}")
            }
        }
    }

    private fun loadAnimal(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            animalRepository.getAnimalById(id)
                .onSuccess { animalData ->
                    _state.update { it.copy(animal = animalData, isLoading = false) }
                }
                .onError { error ->
                    Log.e(TAG, "Помилка завантаження деталей: $error")
                    _state.update { it.copy(isLoading = false, error = "Не вдалося завантажити") }
                }
        }
    }
}