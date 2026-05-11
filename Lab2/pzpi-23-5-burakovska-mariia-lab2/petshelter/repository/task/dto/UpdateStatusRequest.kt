package ua.nure.petshelter.repository.task.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStatusRequest(
    val status: String
)