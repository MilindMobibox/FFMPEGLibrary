package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.activity.ImageSelectionActivity;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;

public class ImageSelectionAdapter extends Adapter<ImageSelectionAdapter.Holder> {
    private final int TYPE_BLANK = 1;
    private final int TYPE_IMAGE = 0;
    private ImageSelectionActivity activity;
    private MyApplication application;
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;
    public boolean isExpanded = false;

    public class Holder extends ViewHolder {
        View clickableView;
        private ImageView ivRemove;
        private ImageView ivThumb;
        View parent;

        Holder(View v) {
            super(v);
            this.parent = v;
            this.ivThumb = v.findViewById(R.id.ivThumb);
            this.ivRemove = v.findViewById(R.id.ivRemove);
            this.clickableView = v.findViewById(R.id.clickableView);
        }

        public void onItemClick(View view, Object item) {
            if (clickListner != null) {
                clickListner.onItemClick(view, item);
            }
        }
    }

    public ImageSelectionAdapter(ImageSelectionActivity activity) {
        this.activity = activity;
        this.application = MyApplication.getInstance();
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        ArrayList<ImageData> list = this.application.getSelectedImages();
        if (this.isExpanded) {
            return list.size();
        }
        return list.size() + 20;
    }

    public int getItemViewType(int position) {
        super.getItemViewType(position);
        if (!this.isExpanded && position >= this.application.getSelectedImages().size()) {
            return TYPE_BLANK;
        }
        return TYPE_IMAGE;
    }

    private boolean hideRemoveBtn() {
        return this.application.getSelectedImages().size() <= 3 && this.activity.isFromPreview;
    }

    private ImageData getItem(int pos) {
        ArrayList<ImageData> list = this.application.getSelectedImages();
        if (list.size() <= pos) {
            return new ImageData();
        }
        return list.get(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") final int pos) {
        if (getItemViewType(pos) == TYPE_BLANK) {
            holder.parent.setVisibility(View.INVISIBLE);
            return;
        }
        holder.parent.setVisibility(View.VISIBLE);
        final ImageData data = getItem(pos);
        this.glide.load(data.imagePath).into(holder.ivThumb);
        if (hideRemoveBtn()) {
            holder.ivRemove.setVisibility(View.GONE);
        } else {
            holder.ivRemove.setVisibility(View.VISIBLE);
        }
        holder.ivRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int index = application.getSelectedImages().indexOf(data);
                if (activity.isFromPreview) {
                    application.min_pos = Math.min(application.min_pos, Math.max(TYPE_IMAGE, pos - TYPE_BLANK));
                }
                application.removeSelectedImage(pos);
                if (clickListner != null) {
                    clickListner.onItemClick(v, data);
                }
                if (hideRemoveBtn()) {
                    Toast.makeText(activity, "At least 3 images require \nif you want to remove this images than add more images.", Toast.LENGTH_LONG).show();
                }
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View v = this.inflater.inflate(R.layout.item_image_selection, parent, false);
        Holder holder = new Holder(v);
        if (getItemViewType(pos) == TYPE_BLANK) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
        return holder;
    }
}
