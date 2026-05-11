package ua.nure.petshelter.ui.animals.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.animal.AnimalRepository
import ua.nure.petshelter.repository.onError
import ua.nure.petshelter.repository.onSuccess
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val animalRepository: AnimalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnimalList.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AnimalList.Event>()
    val event = _event.asSharedFlow()

    init {
        loadAnimals()
    }

    fun onAction(action: AnimalList.Action) = viewModelScope.launch {
        when (action) {
            is AnimalList.Action.OnAnimalClick -> {
                 _event.emit(AnimalList.Event.OnNavigate(route = Screen.Animals.Details(action.animalId)))
            }
            AnimalList.Action.OnRefresh -> loadAnimals()
        }
    }

    private fun loadAnimals() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            animalRepository.getAnimals()
                .onSuccess { list ->
                    _state.update { it.copy(animals = list, isLoading = false) }
                }
                .onError {
                    _state.update { it.copy(isLoading = false, error = "Failed to load") }
                }
        }
    }
}