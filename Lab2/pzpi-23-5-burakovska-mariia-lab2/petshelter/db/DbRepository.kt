package ua.nure.petshelter.db

import kotlinx.coroutines.flow.Flow
import ua.nure.petshelter.db.data.AppDb

interface DbRepository {
    val dbFlow: Flow<AppDb>
    val db: AppDb
}