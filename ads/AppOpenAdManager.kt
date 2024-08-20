import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.cooptech.pdfreader.R
import com.cooptech.pdfreader.ads.InterstitialAdManager.Companion.isInterstitialShowing
import com.cooptech.pdfreader.applicationClass.ApplicationClass
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback

/**
---------------------------------------------
This is App open Ad implementation
---------------------------------------------
 */

class AppOpenAdManager(private val mBaseApp: ApplicationClass): ActivityLifecycleCallbacks, LifecycleEventObserver {

    init {
        mBaseApp.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private var appOpenAd: AppOpenAd? = null
    private var adUnitId: String = mBaseApp.getString(R.string.app_open_ad_id)
    private var isAdLoading = false
    private var isAppOpenShowingAd = false
    private var currentActivity: Activity? = null
    private var appOpenAdVisibilityController: AppOpenAdVisibilityController? = null

    private val appOpenAdLoadCallBack = object: AppOpenAdLoadCallback(){
        override fun onAdLoaded(appOpenAd: AppOpenAd) {
            isAdLoading = false
            this@AppOpenAdManager.appOpenAd = appOpenAd
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            isAdLoading = false
            appOpenAd = null
        }
    }

    fun setAppOpenAdVisibilityController(appOpenAdVisibilityController: AppOpenAdVisibilityController){
        this.appOpenAdVisibilityController = appOpenAdVisibilityController
    }

    fun setAppOpenAdUnitId(adUnitId: String){
        this.adUnitId = adUnitId
    }

    fun loadAd(){
        if (isAdLoading){
            return
        }
        isAdLoading = true

        val adRequest = AdRequest.Builder().build()
        AppOpenAd.load(
            mBaseApp,
            adUnitId,
            adRequest,
            appOpenAdLoadCallBack
        )
    }

    fun showAppOpenAdIfAvailable(){
        if (appOpenAd == null){
            loadAd()
            return
        }

        if(isAppOpenShowingAd){
            return
        }

        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback(){

            override fun onAdShowedFullScreenContent() {
                isAppOpenShowingAd = true
                appOpenAdVisibilityController?.closeAds()
            }

            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
                isAppOpenShowingAd = false
                appOpenAdVisibilityController?.restoreAds()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                appOpenAd = null
                isAppOpenShowingAd = false
                loadAd()
            }

        }

        currentActivity?.let {
            appOpenAd?.show(it)
        }

    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        currentActivity = activity
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) { }

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START){
            if (!isAppOpenShowingAd && !isInterstitialShowing){
                showAppOpenAdIfAvailable()
            }
        }
    }

}
