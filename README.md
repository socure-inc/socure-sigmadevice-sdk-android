# Device Risk Android SDK Quick Start Guide

This document details the integration steps required to integrate the Device Risk Android SDK into your application or library.

## What's new in version 4.0.0

- `SessionToken` has been added as part of the security enhancement.
- We have added an additional `initializeSDK()` call that initializes the SDK.

## Minimum requirements

Before getting started, check that your development environment meets the following requirements:

- Target SDK 34
- Min SDK 21
- Kotlin 1.7.10 and later
- Gradle 7.0 and later

## Installation

Follow the steps below to set up the Android SDK.

1. Add the Socure Maven URL to the root/project level in build.gradle:

```
allprojects {
   repositories {
       ...
       maven {
           url "https://sdk.socure.com/"
       }
   }
}
```

2. Add the SDK dependency to the app's gradle file, make sure to set the version number to the latest one:

```
implementation 'com.socure.devicerisk.sdk:socure-devicerisk:4.0.0'
```

4. Specify `android:allowBackup="true"` in your app's `AndroidManifest.xml` application attributes. This will improve persistence of your `sessionToken` when your app gets reinstalled for clients who have enabled backup in their device settings.

### Proguard Rules

If minify is enabled in Gradle `minifyEnabled true` add the following in your proguard rules:

```
-keep class com.socure.idplus.** { *; }
```

## Configuration and usage

The Device Risk Android SDK's main class is `SigmaDevice`. At a high level the SDK provides the following methods to collect data to send it to the Socure backend:

- `initializeSDK()`
- `processDevice()`

### `initializeSDK()`

Prior to interacting with the Device Risk Android SDK, it must be initialized with `sdkKey` obtained from the [**Developers > SDK Key**](../../admin-dashboard/developers/sdk-keys.md) tab in the Admin Dashboard.

The `initializeSDK()` function configures the SDK. It requires the Android `context`, `sdkKey`, `sigmaDeviceOptions` and a `sigmaDeviceCallback`. It should preferably be called from the App's Application class at the time of initialization:

```
initializeSDK(context: Context, sdkKey: String, sigmaDeviceOptions: SigmaDeviceOptions, sigmaDeviceCallback: SigmaDeviceCallback)
```

#### `SigmaDeviceOptions`

These options can be passed into the `initializeSDK()` method to customize the SDK operation. It is represented as below:

```
data class SigmaDeviceOptions(
  val omitLocationData: Boolean = false,
  val advertisingID: String? = null)
```

- `omitLocationData`:  A boolean flag used to omit the location data from being sent as part of data collection. By default, it is set to `false`. When set to `true,` the location data will not be included during data collection regardless of the location permission given by the user.
- `advertisingID`: An optional string value that, when passed, will allow the advertising ID information to be sent as a part of data collection. Defaults to `null`.

#### `SigmaDeviceCallback`

`SigmaDeviceCallback` provides information about the status of the initialization call. The `initializeSDK()` call sets up data collection and returns the created `sessionToken` using the `onSessionCreated()` callback. The `sessionToken` can then be used on the respective device endpoints to fetch the data associated with the Session.

```
fun onSessionCreated(sessionToken: String) {}
```

### `processDevice()`

The `initializeSDK()` automatically collects the data when initialized. But in cases where you need to explicitly invoke a data collection step, you can make use of `processDevice()`. Here we provide a `SigmaDeviceContext` to define the flow or screen within which the call occurs. 

```
fun processDevice(sigmaDeviceContext: SigmaDeviceContext, sessionTokenCallback: SessionTokenCallback)
```

The following context values can be provided when calling `processDevice()`:

```
SigmaDeviceContext: 
   Default,
   Home,
   SignUp,
   Login,
   Password,
   Checkout,
   Profile,
   Transaction,
   Other("Custom Value")
```

Note the `initializeSDK()` call uses the `Default` value of `SigmaDeviceContext`.

#### `SessionTokenCallback`

`SessionTokenCallback` provides information about the status of the `processDevice()` call. Here we explicitly invoke data collection and return a `sessionToken` using the `onComplete()` callback. The `sessionToken` can then be used on the respective device endpoints to fetch the data associated with the Session.

```
fun onComplete(sessionToken: String) {}
```

### Error Handling

Both the `SigmaDeviceCallback` and `SessionTokenCallback` provide an `onError()` callback to let the user know when the SDK encounters an error. The `SigmaDeviceError` denotes the type of the error. We also provide an additional `errorMessage` string for a more descriptive message around the error.

```
fun onError(errorType: SigmaDeviceError, errorMessage: String?)
```

Below are the types of errors returned by the SDK:

```
SigmaDeviceError:
   NetworkConnectionError,
   DataUploadError,
   DataFetchError,
   UnknownError,
   ContextFetchError
```
