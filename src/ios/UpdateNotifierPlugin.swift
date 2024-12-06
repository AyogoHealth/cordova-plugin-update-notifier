/**
 * Copyright 2020 Ayogo Health Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Siren

@objc(CDVUpdateNotifierPlugin)
class UpdateNotifierPlugin : CDVPlugin {

    override func pluginInitialize() {
        NotificationCenter.default.addObserver(self,
                selector: #selector(UpdateNotifierPlugin._didFinishLaunchingWithOptions(_:)),
                name: UIApplication.didFinishLaunchingNotification,
                object: nil);
    }


    @objc internal func _didFinishLaunchingWithOptions(_ notification : NSNotification) {
        // Check if there's an MDM setting to disable update checking
        let disableUpdateCheck = UserDefaults.standard.dictionary(forKey: "com.apple.configuration.managed")?["DisableUpdateCheck"] as? String
        if (disableUpdateCheck == "true") {
            return;
        }

        let siren = Siren.shared

        if let alertType = self.commandDelegate.settings["sirenalerttype"] as? String {
            switch alertType {
            case "critical":
                siren.rulesManager = RulesManager(globalRules: .critical)
                break;
            case "annoying":
                siren.rulesManager = RulesManager(globalRules: .annoying)
                break;
            case "persistent":
                siren.rulesManager = RulesManager(globalRules: .persistent)
                break;
            case "hinting":
                siren.rulesManager = RulesManager(globalRules: .hinting)
                break;
            case "relaxed":
                siren.rulesManager = RulesManager(globalRules: .relaxed)
                break;
            default:
                siren.rulesManager = RulesManager(globalRules: .default)
            }
        }

        if let countryCode = self.commandDelegate.settings["sirencountrycode"] as? String {
            siren.apiManager = APIManager(countryCode: countryCode)
        }

        siren.wail()
    }
}
