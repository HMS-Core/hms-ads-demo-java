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
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.MediaMuteListener;
import com.huawei.hms.ads.instreamad.InstreamAd;
import com.huawei.hms.ads.instreamad.InstreamAdLoadListener;
import com.huawei.hms.ads.instreamad.InstreamAdLoader;
import com.huawei.hms.ads.instreamad.InstreamMediaChangeListener;
import com.huawei.hms.ads.instreamad.InstreamMediaStateListener;
import com.huawei.hms.ads.instreamad.InstreamView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InstreamActivity extends BaseActivity {
    private static final String TAG = InstreamActivity.class.getSimpleName();

    private TextView videoContent;
    private TextView skipAd;
    private TextView countDown;
    private TextView callToAction;

    private Button loadButton;
    private Button registerButton;
    private Button muteButton;
    private Button pauseButton;

    private RelativeLayout instreamContainer;
    private InstreamView instreamView;
    private ImageView whyThisAd;

    private Context context;
    private int maxAdDuration;
    private String whyThisAdUrl;
    private boolean isMuted = false;

    private InstreamAdLoader adLoader;
    private List<InstreamAd> instreamAds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setTitle(R.string.instream_ad);
        setContentView(R.layout.activity_instream);

        initInstreamAdView();
        initButtons();

        configAdLoader();
    }

    private InstreamMediaChangeListener mediaChangeListener = new InstreamMediaChangeListener() {
        @Override
        public void onSegmentMediaChange(InstreamAd instreamAd) {
            whyThisAdUrl = null;
            whyThisAdUrl = instreamAd.getWhyThisAd();
            Log.i(TAG, "onSegmentMediaChange, whyThisAd: " + whyThisAdUrl);
            if (!TextUtils.isEmpty(whyThisAdUrl)) {
                whyThisAd.setVisibility(View.VISIBLE);
                whyThisAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(whyThisAdUrl)));
                    }
                });
            } else {
                whyThisAd.setVisibility(View.GONE);
            }

            String cta = instreamAd.getCallToAction();
            if (!TextUtils.isEmpty(cta)) {
                callToAction.setVisibility(View.VISIBLE);
                callToAction.setText(cta);
                instreamView.setCallToActionView(callToAction);
            }
        }
    };

    private InstreamMediaStateListener mediaStateListener = new InstreamMediaStateListener() {
        @Override
        public void onMediaProgress(int per, int playTime) {
            updateCountDown(playTime);
        }

        @Override
        public void onMediaStart(int playTime) {
            updateCountDown(playTime);
        }

        @Override
        public void onMediaPause(int playTime) {
            updateCountDown(playTime);
        }

        @Override
        public void onMediaStop(int playTime) {
            updateCountDown(playTime);
        }

        @Override
        public void onMediaCompletion(int playTime) {
            updateCountDown(playTime);
            playVideo();
        }

        @Override
        public void onMediaError(int playTime, int errorCode, int extra) {
            updateCountDown(playTime);
        }
    };

    private MediaMuteListener mediaMuteListener = new MediaMuteListener() {
        @Override
        public void onMute() {
            isMuted = true;
            Toast.makeText(context, "Ad muted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUnmute() {
            isMuted = false;
            Toast.makeText(context, "Ad unmuted", Toast.LENGTH_SHORT).show();
        }
    };

    private void initInstreamAdView() {
        instreamContainer = findViewById(R.id.instream_ad_container);
        instreamView = new InstreamView(getApplicationContext());
        instreamContainer.addView(instreamView, 0);
        videoContent = findViewById(R.id.instream_video_content);
        skipAd = findViewById(R.id.instream_skip);
        skipAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeInstream();
            }
        });

        countDown = findViewById(R.id.instream_count_down);
        callToAction = findViewById(R.id.instream_call_to_action);
        whyThisAd = findViewById(R.id.instream_why_this_ad);

        instreamView.setInstreamMediaChangeListener(mediaChangeListener);
        instreamView.setInstreamMediaStateListener(mediaStateListener);
        instreamView.setMediaMuteListener(mediaMuteListener);
        instreamView.setOnInstreamAdClickListener(new InstreamView.OnInstreamAdClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(context, "instream clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeInstream() {
        if (null != instreamView) {
            instreamView.onClose();
            instreamView.destroy();
            instreamContainer.removeView(instreamView);
            instreamContainer.setVisibility(View.GONE);
            instreamAds.clear();
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.instream_load:
                    if (instreamView.isPlaying()) {
                        Toast.makeText(context, getString(R.string.instream_ads_playing), Toast.LENGTH_SHORT).show();
                    } else if (null != adLoader) {
                        initInstreamAdView();
                        loadButton.setText(getString(R.string.instream_loading));
                        adLoader.loadAd(new AdParam.Builder().build());
                    }
                    break;
                case R.id.instream_register:
                    if (null == instreamAds || instreamAds.size() == 0) {
                        playVideo();
                    } else if (instreamView.isPlaying()) {
                        Toast.makeText(context, getString(R.string.instream_ads_playing), Toast.LENGTH_SHORT).show();
                    } else {
                        playInstreamAds(instreamAds);
                    }
                    break;
                case R.id.instream_mute:
                    if (isMuted) {
                        instreamView.unmute();
                        muteButton.setText(getString(R.string.instream_mute));
                    } else {
                        instreamView.mute();
                        muteButton.setText(getString(R.string.instream_unmute));
                    }
                    break;
                case R.id.instream_pause_play:
                    if (instreamView.isPlaying()) {
                        instreamView.pause();
                        pauseButton.setText(getString(R.string.instream_play));
                    } else {
                        instreamView.play();
                        pauseButton.setText(getString(R.string.instream_pause));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void initButtons() {
        loadButton = findViewById(R.id.instream_load);
        registerButton = findViewById(R.id.instream_register);
        muteButton = findViewById(R.id.instream_mute);
        pauseButton = findViewById(R.id.instream_pause_play);

        loadButton.setOnClickListener(clickListener);
        registerButton.setOnClickListener(clickListener);
        muteButton.setOnClickListener(clickListener);
        pauseButton.setOnClickListener(clickListener);
    }

    private InstreamAdLoadListener instreamAdLoadListener = new InstreamAdLoadListener() {
        @Override
        public void onAdLoaded(final List<InstreamAd> ads) {
            if (null == ads || ads.size() == 0) {
                playVideo();
                return;
            }
            Iterator<InstreamAd> it = ads.iterator();
            while (it.hasNext()) {
                InstreamAd ad = it.next();
                if (ad.isExpired()) {
                    it.remove();
                }
            }
            if (ads.size() == 0) {
                playVideo();
                return;
            }
            loadButton.setText(getString(R.string.instream_loaded));
            instreamAds = ads;
            Toast.makeText(context, "onAdLoaded, ad size: " + ads.size() + ", click REGISTER to play.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailed(int errorCode) {
            Log.w(TAG, "onAdFailed: " + errorCode);
            loadButton.setText(getString(R.string.instream_load));
            Toast.makeText(context, "onAdFailed: " + errorCode, Toast.LENGTH_SHORT).show();
            playVideo();
        }
    };

    private void configAdLoader() {
        /**
         * if the maximum total duration is 60 seconds and the maximum number of roll ads is eight,
         * at most four 15-second roll ads or two 30-second roll ads will be returned.
         * If the maximum total duration is 120 seconds and the maximum number of roll ads is four,
         * no more roll ads will be returned after whichever is reached.
         */
        int totalDuration = 60;
        int maxCount = 4;
        InstreamAdLoader.Builder builder = new InstreamAdLoader.Builder(context, getString(R.string.instream_ad_id));
        adLoader = builder.setTotalDuration(totalDuration)
                .setMaxCount(maxCount)
                .setInstreamAdLoadListener(instreamAdLoadListener)
                .build();
    }

    // play your normal video content.
    private void playVideo() {
        hideAdViews();
        videoContent.setText(R.string.instream_normal_video_playing);
    }

    private void hideAdViews() {
        instreamContainer.setVisibility(View.GONE);
    }

    private void playInstreamAds(List<InstreamAd> ads) {
        maxAdDuration = getMaxInstreamDuration(ads);
        instreamContainer.setVisibility(View.VISIBLE);
        loadButton.setText(getString(R.string.instream_load));

        instreamView.setInstreamAds(ads);
    }

    private void updateCountDown(long playTime) {
        final String time = String.valueOf(Math.round((maxAdDuration - playTime) / 1000));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countDown.setText(time + "s");
            }
        });
    }

    private int getMaxInstreamDuration(List<InstreamAd> ads) {
        int duration = 0;
        for (InstreamAd ad : ads) {
            duration += ad.getDuration();
        }
        return duration;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != instreamView && instreamView.isPlaying()) {
            instreamView.pause();
            pauseButton.setText(getText(R.string.instream_play));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != instreamView && !instreamView.isPlaying()) {
            instreamView.play();
            pauseButton.setText(getText(R.string.instream_pause));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != instreamView) {
            instreamView.removeInstreamMediaStateListener();
            instreamView.removeInstreamMediaChangeListener();
            instreamView.removeMediaMuteListener();
            instreamView.destroy();
        }
    }
}
