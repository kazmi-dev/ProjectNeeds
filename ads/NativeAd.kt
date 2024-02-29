package com.tenx.translator.ads

import android.content.Context
import android.view.View
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.tenx.translator.R

/**
---------------------------------------------
This is Native Ad implementation
---------------------------------------------
 */

class NativeAd(private val context: Context) {

    fun initNativeAd(templateView: TemplateView){

        val adLoader = AdLoader.Builder(context, context.getString(R.string.native_ad_id))
            .forNativeAd {ad->

                templateView.setNativeAd(ad)
                templateView.visibility = View.VISIBLE

            }.build()

        adLoader.loadAd(AdRequest.Builder().build())

    }


}