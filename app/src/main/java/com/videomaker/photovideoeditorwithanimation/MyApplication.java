package com.videomaker.photovideoeditorwithanimation;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;

import androidx.multidex.MultiDex;


import com.universevideomaker.BuildConfig;
import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.videomaker.photovideoeditorwithanimation.data.MusicData;
import com.videomaker.photovideoeditorwithanimation.receiver.OnProgressReceiver;
import com.videomaker.photovideoeditorwithanimation.themes.THEMES;
import com.videomaker.photovideoeditorwithanimation.util.PermissionModelUtil;
import com.example.ffmpeglibrary.videolib.FFmpeg;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("Registered")
public class MyApplication extends Application {
    public static int VIDEO_HEIGHT = 480;
    public static int VIDEO_WIDTH = 720;
    private static MyApplication instance;
    public static boolean isBreak = false;
    public HashMap<String, ArrayList<ImageData>> allAlbum;
    ArrayList<String> allFolder;
    int frame = -1;
    public boolean isEditModeEnable = false;
    public boolean isFromSdCardAudio = false;
    public int min_pos = Integer.MAX_VALUE;
    private MusicData musicData;
    private OnProgressReceiver onProgressReceiver;
    private float second = 2.0f;
    private String selectedFolderId = BuildConfig.FLAVOR;
    private final ArrayList<ImageData> selectedImages = new ArrayList<>();
    public THEMES selectedTheme = THEMES.Shine;
    public ArrayList<String> videoImages = new ArrayList<>();

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        init();

    }

    private void init() {
        if (!new PermissionModelUtil(this).needPermissionCheck()) {
            getFolderList();
        }
        try {
            loadLib();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadLib() {
        if (FFmpeg.getInstance(this).isSupported()) {
            // ffmpeg is supported
        } else {
            // ffmpeg is not supported
        }
    }

    public void initArray() {
        this.videoImages = new ArrayList<>();
    }

    public float getSecond() {
        return this.second;
    }

    public void setSecond(float second) {
        this.second = second;
    }

    public void setMusicData(MusicData musicData) {
        this.isFromSdCardAudio = false;
        this.musicData = musicData;
    }

    public MusicData getMusicData() {
        return this.musicData;
    }

    public void setSelectedFolderId(String selectedFolderId) {
        this.selectedFolderId = selectedFolderId;
    }

    public String getSelectedFolderId() {
        return this.selectedFolderId;
    }

    public HashMap<String, ArrayList<ImageData>> getAllAlbum() {
        return this.allAlbum;
    }

    public ArrayList<ImageData> getImageByAlbum(String folderId) {
        ArrayList<ImageData> imageDatas = getAllAlbum().get(folderId);
        if (imageDatas == null) {
            return new ArrayList<>();
        }
        return imageDatas;
    }

    public ArrayList<ImageData> getSelectedImages() {
        return this.selectedImages;
    }

    public void addSelectedImage(ImageData imageData) {
        this.selectedImages.add(imageData);
        imageData.imageCount++;
    }

    public void removeSelectedImage(int imageData) {
        if (imageData <= this.selectedImages.size()) {
            ImageData imageData2 = this.selectedImages.remove(imageData);
            imageData2.imageCount--;
        }
    }

    public void ReplaceSelectedImage(ImageData imageData, int pos) {
        this.selectedImages.set(pos, imageData);
    }

    public void getFolderList() {
        this.allFolder = new ArrayList<>();
        this.allAlbum = new HashMap<>();
        String[] projection = new String[]{Media.DATA, Media._ID, Media.BUCKET_DISPLAY_NAME, Media.BUCKET_ID, Media.DATE_TAKEN};
        @SuppressLint("Recycle")
        Cursor cur = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, projection, null, null, Media.BUCKET_DISPLAY_NAME + " DESC");
        assert cur != null;
        if (cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex(Media.BUCKET_DISPLAY_NAME);
            int bucketIdColumn = cur.getColumnIndex(Media.BUCKET_ID);
            int dateColumn = cur.getColumnIndex(Media.DATE_TAKEN);
            setSelectedFolderId(cur.getString(bucketIdColumn));
            do {
                ImageData data = new ImageData();
                data.imagePath = cur.getString(cur.getColumnIndex(Media.DATA));
                if (!data.imagePath.endsWith(".gif")) {
                    String date = cur.getString(dateColumn);
                    String folderName = cur.getString(bucketColumn);
                    String folderId = cur.getString(bucketIdColumn);
                    if (!this.allFolder.contains(folderId)) {
                        this.allFolder.add(folderId);
                    }
                    ArrayList<ImageData> imagePath = this.allAlbum.get(folderId);
                    if (imagePath == null) {
                        imagePath = new ArrayList<>();
                    }
                    data.folderName = folderName;
                    imagePath.add(data);
                    this.allAlbum.put(folderId, imagePath);
                }
            } while (cur.moveToNext());
        }
    }

    public void clearAllSelection() {
        this.videoImages.clear();
        this.allAlbum = null;
        getSelectedImages().clear();
        System.gc();
        getFolderList();
    }

    public void setCurrentTheme(String currentTheme) {
        Editor editor = getSharedPreferences("theme", 0).edit();
        editor.putString("current_theme", currentTheme);
        editor.commit();
    }

    public String getCurrentTheme() {
        return getSharedPreferences("theme", 0).getString("current_theme", THEMES.Shine.toString());
    }

    public void setOnProgressReceiver(OnProgressReceiver onProgressReceiver) {
        this.onProgressReceiver = onProgressReceiver;
    }

    public OnProgressReceiver getOnProgressReceiver() {
        return this.onProgressReceiver;
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) context.getSystemService(ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void setFrame(int data) {
        this.frame = data;
    }

    public int getFrame() {
        return this.frame;
    }
}
