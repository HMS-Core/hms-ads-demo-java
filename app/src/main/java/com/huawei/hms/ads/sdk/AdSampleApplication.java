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

import android.app.Application;

import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.RequestOptions;
import com.huawei.hms.ads.UnderAge;
import com.huawei.hms.ads.consent.constant.ConsentStatus;

public class AdSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        updateRequest();
        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);
    }

    private void updateRequest() {
        RequestOptions requestOptions;
        // Obtain global ad singleton variables and add personalized ad request parameters.
        if (HwAds.getRequestOptions() == null) {
            requestOptions = new RequestOptions();
        } else {
            requestOptions = HwAds.getRequestOptions();
        }

        // For non-personalized ads, reset this parameter.
        requestOptions = requestOptions.toBuilder()
                .setNonPersonalizedAd(ConsentStatus.NON_PERSONALIZED.getValue())
                .setTagForUnderAgeOfPromise(UnderAge.PROMISE_TRUE)
                .setRequestLocation(false)
                .build();
        HwAds.setRequestOptions(requestOptions);
    }
}
