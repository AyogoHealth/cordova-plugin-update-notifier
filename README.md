<!--
  Copyright 2020 Ayogo Health Inc.

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


Installation
------------

### Cordova

```
cordova plugin add cordova-plugin-update-notifier
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


Supported Platforms
-------------------

* **iOS** (cordova-ios >= 5.0.0, or capacitor)
* **Android** (cordova-android >= 8.0.0, or capacitor)


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
Copyright © 2020 Ayogo Health Inc.

[siren]: https://sabintsev.com/Siren/
[playlib]: https://developer.android.com/guide/playcore/in-app-updates
[coc]: https://github.com/AyogoHealth/cordova-plugin-update-notifier/blob/main/CODE_OF_CONDUCT.md
