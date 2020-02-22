package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.receiver.OnProgressReceiver;
import com.videomaker.photovideoeditorwithanimation.service.CreateVideoService;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.videomaker.photovideoeditorwithanimation.view.FreshDownloadView;

import java.util.Locale;

public class ProgressVideoActivity extends AppCompatActivity implements OnProgressReceiver {

    private MyApplication application;
    private FreshDownloadView freshDownloadView;
    private String videoPath;
    private TextView tvProgress;
    LinearLayout linearAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        application = MyApplication.getInstance();
        freshDownloadView = findViewById(R.id.freshDownloadView);
        tvProgress = findViewById(R.id.tvProgress);
        linearAds = findViewById(R.id.linearAds);
    }



    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        application.setOnProgressReceiver(this);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        application.setOnProgressReceiver(null);
        if (MyApplication.isMyServiceRunning(this, CreateVideoService.class)) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onImageProgressFrameUpdate(final float progress) {
        if (freshDownloadView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int p = (int) ((25.0f * progress) / 100.0f);
                    tvProgress.setText(String.format(Locale.getDefault(), "Video setup %02d%%", p));
                    freshDownloadView.upDateProgress(p);
                }
            });
        }
    }

    @Override
    public void onProgressFinish(String strVideoPath) {
        this.videoPath = strVideoPath;
        loadVideoPlay();
    }

    private void loadVideoPlay() {
        Intent intent = new Intent(this, VideoShareActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Intent.EXTRA_TEXT, videoPath);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void onVideoProgressFrameUpdate(final float progress) {
        if (freshDownloadView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int p = (int) (25.0f + ((75.0f * progress) / 100.0f));
                    tvProgress.setText(String.format(Locale.getDefault(), "Create a video %02d%%", p));
                    freshDownloadView.upDateProgress(p);
                }
            });
        }
    }
}
