/*
 * Copyright 2024. Huawei Technologies Co., Ltd. All rights reserved.

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

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.BiddingInfo;
import com.huawei.hms.ads.BiddingParam;
import com.huawei.hms.ads.splash.SplashAd;
import com.huawei.hms.ads.splash.SplashView;
import com.huawei.hms.ads.splash.listener.SplashListener;
import com.huawei.hms.ads.splash.listener.SplashLoadListener;
import com.huawei.openalliance.ad.inter.HiAd;
import com.huawei.openalliance.ad.inter.IHiAd;

public class BiddingSplashActivity extends BaseActivity {
    private static final String TAG = BiddingSplashActivity.class.getSimpleName();

    private RelativeLayout splashContainer;

    private SplashAd splashAd;

    // 返回键标志位 按返回键退出时应用不被重新拉起，但是在展示广告时下拉通知栏仍然需要正常跳转
    private boolean hasPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set translucent navigation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                    getWindow().setAttributes(lp);
                }
            }
        } catch (Throwable e) {
            Log.w(TAG, "set CutoutMode error:" + e.getClass().getSimpleName());
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidding_splash);
        splashContainer = findViewById(R.id.hw_bidding_splash_container);
        splashAd = new SplashAd(this);
        configSplashParam();
        // 加载开屏广告
        loadSplashAd();
    }


    private void loadSplashAd() {
        Log.i(TAG, "loadSplashAd");
        splashAd.loadAd(new SplashLoadListener() {
            @Override
            public void onAdLoaded() {
                Log.i(TAG, "onAdLoaded");
                if (splashAd.isLoaded()) {
                    BiddingInfo biddingInfo = splashAd.getBiddingInfo();
                    if (biddingInfo != null) {
                        Log.d(TAG, "AdxServer Return BiddingInfo, price is " + biddingInfo.getPrice() +
                                ", cur is " + biddingInfo.getCur() +
                                ", nurl is " + biddingInfo.getNurl() +
                                ", lur is " + biddingInfo.getLurl());
                    } else {
                        Log.d(TAG, "AdxServer Return BiddingInfo is null. ");
                    }
                    // 展示开屏广告
                    showSplashAd();
                }
                showMsg("onAdLoaded");
            }

            @Override
            public void onAdFailed(int errorCode) {
                Log.i(TAG, "onAdFailed, errorCode: " + errorCode);
            }
        });
    }

    private void showSplashAd() {
        Log.i(TAG, "showSplashAd");
        SplashView splashView = getSplashView();
        splashContainer.removeAllViews();
        RelativeLayout.LayoutParams splashParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        splashContainer.addView(splashView, splashParam);
        splashContainer.bringToFront();
        if (splashAd.isLoaded()) {
            Log.i(TAG, "Ad is Loaded");
            splashAd.showAd(true);
        } else {
            Log.i(TAG, "Ad is Empty");
        }
    }

    private SplashView getSplashView() {
        SplashView splashView = splashAd.getSplashView();
        BiddingSplashActivity BiddingSplashActivity = new BiddingSplashActivity();
        splashView.setAdDisplayListener(new SplashAdDisplayListener(){
            private WeakReference<BiddingSplashActivity> activityRef = new WeakReference<>(BiddingSplashActivity);
            @Override
            public void onAdClick() {
                BiddingSplashActivity splashActivity = activityRef.get();
                if (null == splashActivity) {
                    return;
                }
                super.onAdClick();
                Log.d(TAG, "onAdClick");
                Toast.makeText(splashActivity.getApplicationContext(), "onAdClick", Toast.LENGTH_LONG).show();
            }
        });
        return splashView;
    }

    @Override
    // 从其他页面回到开屏页面时调用，进入应用
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
        hasPaused = false;
        jump();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        hasPaused = true;
        super.onStop();
    }

    private void configSplashParam() {
        IHiAd hiad = HiAd.getInstance(getApplicationContext());
        hiad.initLog(true, Log.DEBUG);
        if (splashAd == null) {
            splashAd = new SplashAd(this);
        }
        // Lock the screen orientation on the device. Your app will automatically adapt to the screen orientation.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        int orientation = getScreenOrientation();
        String slotId;
        // Set the default slogan and the splash ad unit ID based on the screen orientation on the device.
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            splashAd.setSloganResId(R.drawable.default_slogan);
            slotId = getString(R.string.ad_id_splash);
        } else {
            splashAd.setSloganResId(R.drawable.default_slogan_landscape);
            slotId = getString(R.string.ad_id_splash_landscape);
        }
        AdParam.Builder adParam = new AdParam.Builder();
        // 设置广告位参与实时bidding参数
        BiddingParam biddingParam = new BiddingParam();
        adParam.addBiddingParamMap(slotId, biddingParam);
        adParam.setTMax(500);

        // Set app icon.
        splashAd.setLogoResId(R.mipmap.ic_launcher);
        // Set app name.
        splashAd.setMediaNameResId(R.string.media_name);
        // Set the audio focus type for a video splash ad.
        splashAd.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        splashAd.setAdParam(slotId, orientation, adParam.build());
        splashAd.setSplashListener(new SplashListener() {
            @Override
            public void onAdShowStart() {
                Log.i(TAG, "onAdShowStart");
            }

            @Override
            public void onAdError(int errorCode) {
                String errorMsg = "onAdError, errorCode: " + errorCode;
                Log.i(TAG, errorMsg);
                showMsg(errorMsg);
            }

            @Override
            public void onAdDismissed() {
                showMsg("onAdDismissed");
                Log.i(TAG, "onAdDismissed");
                jump();
            }
        });
    }

    private int getScreenOrientation() {
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    private void jump() {
        Log.d(TAG, "jump hasPaused: " + hasPaused);
        if(!hasPaused) {
            hasPaused = true;
            Log.d(TAG, "jump into application");
            startActivity(new Intent(BiddingSplashActivity.this, MainActivity.class));
            finish();
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
