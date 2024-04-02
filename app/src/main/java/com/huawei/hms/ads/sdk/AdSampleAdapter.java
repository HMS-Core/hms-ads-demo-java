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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class AdSampleAdapter extends ArrayAdapter<AdFormat> {
    private final Context mContext;

    private final int resourceId;

    public AdSampleAdapter(Context context, int resource, List<AdFormat> formatList) {
        super(context, resource, formatList);
        mContext = context;
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
        TextView title = view.findViewById(android.R.id.text1);
        AdFormat adFormat = getItem(position);
        if (adFormat != null) {
            title.setText(adFormat.title());
        }
        return view;
    }
}
