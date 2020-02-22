package com.videomaker.photovideoeditorwithanimation.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.videomaker.photovideoeditorwithanimation.view.MyVideoView;
import com.videomaker.photovideoeditorwithanimation.view.MyVideoView.PlayPauseListner;

import java.io.File;

public class VideoShareActivity extends AppCompatActivity implements PlayPauseListner, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private int currentDuration;
    private boolean isComplate;
    private ImageView ivPlayPause;
    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        @Override
        public void run() {
            if (!isComplate) {
                currentDuration = videoView.getCurrentPosition();
                tempCapturetime = tempCapturetime + 100;
                tvDuration.setText(FileUtils.getDuration((long) videoView.getCurrentPosition()));
                tvDurationEnd.setText(FileUtils.getDuration((long) videoView.getDuration()));
                sbVideo.setProgress(currentDuration);
                mHandler.postDelayed(this, 100);
            }
        }
    };
    private SeekBar sbVideo;
    private Long tempCapturetime = 0L;
    private Toolbar toolbar;
    private TextView tvDuration;
    private TextView tvDurationEnd;
    private File videoFile;
    String videoPath;
    private MyVideoView videoView;
    LinearLayout linearAds;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_share);
        bindView();
        init();
        context = VideoShareActivity.this;
        if (Constant.Rate_app) {


            rate();


        }
        else {

            addListner();
        }




        linearAds = findViewById(R.id.linearAds);



    }

    void rate() {

        videoView.pause();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("have you enjoy App then please give RATE to app");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Constant.Rate_app=false;
                try {
                    String urlStrRateUs;
                    if (Constant.isSamsungApps(VideoShareActivity.this)) {
                        urlStrRateUs = "http://apps.samsung.com/appquery/appDetail.as?appId=" + getPackageName();
                    } else {
                        urlStrRateUs = "https://play.google.com/store/apps/details?id=" + getPackageName();
                    }
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlStrRateUs)));
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }




    @Override
    public void onPause() {

        super.onPause();
        videoView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!Constant.Rate_app)
        {
            addListner();
        }

    }

    @Override
    public void onDestroy() {

        videoView.stopPlayback();
        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onDestroy();
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        videoView = findViewById(R.id.videoView);
        tvDuration = findViewById(R.id.tvDuration1);
        tvDurationEnd = findViewById(R.id.tvDuration);
        ivPlayPause = findViewById(R.id.ivPlayPause);
        sbVideo = findViewById(R.id.sbVideo);
        findViewById(R.id.btnShareFace).setOnClickListener(this);
        findViewById(R.id.btnShareInsta).setOnClickListener(this);
        findViewById(R.id.btnShareWhatsApp).setOnClickListener(this);
        findViewById(R.id.btnShareYoutube).setOnClickListener(this);
        findViewById(R.id.btnShareMore).setOnClickListener(this);
    }

    private void init() {
        setSupportActionBar(toolbar);
        videoPath = getIntent().getStringExtra("android.intent.extra.TEXT");
        videoFile = new File(videoPath);
        videoView.setVideoPath(videoPath);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addListner() {
        videoView.setOnPlayPauseListner(this);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                try {
                    mp.seekTo(100);
                    sbVideo.setMax(mp.getDuration());
                    progressToTimer(mp.getDuration(), mp.getDuration());
                    tvDuration.setText(FileUtils.getDuration((long) mp.getCurrentPosition()));
                    tvDurationEnd.setText(FileUtils.getDuration((long) mp.getDuration()));
                    videoView.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.list_item_video_clicker).setOnClickListener(this);
        videoView.setOnClickListener(this);
        ivPlayPause.setOnClickListener(this);
        sbVideo.setOnSeekBarChangeListener(this);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                isComplate = true;
                mHandler.removeCallbacks(mUpdateTimeTask);
                tvDuration.setText(FileUtils.getDuration((long) mp.getDuration()));
                tvDurationEnd.setText(FileUtils.getDuration((long) mp.getDuration()));
            }
        });
    }

    public int progressToTimer(int progress, int totalDuration) {
        return ((int) ((((double) progress) / 100.0d) * ((double) (totalDuration / 1000)))) * 1000;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.videoView:
            case R.id.list_item_video_clicker:
            case R.id.ivPlayPause:
                if (videoView.isPlaying()) {
                    videoView.pause();
                    return;
                }
                videoView.start();
                isComplate = false;
                return;
            case R.id.btnShareWhatsApp:
                shareImageWhatsApp("com.whatsapp", "Whatsapp");
                return;
            case R.id.btnShareFace:
                shareImageWhatsApp("com.facebook.katana", "Facebook");
                return;
            case R.id.btnShareInsta:
                shareImageWhatsApp("com.instagram.android", "Instagram");
                return;
            case R.id.btnShareMore:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("video/mp4");
                Uri uriVideo = FileProvider.getUriForFile(VideoShareActivity.this, getString(R.string.file_provider_authority), videoFile);
                share.putExtra(Intent.EXTRA_STREAM, uriVideo);
                startActivity(Intent.createChooser(share, "Share Video"));
                return;
            case R.id.btnShareYoutube:
                if (videoPath != null) {
                    sentVideoShare(videoPath, getString(R.string.app_name), "https://play.google.com/store/apps/details?id=" + getPackageName(), "com.google.android.youtube");
                }
                return;
            default:
                break;
        }

    }

    public void sentVideoShare(String path, String Title, String content, String appShare) {
        File localFile = new File(path);
        String str = MimeTypeMap.getSingleton().getMimeTypeFromExtension(localFile.getName().substring(localFile.getName().lastIndexOf(".") + 1));
        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.setType(str);
        Uri uriImage = FileProvider.getUriForFile(VideoShareActivity.this, getString(R.string.file_provider_authority), localFile);
        localIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
        localIntent.putExtra(Intent.EXTRA_SUBJECT, Title);
        localIntent.putExtra(Intent.EXTRA_TEXT, content);
        boolean resolved = false;
        for (ResolveInfo app : getPackageManager().queryIntentActivities(localIntent, 0)) {
            if (app.activityInfo.packageName.startsWith(appShare)) {
                ActivityInfo activity = app.activityInfo;
                ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                localIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);//270532608
                localIntent.setComponent(name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(localIntent);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//268435456
            intent.setData(Uri.parse("market://details?id=" + appShare));
            if (Constant.isAvailable(intent, VideoShareActivity.this))
                startActivity(intent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        currentDuration = progressToTimer(seekBar.getProgress(), videoView.getDuration());
        videoView.seekTo(seekBar.getProgress());
        if (videoView.isPlaying()) {
            updateProgressBar();
        }
    }

    public void updateProgressBar() {
        try {
            mHandler.removeCallbacks(mUpdateTimeTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onVideoPause() {
        if (!(mHandler == null || mUpdateTimeTask == null)) {
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ivPlayPause.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ivPlayPause.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public void onVideoPlay() {
        updateProgressBar();
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        ivPlayPause.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ivPlayPause.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ivPlayPause.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void shareImageWhatsApp(String pkg, String appName) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "Try it Now " + "https://play.google.com/store/apps/details?id=" + getPackageName());
        share.setType("video/mp4");
        Uri uriVideo = FileProvider.getUriForFile(VideoShareActivity.this, getString(R.string.file_provider_authority), videoFile);
        share.putExtra(Intent.EXTRA_STREAM, uriVideo);
        if (isPackageInstalled(pkg, this)) {
            share.setPackage(pkg);
            startActivity(Intent.createChooser(share, "Share Video"));
        } else {
            Toast.makeText(getApplicationContext(), "Please Install " + appName, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        try {
            context.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, VideoAlbumActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(VideoAlbumActivity.EXTRA_FROM_VIDEO, true);
        startActivity(intent);
    }
}
