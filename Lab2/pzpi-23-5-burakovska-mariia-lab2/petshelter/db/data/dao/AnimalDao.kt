package ua.nure.petshelter.db.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.nure.petshelter.db.data.entity.AnimalEntity


@Dao
interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animals: List<AnimalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(animal: AnimalEntity)

    @Query("SELECT * FROM animals")
    suspend fun getAll(): List<AnimalEntity>

    @Query("SELECT * FROM animals")
    fun getAllFlow(): Flow<List<AnimalEntity>>

    @Query("SELECT * FROM animals WHERE id = :id")
    suspend fun getById(id: Int): AnimalEntity?

    @Query("SELECT * FROM animals WHERE status = :status")
    fun getAnimalsByStatusFlow(status: String = "available"): Flow<List<AnimalEntity>>

    @Update
    suspend fun update(animal: AnimalEntity)

    @Query("DELETE FROM animals")
    suspend fun deleteAll()
}