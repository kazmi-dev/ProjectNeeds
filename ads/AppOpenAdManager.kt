import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.kazmi.dev.gameborse.R
import com.kazmi.dev.gameborse.application.ApplicationClass
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppOpenAdManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ActivityLifecycleCallbacks, LifecycleEventObserver {

    init {
        (context as? ApplicationClass)?.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    companion object {
        private const val APP_OPEN_LOG = "AppOpenAdManager_982648927492"
        var isInterstitialAdShowing: Boolean = false
    }

    private var appOpenAd: AppOpenAd? = null
    private var isAdLoading: Boolean = false
    private var currentActivity: WeakReference<Activity>? = null
    private var isShowAdONDemand: Boolean = false

    fun setShowAdOnDemand(isShowAdOnDemand: Boolean) {
        this.isShowAdONDemand = isShowAdOnDemand
    }

    fun preloadAd() {

        //check if ad already available
        if (appOpenAd != null) {
            Log.d(APP_OPEN_LOG, "preloadAd: Ad already available")
            return
        }

        //check if ad is loading
        if (isAdLoading) {
            Log.d(APP_OPEN_LOG, "preloadAd: Ad already loading")
            return
        }

        //load ad
        val adUnitId = context.getString(R.string.app_open_ad_id)
        loadAd(adUnitId) { Log.d(APP_OPEN_LOG, "preloadAd: Ad loaded") }

    }

    private fun attachFullScreenAdCallback(
        isPreloadAfterDismiss: Boolean,
        callback: ((AdState) -> Unit)? = null
    ) {
        //callback for ad showing
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d(APP_OPEN_LOG, "onAdShowedFullScreenContent: Ad showed")
                callback?.invoke(AdState.SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(
                    APP_OPEN_LOG,
                    "onAdDismissedFullScreenContent: Ad dismissed preloading ad"
                )
                appOpenAd = null
                if (isPreloadAfterDismiss) preloadAd()
                callback?.invoke(AdState.DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(adShowError: AdError) {
                Log.d(
                    APP_OPEN_LOG,
                    "onAdFailedToShowFullScreenContent: Ad failed to show ${adShowError.message}"
                )
                appOpenAd = null
                callback?.invoke(AdState.FAILED_TO_SHOW)
            }
        }
    }

    private fun loadAd(adUniId: String, callback: (Boolean) -> Unit) {
        isAdLoading = true
        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            context,
            adUniId,
            adRequest,
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    isAdLoading = false
                    appOpenAd = ad
                    Log.d(APP_OPEN_LOG, "onAdLoaded: Ad Loaded.")
                    callback(true)
                }

                override fun onAdFailedToLoad(adLoadError: LoadAdError) {
                    isAdLoading = false
                    appOpenAd = null
                    Log.d(APP_OPEN_LOG, "onAdFailedToLoad: ${adLoadError.message}")
                    callback(false)
                }
            }
        )
    }

    private fun showAdIfAvailable() {
        //check if ad is available
        if (appOpenAd == null) {
            Log.d(APP_OPEN_LOG, "showAdIfAvailable: Ad not available preloading ad.")
            preloadAd()
            return
        }

        //show Ad
        showAd(isPreloadAfterDismiss = true)
    }

    private fun showAdOnDemand() {
        //check if ad is Available
        if (appOpenAd == null) {
            Log.d(APP_OPEN_LOG, "showAdOnDemand: Ad not available loading ad.")
            val adUnitId = context.getString(R.string.app_open_ad_id)
            loadAd(adUnitId) { isLoaded ->
                if (isLoaded) {
                    showAd(isPreloadAfterDismiss = false)
                } else {
                    Log.d(APP_OPEN_LOG, "showAdOnDemand: Ad failed to load.")
                }
            }
            return
        }

        //show Ad
        showAd(isPreloadAfterDismiss = false)
    }

    private fun showAd(isPreloadAfterDismiss: Boolean) {
        attachFullScreenAdCallback(isPreloadAfterDismiss)
        currentActivity?.get()?.let {
            appOpenAd?.show(it)
        }
    }


    //activity lifecycle callbacks
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentActivity = WeakReference(activity)
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        currentActivity?.clear()
        currentActivity = null
    }


    //Lifecycle even observer
    override fun onStateChanged(
        source: LifecycleOwner,
        event: Lifecycle.Event
    ) {
        if (event == Lifecycle.Event.ON_START && !isInterstitialAdShowing) {
            if (isShowAdONDemand) showAdOnDemand() else showAdIfAvailable()
        }
    }

}
