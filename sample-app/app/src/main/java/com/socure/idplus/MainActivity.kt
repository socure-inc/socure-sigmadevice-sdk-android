package com.socure.idplus

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.socure.idplus.devicerisk.androidsdk.model.SocureFingerPrintOptions
import com.socure.idplus.devicerisk.androidsdk.model.SocureFingerprintResult
import com.socure.idplus.devicerisk.androidsdk.model.SocureSigmaDeviceConfig
import com.socure.idplus.devicerisk.androidsdk.sensors.SocureSigmaDevice
import com.socure.idplus.devicerisk.androidsdk.uilts.SocureFingerPrintContext
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity(), MultiplePermissionsListener,
    SocureSigmaDevice.DataUploadCallback {

    private var uploadResult: SocureFingerprintResult? = null
    private var uuid: String? = null
    val sigma = SocureSigmaDevice()
    lateinit var config: SocureSigmaDeviceConfig
    lateinit var options: SocureFingerPrintOptions

    private val permissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(this)
            .onSameThread()
            .check()
        riskButton.setOnClickListener {
            sigma.fingerPrint(config,options,this) }

        loadDeviceRiskManager()
    }

    private fun loadDeviceRiskManager(){
        config = SocureSigmaDeviceConfig(BuildConfig.SocurePublicKey,false,false,"","",this)
        options = SocureFingerPrintOptions(false, SocureFingerPrintContext.Home(),null)

    }


    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {}

    override fun dataUploadFinished(uploadResult: SocureFingerprintResult) {
        this.uploadResult = uploadResult
        this.uuid = uploadResult.deviceSessionID

        informationButton.isEnabled = true
        resultView.text = uploadResult.toString()
    }

    override fun onError(errorType: SocureSigmaDevice.SocureSigmaDeviceError, errorMessage: String?) {
        Snackbar.make(layout, errorMessage!!, Snackbar.LENGTH_LONG).show()
    }
}