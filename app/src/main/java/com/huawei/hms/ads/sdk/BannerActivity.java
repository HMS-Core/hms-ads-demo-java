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

import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;

/**
 * Activity for displaying a banner ad.
 */
public class BannerActivity extends BaseActivity {
    private static final int REFRESH_TIME = 30;

    private BannerView bannerView;

    private BannerView defaultBannerView;

    private FrameLayout adFrameLayout;

    private RadioGroup sizeRadioGroup;

    private RadioGroup colorRadioGroup;

    /**
     * Button tapping event listener.
     */
    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            defaultBannerView.setVisibility(View.INVISIBLE);
            if (bannerView != null) {
                adFrameLayout.removeView(bannerView);
                bannerView.destroy();
            }

            // Call new BannerView(Context context) to create a BannerView class.
            bannerView = new BannerView(v.getContext());

            // Set an ad slot ID.
            bannerView.setAdId(getString(R.string.banner_ad_id));

            // Set the background color and size based on user selection.
            BannerAdSize adSize = getBannerAdSize(sizeRadioGroup.getCheckedRadioButtonId());
            bannerView.setBannerAdSize(adSize);

            int color = getBannerViewBackground(colorRadioGroup.getCheckedRadioButtonId());
            bannerView.setBackgroundColor(color);

            adFrameLayout.addView(bannerView);
            bannerView.setAdListener(adListener);
            bannerView.loadAd(new AdParam.Builder().build());
        }
    };

    /**
     * Ad listener.
     */
    private AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Called when an ad is loaded successfully.
            showToast("Ad loaded.");
        }

        @Override
        public void onAdFailed(int errorCode) {
            // Called when an ad fails to be loaded.
            showToast(String.format(Locale.ROOT, "Ad failed to load with error code %d.", errorCode));
        }

        @Override
        public void onAdOpened() {
            // Called when an ad is opened.
            showToast(String.format("Ad opened "));
        }

        @Override
        public void onAdClicked() {
            // Called when a user taps an ad.
            showToast("Ad clicked");
        }

        @Override
        public void onAdLeave() {
            // Called when a user has left the app.
            showToast("Ad Leave");
        }

        @Override
        public void onAdClosed() {
            // Called when an ad is closed.
            showToast("Ad closed");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.banner_ad));
        setContentView(R.layout.activity_banner);

        TextView bannerTitle = findViewById(R.id.text_banner);
        bannerTitle.setText("This is banner ads sample.");

        sizeRadioGroup = findViewById(R.id.size_radioGroup);
        colorRadioGroup = findViewById(R.id.color_radioGroup);

        // Load the default banner ad.
        loadDefaultBannerAd();

        // Set the button for loading an ad.
        Button loadButton = findViewById(R.id.refreshButton);
        loadButton.setOnClickListener(buttonListener);

        adFrameLayout = findViewById(R.id.ad_frame);
    }

    /**
     * Load the default banner ad.
     */
    private void loadDefaultBannerAd() {
        // Obtain BannerView based on the configuration in layout/activity_main.xml.
        defaultBannerView = findViewById(R.id.hw_banner_view);
        defaultBannerView.setAdListener(adListener);
        defaultBannerView.setBannerRefresh(REFRESH_TIME);

        AdParam adParam = new AdParam.Builder().build();
        defaultBannerView.loadAd(adParam);
    }

    private void showToast(String message) {
        Toast.makeText(BannerActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private BannerAdSize getBannerAdSize(int checkedId) {
        BannerAdSize adSize = null;
        switch (checkedId) {
            case R.id.size_320_50:
                adSize = BannerAdSize.BANNER_SIZE_320_50;
                break;
            case R.id.size_320_100:
                adSize = BannerAdSize.BANNER_SIZE_320_100;
                break;
            case R.id.size_300_250:
                adSize = BannerAdSize.BANNER_SIZE_300_250;
                break;
            case R.id.size_smart:
                adSize = BannerAdSize.BANNER_SIZE_SMART;
                break;
            case R.id.size_360_57:
                adSize = BannerAdSize.BANNER_SIZE_360_57;
                break;
            case R.id.size_360_144:
                adSize = BannerAdSize.BANNER_SIZE_360_144;
                break;
        }
        return adSize;
    }

    private int getBannerViewBackground(int checkedId) {
        int color = Color.TRANSPARENT;
        switch (checkedId) {
            case R.id.color_white:
                color = Color.WHITE;
                break;
            case R.id.color_black:
                color = Color.BLACK;
                break;
            case R.id.color_red:
                color = Color.RED;
                break;
            case R.id.color_transparent:
                color = Color.TRANSPARENT;
                break;
        }
        return color;
    }
}