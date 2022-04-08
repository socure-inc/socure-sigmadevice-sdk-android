package com.socure.idplus.uploader

import android.content.Context
import com.socure.idplus.devicerisk.androidsdk.Constants.Companion.DOCUMENT_UPLOAD_FAILED_BY_ID_PLUS_KEY
import com.socure.idplus.devicerisk.androidsdk.Interfaces
import com.socure.idplus.devicerisk.androidsdk.R
import com.socure.idplus.devicerisk.androidsdk.SDKAppDataPublic
import com.socure.idplus.devicerisk.androidsdk.model.DocumentUploadError
import com.socure.idplus.devicerisk.androidsdk.model.InformationRequest
import com.socure.idplus.devicerisk.androidsdk.model.InformationResponse
import com.socure.idplus.devicerisk.androidsdk.verifyIdPlusKey
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class InformationUploader {
    private var informationUploadCallback: Interfaces.InformationUploadCallback? = null

    private var informationResponse: InformationResponse? = null
    private var informationRequest: InformationRequest? = null

    private var clientApiKey: String? = null

    companion object {
        private val TAG = InformationUploader::class.java.simpleName
        const val DEVICERISK = "devicerisk"
    }

    fun initializeInformationUploader(
        context: Context,
        authUploadCallback: Interfaces.InformationUploadCallback,
        idplus: String?,
        informationRequest: InformationRequest
    ) {
        if (idplus != null) {
            this.clientApiKey = context.getString(R.string.API_Key_Prefix) + " " + idplus
        }
        informationUploadCallback = authUploadCallback
        initializeInformationRequest(informationRequest)
    }


    private fun sendResponse(message: String) {
        if (message.isNotEmpty()) {
            informationUploadCallback?.informationUploadError(DocumentUploadError(message))
        } else {
            SDKAppDataPublic.informationRequest = informationRequest
            SDKAppDataPublic.informationResponse = informationResponse

            informationUploadCallback?.informationUploadFinished(informationResponse)
        }
    }

    private fun initializeInformationRequest(informationRequest: InformationRequest) {
        val modulesArrayList = ArrayList<String>()
        val module = DEVICERISK
        modulesArrayList.add(module)

        this.informationRequest = informationRequest
        this.informationRequest?.modules = modulesArrayList

        informationApiCall(informationRequest)
    }

    private fun informationApiCall(request: InformationRequest?) {
        val message = verifyIdPlusKey(this.clientApiKey)
        if (message != null) {
            informationUploadCallback?.informationUploadError(
                DocumentUploadError(
                    DOCUMENT_UPLOAD_FAILED_BY_ID_PLUS_KEY
                )
            )
            return
        }

        val getResponse = ApiClientMail().getRetrofitSandbox().create(ApiInterface::class.java)
        val call: Call<InformationResponse?>? =
            getResponse.getInformationResponse(request, this.clientApiKey)
        call?.enqueue(object : Callback<InformationResponse?> {
            override fun onResponse(
                call: Call<InformationResponse?>,
                response: Response<InformationResponse?>
            ) {
                if (response.body() != null) {
                    informationResponse = response.body()
                    informationUploadCallback?.informationUploadFinished(informationResponse)
                    sendResponse(response.message())
                } else {
                    response.errorBody()?.string()?.let {
                        if (it.isNotEmpty()) {
                            try {
                                informationUploadCallback?.informationUploadError(
                                    DocumentUploadError(it)
                                )
                                //sendResponse(response.message())
                            } catch (e: java.lang.Exception) {
                            }

                        } else {
                            informationUploadCallback?.informationUploadError(DocumentUploadError(it))
                        }
                    } ?: run {
                        //sendResponse(response.message())
                    }
                }
            }

            override fun onFailure(call: Call<InformationResponse?>, t: Throwable) {
                informationUploadCallback?.informationUploadError(DocumentUploadError(t.message.toString()))
            }
        })
    }
}