package ua.nure.petshelter.repository.auth.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class RegisterRequest(
    @SerialName("name") val fullName: String,
    val email: String,
    @SerialName("password_hash") val password: String,

    @EncodeDefault
    val role: String = "USER"
)