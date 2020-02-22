package com.videomaker.photovideoeditorwithanimation.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.receiver.OnProgressReceiver;
import com.videomaker.photovideoeditorwithanimation.activity.VideoShareActivity;
import com.videomaker.photovideoeditorwithanimation.util.ScalingUtilities;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.example.ffmpeglibrary.videolib.Util;

import androidx.core.app.NotificationCompat.Builder;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateVideoService extends IntentService {

    public static final int NOTIFICATION_ID = 1001;
    MyApplication application;
    private File audio_file;
    File audio_ip;
    private Builder builder;
    int last;
    private NotificationManager notify_manager;
    String time;
    private float toatal_second;
    String CHANNEL_ID = "createvideo";// The id of the channel.

    public CreateVideoService() {
        this(CreateVideoService.class.getName());
    }

    public CreateVideoService(String name) {
        super(name);
        this.time = "\\btime=\\b\\d\\d:\\d\\d:\\d\\d.\\d\\d";
        this.last = 0;
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        this.application = MyApplication.getInstance();
        this.notify_manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notify_manager.createNotificationChannel(mChannel);
        }
        this.builder = new Builder(this, CHANNEL_ID);
        this.builder.setContentTitle("Creating Video")
                .setContentText("Making in progress")
                .setSmallIcon(getNotificationIcon())
                .setOnlyAlertOnce(true);
        createVideo();

    }

    private void createVideo() {
        String[] inputCode;
        Log.e("create VIdeo", "True");
        long startTime = System.currentTimeMillis();
        this.toatal_second = (this.application.getSecond() * ((float) this.application.getSelectedImages().size())) - 1.0f;
        joinAudio();
        while (true) {
            if (CreateImageService.isImageComplate) {
                break;
            }
        }
        Log.e("createVideo", "video create start");
        File file = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        file.delete();
        for (int i = 0; i < this.application.videoImages.size(); i++) {
            Object[] objArr = new Object[]{this.application.videoImages.get(i)};
            Log.e("Video Name", "" + this.application.videoImages.get(0));
            appendVideoLog(String.format("file '%s'", objArr));
        }
        final String videoPath = new File(FileUtils.APP_DIRECTORY, getVideoName()).getAbsolutePath();
        if (this.application.getMusicData() == null) {
            Log.e("Music Null", "True");
//            inputCode = new String[]{FileUtils.getFFmpeg(this), "-r", (30.0f / this.application.getSecond()), "-f", "concat", "-i", r0.getAbsolutePath(), "-r", "30", "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", videoPath};

            inputCode = new String[16];
            FileUtils.frameFile.getAbsolutePath();
            inputCode[5] = "-i";
            inputCode[6] = file.getAbsolutePath();
            inputCode[7] = "-r";
            inputCode[8] = "30";
            inputCode[9] = "-c:v";
            inputCode[10] = "libx264";
            inputCode[11] = "-preset";
            inputCode[12] = "ultrafast";
            inputCode[13] = "-pix_fmt";
            inputCode[14] = "yuv420p";
            inputCode[15] = videoPath;
        } else if (this.application.getFrame() != -1) {
            Log.e("Frmae Added", "True");
            if (!FileUtils.frameFile.exists()) {
                try {
                    Log.e("Frmae exist", "False");
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), this.application.getFrame());
                    if (!(bm.getWidth() == MyApplication.VIDEO_WIDTH && bm.getHeight() == MyApplication.VIDEO_HEIGHT)) {
                        bm = ScalingUtilities.scaleCenterCrop(bm, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    }
                    OutputStream fileOutputStream = new FileOutputStream(FileUtils.frameFile);
                    bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    bm.recycle();
                    System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            inputCode = new String[]{FileUtils.getFFmpeg(this).getAbsolutePath(), "-r", String.valueOf(30.0f / this.application.getSecond()), "-f", "concat", "-safe", "0", "-i", file.getAbsolutePath(), "-i", FileUtils.frameFile.getAbsolutePath(), "-i", this.audio_file.getAbsolutePath(), "-filter_complex", "overlay= 0:0", "-strict", "experimental", "-r", String.valueOf(30.0f / this.application.getSecond()), "-t", String.valueOf(this.toatal_second), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
        } else {
            Log.e("Music Not Null", "True");
            inputCode = new String[]{FileUtils.getFFmpeg(this).getAbsolutePath(), "-r", String.valueOf(30.0f / this.application.getSecond()), "-f", "concat", "-safe", "0", "-i", file.getAbsolutePath(), "-i", this.audio_file.getAbsolutePath(), "-strict", "experimental", "-r", "30", "-t", String.valueOf(this.toatal_second), "-c:v", "libx264", "-preset", "ultrafast", "-pix_fmt", "yuv420p", "-ac", "2", videoPath};
        }
        System.gc();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(inputCode);
            while (!Util.isProcessCompleted(process)) {
                String line = new BufferedReader(new InputStreamReader(process.getErrorStream())).readLine();
                if (line != null) {
                    Log.e("process", line);
                    appendLog(line);
                    final int incr = durationToprogtess(line);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            OnProgressReceiver receiver = application.getOnProgressReceiver();
                            if (receiver != null) {
                                receiver.onVideoProgressFrameUpdate((float) incr);
                            }
                        }
                    });
                    this.builder.setProgress(100, ((int) ((75.0f * ((float) incr)) / 100.0f)) + 25, false);
                    this.notify_manager.notify(NOTIFICATION_ID, this.builder.build());
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            Util.destroyProcess(process);
        }
        this.builder.setContentText("Video created :" + FileUtils.getDuration(System.currentTimeMillis() - startTime)).setProgress(0, 0, false);
        this.notify_manager.notify(NOTIFICATION_ID, this.builder.build());
        try {
            long fileSize = new File(videoPath).length();
            String artist = getResources().getString(R.string.artist_name);
            ContentValues values = new ContentValues();
            values.put("_data", videoPath);
            values.put("_size", fileSize);
            values.put("mime_type", "video/mp4");
            values.put("artist", artist);
            values.put("duration", this.toatal_second * 1000.0f);
            getContentResolver().insert(MediaStore.Audio.Media.getContentUriForPath(videoPath), values);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(videoPath))));
        } catch (Exception e32) {
            e32.printStackTrace();
        }
        this.application.clearAllSelection();
        buildNotification(videoPath);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                OnProgressReceiver receiver = application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onVideoProgressFrameUpdate(100.0f);
                    receiver.onProgressFinish(videoPath);
                }
            }
        });
        FileUtils.deleteTempDir();
        stopSelf();
    }

    private void buildNotification(String videoPath) {
        Intent notificationIntent = new Intent(this, VideoShareActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationIntent.putExtra(Intent.EXTRA_TEXT, videoPath);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = getResources();
        Builder builder = new Builder(this, CHANNEL_ID);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("Video Created");
        Notification n = builder.build();
        n.defaults |= Notification.STREAM_DEFAULT;
        this.notify_manager.notify(NOTIFICATION_ID, n);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_white : R.drawable.logo_color;
    }

    private int durationToprogtess(String input) {
        int progress = 0;
        Matcher matcher = Pattern.compile(this.time).matcher(input);
        int HOUR = 60 * 60;
        if (TextUtils.isEmpty(input) || !input.contains("time=")) {
            Log.e("time", "not contain time " + input);
            return this.last;
        }
        while (matcher.find()) {
            String time = matcher.group();
            time = time.substring(time.lastIndexOf(61) + 1);
            String[] splitTime = time.split(":");
            Log.e("time", "totalSecond:" + time);
            float hour = ((Float.valueOf(splitTime[0]) * ((float) HOUR)) + (Float.valueOf(splitTime[1]) * ((float) 60))) + Float.valueOf(splitTime[2]);
            Log.e("time", "totalSecond:" + hour);
            progress = (int) ((100.0f * hour) / this.toatal_second);
            Log.i("time", "progress:" + progress);
        }
        this.last = progress;
        return progress;
    }

    private void joinAudio() {
        try {
            this.audio_ip = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
            this.audio_file = new File(FileUtils.APP_DIRECTORY, "audio.mp3");
            this.audio_file.delete();
            this.audio_ip.delete();
            int d = 0;
            appendLog("===============================================");
            appendAudioLog(String.format("file '%s'", this.application.getMusicData().track_data));
            Log.e("audio", 0 + " is D  " + this.toatal_second * 1000.0f + "___" + this.application.getMusicData().track_duration * ((long) d));
            appendLog(this.toatal_second * 1000.0f + "___" + this.application.getMusicData().track_duration * ((long) d));
            if (this.toatal_second * 1000.0f > ((float) (this.application.getMusicData().track_duration * ((long) d)))) {
                d = 0 + 1;
            }
            appendLog("Joid Audio");
            appendLog("===============================================");
            Process process = null;
            try {
                process = Runtime.getRuntime()
                        .exec(new String[]{FileUtils.getFFmpeg(this).getAbsolutePath(), "-f", "concat", "-safe", "0", "-i", this.audio_ip.getAbsolutePath(), "-c", "copy", "-preset", "ultrafast", "-ac", "2", this.audio_file.getAbsolutePath()});
                while (!Util.isProcessCompleted(process)) {
                    String line = new BufferedReader(new InputStreamReader(process.getErrorStream())).readLine();
                    if (line != null) {
                        appendLog(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("audio", "io", e);
            } finally {
                Util.destroyProcess(process);
            }
            appendLog("===============================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getVideoName() {
        return "video_" + new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ENGLISH).format(new Date()) + ".mp4";
    }

    public static void appendVideoLog(String text) {
        if (!FileUtils.TEMP_DIRECTORY.exists()) {
            FileUtils.TEMP_DIRECTORY.mkdirs();
        }
        File logFile = new File(FileUtils.TEMP_DIRECTORY, "video.txt");
        Log.d("FFMPEG", "File append " + text);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void appendLog(String text) {
    }

    public static void appendAudioLog(String text) {
        if (!FileUtils.TEMP_DIRECTORY.exists()) {
            FileUtils.TEMP_DIRECTORY.mkdirs();
        }
        File logFile = new File(FileUtils.TEMP_DIRECTORY, "audio.txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}
