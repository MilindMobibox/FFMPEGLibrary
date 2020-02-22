package com.videomaker.photovideoeditorwithanimation.activity;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.request.transition.Transition;
import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.adapters.VideoFrameAdapter;
import com.videomaker.photovideoeditorwithanimation.adapters.VideoAnimationAdapter;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.videomaker.photovideoeditorwithanimation.data.MusicData;
import com.videomaker.photovideoeditorwithanimation.service.CreateImageService;
import com.videomaker.photovideoeditorwithanimation.service.CreateVideoService;
import com.videomaker.photovideoeditorwithanimation.themes.THEMES;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.videomaker.photovideoeditorwithanimation.view.SeekbarWithIntervals;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VideoCreateActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private final int REQUEST_PICK_AUDIO = 101;
    private final int REQUEST_PICK_EDIT = 103;
    private final int REQUEST_PICK_IMAGES = 102;
    private MyApplication application;
    private ArrayList<ImageData> listImageSelected;
    private View linearLoading;
    VideoFrameAdapter videoFrameAdapter;
    private RequestManager requestManager;
    private Handler handler = new Handler();
    int sProgress = 0;
    boolean isFromTouch = false;
    private ImageView ivFrame;
    private ImageView imagePlayPause;
    private ImageView imagePreview;
    ArrayList<ImageData> lastSelectedData = new ArrayList<>();
    private LockRunnable lockRunnable = new LockRunnable();
    private MediaPlayer mPlayer;
    private RecyclerView rvFrame;
    private RecyclerView rvAnimation;
    private float seconds = 2.0f;
    private SeekBar seekBar;
    VideoAnimationAdapter videoAnimationAdapter;
    Toolbar toolbar;
    private TextView tvEndTime;
    private TextView tvTime;
    SeekbarWithIntervals seekbarWithIntervals;
    RelativeLayout relativeDuration;
    LinearLayout linearAds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        application = MyApplication.getInstance();
        application.videoImages.clear();
        MyApplication.isBreak = false;
        Intent intent = new Intent(getApplicationContext(), CreateImageService.class);
        intent.putExtra(CreateImageService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
        startService(intent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        bindView();
        init();
        addListner();

        linearAds = findViewById(R.id.linearAds);
    }





    @Override
    public void onPause() {

        super.onPause();
        lockRunnable.pause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void bindView() {
        linearLoading = findViewById(R.id.linearLoading);
        imagePreview = findViewById(R.id.imagePreview);
        ivFrame = findViewById(R.id.ivFrame);
        seekBar = findViewById(R.id.sbPlayTime);
        tvEndTime = findViewById(R.id.tvEndTime);
        tvTime = findViewById(R.id.tvTime);
        imagePlayPause = findViewById(R.id.imagePlayPause);
        toolbar = findViewById(R.id.toolbar);
        rvAnimation = findViewById(R.id.rvAnimation);
        seekbarWithIntervals = findViewById(R.id.seekbarWithIntervals);
        relativeDuration = findViewById(R.id.relativeDuration);
        rvFrame = findViewById(R.id.rvFrame);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        rvAnimation.setVisibility(View.VISIBLE);
        rvFrame.setVisibility(View.GONE);
        relativeDuration.setVisibility(View.GONE);

    }


    private void init() {
        seconds = application.getSecond();
        requestManager = Glide.with(this);
        application = MyApplication.getInstance();
        listImageSelected = application.getSelectedImages();
        seekBar.setMax((listImageSelected.size() - 1) * 30);
        int total = (int) (((float) (listImageSelected.size() - 1)) * seconds);
        tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", total / 60, total % 60));
        setUpThemeAdapter();
        if (application.getSelectedImages().size() > 0)
            requestManager.load(application.getSelectedImages().get(0).imagePath).into(imagePreview);
        setTheme();
        lockRunnable.play();
    }

    private void setUpThemeAdapter() {
        videoAnimationAdapter = new VideoAnimationAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        GridLayoutManager gridLayoutManagerFrame = new GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false);
        rvAnimation.setLayoutManager(gridLayoutManager);
        rvAnimation.setItemAnimator(new DefaultItemAnimator());
        rvAnimation.setAdapter(videoAnimationAdapter);
        videoFrameAdapter = new VideoFrameAdapter(this);
        rvFrame.setLayoutManager(gridLayoutManagerFrame);
        rvFrame.setItemAnimator(new DefaultItemAnimator());
        rvFrame.setAdapter(videoFrameAdapter);
    }

    public int getFrame() {
        return application.getFrame();
    }

    public void setFrame(int data) {
        if (data == -1) {
            ivFrame.setImageDrawable(null);
        } else {
            ivFrame.setImageResource(data);
        }
        application.setFrame(data);
    }

    public void setTheme() {
        if (application.isFromSdCardAudio) {
            lockRunnable.play();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    THEMES themes = application.selectedTheme;
                    try {
                        FileUtils.TEMP_DIRECTORY_AUDIO.mkdirs();
                        FileUtils.APP_DIRECTORY.mkdir();
                        File tempFile = new File(FileUtils.TEMP_DIRECTORY_AUDIO, "temp.mp3");
                        if (tempFile.exists()) {
                            FileUtils.deleteFile(tempFile);
                        }
                        InputStream in = getResources().openRawResource(themes.getThemeMusic());
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
                        final MusicData musicData = new MusicData();
                        musicData.track_data = tempFile.getAbsolutePath();
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            public void onPrepared(MediaPlayer mp) {
                                musicData.track_duration = (long) mp.getDuration();
                                mp.stop();
                            }
                        });
                        musicData.track_Title = "temp";
                        application.setMusicData(musicData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            reinitMusic();
                            lockRunnable.play();
                        }
                    });
                }
            }).start();
        }
    }

    private void addListner() {
        findViewById(R.id.ibAddImages).setOnClickListener(this);
        findViewById(R.id.video_clicker).setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        findViewById(R.id.ibAddMusic).setOnClickListener(this);
        findViewById(R.id.ibAddDuration).setOnClickListener(this);
        findViewById(R.id.ibEditMode).setOnClickListener(this);
        findViewById(R.id.ibAnimation).setOnClickListener(this);
        findViewById(R.id.ibFrame).setOnClickListener(this);
        List<String> listDuration = new ArrayList<>();
        listDuration.add("1");
        listDuration.add("1.5");
        listDuration.add("2");
        listDuration.add("2.5");
        listDuration.add("3");
        listDuration.add("3.5");
        listDuration.add("4");

        seekbarWithIntervals.setIntervals(listDuration);
//        seekbarWithIntervals.getSeekbar().setMax(4);
        seekbarWithIntervals.getSeekbar().setProgress(2);
        seekbarWithIntervals.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float selectDuration = 2.0f;
                if (seekBar.getProgress() == 0) {
                    selectDuration = 1.0f;
                } else if (seekBar.getProgress() == 1) {
                    selectDuration = 1.5f;
                } else if (seekBar.getProgress() == 2) {
                    selectDuration = 2.0f;
                } else if (seekBar.getProgress() == 3) {
                    selectDuration = 2.5f;
                } else if (seekBar.getProgress() == 4) {
                    selectDuration = 3.0f;
                } else if (seekBar.getProgress() == 5) {
                    selectDuration = 3.5f;
                } else if (seekBar.getProgress() == 6) {
                    selectDuration = 4.0f;
                }
                Log.e("Duration", "===" + selectDuration);
                if (selectDuration != seconds) {
                    seconds = selectDuration;
                    application.setSecond(seconds);
                    lockRunnable.play();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_clicker:
                if (lockRunnable.isPause()) {
                    lockRunnable.play();
                    return;
                } else {
                    lockRunnable.pause();
                    return;
                }
            case R.id.ibAddImages:
                linearLoading.setVisibility(View.GONE);
                MyApplication.isBreak = true;
                application.isEditModeEnable = true;
                lastSelectedData.clear();
                lastSelectedData.addAll(listImageSelected);
                Intent intent = new Intent(this, ImageSelectionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                intent.putExtra(ImageSelectionActivity.EXTRA_FROM_PREVIEW, true);
                startActivityForResult(intent, REQUEST_PICK_IMAGES);
                return;
            case R.id.ibEditMode:
                linearLoading.setVisibility(View.GONE);
                application.isEditModeEnable = true;
                lockRunnable.pause();
                startActivityForResult(new Intent(this, ImageArrangeActivity.class).putExtra(ImageSelectionActivity.EXTRA_FROM_PREVIEW, true), REQUEST_PICK_EDIT);
                return;
            case R.id.ibAnimation:

                rvAnimation.setVisibility(View.VISIBLE);
                rvFrame.setVisibility(View.GONE);
                relativeDuration.setVisibility(View.GONE);

                break;
            case R.id.ibFrame:

                rvAnimation.setVisibility(View.GONE);
                rvFrame.setVisibility(View.VISIBLE);
                relativeDuration.setVisibility(View.GONE);

                break;
            case R.id.ibAddMusic:
                linearLoading.setVisibility(View.GONE);
                startActivityForResult(new Intent(this, MusicListActivity.class), REQUEST_PICK_AUDIO);
                return;
            case R.id.ibAddDuration:
                rvAnimation.setVisibility(View.GONE);
                rvFrame.setVisibility(View.GONE);
                relativeDuration.setVisibility(View.VISIBLE);
                return;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        menu.removeItem(R.id.menu_clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_done:
                handler.removeCallbacks(lockRunnable);
                startService(new Intent(this, CreateVideoService.class));
                viewProgressActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void viewProgressActivity() {
        final Intent intent = new Intent(application, ProgressVideoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        application.isEditModeEnable = false;
        if (resultCode == RESULT_OK) {
            int total;
            Intent intent;
            switch (requestCode) {
                case REQUEST_PICK_AUDIO:
                    application.isFromSdCardAudio = true;
                    sProgress = 0;
                    reinitMusic();
                    return;
                case REQUEST_PICK_IMAGES:
                    if (isNeedRestart()) {
                        Log.e("isNeedRestart", "isNeedRestart true");
                        stopService(new Intent(getApplicationContext(), CreateImageService.class));
                        lockRunnable.stop();
                        seekBar.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MyApplication.isBreak = false;
                                application.videoImages.clear();
                                application.min_pos = Integer.MAX_VALUE;
                                Intent intent = new Intent(getApplicationContext(), CreateImageService.class);
                                intent.putExtra(CreateImageService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                                startService(intent);
                            }
                        }, 1000);
                        total = (int) (((float) (listImageSelected.size() - 1)) * seconds);
                        listImageSelected = application.getSelectedImages();
                        seekBar.setMax((application.getSelectedImages().size() - 1) * 30);
                        tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", total / 60, total % 60));
                        return;
                    }
                    if (CreateImageService.isImageComplate) {
                        Log.e("isNeedRestart", "Service complate");
                        MyApplication.isBreak = false;
                        application.videoImages.clear();
                        application.min_pos = Integer.MAX_VALUE;
                        intent = new Intent(getApplicationContext(), CreateImageService.class);
                        intent.putExtra(CreateImageService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                        startService(intent);
                        sProgress = 0;
                        seekBar.setProgress(0);
                    }
                    total = (int) (((float) (listImageSelected.size() - 1)) * seconds);
                    listImageSelected = application.getSelectedImages();
                    seekBar.setMax((application.getSelectedImages().size() - 1) * 30);
                    tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", total / 60, total % 60));
                    return;
                case REQUEST_PICK_EDIT:
                    lockRunnable.stop();
                    if (CreateImageService.isImageComplate || !MyApplication.isMyServiceRunning(application, CreateImageService.class)) {
                        MyApplication.isBreak = false;
                        application.videoImages.clear();
                        application.min_pos = Integer.MAX_VALUE;
                        intent = new Intent(getApplicationContext(), CreateImageService.class);
                        intent.putExtra(CreateImageService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                        startService(intent);
                    }
                    sProgress = 0;
                    seekBar.setProgress(sProgress);
                    listImageSelected = application.getSelectedImages();
                    total = (int) (((float) (listImageSelected.size() - 1)) * seconds);
                    seekBar.setMax((application.getSelectedImages().size() - 1) * 30);
                    tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", total / 60, total % 60));
                    return;
                default:
                    break;
            }
        }
    }

    private boolean isNeedRestart() {
        if (lastSelectedData.size() > application.getSelectedImages().size()) {
            MyApplication.isBreak = true;
            Log.e("isNeedRestart", "isNeedRestart size");
            return true;
        }
        int i = 0;
        while (i < lastSelectedData.size()) {
            Log.e("isNeedRestart", lastSelectedData.get(i).imagePath + "___ " + application.getSelectedImages().get(i).imagePath);
            if (lastSelectedData.get(i).imagePath.equals(application.getSelectedImages().get(i).imagePath)) {
                i++;
            } else {
                MyApplication.isBreak = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sProgress = progress;
        if (isFromTouch) {
            seekBar.setProgress(Math.min(progress, seekBar.getSecondaryProgress()));
            displayImage();
            seekMediaPlayer();
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isFromTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isFromTouch = false;
    }

    private void seekMediaPlayer() {
        if (mPlayer != null) {
            try {
                mPlayer.seekTo(((int) (((((float) sProgress) / 30.0f) * seconds) * 1000.0f)) % mPlayer.getDuration());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        MyApplication.isBreak = false;
        application.videoImages.clear();
        handler.removeCallbacks(lockRunnable);
        lockRunnable.stop();
        Glide.get(this).clearMemory();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(VideoCreateActivity.this).clearDiskCache();
            }
        }).start();
        FileUtils.deleteTempDir();
        requestManager = Glide.with(this);
        linearLoading.setVisibility(View.VISIBLE);
        setTheme();
    }

    @Override
    public void onBackPressed() {
        onBackDialog();
    }

    private void onBackDialog() {
        final Dialog dialogCustomeMessage = new Dialog(VideoCreateActivity.this, R.style.UploadDialog);
        dialogCustomeMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCustomeMessage.setContentView(R.layout.custom_dialog_layout);
        dialogCustomeMessage.setCancelable(true);
        dialogCustomeMessage.show();

        TextView txtTileDialog = dialogCustomeMessage.findViewById(R.id.textTitle);
        TextView txtMessageDialog = dialogCustomeMessage.findViewById(R.id.textDesc);

        TextView btnNegative = dialogCustomeMessage.findViewById(R.id.btnNegative);
        TextView btnPositive = dialogCustomeMessage.findViewById(R.id.btnPositive);
        LinearLayout linearAdsBanner = dialogCustomeMessage.findViewById(R.id.linearAdsBanner);
        txtTileDialog.setText("Alert");
        txtMessageDialog.setText("Are you sure ? \nYour video is not prepared yet!");
        btnPositive.setText("Go Back");
        btnNegative.setText("Stay here");

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomeMessage.dismiss();
                application.videoImages.clear();
                MyApplication.isBreak = true;
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(1001);
                finish();
            }
        });
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogCustomeMessage.dismiss();
            }
        });
    }

    class LockRunnable implements Runnable {
        boolean isPause = false;

        @Override
        public void run() {
            displayImage();
            if (!isPause) {
                handler.postDelayed(lockRunnable, (long) Math.round(50.0f * seconds));
            }
        }

        boolean isPause() {
            return isPause;
        }

        void play() {
            isPause = false;
            playMusic();
            handler.postDelayed(lockRunnable, (long) Math.round(50.0f * seconds));
            Animation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    imagePlayPause.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    imagePlayPause.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            imagePlayPause.startAnimation(animation);
        }

        void pause() {
            isPause = true;
            pauseMusic();
            Animation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(500);
            animation.setFillAfter(true);
            imagePlayPause.startAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    imagePlayPause.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        void stop() {
            pause();
            sProgress = 0;
            if (mPlayer != null) {
                mPlayer.stop();
            }
            reinitMusic();
            seekBar.setProgress(sProgress);
        }
    }

    private synchronized void displayImage() {
        try {
            if (sProgress >= seekBar.getMax()) {
                sProgress = 0;
                lockRunnable.stop();
            } else {
                if (sProgress > 0 && linearLoading.getVisibility() == View.VISIBLE) {
                    linearLoading.setVisibility(View.GONE);
                    if (!(mPlayer == null || mPlayer.isPlaying())) {
                        mPlayer.start();
                    }
                }
                seekBar.setSecondaryProgress(application.videoImages.size());
                if (seekBar.getProgress() < seekBar.getSecondaryProgress()) {
                    sProgress %= application.videoImages.size();
                    requestManager.asBitmap().load(application.videoImages.get(sProgress))
                            .signature(new MediaStoreSignature("image/*", System.currentTimeMillis(), 0))
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .into((Target<Bitmap>) new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imagePreview.setImageBitmap(resource);
                                }
                            });
                    sProgress++;
                    if (!isFromTouch) {
                        seekBar.setProgress(sProgress);
                    }
                    int j = (int) ((((float) sProgress) / 30.0f) * seconds);
                    int mm = j / 60;
                    int ss = j % 60;
                    tvTime.setText(String.format(Locale.getDefault(), "%02d:%02d", mm, ss));
                    int total = (int) (((float) (listImageSelected.size() - 1)) * seconds);
                    tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", total / 60, total % 60));
                }
            }
        } catch (Exception e) {
            requestManager = Glide.with(this);
        }
    }

    private void reinitMusic() {
        try {
            MusicData musicData = application.getMusicData();
            if (musicData != null) {
                Log.e("Mediaplayer", "create");
                MediaPlayer create = MediaPlayer.create(this, Uri.parse(musicData.track_data));
                mPlayer = create;
                create.setLooping(true);
                try {
                    Log.e("Mediaplayer", "Prepare");
                    mPlayer.prepare();
                    return;
                } catch (IllegalStateException ex) {
                    Log.e("Mediaplayer", "Prepare Illegal");
                    ex.printStackTrace();
                    return;
                } catch (IOException musicData1) {
                    Log.e("Mediaplayer", "Prepare IOExc");
                    musicData1.printStackTrace();
                    return;
                }
            }
            Log.e("Music data", "null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playMusic() {
        if (linearLoading.getVisibility() != View.VISIBLE && mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    private void pauseMusic() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }
}
