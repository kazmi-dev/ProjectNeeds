import com.capra.live.hdwallpaper.R
import com.capra.live.hdwallpaper.util.AppSettings.Companion.API_KEY
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteConfigModule {

    @Provides
    @Singleton
    fun providesFirebaseRemoteConfig(): FirebaseRemoteConfig{
        val remoteConfig = Firebase.remoteConfig.apply {
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            }
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
        }
        fetchAndActivate(remoteConfig)
        return remoteConfig
    }

    private fun fetchAndActivate(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updates = task.result
                    getData(remoteConfig)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun getData(remoteConfig: FirebaseRemoteConfig) {
        API_KEY = remoteConfig.getString("api_key")
    }

    private fun configUpdateListener(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate()
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                error.printStackTrace()
            }

        })
    }

}
