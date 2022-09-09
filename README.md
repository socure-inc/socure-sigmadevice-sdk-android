# Sigma Device Android SDK

The Sigma Device Android SDK provides a framework for adding device fingerprinting into your native
Android applications.

## Minimum requirements

- Android SDK version 21
- Minimum IDE: Android Studio

## Installation

The Sigma Device Android SDK is distributed via Jitpack. You can install the SDK with Maven or by
downloading and importing the AAR library into your application.

To install the SDK with Maven:

1. Add the SDK dependency to the app's Gradle file:

```
implementation 'com.github.socure-inc:device-risk-android-sdk:1.2.0'
```

2. Add the Maven URL to the root/project level in `build.gradle`:

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

3. Import the SDK into your desired View Controller by adding the following line to import the
   desired Sigma Device activity:

```
import com.socure.idplus.devicerisk.androidsdk.sensors.DeviceRiskManager
```

4. Specify `android:allowBackup="true"` in your app's `AndroidManifest.xml`'s application attributes. This will improve persistence of issued deviceSessionIds when your app gets reinstalled for users who have enabled backup in their device settings.

5. We advise that you declare the `READ_EXTERNAL_STORAGE` permission in your app's `AndroidManifest.xml`. The Sigma Device SDK uses this to assess the size and free space of any external storage.

```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## Configuration and usage

For instructions on how to configure the SDK, see the [Android SDK documentation](https://developer.socure.com/docs/sdks/sigma-device/android-sdk/android-overview) on DevHub.
