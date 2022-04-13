# Sigma Device Android SDK

# Version: 1.1.0 - Release Date : Apr 2022

The Sigma Device Android SDK provides a framework for adding device fingerprinting into your native Android applications.

## Minimum requirements

-   Android SDK version 22
-   Minimum IDE: Android Studio

## Installation

The Sigma Device Android SDK is distributed via Jitpack. You can install the SDK with Maven or by downloading and importing the AAR library into your application.

To install the SDK with Maven:

1.  Add the SDK dependency to the project's Gradle file:

```
implementation 'com.github.socure-inc:device-risk-android-sdk:<latest version>'
```

2.  Add your Github username and the `authToken` listed below to the `gradle.properties` file:

```
username=<GitHub username>

authToken=jp_9qd9gr9snt8g4qcmi2ebmo40dr
```

3.  Add the Maven URL to the root/project level in `build.gradle`:

```
allprojects {
    repositories {
        ...
        maven {
            url "https://jitpack.io" 
	   credentials { username authToken }
        }
    }
}
```

## Configuration and usage

The Sigma Device Android SDK uses the following functions to collect device data and send it to Socure:

-   [setTracker()](#settracker)
-   [sendData()](#senddata)

### setTracker()

The `setTracker()` function configures the SDK and specifies the types of device data to collect and track.

To call `setTracker()`:

```
fun setTracker( key: String? = null, trackers: List, userConsent: Boolean?, activity: AppCompatActivity, callback: DataUploadCallback )
```

| Data Type     | Description                                                                                                         |
| ------------- | ------------------------------------------------------------------------------------------------------------------- |
| `key`         | The public SDK key for your account.                                                                                |
| `trackers`    | An array of data sources that identifies the types of device data to collect.                                       |
| `userConsent` | Indicates if the consumer has provided consent. If set to `false`, the SDK will not collect any device information. |
| `activity`    | The `activity` in which device fingerprinting is run.                                                               |
| `callback`    | Retrieves the status of `sendData()`.                                                                               |

#### Device data sources

Sigma Device depends on the following types of data to gauge the authenticity of a device:

| Data Type           | Description                                                                                                                                                           |
| ------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Device-centric data | Data tied to information about linguistic, cultural, and technological conventions used to format data for presentation, such as calendar type and keyboard language. |
| Accessibility       | Active accessibility settings.                                                                                                                                        |
| Network             | WiFi connection information. Enable the Access WiFi Information service for your App ID and include it in `Entitlements.plist` to access the information.             |

While there may be circumstances in which not all data types are available, we encourage you to gather as much data as possible. The data type `location` is strongly recommended. We also recommend, at a minimum:

-   `device`
-   `locale`
-   `accessibility`
-   `network`

The optimal list of data types are as follows:

-   `Device`
-   `Accelerometer`
-   `Magnetometer`
-   `Gyroscope`
-   `Gravity`
-   `Rotation`
-   `Location`
-   `MotionProximity`
-   `Pedometer`
-   `Locale`
-   `Advertising`
-   `Network`
-   `Exif`

For example, to track all services, call `setTracker()` as follows:

```
val list = mutableListOf<DeviceRiskManager.DeviceRiskDataSourcesEnum>() //motion list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Accelerometer) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Magnetometer) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Gyroscope) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Rotation) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Gravity) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.MotionProximity) //info list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Device) //location list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Location) //Advertising list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Advertising) //Locale list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Locale) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Pedometer) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Network) list.add(DeviceRiskManager.DeviceRiskDataSourcesEnum.Exif) deviceRiskManager.setTracker(key: "your-key-goes-here", list, this, this)
```

### sendData()

The `sendData()` function collects the device data, sends it to Socure’s servers, and returns a `deviceSessionId`.

To call `sendData()`:

```
DeviceRiskManager.sendData(context:String)
```

**Note**: To retrieve an existing `deviceSessionId` for a device, use the `uuid` variable of `DeviceRiskManager`.

### Context parameter

The `context` parameter helps Socure understand where, or under what circumstances, device data is collected. The available `context` enums are described below:

| Context       | Description                                                                                     | Example                                                                       |
|---------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| `homepage`    | The main page that loads when the consumer opens an application.                                | `DeviceRiskManager.sharedInstance.sendData(context: .homepage)`               |
| `signup`      | Pages related to creating an account.                                                           | `DeviceRiskManager.sharedInstance.sendData(context: .signup)`                 |
| `login`       | Pages that serve as an entry or access point to an application, such as a login page.           | `DeviceRiskManager.sharedInstance.sendData(context: .login)`                  |
| `profile`     | Pages on which account or profile updates are completed.                                        | `DeviceRiskManager.sharedInstance.sendData(context: .profile)`                |
| `password`    | Pages related to password activities, such as resetting a password and creating a new password. | `DeviceRiskManager.sharedInstance.sendData(context: .password)`               |
| `transaction` | Pages related to transactions, such as adding an item to a cart and completing a purchase.      | `DeviceRiskManager.sharedInstance.sendData(context: .transaction)`            |
| `checkout`    | The page on which a consumer completes a purchase.                                              | `DeviceRiskManager.sharedInstance.sendData(context: .checkout)`               |
| `other`       | Allows you to pass a custom value for pages on which the above context values do not apply.     | `DeviceRiskManager.sharedInstance.sendData(context: .other("custom context")` |

#### DataUploadCallback

`DataUploadCallback` is a protocol object required by `DeviceRiskManager` that retrieves the status of `sendData()`. It implements two main functions:

| Function                                                       | Description                                                                                                                                                                         |
|----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dataUploadFinished(uploadResult: UploadResult)`               | The `dataUploadFinished()` function is returned when `sendData()` is successful. `UploadResult` is an object class that returns the device session `uuid` computed by our services. |
| `onError(errorType: SocureSDKErrorType, errorMessage: String)` | The `onError()` function is returned when `sendData()` encounters an error. We provide the error type via `errorType` and a message via `errorMessage`.                             |
