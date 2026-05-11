package ua.nure.petshelter.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.petshelter.repository.auth.AuthRepository
import ua.nure.petshelter.repository.task.TaskRepository // Твій репозиторій
import ua.nure.petshelter.repository.onError
import ua.nure.petshelter.repository.onSuccess
import ua.nure.petshelter.repository.token.TokenRepository
import ua.nure.petshelter.ui.profile.Profile.Event.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository,
    private val taskRepository: TaskRepository // Додали сюди
) : ViewModel() {

    private val TAG by lazy { ProfileViewModel::class.simpleName }

    private val _state = MutableStateFlow(Profile.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Profile.Event>()
    val event = _event.asSharedFlow()

    init {
        loadProfile()
    }

    fun onAction(action: Profile.Action) = viewModelScope.launch {
        when (action) {
            Profile.Action.OnBack -> _event.emit(OnBack)
            is Profile.Action.OnNavigate -> _event.emit(OnNavigate(route = action.route))
            is Profile.Action.OnTaskStatusChange -> updateTaskStatus(action.taskId, action.isCompleted)
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(inProgress = true) }

            val currentEmail = tokenRepository.userName

            if (currentEmail != null) {
                authRepository.getUserProfile(email = currentEmail)
                    .onSuccess { profile ->
                        _state.update {
                            it.copy(
                                inProgress = false,
                                name = profile.name,
                                email = profile.email,
                                role = profile.role
                            )
                        }

                        loadTasks(profile.id)
                    }
                    .onError { error ->
                        _state.update { it.copy(inProgress = false) }
                        Log.e(TAG, "Помилка завантаження профілю: $error")
                    }
            } else {
                _state.update { it.copy(inProgress = false) }
            }
        }
    }

    private fun loadTasks(volunteerId: Int) {
        viewModelScope.launch {
            taskRepository.getTasksByVolunteer(volunteerId)
                .onSuccess { tasksList ->
                    _state.update { it.copy(tasks = tasksList) }
                }
                .onError { error ->
                    Log.e(TAG, "Помилка завантаження завдань: $error")
                }
        }
    }

    private fun updateTaskStatus(taskId: Int, isCompleted: Boolean) = viewModelScope.launch {
        val newStatus = if (isCompleted) "completed" else "pending"

        _state.update { currentState ->
            val updatedTasks = currentState.tasks.map { task ->
                if (task.id == taskId) task.copy(status = newStatus) else task
            }
            currentState.copy(tasks = updatedTasks)
        }

        taskRepository.updateTaskStatus(taskId, newStatus)
            .onSuccess { updatedTask ->
                _state.update { currentState ->
                    val finalTasks = currentState.tasks.map { task ->
                        if (task.id == taskId) updatedTask else task
                    }
                    currentState.copy(tasks = finalTasks)
                }
            }
            .onError { error ->
                Log.e(TAG, "Не вдалося оновити статус: $error")
                val oldStatus = if (isCompleted) "pending" else "completed"
                _state.update { currentState ->
                    val rolledBackTasks = currentState.tasks.map { task ->
                        if (task.id == taskId) task.copy(status = oldStatus) else task
                    }
                    currentState.copy(tasks = rolledBackTasks)
                }
            }
    }
}