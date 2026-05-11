package ua.nure.petshelter.repository.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class TaskDto(
    val id: Int,
    @SerialName("volunteer_id") val volunteerId: Int,
    val description: String,
    val status: String,
    @SerialName("due_date") val dueDate: String?
)
