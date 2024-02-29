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
    }

    companion object {
        private const val TAG = "MainApplication"
    }
}