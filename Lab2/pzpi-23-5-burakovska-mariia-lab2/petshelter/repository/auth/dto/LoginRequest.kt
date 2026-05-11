package ua.nure.petshelter.repository.auth.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LoginRequest(
    val email: String,
    @SerialName("password_hash") val password: String,
    val loginType: String
)