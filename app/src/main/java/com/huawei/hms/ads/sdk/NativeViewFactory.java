package com.huawei.hms.ads.sdk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.huawei.hms.ads.AppDownloadButton;
import com.huawei.hms.ads.AppDownloadButtonStyle;
import com.huawei.hms.ads.VideoOperator;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeView;

public class NativeViewFactory {
    private static final String TAG = NativeViewFactory.class.getSimpleName();

    public static View createMediumAdView(NativeAd nativeAd, final ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View adRootView = inflater.inflate(R.layout.native_common_medium_template, null);

        final NativeView nativeView = adRootView.findViewById(R.id.native_medium_view);
        nativeView.setTitleView(adRootView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) adRootView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(adRootView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(adRootView.findViewById(R.id.ad_call_to_action));

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }

        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        // Obtain a video controller.
        VideoOperator videoOperator = nativeAd.getVideoOperator();

        // Check whether a native ad contains video materials.
        if (videoOperator.hasVideo()) {
            // Add a video lifecycle event listener.
            videoOperator.setVideoLifecycleListener(new VideoOperator.VideoLifecycleListener() {
                @Override
                public void onVideoStart() {
                    Log.i(TAG, "NativeAd video play start.");
                }

                @Override
                public void onVideoPlay() {
                    Log.i(TAG, "NativeAd video playing.");
                }

                @Override
                public void onVideoEnd() {
                    Log.i(TAG, "NativeAd video play end.");
                }
            });
        }

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);

        return nativeView;
    }

    public static View createSmallImageAdView(NativeAd nativeAd, final ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View adRootView = inflater.inflate(R.layout.native_small_image_template, null);

        final NativeView nativeView = adRootView.findViewById(R.id.native_small_view);
        nativeView.setTitleView(adRootView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) adRootView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(adRootView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(adRootView.findViewById(R.id.ad_call_to_action));

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }

        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        LinearLayout appDetailLayout = adRootView.findViewById(R.id.ad_app_detail_layout);
        appDetailLayout.setVisibility(nativeAd.getInteractionType() == 2 ? View.VISIBLE : View.GONE);
        if (nativeAd.getInteractionType() == 2) {
            setSixElements(parentView.getContext(), adRootView, nativeAd);
        }

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);

        return nativeView;
    }

    public static View createThreeImagesAdView(NativeAd nativeAd, final ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View adRootView = inflater.inflate(R.layout.native_three_images_template, null);

        final NativeView nativeView = adRootView.findViewById(R.id.native_three_images);
        nativeView.setTitleView(adRootView.findViewById(R.id.ad_title));
        nativeView.setAdSourceView(adRootView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(adRootView.findViewById(R.id.ad_call_to_action));

        ImageView imageView1 = adRootView.findViewById(R.id.image_view_1);
        ImageView imageView2 = adRootView.findViewById(R.id.image_view_2);
        ImageView imageView3 = adRootView.findViewById(R.id.image_view_3);

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }

        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        if (nativeAd.getImages() != null && nativeAd.getImages().size() >= 3) {
            imageView1.setImageDrawable(nativeAd.getImages().get(0).getDrawable());
            imageView2.setImageDrawable(nativeAd.getImages().get(1).getDrawable());
            imageView3.setImageDrawable(nativeAd.getImages().get(2).getDrawable());
        }

        LinearLayout appDetailLayout = adRootView.findViewById(R.id.ad_app_detail_layout);
        appDetailLayout.setVisibility(nativeAd.getInteractionType() == 2 ? View.VISIBLE : View.GONE);
        if (nativeAd.getInteractionType() == 2) {
            setSixElements(parentView.getContext(), adRootView, nativeAd);
        }

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);

        return nativeView;
    }

    private static void setSixElements(final Context context, View adRootView, final NativeAd nativeAd) {
        TextView developerView = adRootView.findViewById(R.id.ad_app_developer);
        TextView appVersionView = adRootView.findViewById(R.id.ad_app_version);
        developerView.setText(nativeAd.getAppInfo().getDeveloperName());
        appVersionView.setText(context.getResources().getString(R.string.app_version) + ":" + nativeAd.getAppInfo().getVersionName());

        TextView privacyView = adRootView.findViewById(R.id.ad_privacy);
        TextView permissionView = adRootView.findViewById(R.id.ad_permission);
        TextView detailView = adRootView.findViewById(R.id.ad_detail);
        permissionView.setOnClickListener(view -> nativeAd.getAppInfo().showPermissionPage(context));
        privacyView.setOnClickListener(view -> nativeAd.getAppInfo().showPrivacyPolicy(context));
        detailView.setOnClickListener(view -> nativeAd.showAppDetailPage(context));
    }

    @NonNull
    public static View createAppDownloadButtonAdView(NativeAd nativeAd, final ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View adRootView = inflater.inflate(R.layout.native_ad_with_app_download_btn_template, null);

        final NativeView nativeView = adRootView.findViewById(R.id.native_app_download_button_view);


        nativeView.setTitleView(adRootView.findViewById(R.id.ad_title));
        nativeView.setMediaView((MediaView) adRootView.findViewById(R.id.ad_media));
        nativeView.setAdSourceView(adRootView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(adRootView.findViewById(R.id.ad_call_to_action));

        // Populate a native ad material view.
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }

        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);

        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        LinearLayout appDetailLayout = adRootView.findViewById(R.id.ad_app_detail_layout);
        appDetailLayout.setVisibility(nativeAd.getInteractionType() == 2 ? View.VISIBLE : View.GONE);
        if (nativeAd.getInteractionType() == 2) {
            setSixElements(parentView.getContext(), adRootView, nativeAd);
        }


        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);

        AppDownloadButton appDownloadButton = nativeView.findViewById(R.id.app_download_btn);
        appDownloadButton.setAppDownloadButtonStyle(new MyAppDownloadStyle(parentView.getContext()));
        if (nativeView.register(appDownloadButton)) {
            appDownloadButton.setVisibility(View.VISIBLE);
            appDownloadButton.refreshAppStatus();
            nativeView.getCallToActionView().setVisibility(View.GONE);
        } else {
            appDownloadButton.setVisibility(View.GONE);
            nativeView.getCallToActionView().setVisibility(View.VISIBLE);
        }

        return nativeView;
    }

    @NonNull
    public static View createImageOnlyAdView(NativeAd nativeAd, final ViewGroup parentView) {
        LayoutInflater inflater = LayoutInflater.from(parentView.getContext());
        View adRootView = inflater.inflate(R.layout.native_image_only_template, null);

        final NativeView nativeView = adRootView.findViewById(R.id.native_image_only_view);

        nativeView.setMediaView((MediaView) adRootView.findViewById(R.id.ad_media));
        nativeView.setCallToActionView(adRootView.findViewById(R.id.ad_call_to_action));

        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);

        // Register a native ad object.
        nativeView.setNativeAd(nativeAd);

        return nativeView;
    }

    /**
     * Custom AppDownloadButton Style
     */
    private static class MyAppDownloadStyle extends AppDownloadButtonStyle {

        public MyAppDownloadStyle(Context context) {
            super(context);
            normalStyle.setTextColor(context.getResources().getColor(R.color.white));
            normalStyle.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.native_button_rounded_corners_shape, context.getTheme()));
            processingStyle.setTextColor(context.getResources().getColor(R.color.black));
        }
    }
}


