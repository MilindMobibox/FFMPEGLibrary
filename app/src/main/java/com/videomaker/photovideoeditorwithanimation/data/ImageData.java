package com.videomaker.photovideoeditorwithanimation.data;

import androidx.annotation.NonNull;
import android.text.TextUtils;

public class ImageData {
    public String folderName;
    public long id;
    private String imageAlbum;
    public int imageCount = 0;
    public String imagePath;

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageAlbum() {
        return this.imageAlbum;
    }

    public void setImageAlbum(String imageAlbum) {
        this.imageAlbum = imageAlbum;
    }

    public int getImageCount() {
        return this.imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String toString() {
        if (TextUtils.isEmpty(this.imagePath)) {
            return super.toString();
        }
        return "ImageData { imagePath=" + this.imagePath + ",folderName=" + this.folderName + ",imageCount=" + this.imageCount + " }";
    }
}
