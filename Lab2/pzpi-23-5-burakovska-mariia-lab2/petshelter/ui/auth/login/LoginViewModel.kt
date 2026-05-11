package ua.nure.petshelter.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.petshelter.R
import ua.nure.petshelter.navigation.Screen
import ua.nure.petshelter.repository.auth.AuthRepository
import ua.nure.petshelter.repository.onError
import ua.nure.petshelter.repository.onSuccess
import ua.nure.petshelter.ui.auth.login.Login.Event.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val TAG by lazy { LoginViewModel::class.simpleName }
    private val _state = MutableStateFlow(Login.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Login.Event>()
    val event = _event.asSharedFlow()

    private var loginJob: Job? = null

    fun onAction(action: Login.Action) = viewModelScope.launch {
        when(action) {
            Login.Action.OnBack -> {
                _event.emit(Login.Event.OnBack)
            }
            is Login.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }
            is Login.Action.OnEmailChange -> {
                _state.update { s -> s.copy(email = action.email, loginError = null) }
            }
            is Login.Action.OnPasswordChange -> {
                _state.update { s -> s.copy(password = action.password, loginError = null) }
            }
            Login.Action.OnLogIn -> {
                login(email = state.value.email, password = state.value.password)
            }
        }
    }

    private fun login(
        email: String,
        password: String
    ) {
        loginJob?.cancel()
        loginJob = viewModelScope.launch {
            _state.update { it.copy(inProgress = true) }

            authRepository.login(
                email = email,
                password = password
            ).onSuccess {
                _state.update { it.copy(inProgress = false) }
                _event.emit(
                    Login.Event.OnNavigate(route = Screen.Profile)
                )
            }.onError { error ->
                _state.update { it.copy(inProgress = false, loginError = R.string.noMatches) }
                Log.e(TAG, "Login error: $error")
            }
        }
    }
}