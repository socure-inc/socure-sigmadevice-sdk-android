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
implementation 'com.github.socure-inc:device-risk-android-sdk:1.1.0'
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

## Configuration and usage

The Sigma Device Android SDK's main class is `DeviceRiskManager`. You can initialize a local
instance of `DeviceRiskManager` as `val deviceRiskManager = DeviceRiskManager()`.

### Configure the `DeviceRiskManager` Class

The SDK uses the following functions to collect device data and send it to Socure:

- [setTracker()](#settracker)
- [sendData()](#senddata)

### setTracker()

The `setTracker()` function configures the SDK and specifies the types of device data to collect and
track.

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
| Network             | WiFi connection information.              |

While there may be circumstances in which not all data types are available, we encourage you to
gather as much data as possible. The data type `location` is strongly recommended. We also
recommend, at a minimum:

- `device`
- `locale`
- `network`

The optimal list of data types are as follows:

- `Device`
- `Accelerometer`
- `Magnetometer`
- `Gyroscope`
- `Gravity`
- `Rotation`
- `Location`
- `MotionProximity`
- `Pedometer`
- `Locale`
- `Advertising`
- `Network`
- `Exif`

For example, to track all services, call `setTracker()` as follows:

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

### sendData()

The `sendData()` function collects the device data, sends it to Socure’s servers, and returns
a `deviceSessionId`.

To call `sendData()`:

```
deviceRiskManager.sendData(context:String)
```

### Context parameter

The `context` parameter helps Socure understand where, or under what circumstances, device data is
collected. The available `context` enums are described below:

| Context       | Description                                                                                     | Example                                                                       |
|---------------|-------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|
| `homepage`    | The main page that loads when the consumer opens an application.                                | `deviceRiskManager.sendData(DeviceRiskManager.Context.Home)`               |
| `signup`      | Pages related to creating an account.                                                           | `deviceRiskManager.sendData(DeviceRiskManager.Context.SignUp)`                 |
| `login`       | Pages that serve as an entry or access point to an application, such as a login page.           | `deviceRiskManager.sendData(DeviceRiskManager.Context.Login)`                  |
| `profile`     | Pages on which account or profile updates are completed.                                        |   `deviceRiskManager.sendData(DeviceRiskManager.Context.Profile)`               |
| `password`    | Pages related to password activities, such as resetting a password and creating a new password. | `deviceRiskManager.sendData(DeviceRiskManager.Context.Password)`               |
| `transaction` | Pages related to transactions, such as adding an item to a cart and completing a purchase.      | `deviceRiskManager.sendData(DeviceRiskManager.Context.Transaction)`            |
| `checkout`    | The page on which a consumer completes a purchase.                                              | `deviceRiskManager.sendData(DeviceRiskManager.Context.CheckOut)`               |
| `other`       | Allows you to pass a custom value for pages on which the above context values do not apply.     | `deviceRiskManager.sendData("custom context")` |

#### DataUploadCallback

`DataUploadCallback` is an interface required by `DeviceRiskManager` (set via `setTracker`
call `callback`) that retrieves the status of `sendData()`. It implements two main functions:

| Function                                                       | Description                                                                                                                                                                         |
|----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `dataUploadFinished(uploadResult: UploadResult)`               | The `dataUploadFinished()` function is returned when `sendData()` is successful. `UploadResult` is an object class that returns the device session `uuid` computed by our services. |
| `onError(errorType: SocureSDKErrorType, errorMessage: String)` | The `onError()` function is returned when `sendData()` encounters an error. We provide the error type via `errorType` and a message via `errorMessage`.                             |
