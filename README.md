<!--
  Copyright 2020-2023 Ayogo Health Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->

cordova-plugin-update-notifier
==============================

This plugin provides a mechanism for showing an in-app notification when a new
version of the app is available for download from the App Store or Play Store.

For iOS, this uses the [Siren][siren] library.

For Android, this implements the [Play Store In-App Update][playlib] system.

> ℹ️ **This plugin uses AndroidX!**
>
> Use version 1.x if you are building without AndroidX enabled.


Installation
------------

### Cordova

```
cordova plugin add cordova-plugin-update-notifier
```

#### Specifying Android Library Versions
You may need to specify specific versions of the Android Material Design or
Play App Update frameworks, depending on the Android SDK version and build
tools that your app is targeting. You can override these by specifying versions
as variables when installing.

For example:

```
cordova plugin add cordova-plugin-update-notifier \
    --variable ANDROIDX_MATERIAL_DESIGN_VERSION=1.8.0 \
    --variable PLAY_APP_UPDATE_SDK_VERSION=2.1.0
```

### Capacitor

```
npm install cordova-plugin-update-notifier
npx cap sync
```

#### Note about Android strings for Capacitor

To override the text shown in the banner when an update is ready to install,
add the following to `app/src/main/res/values/strings.xml`:

```xml
<string name="app_update_ready">An update has just been downloaded.</string>
<string name="app_update_install">RESTART</string>
```

Configuration Preferences
------------

### Alert Type

Siren's implementation for iOS allows for different alert types (see https://github.com/ArtSabintsev/Siren#screenshots). You can set the value to "critical" or "annoying" in config.xml.

```xml
<preference name="SirenAlertType" value="critical" />
<preference name="SirenAlertType" value="annoying" />
```

For Android, you can force all updates to be considered "immediate" with the `AndroidUpdateAlertType` preference in config.xml.

```xml
<preference name="AndroidUpdateAlertType" value="Immediate" />
```

### Non US-AppStore iOS apps

Siren's implementation for iOS requires specifying a country code if your app is not published to the US AppStore.

```xml
<preference name="SirenCountryCode" value="CA" />
```

For Capacitor, add `"SirenCountryCode": "CA"` to your capacitor.config.json file.


### Managed App Configuration

When deploying an app using an MDM, you can take advantage of [Managed App Configuration](https://developer.apple.com/library/archive/samplecode/sc2279/Introduction/Intro.html) to disable the update check. Simply create a preference called "DisableUpdateCheck" and set it's value to "true".

Supported Platforms
-------------------

* **Cordova CLI** (cordova-cli >= 9.0.0)
* **iOS** (cordova-ios >= 5.0.0, or capacitor)
* **Android** (cordova-android >= 9.0.0, or capacitor) with AndroidX


Contributing
------------

Contributions of bug reports, feature requests, and pull requests are greatly
appreciated!

Please note that this project is released with a [Contributor Code of
Conduct][coc]. By participating in this project you agree to abide by its
terms.


Licence
-------

Released under the Apache 2.0 Licence.  
Copyright © 2020-2023 Ayogo Health Inc.

[siren]: https://sabintsev.com/Siren/
[playlib]: https://developer.android.com/guide/playcore/in-app-updates
[coc]: https://github.com/AyogoHealth/cordova-plugin-update-notifier/blob/main/CODE_OF_CONDUCT.md
