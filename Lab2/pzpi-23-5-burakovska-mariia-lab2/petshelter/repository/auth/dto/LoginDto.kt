package ua.nure.petshelter.repository.auth.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class LoginDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
    @SerialName("access_token")
    val accessToken: String? = null
)