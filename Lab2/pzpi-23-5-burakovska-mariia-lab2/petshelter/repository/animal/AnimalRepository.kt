package ua.nure.petshelter.repository.animal

import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.dto.AnimalDto

interface AnimalRepository {
    suspend fun getAnimals(): Result<List<AnimalDto>, DataError>
    suspend fun getAnimalById(id: Int): Result<AnimalDto, DataError>
}