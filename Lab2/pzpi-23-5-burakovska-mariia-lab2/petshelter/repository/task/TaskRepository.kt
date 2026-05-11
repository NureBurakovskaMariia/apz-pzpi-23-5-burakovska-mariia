package ua.nure.petshelter.repository.task

import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.dto.TaskDto

interface TaskRepository {
    suspend fun getTasksByVolunteer(userId: Int): Result<List<TaskDto>, DataError>
    suspend fun updateTaskStatus(taskId: Int, status: String): Result<TaskDto, DataError>
}