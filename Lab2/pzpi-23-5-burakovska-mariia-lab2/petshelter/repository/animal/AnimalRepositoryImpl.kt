package ua.nure.petshelter.repository.animal

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.dto.AnimalDto
import ua.nure.petshelter.repository.safeCall

class AnimalRepositoryImpl(
    private val httpClient: HttpClient
) : AnimalRepository {

    override suspend fun getAnimals(): Result<List<AnimalDto>, DataError> = withContext(Dispatchers.IO) {
        safeCall<List<AnimalDto>> {
            httpClient.get("animals")
        }
    }

    override suspend fun getAnimalById(id: Int): Result<AnimalDto, DataError> {
        return try {
            val response = httpClient.get("/api/animals/$id")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val animal = response.body<AnimalDto>()
                    Result.Success(animal)
                }
                HttpStatusCode.NotFound -> {
                    Result.Error(DataError.ApiError("Тварину не знайдено"))
                }
                else -> {
                    Result.Error(DataError.Remote.SERVER)
                }
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
            Result.Error(DataError.Remote.SERIALIZATION)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(DataError.Remote.NO_INTERNET)
        }
    }
}