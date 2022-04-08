# Device Risk Android SDK

# Version: 1.0.0.7 - Release Date : July 2021

The Socure Device Risk  SDK provides a framework to identify the authenticity of phones and users of your mobile application. This guide covers integration with Android.

- **Minimum Requirements**: minSdkVersion 22
- **Minimum IDE**: Android Studio

NOTE: If you are also integrating the Document Verification SDK, Device Risk is already included with it. You would only need to declare a variable in you project's gradle.properties "useDeviceRisk=true"
If you are integrating Device Risk only, please continue following the directions below.

## Introduction

The Device Risk SDK uses a number of device features to ascertain the authenticity of a device. These features are:

* Device-centered data: Data tied to properties of the device (available RAM, OS version used, etc.).
* Locale information: Information pertaining to the user’s locale (for example: calendar type used, language used in keyboard, etc.).
* Accessibility data: Any accessibility settings active on the device
* Network data: Current WiFi connection information from the device
* Location
* Device motion data
* Device’s Advertising ID
* Image Exif Information (when used with Socure's Document Verification framework)

For the most part, Device Risk will only leverage the device features and services already used by your app. Device Risk will **only** track the devices features you specify during the initialization process. In order to minimize any reinitialization of existing services, the DeviceRisk framework expects that you, as the end developer, will provide the data expected for the features and services DeviceRisk was initialized and tasked to track. If said data is not provided to the SDK before the data is ready to be transmitted, the SDK will attempt to gather the data on its own. **This may mean that the SDK itself will attempt to request permissions for services out of order from your intended flow**

Please note, if you initialize the SDK with a specific service or feature to use, and you do not provide it with the service’s appropriate data before transmitting it to us, the SDK **will do a best effort to compile the data on its own.**

## Installation

**Step 1a: Install Socure SDK**

The SDK, at this time, is published as an AAR library and must be imported into your application.

_Note: Once a release version is ready, we will make it available via Gradle and Maven._

Add the dependency to the project's Gradle file as

_implementation 'com.github.socure-inc:device-risk-android-sdk:X.X'_

**Step 1b: Install With Maven

1. Add your GitHub username and the below listed Token in your `gradle.properties` file

```

username=Your GitHub User Name
authToken=jp_9qd9gr9snt8g4qcmi2ebmo40dr

```

2. Add the maven URL in the root/project level `build.gradle` :

```
buildscript {
	..............
	dependencies {
		........................
		classpath 'com.github.dcendents:android-maven-gradle-plugin:1.5'
	}
}

(NOT IN THE `buildscript` code block above. allprojects is a sibling to build script)


allprojects {
    repositories {
        .......

        maven {
            url "https://jitpack.io"
	   credentials { username authToken }
        }
    }
}

build.gradle
implementation 'com.github.socure-inc:device-risk-android-sdk:1.0.0.7'


**Step 2: Add appropriate permissions for the services you want DeviceRisk SDK to use**

This is explained in more detail in the [Introduction](#Introduction).

**Step 3: Import the SDK into your desired View Controller**

Add the following line to import the desired DeviceRisk activity.

```
import com.socure.idplus.devicerisk.androidsdk.sensors.DeviceRiskManager
```

**Step 4: Implement Extension**

  The DeviceRisk SDK uses the `DataUploadCallback` protocol to provide feedback on the results of the `sendData` function. There are two protocols that needs to be implemented.
* `dataUploadFinished(uploadResult: UploadResult) `. This is returned when `sendData` has no complications. `UploadResult` is an object class that, at this time, returns the UUID  computed by our services.
* `onError(errorType: SocureSDKErrorType, errorMessage: String)`. This is returned when `sendData` encounters an error. We provide the error type via `errorType` and a message via `errorMessage`.

## Usage

### Set-up and Configuration

The DeviceRisk SDK’s main class available to end-developers is `DeviceRiskManager`. You can either initialize a local instance of `DeviceRiskManager` via.

```
val deviceRiskManager = DeviceRiskManager()
```

In either case, your next step is to configure `DeviceRiskManager`.

### setTrackerda

Call `setTracker` with the following:

```
fun setTracker(
            key: String? = null,
            trackers: List<DeviceRiskDataSourcesEnum>,
            userConsent: Boolean?,
            activity: AppCompatActivity,
            callback: DataUploadCallback
    )
```


In setTracker:

- `key` input parameter is your SDK key procured from the Socure admin dashboard.
- `DeviceRiskDataSources` is an enum that encompasses all of the different device features and services we support.
- `setTracker` is expecting an array of `DeviceRiskDataSources` to determine which data from which services to expect.
- `userConsent` You must set the userConsent parameter to true before setTracker for Android and iOS.

These are:

### DeviceRiskDataSources

The data type `location` is strongly recommended. We also recommend, at a minimum:

- `device`
- `locale`
- `network`

The full list:

```
    Device,
    Accelerometer,
    Magnetometer,
    Gyroscope,
    Gravity,
    Rotation,
    Location,
    MotionProximity,
    Pedometer,
    Locale,
    Advertising,
    Network,
    Exif
```

If you want to track all services possible, for example, call `setTracker` with:

```
  val list = mutableListOf<DeviceRiskManager.DeviceRiskDataSourcesEnum>()
  //motion
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Accelerometer)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Magnetometer)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Gyroscope)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Rotation)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Gravity)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.MotionProximity)
  //info
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Device)
  //location
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Location)
  //Advertising
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Advertising)
  //Locale
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Locale)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Pedometer)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Network)
  list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Exif)
  deviceRiskManager.setTracker(key: "your-key-goes-here", list, this, this)
```

To obtain the `DeviceRiskManager` callback from `DataUploadCallback`, the activity you'd like to listen in on the callback must implement the interface `DeviceRiskManager.DataUploadCallback` as follows:

```
class MyActivity : AppCompatActivity(), DeviceRiskManager.DataUploadCallback
```

### Provide Data to SDK

There are certain Android device features like Location retrieval, device motion retrieval, pedometer data where running multiple instances of these services is battery-intensive and not recommended. To that end, we have created a number of functions for you to use to provide the SDK with the expected necessary data.

Note: these calls must be called **after** [setTracker](#setTracker), but **before** [sendData](#sendData) is called.

> Warning: Run `sendData` BEFORE making an API call to ID+. This ensures ID+ has the most current up-to-date device data for that device.

These are:

### passMotionData

```
fun passMotionData(
            accelerometerModel: AccelerometerModel?,
            magnetometerModel: MagnetometerModel?,
            gravityModel: GravityModel?,
            gyroscopeModel: GyroscopeModel?,
            rotationModel: RotationModel?,
            proximity: String?
    )
```

`passMotionData` accepts:
- Accelerometer data via a `AccelerometerModel` object
- Magnetometer data via a `MagnetometerModel` object
- Gravity data via a `GravityModel` object
- Gyroscope data via a `GyroscopeModel` object
- RotationModel data via a `RotationModel` object
- Proximity data via a String

###  passPedometerData

```
fun passPedometerData(pedometerModel: PedometerModel?)
```

`passPedometerData` accepts the pedometer data via a `PedometerModel` object

### passLocationData

```
fun passLocationData(locationModel: LocationModel?)
```

`passLocationData` accepts the location data via a `LocationModel` object


### passDocumentVerificationData

```
fun passDocumentVerificationData(exifData: MutableList<Pair<String, String>>?)
```

`passDocumentVerificationData` accepts the data from the images via a `MutableList<Pair<String, String>>`.

### sendData

`sendData` communicates with Socure’s back-end services, takes all of the information provided and calculates a `UUID` for your device.  Please note: once successful, you can retrieve the calculated UUID from the `uuid` variable of `DeviceRiskManager`.

You can call `sendData` like so:

```
deviceRiskManager.sendData()
```

Note: to get the UUID, as well as the end status of `sendData`, you need to implement:

### DataUploadCallback

`DataUploadCallback` is a protocol object that implements two main functions.

- `dataUploadFinished(uploadResult: UploadResult) `. This is returned when `sendData` has no complications. `UploadResult` is an object class that, at this time, returns the UUID  computed by our services.
- `onError(errorType: SocureSDKErrorType, errorMessage: String)`. This is returned when `sendData` encounters an error. We provide the error type via `errorType` and a message via `errorMessage`
