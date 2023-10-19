// Copyright 2015-present 650 Industries. All rights reserved.

package host.exp.exponent;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import expo.modules.splashscreen.SplashScreenImageResizeMode;
import host.exp.exponent.analytics.EXL;

public class Constants {

  public static class ExpoViewAppConstants {
    public String VERSION_NAME;
    public String RELEASE_CHANNEL;
    public boolean ARE_REMOTE_UPDATES_ENABLED;
    public boolean UPDATES_CHECK_AUTOMATICALLY;
    public int UPDATES_FALLBACK_TO_CACHE_TIMEOUT;
    public List<Constants.EmbeddedResponse> EMBEDDED_RESPONSES;
    public int ANDROID_VERSION_CODE;
    public boolean FCM_ENABLED;
    public SplashScreenImageResizeMode SPLASH_SCREEN_IMAGE_RESIZE_MODE;
  }

  private static final String TAG = Constants.class.getSimpleName();

  public static String VERSION_NAME = null;
  public static final String API_HOST = "https://exp.host";
  public static String ABI_VERSIONS;
  public static String SDK_VERSIONS;
  public static List<String> SDK_VERSIONS_LIST;
  public static final String TEMPORARY_ABI_VERSION = null;
  public static final String EMBEDDED_KERNEL_PATH = "assets://kernel.android.bundle";
  public static List<EmbeddedResponse> EMBEDDED_RESPONSES;
  public static boolean DISABLE_NUX = false;
  public static String RELEASE_CHANNEL = "default";
  public static boolean ARE_REMOTE_UPDATES_ENABLED = true;
  public static boolean UPDATES_CHECK_AUTOMATICALLY = true;
  public static int UPDATES_FALLBACK_TO_CACHE_TIMEOUT = 0;
  public static int ANDROID_VERSION_CODE;
  public static boolean FCM_ENABLED;
  public static SplashScreenImageResizeMode SPLASH_SCREEN_IMAGE_RESIZE_MODE;

  public static void setSdkVersions(List<String> sdkVersions) {
    ABI_VERSIONS = TextUtils.join(",", sdkVersions);

    // NOTE: Currently public-facing SDK versions and internal ABI versions are the same, but
    // eventually we may decouple them
    SDK_VERSIONS = ABI_VERSIONS;
    SDK_VERSIONS_LIST = sdkVersions;
  }

  static {
    List<String> abiVersions = new ArrayList<>();
    // WHEN_DISTRIBUTING_REMOVE_FROM_HERE
    // WHEN_PREPARING_SHELL_REMOVE_FROM_HERE
    // ADD ABI VERSIONS HERE DO NOT MODIFY
    // BEGIN_SDK_49
    abiVersions.add("49.0.0");
    // END_SDK_49
    // BEGIN_SDK_48
    abiVersions.add("48.0.0");
    // END_SDK_48
    // BEGIN_SDK_47
    abiVersions.add("47.0.0");
    // END_SDK_47
    // WHEN_PREPARING_SHELL_REMOVE_TO_HERE
    // WHEN_DISTRIBUTING_REMOVE_TO_HERE

    if (TEMPORARY_ABI_VERSION != null) {
      abiVersions.add(TEMPORARY_ABI_VERSION);
    }

    setSdkVersions(abiVersions);

    List<EmbeddedResponse> embeddedResponses = new ArrayList<>();
    // WHEN_PREPARING_SHELL_REMOVE_FROM_HERE
    embeddedResponses.add(new EmbeddedResponse("https://exp.host/@exponent/home/bundle", EMBEDDED_KERNEL_PATH, "application/javascript"));
    // WHEN_PREPARING_SHELL_REMOVE_TO_HERE

    // ADD EMBEDDED RESPONSES HERE
    // START EMBEDDED RESPONSES
    // END EMBEDDED RESPONSES

    try {
      Class appConstantsClass = Class.forName("host.exp.exponent.generated.AppConstants");
      ExpoViewAppConstants appConstants = (ExpoViewAppConstants) appConstantsClass.getMethod("get").invoke(null);
      VERSION_NAME = appConstants.VERSION_NAME;
      RELEASE_CHANNEL = appConstants.RELEASE_CHANNEL;
      ARE_REMOTE_UPDATES_ENABLED = appConstants.ARE_REMOTE_UPDATES_ENABLED;
      UPDATES_CHECK_AUTOMATICALLY = appConstants.UPDATES_CHECK_AUTOMATICALLY;
      UPDATES_FALLBACK_TO_CACHE_TIMEOUT = appConstants.UPDATES_FALLBACK_TO_CACHE_TIMEOUT;
      ANDROID_VERSION_CODE = appConstants.ANDROID_VERSION_CODE;
      FCM_ENABLED = appConstants.FCM_ENABLED;

      embeddedResponses.addAll(appConstants.EMBEDDED_RESPONSES);
      EMBEDDED_RESPONSES = embeddedResponses;
      SPLASH_SCREEN_IMAGE_RESIZE_MODE = appConstants.SPLASH_SCREEN_IMAGE_RESIZE_MODE;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  public static final boolean DEBUG_COLD_START_METHOD_TRACING = false;
  public static final boolean DEBUG_MANIFEST_METHOD_TRACING = false;
  public static final boolean DEBUG_METHOD_TRACING = DEBUG_COLD_START_METHOD_TRACING || DEBUG_MANIFEST_METHOD_TRACING;
  public static final boolean ENABLE_LEAK_CANARY = false;
  public static final boolean WRITE_BUNDLE_TO_LOG = false;
  public static final boolean WAIT_FOR_DEBUGGER = false;

  public static class EmbeddedResponse {
    public final String url;
    public final String responseFilePath;
    public final String mediaType;

    public EmbeddedResponse(final String url, final String responseFilePath, final String mediaType) {
      this.url = url;
      this.responseFilePath = responseFilePath;
      this.mediaType = mediaType;
    }
  }

  public static String getVersionName(Context context) {
    if (VERSION_NAME != null) {
      // This will be set in shell apps
      return VERSION_NAME;
    } else {
      try {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionName;
      } catch (PackageManager.NameNotFoundException e) {
        EXL.e(TAG, e.toString());
        return "";
      }
    }
  }

  private static boolean sIsTest = false;

  public static void setInTest() {
    sIsTest = true;
  }

  public static boolean isTest() {
    return sIsTest;
  }
}
