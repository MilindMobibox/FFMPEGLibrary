package com.videomaker.photovideoeditorwithanimation.imageedit;

import android.app.Activity;
import android.graphics.BitmapFactory;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.imageedit.filters.IF1977Filter;
import com.videomaker.photovideoeditorwithanimation.imageedit.filters.IFBrannanFilter;
import com.videomaker.photovideoeditorwithanimation.imageedit.filters.IFInkwellFilter;
import com.videomaker.photovideoeditorwithanimation.imageedit.filters.IFSierraFilter;
import com.videomaker.photovideoeditorwithanimation.imageedit.filters.IFToasterFilter;

import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

public class DataBinder {
    public static ArrayList<Filter> fetchFilters() {
        ArrayList<Filter> filterArrayList = new ArrayList<>();
        filterArrayList.add(new Filter(R.drawable.filter_1, "Original"));
        filterArrayList.add(new Filter(R.drawable.filter_2, "Tropic"));
        filterArrayList.add(new Filter(R.drawable.filter_3, "Valencia"));
        filterArrayList.add(new Filter(R.drawable.filter_5, "B&W"));
        filterArrayList.add(new Filter(R.drawable.filter_6, "Lomo"));
        filterArrayList.add(new Filter(R.drawable.filter_7, "Autumn"));
        filterArrayList.add(new Filter(R.drawable.filter_9, "Elegance"));
        filterArrayList.add(new Filter(R.drawable.filter_10, "Mellow"));
        filterArrayList.add(new Filter(R.drawable.filter_11, "Time"));
        filterArrayList.add(new Filter(R.drawable.filter_12, "Earlybird"));
        filterArrayList.add(new Filter(R.drawable.filter_13, "Dark"));
        filterArrayList.add(new Filter(R.drawable.filter_14, "Retro"));
        filterArrayList.add(new Filter(R.drawable.filter_15, "Twilight"));
        filterArrayList.add(new Filter(R.drawable.filter_16, "Inkwell"));
        filterArrayList.add(new Filter(R.drawable.filter_17, "Rise"));
        filterArrayList.add(new Filter(R.drawable.filter_18, "Myth"));
        filterArrayList.add(new Filter(R.drawable.filter_19, "Soft"));
        filterArrayList.add(new Filter(R.drawable.filter_20, "Sweet"));
        filterArrayList.add(new Filter(R.drawable.filter_21, "Forest"));
        return filterArrayList;
    }

    public static GPUImageFilter applyFilter(int position, Activity activity) {
        GPUImageLookupFilter gpuImageLookupFilter = new GPUImageLookupFilter();
        switch (position) {
            case 0:
                return new GPUImageFilter();
            case 1:
                return new IF1977Filter(activity);
            case 2:
                return new IFBrannanFilter(activity);
            case 3:
                return new IFInkwellFilter(activity);
            case 4:
                return new IFSierraFilter(activity);
            case 5:
                return new IFToasterFilter(activity);
            case 6:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf2));
                return gpuImageLookupFilter;
            case 7:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf3));
                return gpuImageLookupFilter;
            case 8:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf6));
                return gpuImageLookupFilter;
            case 9:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf8));
                return gpuImageLookupFilter;
            case 10:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf10));
                return gpuImageLookupFilter;
            case 11:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf11));
                return gpuImageLookupFilter;
            case 12:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf12));
                return gpuImageLookupFilter;
            case 13:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf14));
                return gpuImageLookupFilter;
            case 14:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf17));
                return gpuImageLookupFilter;
            case 15:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf18));
                return gpuImageLookupFilter;
            case 16:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf24));
                return gpuImageLookupFilter;
            case 17:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf28));
                return gpuImageLookupFilter;
            case 18:
                gpuImageLookupFilter.setBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.pf32));
                return gpuImageLookupFilter;
            default:
                return null;
        }
    }
}
