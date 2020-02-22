package com.videomaker.photovideoeditorwithanimation.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.universevideomaker.R;

import java.util.List;

public class Constant {

    public static boolean ADS_STATUS = true;
    // if you show ads in your app then its true otherwise set it false

    public static String ADS_TYPE = "admob";
    // if you require google abmob ads then write 'admob'
    // if you require facebook audiance network ads then write 'facebook'

 public static String ADS_ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
  //  public static String ADS_ADMOB_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    // here set your Admob banner ad unit id

    public static String ADS_ADMOB_FULLSCREEN_ID = "ca-app-pub-3940256099942544/1033173712";
   // public static String ADS_ADMOB_FULLSCREEN_ID = "ca-app-pub-3940256099942544/1033173712";
    // here set your admob Interstitial ad unit id

   public static String ADS_ADMOB_NATIVE_ID = "ca-app-pub-7994250233949511/4740930875";
   // public static String ADS_ADMOB_NATIVE_ID = "ca-app-pub-3940256099942544/2247696110";
    // here set your Admob native ad unit id

    public static String ADS_FACEBOOK_NATIVE_BANNER_ID = "YOUR_PLACEMENT_ID";
    // here set your facebook audiance network native banner Placement ID

    public static String ADS_FACEBOOK_FULLSCREEN_ID = "YOUR_PLACEMENT_ID";
    // here set your facebook audiance network Interstitial Placement ID

    public static String ADS_FACEBOOK_NATIVE_ID = "YOUR_PLACEMENT_ID";
    // here set your facebook audiance network native ads Placement ID

    public static String DEVELOPER_ACCOUNT_LINK = "https://play.google.com/store/apps/dev?id=4873742009842360386";
    // here set your more app playstore link

    public static String PolicyUrl = "https://www.google.com/";
    //here set your privacy policy url


    public static String editedImagePath = "editedImagePath";
    public static int goneLayout = 8;
    public static int visibleLayout = 0;
    public static boolean SHOW_FULLSCREEN_ADS = true;
    public static boolean Rate_app=true;
    public static String BackgroundName = "file:///android_asset/background_image/bg";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isSamsungApps(Context context) {
        final PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equalsIgnoreCase("com.sec.android.app.samsungapps"))
                return true;
        }
        return false;
    }


    public static boolean isAvailable(Intent intent, Context context) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static void privacyPolicy(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.AlertDialogCustom);
        alert.setTitle("Universe Video Maker");
        final WebView wv = new WebView(context);
        wv.loadUrl(PolicyUrl);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return false;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest request) {
                Uri uri = request.getUrl();
                webView.loadUrl(uri.toString());
                return false;
            }
        });
        alert.setView(wv);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
