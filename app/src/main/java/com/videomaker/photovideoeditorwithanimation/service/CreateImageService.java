package com.videomaker.photovideoeditorwithanimation.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.core.app.NotificationCompat.Builder;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.videomaker.photovideoeditorwithanimation.mask.FinalMaskBitmap;
import com.videomaker.photovideoeditorwithanimation.receiver.OnProgressReceiver;
import com.videomaker.photovideoeditorwithanimation.util.ScalingUtilities;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class CreateImageService extends IntentService {
    public static final String EXTRA_SELECTED_THEME = "selected_theme";
    public static boolean isImageComplate = false;
    MyApplication application;
    ArrayList<ImageData> arrayList;
    private Builder mBuilder;
    private NotificationManager mNotifyManager;
    private String selectedTheme;
    int totalImages;

    String CHANNEL_ID = "createimage";// The id of the channel.

    public CreateImageService() {
        this(CreateImageService.class.getName());
    }

    public CreateImageService(String name) {
        super(name);
    }

    public void onCreate() {
        super.onCreate();
        this.application = MyApplication.getInstance();
    }

    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void onHandleIntent(Intent intent) {
        this.mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            mNotifyManager.createNotificationChannel(mChannel);
        }
        this.mBuilder = new Builder(this, CHANNEL_ID);
        this.mBuilder.setContentTitle("Preparing Video")
                .setContentText("Making in progress")
                .setSmallIcon(getNotificationIcon())
                .setOnlyAlertOnce(true);
        this.selectedTheme = intent.getStringExtra(EXTRA_SELECTED_THEME);
        this.arrayList = this.application.getSelectedImages();
        this.application.initArray();
        isImageComplate = false;
        createImages();
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_white : R.drawable.logo_color;
    }

    private void createImages() {
        Bitmap newSecondBmp2 = null;
        this.totalImages = this.arrayList.size();
        int i = 0;
        while (i < this.arrayList.size() - 1 && isSameTheme() && !MyApplication.isBreak) {
            Bitmap newFirstBmp;
            File imgDir = FileUtils.getImageDirectory(this.application.selectedTheme.toString(), i);
            Bitmap firstBitmap;
            Bitmap temp;
            if (i == 0) {
                firstBitmap = ScalingUtilities.checkBitmap(this.arrayList.get(i).imagePath);
                assert firstBitmap != null;
                temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                newFirstBmp = ScalingUtilities.ConvetrSameSize(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                temp.recycle();
                firstBitmap.recycle();
                System.gc();
            } else {
                if (newSecondBmp2 == null || newSecondBmp2.isRecycled()) {
                    firstBitmap = ScalingUtilities.checkBitmap(this.arrayList.get(i).imagePath);
                    assert firstBitmap != null;
                    temp = ScalingUtilities.scaleCenterCrop(firstBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                    newSecondBmp2 = ScalingUtilities.ConvetrSameSize(firstBitmap, temp, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
                    temp.recycle();
                    firstBitmap.recycle();
                }
                newFirstBmp = newSecondBmp2;
            }
            Bitmap secondBitmap = ScalingUtilities.checkBitmap(this.arrayList.get(i + 1).imagePath);
            assert secondBitmap != null;
            Bitmap temp2 = ScalingUtilities.scaleCenterCrop(secondBitmap, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
            newSecondBmp2 = ScalingUtilities.ConvetrSameSize(secondBitmap, temp2, MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, 1.0f, 0.0f);
            temp2.recycle();
            secondBitmap.recycle();
            System.gc();
            FinalMaskBitmap.reintRect();
            FinalMaskBitmap.EFFECT effect = this.application.selectedTheme.getTheme().get(i % this.application.selectedTheme.getTheme().size());
//            Bitmap logo = ((BitmapDrawable) getResources().getDrawable(R.drawable.appicon)).getBitmap();
            for (int j = 0; ((float) j) < FinalMaskBitmap.ANIMATED_FRAME && isSameTheme() && !MyApplication.isBreak; j++) {
                Bitmap bmp = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Paint paint = new Paint(1);
                paint.setXfermode(new PorterDuffXfermode(Mode.DST_OUT));
                Canvas canvas = new Canvas(bmp);
                canvas.drawBitmap(newFirstBmp, 0.0f, 0.0f, null);
                canvas.drawBitmap(effect.getMask(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, j), 0.0f, 0.0f, paint);
                Bitmap bitmap3 = Bitmap.createBitmap(MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT, Config.ARGB_8888);
                Canvas canvas2 = new Canvas(bitmap3);
                canvas2.drawBitmap(newSecondBmp2, 0.0f, 0.0f, null);
                canvas2.drawBitmap(bmp, 0.0f, 0.0f, new Paint());
                if (isSameTheme()) {
                    File file2 = new File(imgDir, String.format(Locale.getDefault(), "img%02d.jpg", j));
                    try {
                        if (file2.exists()) {
                            file2.delete();
                        }
                        OutputStream fileOutputStream = new FileOutputStream(file2);
                        bitmap3.compress(CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    boolean isBreak = false;
                    while (this.application.isEditModeEnable) {
                        if (this.application.min_pos != Integer.MAX_VALUE) {
                            i = this.application.min_pos;
                            isBreak = true;
                        }
                        if (MyApplication.isBreak) {
                            isImageComplate = true;
                            stopSelf();
                            return;
                        }
                    }
                    if (isBreak) {
                        ArrayList<String> str = new ArrayList<>(this.application.videoImages);
                        this.application.videoImages.clear();
                        int size = Math.min(str.size(), Math.max(0, i - i) * 30);
                        for (int p = 0; p < size; p++) {
                            this.application.videoImages.add(str.get(p));
                        }
                        this.application.min_pos = Integer.MAX_VALUE;
                    }
                    if (!isSameTheme() || MyApplication.isBreak) {
                        break;
                    }
                    this.application.videoImages.add(file2.getAbsolutePath());
                    if (((float) j) == FinalMaskBitmap.ANIMATED_FRAME - 1.0f) {
                        for (int m = 0; m < 8 && isSameTheme() && !MyApplication.isBreak; m++) {
                            this.application.videoImages.add(file2.getAbsolutePath());
                        }
                    }
                    calculateProgress(i, j);
                }
            }
            i++;
        }
        Glide.get(this).clearDiskCache();
        isImageComplate = true;
        stopSelf();
        isSameTheme();
    }

    private boolean isSameTheme() {
        return this.selectedTheme.equals(this.application.getCurrentTheme());
    }

    private void calculateProgress(int i, int j) {
        final int progress = (int) ((100.0f * ((float) this.application.videoImages.size())) / ((float) ((this.totalImages - 1) * 30)));
        updateNotification(progress);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                OnProgressReceiver receiver = application.getOnProgressReceiver();
                if (receiver != null) {
                    receiver.onImageProgressFrameUpdate(progress);
                }
            }
        });
    }

    private void updateNotification(int progress) {
        this.mBuilder.setProgress(100, (int) ((25.0f * ((float) progress)) / 100.0f), false);
        this.mNotifyManager.notify(1001, this.mBuilder.build());
    }
}
