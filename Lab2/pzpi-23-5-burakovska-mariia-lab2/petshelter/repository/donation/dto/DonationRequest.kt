package ua.nure.petshelter.repository.donation.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DonationRequest(
    val amount: Double,
    val type: String,
    @SerialName("user_id") val userId: Int?,
    val note: String? = null,
)