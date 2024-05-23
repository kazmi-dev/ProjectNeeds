package com.cooptech.pdfeditor.mvm.repositories

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class RemoteConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig,
) {

    private val REMOTE_CONFIG_LOG = "RemoteConfigRepository"

    //getValues

    fun getString(key: String): String{
        return remoteConfig.getString(key)
    }



}