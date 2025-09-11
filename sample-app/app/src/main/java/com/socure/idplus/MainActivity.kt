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
import com.socure.idplus.device.SigmaDevice
import com.socure.idplus.device.callback.SessionTokenCallback
import com.socure.idplus.device.error.SigmaDeviceError

class MainActivity : AppCompatActivity(), MultiplePermissionsListener {

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

        Dexter.withContext(this).withPermissions(permissions).withListener(this).onSameThread()
            .check()
        fetchSessionToken()
        viewBinding.sessionTokenButton.setOnClickListener {
            viewBinding.resultView.text = getString(R.string.fetch)
            fetchSessionToken()
        }
    }

    private fun fetchSessionToken() {
        SigmaDevice.getSessionToken(object : SessionTokenCallback {
            override fun onComplete(sessionToken: String) {
                viewBinding.resultView.text = sessionToken
            }

            override fun onError(errorType: SigmaDeviceError, errorMessage: String?) {
                val error = if (!errorMessage.isNullOrEmpty()) errorMessage else UNKNOWN_ERROR
                Snackbar.make(viewBinding.layout, error, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {}

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?, p1: PermissionToken?
    ) {
    }

    companion object {
        const val UNKNOWN_ERROR = "unknown error"
    }
}