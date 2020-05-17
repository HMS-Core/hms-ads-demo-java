/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.hms.ads.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hms.ads.consent.bean.AdProvider;
import com.huawei.hms.ads.consent.constant.ConsentStatus;
import com.huawei.hms.ads.consent.constant.DebugNeedConsent;
import com.huawei.hms.ads.consent.inter.Consent;
import com.huawei.hms.ads.consent.inter.ConsentUpdateListener;
import com.huawei.hms.ads.sdk.dialogs.ConsentDialog;
import com.huawei.hms.ads.sdk.dialogs.ProtocolDialog;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int PROTOCOL_MSG_TYPE = 100;

    private static final int CONSENT_MSG_TYPE = 200;

    private static final int MSG_DELAY_MS = 1000;

    private ListView listView;

    private List<AdFormat> adFormats = new ArrayList<>();

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (MainActivity.this.hasWindowFocus()) {
                switch (msg.what) {
                    case PROTOCOL_MSG_TYPE:
                        showPrivacyDialog();
                        break;
                    case CONSENT_MSG_TYPE:
                        checkConsentStatus();
                        break;
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAdItems();

        listView = findViewById(R.id.item_list_view);
        final AdSampleAdapter adapter =
            new AdSampleAdapter(MainActivity.this, android.R.layout.simple_list_item_1, adFormats);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AdFormat adFormat = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), adFormat.getTargetClass());
                startActivity(intent);
            }
        });

        // Show the dialog for setting user privacy.
        sendMessage(PROTOCOL_MSG_TYPE, MSG_DELAY_MS);
    }

    private void initAdItems() {
        adFormats.add(new AdFormat(getString(R.string.banner_ad), BannerActivity.class));
        adFormats.add(new AdFormat(getString(R.string.native_ad), NativeActivity.class));
        adFormats.add(new AdFormat(getString(R.string.reward_ad), RewardActivity.class));
        adFormats.add(new AdFormat(getString(R.string.interstitial_ad), InterstitialActivity.class));
    }

    /**
     * Display the app privacy protocol dialog box.
     */
    private void showPrivacyDialog() {
        // If a user does not agree to the service agreement, the service agreement dialog is displayed.
        if (getPreferences(AdsConstant.SP_PROTOCOL_KEY, AdsConstant.DEFAULT_SP_PROTOCOL_VALUE) == 0) {
            Log.i(TAG, "Show protocol dialog.");
            ProtocolDialog dialog = new ProtocolDialog(this);
            dialog.setCallback(new ProtocolDialog.ProtocolDialogCallback() {
                @Override
                public void agree() {
                    sendMessage(CONSENT_MSG_TYPE, MSG_DELAY_MS);
                }

                @Override
                public void cancel() {
                    // if the user selects the CANCEL button, exit application.
                    finish();
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            sendMessage(CONSENT_MSG_TYPE, MSG_DELAY_MS);
        }
    }

    /**
     * If a user has not set consent, the consent dialog box is displayed.
     */
    private void showConsentDialog(List<AdProvider> adProviders) {
        Log.i(TAG, "Show consent dialog.");
        ConsentDialog dialog = new ConsentDialog(this, adProviders);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private int getPreferences(String key, int defValue) {
        SharedPreferences preferences = getSharedPreferences(AdsConstant.SP_NAME, Context.MODE_PRIVATE);
        int value = preferences.getInt(key, defValue);
        Log.i(TAG, "Key:" + key + ", Preference value is: " + value);
        return value;
    }

    private void sendMessage(int what, int delayMillis) {
        Message msg = Message.obtain();
        msg.what = what;
        mHandler.sendMessageDelayed(msg, delayMillis);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ad_sample_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.privacy_settings:
                startActivity(new Intent(getApplicationContext(), ProtocolActivity.class));
                break;
            case R.id.consent_settings:
                startActivity(new Intent(getApplicationContext(), ConsentActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void checkConsentStatus() {
        final List<AdProvider> adProviderList = new ArrayList<>();
        Consent consentInfo = Consent.getInstance(this);
        consentInfo.addTestDeviceId("********");
        consentInfo.setDebugNeedConsent(DebugNeedConsent.DEBUG_NEED_CONSENT);
        consentInfo.requestConsentUpdate(new ConsentUpdateListener() {
            @Override
            public void onSuccess(ConsentStatus consentStatus, boolean isNeedConsent, List<AdProvider> adProviders) {
                Log.i(TAG, "ConsentStatus: " + consentStatus + ", isNeedConsent: " + isNeedConsent);
                if (isNeedConsent) {
                    if (adProviders != null && adProviders.size() > 0) {
                        adProviderList.addAll(adProviders);
                    }
                    showConsentDialog(adProviderList);
                }
            }

            @Override
            public void onFail(String errorDescription) {
                Log.e(TAG, "User's consent status failed to update: " + errorDescription);
                if (getPreferences(AdsConstant.SP_CONSENT_KEY, AdsConstant.DEFAULT_SP_CONSENT_VALUE) < 0) {
                    // In this example, if the request fails, the consent dialog box is still displayed. In this case, the ad publisher list is empty.
                    showConsentDialog(adProviderList);
                }
            }
        });
    }
}
