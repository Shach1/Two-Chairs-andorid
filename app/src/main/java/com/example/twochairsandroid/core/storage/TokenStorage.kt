package com.example.twochairsandroid.core.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val DATASTORE_NAME = "two_chairs_session"
private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class TokenStorage(
    context: Context,
) {
    private val dataStore = context.applicationContext.sessionDataStore

    val accessTokenFlow: Flow<String?> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences()) else throw it
        }
        .map { prefs -> prefs[ACCESS_TOKEN_KEY] }

    val isPremiumFlow: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences()) else throw it
        }
        .map { prefs -> prefs[IS_PREMIUM_KEY] ?: false }

    suspend fun getAccessToken(): String? = accessTokenFlow.first()
    suspend fun getIsPremium(): Boolean = isPremiumFlow.first()

    suspend fun saveAccessToken(token: String) {
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = token
        }
    }

    suspend fun saveIsPremium(isPremium: Boolean) {
        dataStore.edit { prefs ->
            prefs[IS_PREMIUM_KEY] = isPremium
        }
    }

    suspend fun clearAccessToken() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    private companion object {
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val IS_PREMIUM_KEY = booleanPreferencesKey("is_premium")
    }
}
