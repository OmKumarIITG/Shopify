package com.example.empty.datastore

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceDataStoreConstants {
    val USER_NAME = intPreferencesKey("USER_NAME")
    val USER_DATA = stringPreferencesKey("USER_DATA")
}