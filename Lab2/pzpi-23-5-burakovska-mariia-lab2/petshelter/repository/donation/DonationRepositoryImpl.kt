package ua.nure.petshelter.repository.donation

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.donation.dto.DonationRequest
import ua.nure.petshelter.repository.donation.dto.DonationResponseDto
import ua.nure.petshelter.repository.safeCall
import ua.nure.petshelter.repository.token.TokenRepository

class DonationRepositoryImpl(
    private val httpClient: HttpClient,
    private val tokenRepository: TokenRepository
) : DonationRepository {

    override suspend fun makeDonation(amount: Double, type: String, note: String?): Result<DonationResponseDto, DataError> =
        withContext(Dispatchers.IO) {

            val currentUserId = tokenRepository.userId

            safeCall<DonationResponseDto> {
                httpClient.post("donations") {
                    setBody(
                        DonationRequest(
                            amount = amount,
                            type = type,
                            userId = currentUserId,
                            note = note
                        )
                    )
                }
            }
        }
}