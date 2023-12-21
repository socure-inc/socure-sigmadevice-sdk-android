package com.socure.idplus

import android.app.Application
import com.socure.idplus.device.SocureSigmaDevice

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        SocureSigmaDevice.initializeSDK(this,BuildConfig.SocurePublicKey)
    }
}