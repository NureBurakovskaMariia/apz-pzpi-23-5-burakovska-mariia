package ua.nure.petshelter.db.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "animals")
data class AnimalEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,
    @SerializedName("birth_date") val birthDate: String?,
    val description: String?,
    val status: String
)