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

package com.huawei.hms.ads.sdk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.huawei.hms.ads.consent.bean.AdProvider;
import com.huawei.hms.ads.consent.constant.ConsentStatus;
import com.huawei.hms.ads.consent.inter.Consent;
import com.huawei.hms.ads.sdk.AdsConstant;
import com.huawei.hms.ads.sdk.R;

import java.util.List;

/**
 * Control on consent-related dialog boxes.
 */
public class ConsentDialog extends Dialog {
    private Context mContext;

    private LayoutInflater inflater;

    private LinearLayout contentLayout;

    private TextView titleTv;

    private TextView initInfoTv;

    private TextView moreInfoTv;

    private TextView partnersListTv;

    private View consentDialogView;

    private View initView;

    private View moreInfoView;

    private View partnersListView;

    private Button consentYesBtn;

    private Button consentNoBtn;

    private Button moreInfoBackBtn;

    private Button partnerListBackBtn;

    private List<AdProvider> madProviders;

    private ConsentDialogCallback mCallback;

    /**
     * Consent dialog box callback interface.
     */
    public interface ConsentDialogCallback {
        /**
         * Update a user selection result.
         *
         * @param consentStatus ConsentStatus
         */
        void updateConsentStatus(ConsentStatus consentStatus);
    }

    /**
     * Constructor.
     *
     * @param context context
     * @param adProviders adProviders
     */
    public ConsentDialog(@NonNull Context context, List<AdProvider> adProviders) {
        // Customize a dialog box style.
        super(context, R.style.dialog);
        mContext = context;
        madProviders = adProviders;
    }

    /**
     * Set a dialog box callback.
     *
     * @param callback callback
     */
    public void setCallback(ConsentDialog.ConsentDialogCallback callback) {
        mCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window dialogWindow = getWindow();
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);

        inflater = LayoutInflater.from(mContext);
        consentDialogView = inflater.inflate(R.layout.dialog_consent, null);
        setContentView(consentDialogView);

        titleTv = findViewById(R.id.consent_dialog_title_text);
        titleTv.setText(mContext.getString(R.string.consent_title));

        initView = inflater.inflate(R.layout.dialog_consent_content, null);
        moreInfoView = inflater.inflate(R.layout.dialog_consent_moreinfo, null);
        partnersListView = inflater.inflate(R.layout.dialog_consent_partner_list, null);

        // Add content to the initialization dialog box.
        showInitConsentInfo();
    }

    /**
     * Update the consent status.
     *
     * @param consentStatus ConsentStatus
     */
    private void updateConsentStatus(ConsentStatus consentStatus) {
        // Update the consent status.
        Consent.getInstance(mContext).setConsentStatus(consentStatus);

        // Save a user selection to the local SP.
        SharedPreferences preferences = mContext.getSharedPreferences(AdsConstant.SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(AdsConstant.SP_CONSENT_KEY, consentStatus.getValue()).commit();

        // Callback activity
        if (mCallback != null) {
            mCallback.updateConsentStatus(consentStatus);
        }
    }

    /**
     * Display initial consent content.
     */
    private void showInitConsentInfo() {
        addContentView(initView);

        // Add a button and text link and their tapping events to the initialization page.
        addInitButtonAndLinkClick(consentDialogView);
    }

    /**
     * Add a button and a text link and their tapping events to the initial page.
     *
     * @param rootView rootView
     */
    private void addInitButtonAndLinkClick(View rootView) {
        consentYesBtn = rootView.findViewById(R.id.btn_consent_init_yes);
        consentYesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                updateConsentStatus(ConsentStatus.PERSONALIZED);
            }
        });

        consentNoBtn = rootView.findViewById(R.id.btn_consent_init_skip);
        consentNoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                updateConsentStatus(ConsentStatus.NON_PERSONALIZED);
            }
        });

        initInfoTv = rootView.findViewById(R.id.consent_center_init_content);
        initInfoTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        String initText = mContext.getString(R.string.consent_init_text);
        final SpannableStringBuilder spanInitText = new SpannableStringBuilder(initText);

        // Set the listener on the event for tapping some text.
        ClickableSpan initTouchHere = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showTouchHereInfo();
            }
        };

        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        int initTouchHereStart = mContext.getResources().getInteger(R.integer.init_here_start);
        int initTouchHereEnd = mContext.getResources().getInteger(R.integer.init_here_end);
        spanInitText.setSpan(initTouchHere, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanInitText.setSpan(colorSpan, initTouchHereStart, initTouchHereEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        initInfoTv.setText(spanInitText);
        initInfoTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Display the content on the more information page.
     */
    private void showTouchHereInfo() {
        addContentView(moreInfoView);

        // Set the listener on the tapping event on the more information page.
        addMoreInfoButtonAndLinkClick(consentDialogView);
    }

    /**
     * Add a button and a text link and their tapping events to the more information page.
     *
     * @param rootView rootView
     */
    private void addMoreInfoButtonAndLinkClick(View rootView) {
        moreInfoBackBtn = rootView.findViewById(R.id.btn_consent_more_info_back);
        moreInfoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInitConsentInfo();
            }
        });
        moreInfoTv = rootView.findViewById(R.id.consent_center_more_info_content);
        moreInfoTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        String moreInfoText = mContext.getString(R.string.consent_more_info_text);
        final SpannableStringBuilder spanMoreInfoText = new SpannableStringBuilder(moreInfoText);

        // Set the listener on the event for tapping some text.
        ClickableSpan moreInfoTouchHere = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                showPartnersListInfo();
            }
        };
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0000FF"));
        int moreInfoTouchHereStart = mContext.getResources().getInteger(R.integer.more_info_here_start);
        int moreInfoTouchHereEnd = mContext.getResources().getInteger(R.integer.more_info_here_end);
        spanMoreInfoText.setSpan(moreInfoTouchHere, moreInfoTouchHereStart, moreInfoTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanMoreInfoText.setSpan(colorSpan, moreInfoTouchHereStart, moreInfoTouchHereEnd,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        moreInfoTv.setText(spanMoreInfoText);
        moreInfoTv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Display the partner list page.
     */
    private void showPartnersListInfo() {
        partnersListTv = partnersListView.findViewById(R.id.partners_list_content);
        partnersListTv.setMovementMethod(ScrollingMovementMethod.getInstance());
        partnersListTv.setText("");
        List<AdProvider> learnAdProviders = madProviders;
        if (learnAdProviders != null) {
            for (AdProvider learnAdProvider : learnAdProviders) {
                String link = "<font color='#0000FF'><a href=" + learnAdProvider.getPrivacyPolicyUrl() + ">"
                    + learnAdProvider.getName() + "</a>";
                partnersListTv.append(Html.fromHtml(link));
                partnersListTv.append("  ");
            }
        } else {
            partnersListTv.append(" 3rd party’s full list of advertisers is empty");
        }
        partnersListTv.setMovementMethod(LinkMovementMethod.getInstance());
        addContentView(partnersListView);

        // Set the listener on the tapping event on the partner list page.
        addPartnersListButtonAndLinkClick(consentDialogView);
    }

    /**
     * Add a button and a text link and their tapping events to the partner list page.
     *
     * @param rootView rootView
     */
    private void addPartnersListButtonAndLinkClick(View rootView) {
        partnerListBackBtn = rootView.findViewById(R.id.btn_partners_list_back);
        partnerListBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTouchHereInfo();
            }
        });
    }

    /**
     * Add layout content in the dialog box that is displayed.
     */
    private void addContentView(View view) {
        contentLayout = findViewById(R.id.consent_center_layout);
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }
}
