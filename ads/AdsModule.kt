package com.tenx.translator.ads

import android.content.Context
import com.tenx.translator.baseapplication.ApplicationClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun providesAppOpenInstance(@ApplicationContext context: Context): AppOpenAdManager {
        return AppOpenAdManager(context as ApplicationClass)
    }


    @Provides
    @Singleton
    fun providesInterstitialInstance(@ApplicationContext context: Context): InterstitialAdManager {
        return InterstitialAdManager(context)
    }

    @Provides
    @Singleton
    fun providesNativeAdInstance(@ApplicationContext context: Context): NativeAd{
        return NativeAd(context)
    }

}