# Device Risk Android SDK Quick Start Guide

This document details the integration steps required to integrate the Device Risk Android SDK into your application or library.

## Latest Release

![GitHub release (latest by date)](https://img.shields.io/github/v/release/socure-inc/socure-sigmadevice-sdk-android)

## Minimum requirements

Before getting started, check that your development environment meets the following requirements:

- Target SDK 34
- Min SDK 21
- Kotlin 1.8.10 and later
- Gradle 8.0 and later

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
implementation 'com.socure.devicerisk.sdk:socure-devicerisk:<latest-release-version>'
```

4. Specify `android:allowBackup="true"` in your app's `AndroidManifest.xml` application attributes. This will improve persistence of your `sessionToken` when your app gets reinstalled for clients who have enabled backup in their device settings.

### Proguard Rules

If minify is enabled in Gradle `minifyEnabled true` add the following in your proguard rules:

```
-keep class com.socure.idplus.** { *; }
```

## Configuration and usage
For instructions on how to configure the SDK, see the [Android SDK documentation](https://developer.socure.com/docs/sdks/digital-intelligence/android-sdk) on DevHub.
