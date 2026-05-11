package ua.nure.petshelter.config

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val token = stringPreferencesKey("token")
    val userId = intPreferencesKey("user_id")
    val userRole = stringPreferencesKey("user_role")
}