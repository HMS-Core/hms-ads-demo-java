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

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.VideoConfiguration;
import com.huawei.hms.ads.nativead.DislikeAdListener;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdConfiguration;
import com.huawei.hms.ads.nativead.NativeAdLoader;

public class NativeActivity extends BaseActivity {
    private static final String TAG = NativeActivity.class.getSimpleName();

    private RadioButton bigImage;
    private RadioButton threeSmall;
    private RadioButton smallImage;
    private RadioButton videoWithText;
    private RadioButton appDownloadBtn;

    private Button loadBtn;
    private ScrollView adScrollView;

    private NativeAd globalNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.native_ad));
        setContentView(R.layout.activity_native);

        bigImage = findViewById(R.id.radio_button_large);
        threeSmall = findViewById(R.id.radio_button_three_small);
        smallImage = findViewById(R.id.radio_button_small);
        videoWithText = findViewById(R.id.radio_button_video);
        appDownloadBtn = findViewById(R.id.radio_button_app_download_button);

        loadBtn = findViewById(R.id.btn_load);
        adScrollView = findViewById(R.id.scroll_view_ad);

        loadBtn.setOnClickListener(view -> loadAd(getAdId()));

        loadAd(getAdId());
    }

    /**
     * Initialize ad slot ID and layout template.
     *
     * @return ad slot ID
     */
    private String getAdId() {
        String adId = getString(R.string.ad_id_native);
        ;
        if (bigImage.isChecked()) {
            adId = getString(R.string.ad_id_native);
        } else if (smallImage.isChecked()) {
            adId = getString(R.string.ad_id_native_small);
        } else if (threeSmall.isChecked()) {
            adId = getString(R.string.ad_id_native_three);
        } else if (videoWithText.isChecked()) {
            adId = getString(R.string.ad_id_native_video);
        } else if (appDownloadBtn.isChecked()) {
            adId = getString(R.string.ad_id_native_video);
        }
        return adId;
    }

    /**
     * Load a native ad.
     *
     * @param adId ad slot ID.
     */
    private void loadAd(String adId) {
        updateStatus(null, false);

        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(this, adId);
        builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                // Call this method when an ad is successfully loaded.
                updateStatus(getString(R.string.status_load_ad_success), true);

                // Display native ad.
                showNativeAd(nativeAd);
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                updateStatus(getString(R.string.status_load_ad_finish), true);
            }

            @Override
            public void onAdFailed(int errorCode) {
                // Call this method when an ad fails to be loaded.
                updateStatus(getString(R.string.status_load_ad_fail) + errorCode, true);
            }
        });

        VideoConfiguration videoConfiguration = new VideoConfiguration.Builder()
                .setStartMuted(true)
                .build();

        NativeAdConfiguration adConfiguration = new NativeAdConfiguration.Builder()
                .setChoicesPosition(NativeAdConfiguration.ChoicesPosition.BOTTOM_RIGHT) // Set custom attributes.
                .setVideoConfiguration(videoConfiguration)
                .setRequestMultiImages(true)
                .build();

        NativeAdLoader nativeAdLoader = builder.setNativeAdOptions(adConfiguration).build();
        nativeAdLoader.loadAd(new AdParam.Builder().build());

        updateStatus(getString(R.string.status_ad_loading), false);
    }

    /**
     * Display native ad.
     *
     * @param nativeAd native ad object that contains ad materials.
     */
    private void showNativeAd(NativeAd nativeAd) {
        // Destroy the original native ad.
        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }
        globalNativeAd = nativeAd;

        final View nativeView = createNativeView(nativeAd, adScrollView);
        if (nativeView != null) {
            globalNativeAd.setDislikeAdListener(new DislikeAdListener() {
                @Override
                public void onAdDisliked() {
                    // Call this method when an ad is closed.
                    updateStatus(getString(R.string.ad_is_closed), true);
                    adScrollView.removeView(nativeView);
                }
            });

            // Add NativeView to the app UI.
            adScrollView.removeAllViews();
            adScrollView.addView(nativeView);
        }
    }

    /**
     * Create a nativeView by creativeType and fill in ad material.
     *
     * @param nativeAd   native ad object that contains ad materials.
     * @param parentView parent view of nativeView.
     */
    private View createNativeView(NativeAd nativeAd, ViewGroup parentView) {
        int createType = nativeAd.getCreativeType();
        Log.i(TAG, "Native ad createType is " + createType);
        if (createType == 2 || createType == 102) {
            // Large image
            return NativeViewFactory.createImageOnlyAdView(nativeAd, parentView);
        } else if (createType == 3 || createType == 6) {
            // Large image with text or video with text
            return NativeViewFactory.createMediumAdView(nativeAd, parentView);
        } else if (createType == 103 || createType == 106) {
            // Large image with text or Video with text, using AppDownloadButton template.
            return NativeViewFactory.createAppDownloadButtonAdView(nativeAd, parentView);
        } else if (createType == 7 || createType == 107) {
            // Small image with text-
            return NativeViewFactory.createSmallImageAdView(nativeAd, parentView);
        } else if (createType == 8 || createType == 108) {
            // Three small images with text
            return NativeViewFactory.createThreeImagesAdView(nativeAd, parentView);
        } else {
            // Undefined creative type
            return null;
        }
    }

    /**
     * Update tip and status of the load button.
     *
     * @param text           tip.
     * @param loadBtnEnabled status of the load button.
     */
    private void updateStatus(String text, boolean loadBtnEnabled) {
        if (null != text) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
        loadBtn.setEnabled(loadBtnEnabled);
    }

    @Override
    protected void onDestroy() {
        if (null != globalNativeAd) {
            globalNativeAd.destroy();
        }

        super.onDestroy();
    }
}
