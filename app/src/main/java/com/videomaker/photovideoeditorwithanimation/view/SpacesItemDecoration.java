package com.videomaker.photovideoeditorwithanimation.view;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import android.view.View;

public class SpacesItemDecoration extends ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
        outRect.left = this.space;
        outRect.right = this.space;
        outRect.bottom = this.space;
        LayoutManager manager = parent.getLayoutManager();
        int span = 1;
        if (manager instanceof GridLayoutManager) {
            span = ((GridLayoutManager) manager).getSpanCount();
        }
        if (parent.getChildLayoutPosition(view) < span) {
            outRect.top = this.space * 2;
        } else {
            outRect.top = 0;
        }
    }
}
