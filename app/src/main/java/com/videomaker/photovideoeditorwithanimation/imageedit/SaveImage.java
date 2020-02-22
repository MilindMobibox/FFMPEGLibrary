package com.videomaker.photovideoeditorwithanimation.imageedit;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.MediaStore.Files;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SaveImage {
    public static void deleteFileFromMediaStore(ContentResolver contentResolver, File file) {
        String canonicalPath;
        try {
            canonicalPath = file.getCanonicalPath();
        } catch (IOException e) {
            canonicalPath = file.getAbsolutePath();
        }
        Uri uri = Files.getContentUri("external");
        if (contentResolver.delete(uri, "_data=?", new String[]{canonicalPath}) == 0 && !file.getAbsolutePath().equals(canonicalPath)) {
            contentResolver.delete(uri, "_data=?", new String[]{file.getAbsolutePath()});
        }
    }

    public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {
                copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
            return;
        }
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);
        byte[] buf = new byte[1024];
        while (true) {
            int len = in.read(buf);
            if (len > 0) {
                out.write(buf, 0, len);
            } else {
                in.close();
                out.close();
                return;
            }
        }
    }
}
