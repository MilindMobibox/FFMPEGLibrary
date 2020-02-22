package com.videomaker.photovideoeditorwithanimation.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {
    private AdapterDataObserver dataObserver;
    private View emptyView;

    public EmptyRecyclerView(Context context) {
        this(context, null);
    }

    public EmptyRecyclerView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public EmptyRecyclerView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.dataObserver = new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                Adapter<?> adapter = getAdapter();
                if (adapter != null && emptyView != null) {
                    if (adapter.getItemCount() == 0) {
                        emptyView.setVisibility(VISIBLE);
                        setVisibility(GONE);
                    } else {
                        emptyView.setVisibility(GONE);
                        setVisibility(VISIBLE);
                    }
                }
            }
        };
    }

    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.dataObserver);
        }
        this.dataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
