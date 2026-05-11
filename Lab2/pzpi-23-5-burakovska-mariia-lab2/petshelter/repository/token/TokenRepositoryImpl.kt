package ua.nure.petshelter.repository.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.nure.petshelter.config.PreferencesKeys

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : TokenRepository {

    override val token: String? get() = _token
    override val userName: String? get() = _userName
    override val userId: Int? get() = _userId
    override val userRole: String? get() = _userRole

    private val _userNameFlow = MutableStateFlow<UserName>(null)
    override val userNameFlow: StateFlow<UserName>
        get() = _userNameFlow.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val prefs = dataStore.data.firstOrNull()
            if (prefs != null) {
                _token = prefs[PreferencesKeys.token]
                _userId = prefs[PreferencesKeys.userId]
                _userRole = prefs[PreferencesKeys.userRole]


                _userName?.let { _userNameFlow.emit(it) }
            }
        }
    }

    override suspend fun setToken(newToken: String?): Unit = withContext(Dispatchers.IO) {
        _token = newToken
        dataStore.edit {
            if (newToken == null) it.remove(PreferencesKeys.token)
            else it[PreferencesKeys.token] = newToken
        }
    }

    override suspend fun setUserName(newUserName: String?): Unit = withContext(Dispatchers.IO) {
        _userName = newUserName
        _userNameFlow.emit(newUserName)
    }

    override suspend fun setUserId(newId: Int?): Unit = withContext(Dispatchers.IO) {
        _userId = newId
        dataStore.edit {
            if (newId == null) it.remove(PreferencesKeys.userId)
            else it[PreferencesKeys.userId] = newId
        }
    }

    override suspend fun setUserRole(newRole: String?): Unit = withContext(Dispatchers.IO) {
        _userRole = newRole
        dataStore.edit {
            if (newRole == null) it.remove(PreferencesKeys.userRole)
            else it[PreferencesKeys.userRole] = newRole
        }
    }

    override suspend fun clear(): Unit = withContext(Dispatchers.IO) {
        _token = null
        _userName = null
        _userId = null
        _userRole = null
        _userNameFlow.emit(null)

        dataStore.edit { it.clear() }
    }

    companion object {
        private var _token: String? = null
        private var _userName: String? = null
        private var _userId: Int? = null
        private var _userRole: String? = null
    }
}