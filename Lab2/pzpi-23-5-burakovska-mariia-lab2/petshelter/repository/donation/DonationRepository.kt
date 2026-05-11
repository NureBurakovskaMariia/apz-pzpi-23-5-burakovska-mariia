package ua.nure.petshelter.repository.donation

import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.donation.dto.DonationResponseDto

interface DonationRepository {
    suspend fun makeDonation(amount: Double, type: String, note: String?): Result<DonationResponseDto, DataError>
}