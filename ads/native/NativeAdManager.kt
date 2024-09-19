package com.cooptech.collagephotoeditor.ads.native

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.cooptech.collagephotoeditor.R
import com.cooptech.collagephotoeditor.databinding.AdNativeMediumSmallViewBinding
import com.cooptech.collagephotoeditor.databinding.AdNativeMediumViewBinding
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class NativeAdManager(
    private val context: Context,
    private val layout: NativeTemplates,
    private val container: FrameLayout,
    private val isDark: Boolean = false
) {

    init {
        initNativeAd()
    }

    private fun initNativeAd(){

        val adLoader = AdLoader.Builder(context, context.getString(R.string.native_ad_id)).forNativeAd { nativeAd->

            when(layout){
                NativeTemplates.MEDIUM_SMALL-> {
                    val binding = AdNativeMediumSmallViewBinding.inflate(LayoutInflater.from(context))
                    populateMediumSmallAd(nativeAd, binding, container, isDark)
                }
                else->{
                    val binding= AdNativeMediumViewBinding.inflate(LayoutInflater.from(context))
                    populateMediumAd(nativeAd, binding, container, isDark)
                }
            }

        }
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        val adRequest = AdRequest.Builder().build()
        adLoader.loadAd(adRequest)
    }

    private fun populateMediumAd(
        nativeAd: NativeAd,
        binding: AdNativeMediumViewBinding,
        container: FrameLayout,
        isDark: Boolean
    ) {
        binding.apply {
//            if (isDark){
//                nativeAdView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.app_dark_bg, null)
//            }else{
//                nativeAdView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.app_light_bg, null)
//            }

            headlineTv.text = nativeAd.headline
            nativeAdView.mediaView = mediaView
            mediaView.mediaContent = nativeAd.mediaContent

            // Body
            if (nativeAd.body == null) {
                bodyTv.visibility = View.INVISIBLE
            } else {
                bodyTv.visibility = View.VISIBLE
                bodyTv.text = nativeAd.body
            }

            // Call to Action
            if (nativeAd.callToAction == null) {
                callToActionBtn.visibility = View.INVISIBLE
            } else {
                callToActionBtn.visibility = View.VISIBLE
                callToActionBtn.text = nativeAd.callToAction
            }

            // Icon
            if (nativeAd.icon == null) {
                icon.visibility = View.GONE
            } else {
                icon.setImageDrawable(nativeAd.icon?.drawable)
                icon.visibility = View.VISIBLE
            }

            // Star Rating
            if (nativeAd.starRating == null) {
                ratingBar.visibility = View.INVISIBLE
            } else {
                ratingBar.rating = nativeAd.starRating!!.toFloat()
                ratingBar.visibility = View.VISIBLE
            }

            // Advertiser
            if (nativeAd.advertiser == null) {
                advertiser.visibility = View.INVISIBLE
            } else {
                advertiser.text = nativeAd.advertiser
                advertiser.visibility = View.VISIBLE
            }

            nativeAdView.setNativeAd(nativeAd)

        }
        // Attach the ad view to the provided container
        container.removeAllViews()
        container.addView(binding.root)
    }

    private fun populateMediumSmallAd(
        nativeAd: NativeAd,
        binding: AdNativeMediumSmallViewBinding,
        container: FrameLayout,
        isDark: Boolean
    ) {
        binding.apply {

//            if (isDark){
//                nativeAdView.background = ResourcesCompat.getDrawable(context.resources, R.d, null)
//                headlineTv.setTextColor(ResourcesCompat.getColor(context.resources, R.color.app_light_bg, null))
//                bodyTv.setTextColor(ResourcesCompat.getColor(context.resources, R.color.app_light_bg, null))
//            }else{
//                nativeAdView.background = ResourcesCompat.getDrawable(context.resources, R.drawable.app_light_bg, null)
//                headlineTv.setTextColor(ResourcesCompat.getColor(context.resources, R.color.app_dark_bg, null))
//                bodyTv.setTextColor(ResourcesCompat.getColor(context.resources, R.color.app_dark_bg, null))
//            }

            headlineTv.text = nativeAd.headline
            nativeAdView.mediaView = mediaView
            mediaView.mediaContent = nativeAd.mediaContent

            // Body
            if (nativeAd.body == null) {
                bodyTv.visibility = View.INVISIBLE
            } else {
                bodyTv.visibility = View.VISIBLE
                bodyTv.text = nativeAd.body
            }

            // Call to Action
            if (nativeAd.callToAction == null) {
                callToActionBtn.visibility = View.INVISIBLE
            } else {
                callToActionBtn.visibility = View.VISIBLE
                callToActionBtn.text = nativeAd.callToAction
            }

            // Icon
            if (nativeAd.icon == null) {
                icon.visibility = View.GONE
            } else {
                icon.setImageDrawable(nativeAd.icon?.drawable)
                icon.visibility = View.VISIBLE
            }

            // Star Rating
            if (nativeAd.starRating == null) {
                ratingBar.visibility = View.INVISIBLE
            } else {
                ratingBar.rating = nativeAd.starRating!!.toFloat()
                ratingBar.visibility = View.VISIBLE
            }

            // Advertiser
            nativeAdView.advertiserView = advertiser
            if (nativeAd.advertiser == null) {
                advertiser.visibility = View.VISIBLE
                advertiser.text = ""
            } else {
                advertiser.text = nativeAd.advertiser
                advertiser.visibility = View.VISIBLE
            }

            nativeAdView.setNativeAd(nativeAd)

        }

        // Attach the ad view to the provided container
        container.removeAllViews()
        container.addView(binding.root)
    }

}