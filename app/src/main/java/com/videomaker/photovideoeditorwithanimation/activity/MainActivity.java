package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.service.CreateVideoService;
import com.videomaker.photovideoeditorwithanimation.service.CreateImageService;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.videomaker.photovideoeditorwithanimation.util.PermissionModelUtil;
import com.example.ffmpeglibrary.videolib.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    PermissionModelUtil modelUtil;
    LinearLayout buttonStart, buttonAlbum;
    LinearLayout buttonShare, buttonRateUs, buttonMoreApps;
    TextView textPrivacyPolicy;
    LinearLayout linearAds;
    RelativeLayout rl_Main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isVideoInprocess()) {
            startActivity(new Intent(this, ProgressVideoActivity.class));
            overridePendingTransition(0, 0);
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        rl_Main = findViewById(R.id.rl_Main);
       // rl_Main.setBackground(getResources().getDrawable(R.drawable.dst_bg));
        init();
        addListener();
      //  audioSet();
       // audioSet1();
         /*  audioSet2();
        audioSet3();
        audioSet4();*/
    }

    private void audioSet() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            FileUtils.APP_DIRECTORY.mkdir();
            File tempFile = new File(FileUtils.APP_DIRECTORY, "temp5.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(R.raw.bekhayali);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    break;
                }
                out.write(buff, 0, read);
            }
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(tempFile.getAbsolutePath());
            player.setAudioStreamType(3);
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void audioSet1() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            FileUtils.APP_DIRECTORY.mkdir();
            File tempFile = new File(FileUtils.APP_DIRECTORY, "temp1.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(R.raw.fluteonmyway);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    break;
                }
                out.write(buff, 0, read);
            }
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(tempFile.getAbsolutePath());
            player.setAudioStreamType(3);
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void audioSet2() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            FileUtils.APP_DIRECTORY.mkdir();
            File tempFile = new File(FileUtils.APP_DIRECTORY, "temp2.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(R.raw.flutetumhiaana);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    break;
                }
                out.write(buff, 0, read);
            }
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(tempFile.getAbsolutePath());
            player.setAudioStreamType(3);
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void audioSet3() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            FileUtils.APP_DIRECTORY.mkdir();
            File tempFile = new File(FileUtils.APP_DIRECTORY, "temp3.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(R.raw.hawabanke);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    break;
                }
                out.write(buff, 0, read);
            }
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(tempFile.getAbsolutePath());
            player.setAudioStreamType(3);
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void audioSet4() {
        try {
            FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
            FileUtils.APP_DIRECTORY.mkdir();
            File tempFile = new File(FileUtils.APP_DIRECTORY, "temp4.mp3");
            if (tempFile.exists()) {
                FileUtils.deleteFile(tempFile);
            }
            InputStream in = getResources().openRawResource(R.raw.iphone11promax);
            FileOutputStream out = new FileOutputStream(tempFile);
            byte[] buff = new byte[1024];
            while (true) {
                int read = in.read(buff);
                if (read <= 0) {
                    break;
                }
                out.write(buff, 0, read);
            }
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(tempFile.getAbsolutePath());
            player.setAudioStreamType(3);
            player.prepare();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void addListener() {
        buttonStart.setOnClickListener(this);
        buttonAlbum.setOnClickListener(this);
        buttonShare.setOnClickListener(this);
        buttonRateUs.setOnClickListener(this);
        buttonMoreApps.setOnClickListener(this);
        textPrivacyPolicy.setOnClickListener(this);
    }

    private void init() {
        modelUtil = new PermissionModelUtil(this);
        if (modelUtil.needPermissionCheck()) {
            modelUtil.showPermissionExplanationThenAuthorization();
        } else {
            MyApplication.getInstance().getFolderList();
        }
        buttonStart = findViewById(R.id.linearStart);
        buttonAlbum = findViewById(R.id.linearAlbum);
        buttonShare = findViewById(R.id.linearShare);
        buttonRateUs = findViewById(R.id.linearRateUs);
        buttonMoreApps = findViewById(R.id.linearMoreApps);
        textPrivacyPolicy = findViewById(R.id.textPrivacyPolicy);
        linearAds = findViewById(R.id.linearAds);

    }

    private boolean isVideoInprocess() {
        return MyApplication.isMyServiceRunning(this, CreateVideoService.class) || MyApplication.isMyServiceRunning(this, CreateImageService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linearMoreApps:
                loadGetMore();
                return;
            case R.id.linearStart:
                if (modelUtil.needPermissionCheck()) {
                    modelUtil.showPermissionExplanationThenAuthorization();
                } else {
                    MyApplication.isBreak = false;
                    MyApplication.getInstance().setMusicData(null);
                    loadImageSelectionActivity();
                }
                return;
            case R.id.linearAlbum:
                if (modelUtil.needPermissionCheck()) {
                    modelUtil.showPermissionExplanationThenAuthorization();
                } else {
                    loadVideoAlbumActivity();
                }
                return;
            case R.id.linearShare:
                loadeShare();
                return;
            case R.id.textPrivacyPolicy:
                loadPrivacy();
                break;
            case R.id.linearRateUs:
                loadLike();
                return;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!modelUtil.needPermissionCheck()) {
            MyApplication.getInstance().getFolderList();
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private void loadLike() {
        try {
            String urlStrRateUs;
            if (Constant.isSamsungApps(MainActivity.this)) {
                urlStrRateUs = "http://apps.samsung.com/appquery/appDetail.as?appId=" + getPackageName();
            } else {
                urlStrRateUs = "https://play.google.com/store/apps/details?id=" + getPackageName();
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlStrRateUs)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    private void loadeShare() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + "Try it Now " + "https://play.google.com/store/apps/details?id=" + getPackageName());
        shareIntent.setType("text/plain");
        if (Constant.isAvailable(shareIntent, MainActivity.this)) {
            startActivity(shareIntent);
        } else {
            Toast.makeText(MainActivity.this, "There is no app availalbe for this task", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPrivacy() {
        if (Constant.isConnected(MainActivity.this)) {
            Constant.privacyPolicy(MainActivity.this);
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVideoAlbumActivity() {
        startActivity(new Intent(this, VideoAlbumActivity.class));
    }

    private void loadImageSelectionActivity() {
        Intent intent = new Intent(this, ImageSelectionActivity.class);
        startActivity(intent);
    }

    private void loadGetMore() {
        if (Constant.isConnected(MainActivity.this)) {
            String urlStr = Constant.DEVELOPER_ACCOUNT_LINK;
            Intent inMoreapp = new Intent(Intent.ACTION_VIEW, Uri.parse(urlStr));
            if (urlStr != null && Constant.isAvailable(inMoreapp, MainActivity.this)) {
                startActivity(inMoreapp);
            } else {
                Toast.makeText(MainActivity.this, "There is no app availalbe for this task", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
