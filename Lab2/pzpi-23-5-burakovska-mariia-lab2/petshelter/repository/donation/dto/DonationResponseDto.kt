package ua.nure.petshelter.repository.donation.dto

import kotlinx.serialization.Serializable

@Serializable
data class DonationResponseDto(
    val id: Int,
    val amount: Double,
    val type: String,
    val date: String,
    val note: String? = null,
)