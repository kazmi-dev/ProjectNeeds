package com.cooptech.collagephotoeditor.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cooptech.collagephotoeditor.R
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform

class GoogleConsentFormManager(private val context: Context) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(context)

    /** Helper variable to determine if the app can request ads. */
    private val canRequestAds: Boolean
        get() = consentInformation.canRequestAds()

    /** Helper variable to determine if the privacy options form is required. */
    val isPrivacyOptionsRequired: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED


    fun gatherConsent(
        activity: Activity,
        callback: (error: FormError?)-> Unit
    ){

        Log.d("consentError87654", "gatherConsent: gathering consent")

        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(activity.getString(R.string.device_hashed_id))
            .build()

        val params = ConsentRequestParameters.Builder()
//            .setConsentDebugSettings(debugSettings)
            .build()


        consentInformation.requestConsentInfoUpdate(activity, params, {

            //success listener

            UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity){formError->
                callback(formError)

                if (canRequestAds){
                    callback(formError)
                }

            }


        },{requestConsentError->
            // dismiss listener
            callback(requestConsentError)
        })

        if (canRequestAds){
            callback(null)
        }

    }

    /** Helper method to call the UMP SDK method to show the privacy options form. */
    fun showPrivacyOptionsForm(
        activity: Activity,
        onConsentFormDismissedListener: ConsentForm.OnConsentFormDismissedListener
    ) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener)
    }

}