package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.FAN;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_ADMOB;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.FAN_BIDDING_APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.GOOGLE_AD_MANAGER;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;
import static com.solodroid.ads.sdk.util.Constant.UNITY;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.AdManagerTemplateView;
import com.solodroid.ads.sdk.util.Constant;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.TemplateView;
import com.solodroid.ads.sdk.util.Tools;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.ArrayList;
import java.util.List;

public class NativeAdFragment {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        View view;

        LinearLayout nativeAdViewContainer;

        MediaView mediaView;
        TemplateView admobNativeAd;
        LinearLayout admobNativeBackground;

        MediaView adManagerMediaView;
        AdManagerTemplateView adManagerNativeAd;
        LinearLayout adManagerNativeBackground;

        com.facebook.ads.NativeAd fanNativeAd;
        NativeAdLayout fanNativeAdLayout;

        View startappNativeAd;
        ImageView startappNativeImage;
        ImageView startappNativeIcon;
        TextView startappNativeTitle;
        TextView startappNativeDescription;
        Button startappNativeButton;
        LinearLayout startappNativeBackground;

        FrameLayout applovinNativeAd;
        MaxNativeAdLoader nativeAdLoader;
        MaxAd nativeAd;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private String adManagerNativeId = "";
        private String fanNativeId = "";
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        private String nativeAdStyle = "";
        private int nativeBackgroundLight = R.color.color_native_background_light;
        private int nativeBackgroundDark = R.color.color_native_background_dark;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setPadding(int left, int top, int right, int bottom) {
            setNativeAdPadding(left, top, right, bottom);
            return this;
        }

        public Builder setMargin(int left, int top, int right, int bottom) {
            setNativeAdMargin(left, top, right, bottom);
            return this;
        }

        public Builder setBackgroundResource(int drawableBackground) {
            setNativeAdBackgroundResource(drawableBackground);
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobNativeId(String adMobNativeId) {
            this.adMobNativeId = adMobNativeId;
            return this;
        }

        public Builder setAdManagerNativeId(String adManagerNativeId) {
            this.adManagerNativeId = adManagerNativeId;
            return this;
        }

        public Builder setFanNativeId(String fanNativeId) {
            this.fanNativeId = fanNativeId;
            return this;
        }

        public Builder setAppLovinNativeId(String appLovinNativeId) {
            this.appLovinNativeId = appLovinNativeId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public Builder setNativeAdStyle(String nativeAdStyle) {
            this.nativeAdStyle = nativeAdStyle;
            return this;
        }

        public Builder setNativeAdBackgroundColor(int colorLight, int colorDark) {
            this.nativeBackgroundLight = colorLight;
            this.nativeBackgroundDark = colorDark;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);

                admobNativeAd = view.findViewById(R.id.admob_native_ad_container);
                mediaView = view.findViewById(R.id.media_view);
                admobNativeBackground = view.findViewById(R.id.background);

                adManagerNativeAd = view.findViewById(R.id.google_ad_manager_native_ad_container);
                adManagerMediaView = view.findViewById(R.id.ad_manager_media_view);
                adManagerNativeBackground = view.findViewById(R.id.ad_manager_background);

                fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

                startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = view.findViewById(R.id.startapp_native_image);
                startappNativeIcon = view.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = view.findViewById(R.id.startapp_native_title);
                startappNativeDescription = view.findViewById(R.id.startapp_native_description);
                startappNativeButton = view.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v -> startappNativeAd.performClick());
                startappNativeBackground = view.findViewById(R.id.startapp_native_background);

                applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);

                switch (adNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case FAN:
                        fanNativeAd = new com.facebook.ads.NativeAd(activity, fanNativeId);
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                loadBackupNativeAd();
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                // Race condition, load() called again before last ad was displayed
                                fanNativeAdLayout.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                if (fanNativeAd != ad) {
                                    return;
                                }
                                // Inflate Native Ad into Container
                                //inflateAd(nativeAd);
                                fanNativeAd.unregisterView();
                                // Add the Ad view into the ad container.
                                LayoutInflater inflater = LayoutInflater.from(activity);
                                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                LinearLayout nativeAdView;

                                switch (nativeAdStyle) {
                                    case Constant.STYLE_NEWS:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_VIDEO_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_VIDEO_LARGE:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_RADIO:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                        break;
                                    default:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                        break;
                                }
                                fanNativeAdLayout.addView(nativeAdView);

                                // Add the AdOptionsView
                                LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                AdOptionsView adOptionsView = new AdOptionsView(activity, fanNativeAd, fanNativeAdLayout);
                                adChoicesContainer.removeAllViews();
                                adChoicesContainer.addView(adOptionsView, 0);

                                // Create native UI using the ad metadata.
                                TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                if (darkTheme) {
                                    nativeAdTitle.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                    nativeAdSocialContext.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                    sponsoredLabel.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                    nativeAdBody.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                                // Set the Text.
                                nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                // Create a list of clickable views
                                List<View> clickableViews = new ArrayList<>();
                                clickableViews.add(nativeAdTitle);
                                clickableViews.add(sponsoredLabel);
                                clickableViews.add(nativeAdIcon);
                                clickableViews.add(nativeAdMedia);
                                clickableViews.add(nativeAdBody);
                                clickableViews.add(nativeAdSocialContext);
                                clickableViews.add(nativeAdCallToAction);

                                // Register the Title and CTA button to listen for clicks.
                                fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                        fanNativeAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        if (startappNativeAd.getVisibility() != View.VISIBLE) {
                            StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
                            NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                    .setAdsNumber(3)
                                    .setAutoBitmapDownload(true)
                                    .setPrimaryImageSize(Constant.STARTAPP_IMAGE_MEDIUM);
                            AdEventListener adListener = new AdEventListener() {
                                @Override
                                public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad arg0) {
                                    Log.d(TAG, "StartApp Native Ad loaded");
                                    startappNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    //noinspection rawtypes
                                    ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                    // Print all ads details to log
                                    for (Object ad : ads) {
                                        Log.d(TAG, "StartApp Native Ad " + ad.toString());
                                    }

                                    NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                    if (ad != null) {
                                        startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                        startappNativeIcon.setImageBitmap(ad.getSecondaryImageBitmap());
                                        startappNativeTitle.setText(ad.getTitle());
                                        startappNativeDescription.setText(ad.getDescription());
                                        startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                        ad.registerViewForInteraction(startappNativeAd);
                                    }

                                    if (darkTheme) {
                                        startappNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        startappNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }

                                }

                                @Override
                                public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad arg0) {
                                    loadBackupNativeAd();
                                    Log.d(TAG, "StartApp Native Ad failed loaded");
                                }
                            };
                            startAppNativeAd.loadAd(nativePrefs, adListener);
                        } else {
                            Log.d(TAG, "StartApp Native Ad has been loaded");
                        }
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovinNativeAd.removeAllViews();
                                    applovinNativeAd.addView(nativeAdView);
                                    applovinNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);

                                    LinearLayout applovinNativeBackground = nativeAdView.findViewById(R.id.applovin_native_background);
                                    if (darkTheme) {
                                        applovinNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        applovinNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                    loadBackupNativeAd();
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark(nativeAdStyle));
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView(nativeAdStyle));
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                        }
                        break;

                    case UNITY:
                        //do nothing
                        break;

                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);

                admobNativeAd = view.findViewById(R.id.admob_native_ad_container);
                mediaView = view.findViewById(R.id.media_view);
                admobNativeBackground = view.findViewById(R.id.background);

                adManagerNativeAd = view.findViewById(R.id.google_ad_manager_native_ad_container);
                adManagerMediaView = view.findViewById(R.id.ad_manager_media_view);
                adManagerNativeBackground = view.findViewById(R.id.ad_manager_background);

                fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

                startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
                startappNativeImage = view.findViewById(R.id.startapp_native_image);
                startappNativeIcon = view.findViewById(R.id.startapp_native_icon);
                startappNativeTitle = view.findViewById(R.id.startapp_native_title);
                startappNativeDescription = view.findViewById(R.id.startapp_native_description);
                startappNativeButton = view.findViewById(R.id.startapp_native_button);
                startappNativeButton.setOnClickListener(v -> startappNativeAd.performClick());
                startappNativeBackground = view.findViewById(R.id.startapp_native_background);
                applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);

                switch (backupAdNetwork) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (admobNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admobNativeAd.setStyles(styles);
                                            admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admobNativeAd.setNativeAd(NativeAd);
                                        admobNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admobNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adManagerNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, nativeBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            adManagerNativeAd.setStyles(styles);
                                            adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                        }
                                        adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        adManagerNativeAd.setNativeAd(NativeAd);
                                        adManagerNativeAd.setVisibility(View.VISIBLE);
                                        nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            adManagerNativeAd.setVisibility(View.GONE);
                                            nativeAdViewContainer.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getGoogleAdManagerRequest());
                        } else {
                            Log.d(TAG, "Ad Manager Native Ad has been loaded");
                        }
                        break;

                    case STARTAPP:
                        if (startappNativeAd.getVisibility() != View.VISIBLE) {
                            StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
                            NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                    .setAdsNumber(3)
                                    .setAutoBitmapDownload(true)
                                    .setPrimaryImageSize(Constant.STARTAPP_IMAGE_MEDIUM);
                            AdEventListener adListener = new AdEventListener() {
                                @Override
                                public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad arg0) {
                                    Log.d(TAG, "StartApp Native Ad loaded");
                                    startappNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                    //noinspection rawtypes
                                    ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                    // Print all ads details to log
                                    for (Object ad : ads) {
                                        Log.d(TAG, "StartApp Native Ad " + ad.toString());
                                    }

                                    NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                    if (ad != null) {
                                        startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                        startappNativeTitle.setText(ad.getTitle());
                                        startappNativeDescription.setText(ad.getDescription());
                                        startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                        ad.registerViewForInteraction(startappNativeAd);
                                    }

                                    if (darkTheme) {
                                        startappNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        startappNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }

                                }

                                @Override
                                public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad arg0) {
                                    startappNativeAd.setVisibility(View.GONE);
                                    nativeAdViewContainer.setVisibility(View.GONE);
                                    Log.d(TAG, "StartApp Native Ad failed loaded");
                                }
                            };
                            startAppNativeAd.loadAd(nativePrefs, adListener);
                        } else {
                            Log.d(TAG, "StartApp Native Ad has been loaded");
                        }
                        break;

                    case FAN:
                        fanNativeAd = new com.facebook.ads.NativeAd(activity, fanNativeId);
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                // Race condition, load() called again before last ad was displayed
                                fanNativeAdLayout.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                if (fanNativeAd != ad) {
                                    return;
                                }
                                // Inflate Native Ad into Container
                                //inflateAd(nativeAd);
                                fanNativeAd.unregisterView();
                                // Add the Ad view into the ad container.
                                LayoutInflater inflater = LayoutInflater.from(activity);
                                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                LinearLayout nativeAdView;

                                switch (nativeAdStyle) {
                                    case Constant.STYLE_NEWS:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_VIDEO_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_VIDEO_LARGE:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                        break;
                                    case Constant.STYLE_RADIO:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                        break;
                                    default:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                        break;
                                }
                                fanNativeAdLayout.addView(nativeAdView);

                                // Add the AdOptionsView
                                LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                AdOptionsView adOptionsView = new AdOptionsView(activity, fanNativeAd, fanNativeAdLayout);
                                adChoicesContainer.removeAllViews();
                                adChoicesContainer.addView(adOptionsView, 0);

                                // Create native UI using the ad metadata.
                                TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                if (darkTheme) {
                                    nativeAdTitle.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                    nativeAdSocialContext.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_primary_text_color));
                                    sponsoredLabel.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                    nativeAdBody.setTextColor(ContextCompat.getColor(activity, R.color.applovin_dark_secondary_text_color));
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                                // Set the Text.
                                nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                // Create a list of clickable views
                                List<View> clickableViews = new ArrayList<>();
                                clickableViews.add(nativeAdTitle);
                                clickableViews.add(sponsoredLabel);
                                clickableViews.add(nativeAdIcon);
                                clickableViews.add(nativeAdMedia);
                                clickableViews.add(nativeAdBody);
                                clickableViews.add(nativeAdSocialContext);
                                clickableViews.add(nativeAdCallToAction);

                                // Register the Title and CTA button to listen for clicks.
                                fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                        fanNativeAd.loadAd(loadAdConfig);
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                            nativeAdLoader = new MaxNativeAdLoader(appLovinNativeId, activity);
                            nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                                @Override
                                public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                                    // Clean up any pre-existing native ad to prevent memory leaks.
                                    if (nativeAd != null) {
                                        nativeAdLoader.destroy(nativeAd);
                                    }

                                    // Save ad for cleanup.
                                    nativeAd = ad;

                                    // Add ad view to view.
                                    applovinNativeAd.removeAllViews();
                                    applovinNativeAd.addView(nativeAdView);
                                    applovinNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);

                                    LinearLayout applovinNativeBackground = nativeAdView.findViewById(R.id.applovin_native_background);
                                    if (darkTheme) {
                                        applovinNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        applovinNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                }

                                @Override
                                public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                                    // We recommend retrying with exponentially higher delays up to a maximum delay
                                }

                                @Override
                                public void onNativeAdClicked(final MaxAd ad) {
                                    // Optional click callback
                                }
                            });
                            if (darkTheme) {
                                nativeAdLoader.loadAd(createNativeAdViewDark(nativeAdStyle));
                            } else {
                                nativeAdLoader.loadAd(createNativeAdView(nativeAdStyle));
                            }
                        } else {
                            Log.d(TAG, "AppLovin Native Ad has been loaded");
                        }
                        break;

                    case UNITY:

                    case NONE:
                        nativeAdViewContainer.setVisibility(View.GONE);
                        break;

                }

            }

        }

        public void setNativeAdPadding(int left, int top, int right, int bottom) {
            nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);
            nativeAdViewContainer.setPadding(left, top, right, bottom);
            if (darkTheme) {
                nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(activity, nativeBackgroundDark));
            } else {
                nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(activity, nativeBackgroundLight));
            }
        }

        public void setNativeAdMargin(int left, int top, int right, int bottom) {
            nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);
            setMargins(nativeAdViewContainer, left, top, right, bottom);
        }

        public void setMargins(View view, int left, int top, int right, int bottom) {
            if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                p.setMargins(left, top, right, bottom);
                view.requestLayout();
            }
        }

        public void setNativeAdBackgroundResource(int drawableBackground) {
            nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);
            nativeAdViewContainer.setBackgroundResource(drawableBackground);
        }

        public MaxNativeAdView createNativeAdView(String nativeAdStyle) {
            MaxNativeAdViewBinder binder;
            switch (nativeAdStyle) {
                case Constant.STYLE_NEWS:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_news_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_RADIO:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_radio_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_VIDEO_LARGE:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_video_large_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_VIDEO_SMALL:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_video_small_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                default:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_medium_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
            }
            return new MaxNativeAdView(binder, activity);
        }

        public MaxNativeAdView createNativeAdViewDark(String nativeAdStyle) {
            MaxNativeAdViewBinder binder;
            switch (nativeAdStyle) {
                case Constant.STYLE_NEWS:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_news_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_RADIO:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_radio_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_VIDEO_LARGE:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_video_large_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                case Constant.STYLE_VIDEO_SMALL:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_video_small_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
                default:
                    binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_medium_template_view)
                            .setTitleTextViewId(R.id.title_text_view)
                            .setBodyTextViewId(R.id.body_text_view)
                            .setAdvertiserTextViewId(R.id.advertiser_textView)
                            .setIconImageViewId(R.id.icon_image_view)
                            .setMediaContentViewGroupId(R.id.media_view_container)
                            .setOptionsContentViewGroupId(R.id.ad_options_view)
                            .setCallToActionButtonId(R.id.cta_button)
                            .build();
                    break;
            }
            return new MaxNativeAdView(binder, activity);
        }

    }

}