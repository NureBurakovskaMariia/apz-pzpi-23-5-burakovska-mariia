package ua.nure.petshelter.repository.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    val message: String? = null
)