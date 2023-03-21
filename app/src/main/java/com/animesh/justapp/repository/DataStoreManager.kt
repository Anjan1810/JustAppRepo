package com.animesh.justapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_NAME = "my_data_store"

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class DataStoreManager(private val context: Context) {



    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val THEME = stringPreferencesKey("theme")
        private val CURRENCY = stringPreferencesKey("currency")
        private val USER = stringPreferencesKey("user")
    }

    val getAccessToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN_KEY] ?: ""
    }
    val getTheme: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME] ?: ""
    }
    val getCurrency: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENCY] ?: "INR"
    }
    val getUser: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER] ?: ""
    }
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME] = theme
        }
    }
    suspend fun saveUser(user: String) {
        context.dataStore.edit { preferences ->
            preferences[USER] = user
        }
    }
    suspend fun saveCurrency(currency: String) {
        context.dataStore.edit { preferences ->
            preferences[CURRENCY] = currency
        }
    }
}