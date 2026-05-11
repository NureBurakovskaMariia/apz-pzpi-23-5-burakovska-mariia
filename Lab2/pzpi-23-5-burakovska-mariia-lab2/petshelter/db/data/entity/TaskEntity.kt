package ua.nure.petshelter.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: Int,
    @SerializedName("volunteer_id") val volunteerId: Int,
    val description: String,
    val status: String,
    @SerializedName("due_date") val dueDate: String?
)
