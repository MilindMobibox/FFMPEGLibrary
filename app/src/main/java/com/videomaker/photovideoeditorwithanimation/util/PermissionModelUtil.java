package com.videomaker.photovideoeditorwithanimation.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;

import com.universevideomaker.R;


@SuppressLint({"NewApi"})
public class PermissionModelUtil {
    private static final String[] NECESSARY_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final String PERMISSION_CHECK_PREF = "marshmallow_permission_check";
    private Context context;
    private SharedPreferences sharedPrefs;

    public PermissionModelUtil(Context context) {
        this.context = context;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean needPermissionCheck() {
        if (VERSION.SDK_INT >= 23 && this.context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void showPermissionExplanationThenAuthorization() {
        new Builder(this.context, R.style.MovieMakerAlertDialog)
                .setTitle(R.string.permission_check_title)
                .setMessage(R.string.permission_check_message)
                .setPositiveButton(R.string.alert_ok_button, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions();
                        sharedPrefs.edit()
                                .putBoolean(PERMISSION_CHECK_PREF, false)
                                .commit();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void requestPermissions() {
        ((Activity) this.context).requestPermissions(NECESSARY_PERMISSIONS, 1);
    }
}
