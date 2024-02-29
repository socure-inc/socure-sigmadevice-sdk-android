package com.socure.idplus

import android.app.Application
import android.util.Log
import com.socure.idplus.device.SigmaDevice
import com.socure.idplus.device.SigmaDeviceOptions
import com.socure.idplus.device.callback.SigmaDeviceCallback
import com.socure.idplus.device.error.SigmaDeviceError

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val option = SigmaDeviceOptions()
        SigmaDevice.initializeSDK(
            this,
            BuildConfig.SocurePublicKey,
            option,
            object : SigmaDeviceCallback {
                override fun onSessionCreated(sessionToken: String) {
                    Log.d(TAG, "onSessionCreated: $sessionToken")
                }

                override fun onError(errorType: SigmaDeviceError, errorMessage: String?) {
                    Log.d(
                        TAG,
                        "onError: errorType: ${errorType.name} errorMessage: $errorMessage"
                    )
                }
            })
    }

    companion object {
        private const val TAG = "MainApplication"
    }
}