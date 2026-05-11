package ua.nure.petshelter.repository.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AnimalDto(
    val id: Int,
    val name: String,
    val species: String,
    val breed: String?,
    val gender: String?,
    @SerialName("birth_date") val birthDate: String?,
    val description: String?,
    val status: String
)
