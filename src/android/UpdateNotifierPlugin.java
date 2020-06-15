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

package com.ayogo.cordova.updatenotifier;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import static android.app.Activity.RESULT_OK;

public class UpdateNotifierPlugin extends CordovaPlugin {
    private AppUpdateManager mAppUpdateManager;
    private InstallStateUpdatedListener mInstallListener;

    private final String TAG = "UpdateNotifierPlugin";
    private static final Integer RC_APP_UPDATE = 577;


    /**
     * Called after plugin construction and fields have been initialized.
     */
    @Override
    public void pluginInitialize() {
        LOG.i(TAG, "Initializing");
    }


    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    public void onStart() {
        mInstallListener = new InstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(InstallState state) {
                if (state.installStatus() == InstallStatus.DOWNLOADED){
                    popupSnackbarForCompleteUpdate();
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null){
                        mAppUpdateManager.unregisterListener(mInstallListener);
                    }

                } else {
                    LOG.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                }
            }
        };

        mAppUpdateManager = AppUpdateManagerFactory.create(cordova.getActivity());
        mAppUpdateManager.registerListener(mInstallListener);

        Task<AppUpdateInfo> appUpdateInfoTask = mAppUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, cordova.getActivity(), RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, cordova.getActivity(), RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else {
                    LOG.e(TAG, "getAppUpdateInfo: Unhandled case");
                }
            }
        });
    }

    /**
     * Called when the activity is no longer visible to the user.
     */
    @Override
    public void onStop() {
        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(mInstallListener);
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode   The request code originally supplied to startActivityForResult(),
     *                      allowing you to identify who this result came from.
     * @param resultCode    The integer result code returned by the child activity through its setResult().
     * @param intent        An Intent, which can return result data to the caller (various data can be
     *                      attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_APP_UPDATE && resultCode != RESULT_OK) {
            LOG.e(TAG, "App Update failed! Result code: " + resultCode);
        }
    }

    private void popupSnackbarForCompleteUpdate() {
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Activity activity = cordova.getActivity();

                Snackbar snackbar = Snackbar.make(webView.getView(), activity.getString(activity.getResources().getIdentifier("app_update_ready", "string", activity.getPackageName())), Snackbar.LENGTH_INDEFINITE);

                snackbar.setAction(activity.getString(activity.getResources().getIdentifier("app_update_install", "string", activity.getPackageName())), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.completeUpdate();
                        }
                    }
                });

                snackbar.show();
            }
        });
    }
}
