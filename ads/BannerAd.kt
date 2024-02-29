package com.tenx.translator.ads

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.tenx.translator.R
import java.util.UUID

/**
-----------------------------------------------
This is Anchored Adaptive banner Implementation
-----------------------------------------------
 */

class BannerAd(
    private val context: Context,
    private val adViewContainer: FrameLayout,
    private val loadingView: TextView,
    private val isAnchor: Boolean = true,
    private val isCollapsible: Boolean = false
) {

    private val bannerLog = "23982389758934"

    private lateinit var adView: AdView
    private var initialLayoutComplete = false

    private lateinit var windowManager: WindowManager

    init {
        loadBannerAd()
    }

    //get banner ad size
    private val adSize: AdSize
        get() {
            return if (isAnchor){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getAdSizeForAndroidAboveAndEqualApi30()
                } else {
                    getAdSizeForAndroidBelowApi30()
                }
            }else{
                AdSize.BANNER
            }
        }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getAdSizeForAndroidAboveAndEqualApi30(): AdSize {
        val windowMetrics = windowManager.currentWindowMetrics
        val bounds = windowMetrics.bounds
        var adWidthPixels = adViewContainer.width.toFloat()

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = bounds.width().toFloat()
        }

        val density = context.resources.displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    private fun getAdSizeForAndroidBelowApi30(): AdSize {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var adWidthPixels = adViewContainer.width.toFloat()

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0f) {
            adWidthPixels = displayMetrics.widthPixels.toFloat()
        }

        val density = displayMetrics.density
        val adWidth = (adWidthPixels / density).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }

    private fun loadBannerAd() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        adView = AdView(context)
        //loading customization
        loadingView.layoutParams.height = adSize.getHeightInPixels(context)
        loadingView.background = ColorDrawable(context.resources.getColor(R.color.btn_color_orange, null))

        adViewContainer.addView(adView)
        adViewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                //load banner ad fun
                loadBanner(loadingView)
            }
        }
    }

//    private fun convertDpToPx(dp: Int): Int {
//        val density = context.resources.displayMetrics.density
//        return (dp * density).toInt()
//    }

    private fun loadBanner(loadingView: TextView) {
        //set banner ad id
        adView.adUnitId = if (isCollapsible) context.getString(R.string.collapsive_banner_ad_id) else context.getString(R.string.banner_ad_id)
        Log.d(bannerLog, "banner ad id: ${context.getString(R.string.banner_ad_id)}")

        //set banner ad size
        adView.setAdSize(adSize)
        Log.d(bannerLog, "ad size: $adSize")

        //request banner ad




        val adRequestBuilder: AdRequest = if (isCollapsible){
            val extras = Bundle()
            extras.putString("collapsible", "bottom")
            extras.putString("collapsible_request_id", UUID.randomUUID().toString())
            AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                .build()
        }else{
            AdRequest.Builder().build()
        }

        //load banner ad
        adView.loadAd(adRequestBuilder)

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adViewContainer.visibility = View.VISIBLE
                loadingView.visibility = View.GONE
            }
        }

    }


}