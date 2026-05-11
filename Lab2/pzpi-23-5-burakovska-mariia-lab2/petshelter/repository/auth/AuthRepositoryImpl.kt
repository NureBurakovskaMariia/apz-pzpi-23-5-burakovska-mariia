package ua.nure.petshelter.repository.auth

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.nure.petshelter.db.DbRepository
import ua.nure.petshelter.db.data.entity.UserEntity
import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.auth.dto.*
import ua.nure.petshelter.repository.onSuccess
import ua.nure.petshelter.repository.safeCall
import ua.nure.petshelter.repository.token.TokenRepository

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val tokenRepository: TokenRepository,
    private val dbRepository: DbRepository
) : AuthRepository {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<ResponseDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<ResponseDto> {
            httpClient.post("register") {
                setBody(
                    RegisterRequest(
                        fullName = name,
                        email = email,
                        password = password
                    )
                )
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<LoginDto, DataError> = withContext(Dispatchers.IO) {
        safeCall<LoginDto> {
            httpClient.post("login") {
                setBody(
                    LoginRequest(
                        email = email,
                        password = password,
                        loginType = "user"
                    )
                )
            }
        }.onSuccess { userDto ->
            tokenRepository.setToken(userDto.accessToken)
            tokenRepository.setUserName(userDto.email)
            tokenRepository.setUserId(userDto.id)
            tokenRepository.setUserRole(userDto.role)
        }
    }

    override suspend fun getUserProfile(email: String): Result<UserProfileDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<UserProfileDto> {
                httpClient.get("users/email/$email")
            }.onSuccess { profile ->
                tokenRepository.setUserName(profile.email)
                tokenRepository.setUserId(profile.id)
                tokenRepository.setUserRole(profile.role)

                dbRepository.db.userDao.insertUser(
                    UserEntity(
                        id = profile.id,
                        name = profile.name,
                        email = profile.email,
                        role = profile.role
                    )
                )
            }
        }

    override suspend fun logout() {
        tokenRepository.clear()
    }
}