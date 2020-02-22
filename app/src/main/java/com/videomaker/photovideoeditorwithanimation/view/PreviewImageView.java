package com.videomaker.photovideoeditorwithanimation.view;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;

public class PreviewImageView extends AppCompatImageView {
    public static int mAspectRatioHeight = 360;
    public static int mAspectRatioWidth = 640;

    public PreviewImageView(Context context) {
        super(context);
    }

    public PreviewImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context, attrs);
    }

    public PreviewImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context, attrs);
    }

    private void Init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PreviewImageView);
        mAspectRatioWidth = a.getInt(R.styleable.PreviewImageView_mWidth, MyApplication.VIDEO_WIDTH);
        mAspectRatioHeight = a.getInt(R.styleable.PreviewImageView_mHeight, MyApplication.VIDEO_HEIGHT);
        Log.e("mAspectRatioWidth", "mAspectRatioWidth:" + mAspectRatioWidth + " mAspectRatioHeight:" + mAspectRatioHeight);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int finalWidth;
        int finalHeight;
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        int calculatedHeight = (int) (((float) (mAspectRatioHeight * originalWidth)) / ((float) mAspectRatioWidth));
        if (calculatedHeight > originalHeight) {
            finalWidth = (int) (((float) (mAspectRatioWidth * originalHeight)) / ((float) mAspectRatioHeight));
            finalHeight = originalHeight;
        } else {
            finalWidth = originalWidth;
            finalHeight = calculatedHeight;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
    }
}
