import android.app.Activity
import android.content.Context
import android.util.Log
import com.cooptech.pdfreader.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


/**
---------------------------------------------
This is Interstitial Ad implementation
---------------------------------------------
 */

class InterstitialAdManager(
    private val context: Context,
) {

    companion object {
        private const val INTERSTITIAL_AD_LOG = "interstitial_ad_log_267362545"
        var isInterstitialShowing: Boolean = false
        var interstitialAdCount: Int = 0
        var adLoadFailCount: Int = 0
    }

    private var interstitialAd: InterstitialAd? = null
    private var isAdLoading: Boolean = false
    private var adUnitId: String = context.getString(R.string.interstitial_ad_id)

    private val interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(ad: InterstitialAd) {
            adLoadFailCount = 0
            isAdLoading = false
            interstitialAd = ad
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            isAdLoading = false
            interstitialAd = null
            handleAdLoadShowFail()
        }
    }

    fun setInterstitialAdUnitId(adUnitId: String) {
        this@InterstitialAdManager.adUnitId = adUnitId
    }

    private fun loadAd() {
        //check if ad is not null
        if (interstitialAd != null) {
            return
        }

        //check if ad is already loading
        if (isAdLoading) {
            return
        }
        isAdLoading = true
        //request ad
        val adRequest = AdRequest.Builder().build()
        //load ad
        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            interstitialAdLoadCallback
        )
    }

    fun showInterstitialAdIfAvailable(activity: Activity, callback: (AdStates) -> Unit) {

        //check if ad is available
        if (interstitialAd == null) {
            loadAd()
            return
        }

        //check if ad is already showing
        if (isInterstitialShowing) {
            return
        }

        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d(INTERSTITIAL_AD_LOG, "onAdShowedFullScreenContent: Ad Showed")
                isInterstitialShowing = true
                callback(AdStates.AD_SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(INTERSTITIAL_AD_LOG, "onAdDismissedFullScreenContent: Ad Dismissed")
                interstitialAd = null
                isInterstitialShowing = false
                loadAd()
                callback(AdStates.AD_DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(INTERSTITIAL_AD_LOG, "onAdFailedToShowFullScreenContent: ${error.message}")
                interstitialAd = null
                isInterstitialShowing = false
                handleAdLoadShowFail()
                callback(AdStates.AD_FAILED_TO_SHOW)
            }

        }

        interstitialAd?.show(activity)

    }

    private fun handleAdLoadShowFail() {
        if (adLoadFailCount <= 5){
            adLoadFailCount++
            loadAd()
        }else{
            adLoadFailCount = 0
        }
    }


    fun showInterstitialAdIfAvailable(
        activity: Activity,
        adCount: Int,
        callback: (AdStates) -> Unit,
    ) {
        if (adCount == interstitialAdCount) {
            interstitialAdCount = 0
            showInterstitialAdIfAvailable(activity, callback)
        } else {
            interstitialAdCount++
        }
    }


}
