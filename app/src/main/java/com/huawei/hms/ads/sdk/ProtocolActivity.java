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

import com.huawei.hms.ads.sdk.dialogs.ProtocolDialog;

/**
 * Activity for displaying privacy information.
 */
public class ProtocolActivity extends BaseActivity implements ProtocolDialog.ProtocolDialogCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.privacy_settings));

        showProtocolDialog();
    }

    /**
     * Display a protocol dialog.
     */
    private void showProtocolDialog() {
        // Start to process the protocol dialog box.
        ProtocolDialog dialog = new ProtocolDialog(this);
        dialog.setCallback(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void agree() {
        finish();
    }

    @Override
    public void cancel() {
        finish();
    }
}
