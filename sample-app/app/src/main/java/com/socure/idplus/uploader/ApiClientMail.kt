package com.socure.idplus.uploader

import com.socure.idplus.BuildConfig
import com.socure.idplus.Constants
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientMail {

    fun getRetrofitSandbox(): Retrofit {
        val certPinner = CertificatePinner.Builder()
            .add(
                Constants.CERT_PIN_DOMAIN,
                BuildConfig.CERT_PIN_SOCURE
            )
            .add(
                Constants.CERT_PIN_DOMAIN,
                BuildConfig.CERT_PIN_AMAZON_INTERMEDIATE
            )
            .add(
                Constants.CERT_PIN_DOMAIN,
                BuildConfig.CERT_PIN_AMAZON_ROOT
            )
            .build()

        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            client.addInterceptor(loggingInterceptor)
        }

        client.connectTimeout(50, TimeUnit.SECONDS)
        client.readTimeout(50, TimeUnit.SECONDS)
        client.writeTimeout(50, TimeUnit.SECONDS)
        client.certificatePinner(certPinner)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.IDPLUS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}