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
import com.socure.idplus.databinding.MainActivityBinding
import com.socure.idplus.device.SocureFingerprintOptions
import com.socure.idplus.device.SocureFingerprintResult
import com.socure.idplus.device.SocureSigmaDevice
import com.socure.idplus.device.callback.DataUploadCallback
import com.socure.idplus.device.context.SocureFingerprintContext
import com.socure.idplus.device.error.SocureSigmaDeviceError


class MainActivity : AppCompatActivity(), MultiplePermissionsListener,
    DataUploadCallback {

    private var uploadResult: SocureFingerprintResult? = null
    lateinit var options: SocureFingerprintOptions
    lateinit var viewBinding: MainActivityBinding

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
        viewBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Dexter.withContext(this)
            .withPermissions(permissions)
            .withListener(this)
            .onSameThread()
            .check()

        options = SocureFingerprintOptions(
            false,
            SocureFingerprintContext.Home(),
            ""
        )
        viewBinding.fingerprintButton.setOnClickListener {
            SocureSigmaDevice.fingerprint(options,this)
        }
    }


    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {}

    override fun dataUploadFinished(uploadResult: SocureFingerprintResult) {
        this.uploadResult = uploadResult
        viewBinding.resultView.text = uploadResult.toString()
    }

    override fun onError(errorType: SocureSigmaDeviceError, errorMessage: String?) {
        if(!errorMessage.isNullOrEmpty()){
            Snackbar.make(viewBinding.layout, errorMessage, Snackbar.LENGTH_LONG).show()
        }
    }
}