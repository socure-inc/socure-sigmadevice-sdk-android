# Device Risk Android SDK Quick Start Guide

The Device Risk Android SDK provides a framework for adding device fingerprinting into your native Android applications. 

## What's new in version 3

Customers integrating with the Device Risk Android SDK v3 have the option to enable a new functionality that collects user behavioral biometrics, which are used as additional signals for identifying fraud. This feature works passively in the background on the user's mobile session to capture and analyze usage patterns such as: 

- Taps and swipes
- How form fields are accessed and filled
- Navigation patterns
- Timing patterns (such as session duration or the amount of time spent in specific form fields)

For more information on how to enable this feature, see the [Configuration and usage](#configuration-and-usage) section below.

## Minimum requirements

Before getting started, check that your development environment meets the following requirements:

- Target SDK 33
- Min SDK 21
- Kotlin 1.7.0 and later
- Gradle 7.0 and later

## Installation

The Device Risk Android SDK is distributed via Jitpack. You can install the SDK with Maven or by downloading and importing the AAR library into your application.

To install the SDK with Maven:

1. Add the SDK dependency to the app's Gradle file: 

```
implementation 'com.github.socure-inc:device-risk-android-sdk:x.x.x'
```

2. Add the Maven URL to the root/project level in build.gradle:

```
allprojects {
   repositories {
       ...
       maven {
           url "https://jitpack.io"
       }
   }
}
```

3. Import the SDK into your desired View Controller by adding the following line to import the desired Device Risk activity:

```
import com.socure.idplus.devicerisk.androidsdk.sensors.SocureSigmaDevice
```

4. Specify android:allowBackup="true" in your app's AndroidManifest.xml's application attributes. This will improve persistence of your `deviceSessionId` when your app gets reinstalled for clients who have enabled backup in their device settings.

5. We advise that you declare the READ_EXTERNAL_STORAGE permission in your app's AndroidManifest.xml. The Device Risk SDK uses this to assess the size and free space of any external storage:

```
<uses-permission 
android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### Proguard Rules:

If minify is enabled in Gradle `minifyEnabled true` please add the following in your proguard rules:

```
-keep class com.socure.idplus.** { *; }
```

## Configuration and usage

The Device Risk Android SDK's main class is `SocureSigmaDevice`. You can initialize a local instance of `SocureSigmaDevice` as `val socureSigmaDevice = SocureSigmaDevice()`.

### Configure the SocureSigmaDevice class

The SDK uses the following functions to collect device data and send it to Socure:

- `fingerPrint()`
- `SocureSigmaDeviceConfig()`
- `SocureFingerPrintOptions()`

### fingerPrint()

The `fingerPrint()` function configures the SDK and specifies the types of device data to collect and track. To call `fingerPrint()`:

```
fun fingerPrint(config: SocureSigmaDeviceConfig,
               options: SocureFingerPrintOptions,
               callback: DataUploadCallback)
```

In the example above, you can see that the `fingerPrint` method accepts three parameters: config, options, and a callback. 

#### Config

`Config` is a required parameter used to configure the Device Risk SDK. `SocureSigmaDeviceConfig` is the data model that represents this parameter value.

```
data class SocureSigmaDeviceConfig(
   val SDKKey: String,
   val isReact: Boolean = false,
   var enableBehavioralBiometrics: Boolean = false,
   var fingerprintEndpointHost: String,
   var flagEndpointHost: String,
   var activity: FragmentActivity)
```

As seen in the code block above, the following properties are necessary to configure the Device Risk SDK:

- `SDKKey`: The unique SDK key obtained from Admin Dashboard. See [SDK Keys](https://developer.socure.com/docs/admin-dashboard/developers/sdk-keys) for more information.
- `context` An optional value that is of an enum type called `SocureFingerprintContext`. When passed, it will specify the context from where the fingerprint request is being fired. 
- `isReact`: This value is always false until it's from the React Native app.
- `enableBehavioralBiometrics`: An optional value that must be set to `true` to collect user behavioral metrics, which customers can use as additional signals for identifying fraud. 
- `fingerprintEndpointHost`: An optional host for the fingerprint requests sent from the SDK. When it is not provided, the SDK will send the fingerprint requests to the default host, `dvnfo.com`.
- `flagEndpointHost`: An optional event for behavioral biometrics. If `enableBehavioralBiometrics` is set to `true` and `flagEndpointHost` is `null` or an empty string, the SDK will take the default value.
- `activity` The `FragmentActivity` where the device fingerprinting is run.

#### Options

`Options` is an optional parameter used to customize the fingerprint request. `SocureFingerprintOptions` is the data model that represents this parameter value.

```
data class SocureFingerPrintOptions(
  var omitLocationData: Boolean = false,
  val contextValue: SocureFigureprintContext,
  var mAdvertisingID: String)
```

Below is a list of `SocureFingerprintContext` enum values. You must pass a relevant string for the `other` value. For the remainder of the values, there is no need to pass any other parameters. 

```
{
   Home(),
   SignUp(),
   Login(),
   Password(),
   CheckOut(),
   Profile(),
   Transaction(),
   other(“Unknown”)
}

```

- `omitLocationData`: An optional boolean flag used to omit the location data from being sent as a part of the fingerprint request. By default, it is set to `false`. When set to `true`, the location data will not be included in the fingerprint request regardless of the location permission given by the user.
- `advertisingID`: An optional string value that, when passed, will allow the advertising ID information to be sent as a part of the fingerprint request.

#### Callback 

`Callback` is a required parameter. It's a closure that will be invoked by a success or failure response from the fingerprint request. The following code snippet shows the method signature of the callback. The closure has two responses: `onSuccess` and `onError`, which occur in that order.

- `onSuccess`: The value is optional and is of the `SocureFingerprintResult` type. It's only populated when the fingerprint request succeeds. The model contains a property `deviceSessionId`, which is the unique ID returned after a successful fingerprint request. The following is a model case for the `onSuccess` response.


```
override fun dataUploadFinished(uploadResult: SocureFingerprintResult) {
   uuid = uploadResult.deviceSessionID
}
```

- `onError`: This response is only populated when the fingerprint request fails. `SocureSDKErrorType` is a parameter of this response that gives information about an error that has occurred. The following is a model case for the `onError` response.

```
enum class SocureSigmaDeviceErrorType {
   NetworkConnectionError,
   DataUploadError,
   DataFetchError,
   UnknownError
}
```

The second parameter contains a string which gives information the exact error message:

```
fun onError(errorType: SocureSigmaDeviceErrorType, errorMessage: String?)
```

## Migration to v2

Device Risk SDK version 2.0.1 (hereafter SDK v2) introduces a new, simplified API for fingerprinting. The SDK v2 adds a new class, `SocureSigmaDevice` to expose the new API. The existing class, `DeviceRiskManager`, is still present and marked as deprecated, making the SDK v2 backwards compatible.

### v1 and v2 API parity

The v2 API does not maintain parity with the v1 API. The v2 API exposes just a single method fingerprint, compared to the multiple methods exposed by the v1 API.

### No DeviceRiskDataSources in v2

SDK v2 gets rid of the concept of data sources altogether. v2's API does not require the customer to specify the sources from where the data has been collected. Version 2 of the SDK intrinsically collects the data about the device, accessibility settings, locale settings, network settings, and location. The remaining sources of information that were a part of v1 have been discarded and are not a part of v2.

Another key detail about v2 is that the library does not trigger any requests for system permissions to access the device information. For example, with v1, when a customer specifies location as one of the sources, it will trigger an alert asking for system permission to access location data, which will no longer be the case with v2.

### No DeviceRiskUploadCallback in v2

The `DeviceRiskUploadCallback` delegate has been replaced with `SocureSigmaDevice.DataUploadCallback`.

#### Changes

For the Existing users instead of using `UploadResult` in the `dataUploadFinished` callback, it's replaced with `SocureFingerprintResult`.
