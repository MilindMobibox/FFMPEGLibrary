package com.videomaker.photovideoeditorwithanimation.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;

public class ProportionalRelativeLayout extends RelativeLayout {
    private float mProportion;

    @SuppressLint({"Recycle"})
    public ProportionalRelativeLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.mProportion = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ProportionalRelativeLayout)
                .getFloat(R.styleable.ProportionalRelativeLayout_scaleProportionalWidth, ((float) MyApplication.VIDEO_HEIGHT) / ((float) MyApplication.VIDEO_WIDTH));
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        int j = MeasureSpec.getSize(paramInt1);
        int i = MeasureSpec.getSize(paramInt2);
        j = MeasureSpec.makeMeasureSpec((int) Math.ceil((double) (this.mProportion * ((float) j))), MeasureSpec.getMode(paramInt2));
        if (i != 0) {
            i = MeasureSpec.makeMeasureSpec((int) Math.ceil((double) (((float) i) / this.mProportion)), MeasureSpec.getMode(paramInt2));
            if (paramInt2 > j) {
                super.onMeasure(paramInt1, j);
                return;
            } else {
                super.onMeasure(i, paramInt2);
                return;
            }
        }
        super.onMeasure(paramInt1, j);
    }
}
