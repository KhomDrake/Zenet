package br.com.khomdrake.zenet.request

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.lang.ref.WeakReference

object DiskData {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "TEST_DATA_STORE"
    )

    private var context: WeakReference<Context> = WeakReference(null)

    fun init(ctx: Context) {
        context = WeakReference(ctx)
        context.get()?.dataStore
    }

    private val dataStore: DataStore<Preferences>?
        get() = context.get()?.dataStore

    suspend fun setValue(key: String, value: String) {
        dataStore?.apply {
            val newKey = stringPreferencesKey(key)
            dataStore?.edit { settings ->
                settings[newKey] = value
            }
        }
    }

    suspend fun getValue(key: String) : String? {
        return dataStore?.data?.map { settings: Preferences ->
            val newKey = stringPreferencesKey(key)
            settings[newKey]
        }?.first()
    }

}