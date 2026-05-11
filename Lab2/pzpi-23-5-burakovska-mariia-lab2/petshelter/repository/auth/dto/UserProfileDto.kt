package ua.nure.petshelter.repository.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val id: Int,
    val name: String,
    val email: String,
    val role: String
)