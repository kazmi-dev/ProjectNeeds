
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class PreferenceRepository@Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    companion object{
        private const val DATASTORE_LOG = "2389578969023"
    }

    //save and get boolean
    suspend fun saveBoolean(key: String, value: Boolean) = withContext(Dispatchers.IO){
        try {
            dataStore.edit { pref ->
                pref[booleanPreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "Boolean: cant save.", e)
        }catch (e:Exception){
            Log.e(DATASTORE_LOG, "Boolean: cant save.", e)
        }
    }
    
    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean{
        return withContext(Dispatchers.IO){
            dataStore.data.first()[booleanPreferencesKey(key)]?: defaultValue
        }
    }



    //save and get String
    suspend fun saveString(key: String, value: String) = withContext(Dispatchers.IO){
        try {
            dataStore.edit {pref->
                pref[stringPreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "String: cant save.", e)
        }catch (e:Exception){
            Log.e(DATASTORE_LOG, "String: cant save.", e)
        }
    }
    
    suspend fun getString(key: String, defaultValue: String): String{
        return withContext(Dispatchers.IO){
            dataStore.data.first()[stringPreferencesKey(key)]?: defaultValue
        }
    }



    //save and get Int
    suspend fun saveInt(key: String, value: Int) = withContext(Dispatchers.IO){
        try {
            dataStore.edit {pref->
                pref[intPreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "Int: cant save.", e)
        }catch (e:Exception){
            Log.e(DATASTORE_LOG, "Int: cant save.", e)
        }
    }
    
    suspend fun getInt(key: String, defaultValue: Int): Int{
        return withContext(Dispatchers.IO){
            dataStore.data.first()[intPreferencesKey(key)]?: defaultValue
        }
    }



    //save and get Double
    suspend fun saveDouble(key: String, value: Double) = withContext(Dispatchers.IO){
        try {
            dataStore.edit {pref->
                pref[doublePreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "Double: cant save.", e)
        }catch (e:Exception){
            Log.e(DATASTORE_LOG, "Double: cant save.", e)
        }
    }
    
    suspend fun getDouble(key: String, defaultValue: Double): Double{
        return withContext(Dispatchers.IO){
            dataStore.data.first()[doublePreferencesKey(key)]?: defaultValue
        }
    }



    //save and get Float
    suspend fun saveFloat(key: String, value: Float) = withContext(Dispatchers.IO){
        try {
            dataStore.edit {pref->
                pref[floatPreferencesKey(key)] = value
            }
        }catch (e: IOException){
            Log.e(DATASTORE_LOG, "Float: cant save.", e)
        }catch (e:Exception){
            Log.e(DATASTORE_LOG, "Float: cant save.", e)
        }
    }
    
    suspend fun getFloat(key: String, defaultValue: Float): Float{
        return withContext(Dispatchers.IO){
            dataStore.data.first()[floatPreferencesKey(key)]?: defaultValue
        }
    }

}
