package ua.nure.petshelter.repository.token

import kotlinx.coroutines.flow.StateFlow

interface TokenRepository {
    val token: String?
    val userName: String?
    val userId: Int?
    val userRole: String?

    val userNameFlow: StateFlow<UserName>

    suspend fun setToken(newToken: String?)
    suspend fun setUserName(newUserName: String?)
    suspend fun setUserId(newId: Int?)
    suspend fun setUserRole(newRole: String?)

    suspend fun clear()
}

typealias UserName = String?