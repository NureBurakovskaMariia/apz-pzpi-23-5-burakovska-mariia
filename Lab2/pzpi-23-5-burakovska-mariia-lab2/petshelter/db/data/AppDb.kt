package ua.nure.petshelter.db.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ua.nure.petshelter.db.data.dao.AnimalDao
import ua.nure.petshelter.db.data.dao.UserDao
import ua.nure.petshelter.db.data.entity.AnimalEntity
import ua.nure.petshelter.db.data.entity.TaskEntity
import ua.nure.petshelter.db.data.entity.UserEntity
import ua.nure.shelter.db.data.dao.TaskDao

@Database(
    entities = [
        AnimalEntity::class,
        TaskEntity::class,
        UserEntity::class,
    ],
    version = 1,
    exportSchema = false
)

@TypeConverters(DbConverters::class)
abstract class AppDb : RoomDatabase() {
    abstract val animalDao: AnimalDao
    abstract val taskDao: TaskDao
    abstract val userDao: UserDao
}