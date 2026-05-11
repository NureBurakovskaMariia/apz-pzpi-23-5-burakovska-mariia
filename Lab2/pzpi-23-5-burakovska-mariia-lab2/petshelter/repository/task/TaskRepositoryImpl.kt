package ua.nure.petshelter.repository.task

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.nure.petshelter.repository.DataError
import ua.nure.petshelter.repository.Result
import ua.nure.petshelter.repository.dto.TaskDto
import ua.nure.petshelter.repository.safeCall
import ua.nure.petshelter.repository.task.dto.UpdateStatusRequest

class TaskRepositoryImpl(
    private val httpClient: HttpClient
) : TaskRepository {

    override suspend fun getTasksByVolunteer(userId: Int): Result<List<TaskDto>, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<List<TaskDto>> {
                httpClient.get("users/$userId/tasks")
            }
        }

    override suspend fun updateTaskStatus(taskId: Int, status: String): Result<TaskDto, DataError> =
        withContext(Dispatchers.IO) {
            safeCall<TaskDto> {
                httpClient.put("tasks/$taskId/status") {
                    setBody(UpdateStatusRequest(status = status))
                }
            }
        }
}