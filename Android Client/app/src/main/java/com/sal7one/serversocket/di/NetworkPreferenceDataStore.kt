package com.sal7one.serversocket.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sal7one.serversocket.domain.StringConstants
import javax.inject.Inject

class NetworkPreferenceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    object StoredKeys {
        val ipAddress = stringPreferencesKey(StringConstants.IP_PREFERENCES_KEY)
        val portAddress = intPreferencesKey(StringConstants.PORT_PREFERENCES_KEY)
    }

    suspend fun saveNetworkOptions(
        ipAddress: String,
        portAddress: Int,
    ) {
        dataStore.edit { preference ->
            preference[StoredKeys.ipAddress] = ipAddress
            preference[StoredKeys.portAddress] = portAddress
        }
    }

    fun getFromDataStore() = dataStore.data
}