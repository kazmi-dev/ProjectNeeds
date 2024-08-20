package com.cooptech.pdfreader.billingLibrary

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingClientModule {


    @Provides
    @Singleton
    fun providesBillingClientLibrary(@ApplicationContext context: Context): GoogleBillingLibrary{
        return GoogleBillingLibrary(context)
    }



}