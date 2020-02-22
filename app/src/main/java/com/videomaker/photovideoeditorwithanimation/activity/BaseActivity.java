package com.videomaker.photovideoeditorwithanimation.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.util.Constant;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    public static int nativeRefreshCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void loadNativeAdsList() {
        if (Constant.ADS_STATUS) {
            String adsStatus = Constant.ADS_TYPE;
            String adsNativeId = "";

            if (adsStatus != null && !adsStatus.equals("") && adsStatus.equals("admob")) {
                if (Constant.ADS_ADMOB_NATIVE_ID.contains("/")) {
                    adsStatus = "admob";
                    adsNativeId = Constant.ADS_ADMOB_NATIVE_ID;
                } else {
                    adsStatus = "facebook";
                    adsNativeId = Constant.ADS_ADMOB_NATIVE_ID;
                }
            } else if (adsStatus != null && !adsStatus.equals("") && adsStatus.equals("facebook")) {
                if (Constant.ADS_FACEBOOK_NATIVE_ID.contains("/")) {
                    adsStatus = "admob";
                    adsNativeId = Constant.ADS_FACEBOOK_NATIVE_ID;
                } else {
                    adsStatus = "facebook";
                    adsNativeId = Constant.ADS_FACEBOOK_NATIVE_ID;
                }
            }

        }
    }

    public void showExitDialog() {
        final Dialog dialogCustomExit = new Dialog(BaseActivity.this, R.style.UploadDialog);
        dialogCustomExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomExit.setContentView(R.layout.custom_dialog_layout);
        dialogCustomExit.setCancelable(true);
        dialogCustomExit.show();

        TextView txtTileDialog = dialogCustomExit.findViewById(R.id.textTitle);
        final TextView txtMessageDialog = dialogCustomExit.findViewById(R.id.textDesc);

        TextView btnNegative = dialogCustomExit.findViewById(R.id.btnNegative);
        TextView btnPositive = dialogCustomExit.findViewById(R.id.btnPositive);
        LinearLayout linearAdsBanner = dialogCustomExit.findViewById(R.id.linearAdsBanner);

        txtTileDialog.setText(getString(R.string.exit));
        txtMessageDialog.setText(getString(R.string.sure_exit));
        btnPositive.setText(getString(R.string.no_exit));
        btnNegative.setText(getString(R.string.rate_now));

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomExit.dismiss();
                finish();

            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCustomExit.dismiss();
                rateApp();
            }
        });
    }

    void rateApp() {
        String urlStrRateUs;
        if (Constant.isSamsungApps(this)) {
            urlStrRateUs = "http://apps.samsung.com/appquery/appDetail.as?appId=" + getPackageName();
        } else {
            urlStrRateUs = "market://details?id=" + getPackageName();
        }
    }
}
