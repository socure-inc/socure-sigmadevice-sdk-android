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
implementation 'com.socure.devicerisk.sdk:socure-devicerisk:4.0.1'
```

4. Specify `android:allowBackup="true"` in your app's `AndroidManifest.xml` application attributes. This will improve persistence of your `sessionToken` when your app gets reinstalled for clients who have enabled backup in their device settings.

### Proguard Rules

If minify is enabled in Gradle `minifyEnabled true` add the following in your proguard rules:

```
-keep class com.socure.idplus.** { *; }
```

## Configuration and usage
For instructions on how to configure the SDK, see the [android SDK documentation](https://developer.socure.com/docs/sdks/sigma-device/android-sdk/) on DevHub.
