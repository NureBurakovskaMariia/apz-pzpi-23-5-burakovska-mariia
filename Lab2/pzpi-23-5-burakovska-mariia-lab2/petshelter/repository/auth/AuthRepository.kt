package ua.nure.petshelter.repository.auth

import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.auth.dto.LoginDto
import ua.nure.petshelter.repository.auth.dto.ResponseDto
import ua.nure.petshelter.repository.auth.dto.UserProfileDto

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<ResponseDto, DataError>
    suspend fun login(email: String, password: String): Result<LoginDto, DataError>
    suspend fun logout()
    suspend fun getUserProfile(email: String): Result<UserProfileDto, DataError>
}