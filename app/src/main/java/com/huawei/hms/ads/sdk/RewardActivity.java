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

import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.reward.Reward;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;

/**
 * Activity for displaying a rewarded ad.
 */
public class RewardActivity extends BaseActivity {
    private static final int PLUS_SCORE = 1;

    private static final int MINUS_SCORE = 5;

    private static final int RANGE = 2;

    private TextView rewardedTitle;

    private TextView scoreView;

    private Button reStartButton;

    private Button watchAdButton;

    private RewardAd rewardedAd;

    private int score = 1;

    private final int defaultScore = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.reward_ad));
        setContentView(R.layout.activity_reward);

        rewardedTitle = findViewById(R.id.text_reward);
        rewardedTitle.setText(R.string.reward_ad_title);

        // Load a rewarded ad.
        loadRewardAd();

        // Load a score view.
        loadScoreView();

        // Load the button for watching a rewarded ad.
        loadWatchButton();

        // Load the button for starting a game.
        loadPlayButton();
    }

    /**
     * Load a rewarded ad.
     */
    private void loadRewardAd() {
        if (rewardedAd == null) {
            rewardedAd = new RewardAd(RewardActivity.this, getString(R.string.ad_id_reward));
        }

        RewardAdLoadListener rewardAdLoadListener = new RewardAdLoadListener() {
            @Override
            public void onRewardAdFailedToLoad(int errorCode) {
                Toast
                    .makeText(RewardActivity.this, "onRewardAdFailedToLoad " + "errorCode is :" + errorCode,
                        Toast.LENGTH_SHORT)
                    .show();
            }

            @Override
            public void onRewardedLoaded() {
                Toast.makeText(RewardActivity.this, "onRewardedLoaded", Toast.LENGTH_SHORT).show();
            }
        };

        rewardedAd.loadAd(new AdParam.Builder().build(), rewardAdLoadListener);
    }

    /**
     * Display a rewarded ad.
     */
    private void rewardAdShow() {
        if (rewardedAd.isLoaded()) {
            rewardedAd.show(RewardActivity.this, new RewardAdStatusListener() {
                @Override
                public void onRewardAdClosed() {
                    loadRewardAd();
                }

                @Override
                public void onRewardAdFailedToShow(int errorCode) {
                    Toast
                        .makeText(RewardActivity.this, "onRewardAdFailedToShow " + "errorCode is :" + errorCode,
                            Toast.LENGTH_SHORT)
                        .show();
                }

                @Override
                public void onRewardAdOpened() {
                    Toast.makeText(RewardActivity.this, "onRewardAdOpened", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRewarded(Reward reward) {
                    // You are advised to grant a reward immediately and at the same time, check whether the reward
                    // takes effect on the server. If no reward information is configured, grant a reward based on the
                    // actual scenario.
                    int addScore = reward.getAmount() == 0 ? defaultScore : reward.getAmount();
                    Toast
                        .makeText(RewardActivity.this, "Watch video show finished , add " + addScore + " scores",
                            Toast.LENGTH_SHORT)
                        .show();
                    score += addScore;
                    setScore(score);
                    loadRewardAd();
                }
            });
        }
    }

    /**
     * Set a score.
     *
     * @param score
     */
    private void setScore(int score) {
        scoreView.setText("Score:" + score);
    }

    /**
     * Load the button for watching a rewarded ad.
     */
    private void loadWatchButton() {
        watchAdButton = findViewById(R.id.show_video_button);
        watchAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardAdShow();
            }
        });
    }

    /**
     * Load the button for starting a game.
     */
    private void loadPlayButton() {
        reStartButton = findViewById(R.id.play_button);
        reStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
    }

    private void loadScoreView() {
        scoreView = findViewById(R.id.score_count_text);
        scoreView.setText("Score:" + score);
    }

    /**
     * Used to play a game.
     */
    private void play() {
        // If the score is 0, a message is displayed, asking users to watch the ad in exchange for scores.
        if (score == 0) {
            Toast.makeText(RewardActivity.this, "Watch video ad to add score", Toast.LENGTH_SHORT).show();
            return;
        }

        // The value 0 or 1 is returned randomly. If the value is 1, the score increases by 1. If the value is 0, the
        // score decreases by 5. If the score is a negative number, the score is set to 0.
        int random = new Random().nextInt(RANGE);
        if (random == 1) {
            score += PLUS_SCORE;
            Toast.makeText(RewardActivity.this, "You win！", Toast.LENGTH_SHORT).show();
        } else {
            score -= MINUS_SCORE;
            score = score < 0 ? 0 : score;
            Toast.makeText(RewardActivity.this, "You lose！", Toast.LENGTH_SHORT).show();
        }
        setScore(score);
    }
}
