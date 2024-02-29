package com.tenx.translator.mvvm.preference_datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import java.io.IOException
import javax.inject.Inject

class PreferenceRepository@Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    companion object{
        private const val DATASTORE_LOG = "2389578969023"
    }

    //save and get boolean
    suspend fun saveBoolean(key: String, value: Boolean){
        try {
            dataStore.edit { pref ->
                pref[booleanPreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "boolean: cant save file.", e)
        }
    }
    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean{
        return dataStore.data.first()[booleanPreferencesKey(key)]?: defaultValue
    }



    //save and get String
    suspend fun saveString(key: String, value: String){
        dataStore.edit {pref->
            pref[stringPreferencesKey(key)] = value
        }
    }
    suspend fun getString(key: String, defaultValue: String): String{
        return dataStore.data.first()[stringPreferencesKey(key)]?: defaultValue
    }



    //save and get Int
    suspend fun saveInt(key: String, value: Int){
        dataStore.edit {pref->
            pref[intPreferencesKey(key)] = value
        }
    }
    suspend fun getInt(key: String, defaultValue: Int): Int{
        return dataStore.data.first()[intPreferencesKey(key)]?: defaultValue
    }



    //save and get Double
    suspend fun saveDouble(key: String, value: Double){
        dataStore.edit {pref->
            pref[doublePreferencesKey(key)] = value
        }
    }
    suspend fun getDouble(key: String, defaultValue: Double): Double{
        return dataStore.data.first()[doublePreferencesKey(key)]?: defaultValue
    }



    //save and get Float
    suspend fun saveFloat(key: String, value: Float){
        dataStore.edit {pref->
            pref[floatPreferencesKey(key)] = value
        }
    }
    suspend fun getFloat(key: String, defaultValue: Float): Float{
        return dataStore.data.first()[floatPreferencesKey(key)]?: defaultValue
    }

}