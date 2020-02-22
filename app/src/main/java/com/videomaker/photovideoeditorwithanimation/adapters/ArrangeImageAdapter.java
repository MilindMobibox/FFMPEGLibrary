package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.activity.EditImageActivity;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.ArrayList;
import java.util.Collections;

public class ArrangeImageAdapter extends Adapter<ArrangeImageAdapter.Holder> {
    private final int TYPE_BLANK = 1;
    private final int TYPE_IMAGE = 0;
    private MyApplication application = MyApplication.getInstance();
    private OnItemClickListner<Object> clickListner;
    private RequestManager glide;
    private LayoutInflater inflater;
    private Activity activity;

    public class Holder extends ViewHolder {
        private View clickableView;
        private ImageView ivRemove;
        private ImageView ivThumb;
        View parent;
        ImageView ivEdit;

        Holder(View view) {
            super(view);
            this.parent = view;
            this.ivThumb = view.findViewById(R.id.ivThumb);
            this.ivRemove = view.findViewById(R.id.ivRemove);
            this.clickableView = view.findViewById(R.id.clickableView);
            this.ivEdit = view.findViewById(R.id.ivEdit);
        }

        public void onItemClick(View view, Object item) {
            if (clickListner != null) {
                clickListner.onItemClick(view, item);
            }
        }
    }

    public ArrangeImageAdapter(Activity activity) {
        this.inflater = LayoutInflater.from(activity);
        this.glide = Glide.with(activity);
        this.activity = activity;
    }

    public void setOnItemClickListner(OnItemClickListner<Object> clickListner) {
        this.clickListner = clickListner;
    }

    public int getItemCount() {
        return this.application.getSelectedImages().size();
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
        holder.parent.setVisibility(View.VISIBLE);
        final ImageData data = getItem(pos);
        this.glide.load(data.imagePath).into(holder.ivThumb);
        if (getItemCount() <= 2) {
            holder.ivRemove.setVisibility(View.GONE);
        } else {
            holder.ivRemove.setVisibility(View.VISIBLE);
        }
        holder.ivRemove.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                application.min_pos = Math.min(application.min_pos, Math.max(TYPE_IMAGE, pos - TYPE_BLANK));
                MyApplication.isBreak = true;
                application.removeSelectedImage(pos);
                if (clickListner != null) {
                    clickListner.onItemClick(v, data);
                }
                notifyDataSetChanged();
            }
        });
        holder.ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, EditImageActivity.class);
                i.putExtra("editImage", getItem(pos).getImagePath());
                i.putExtra("position", pos);
                activity.startActivityForResult(new Intent(i), 100);
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View v = this.inflater.inflate(R.layout.item_image_selected, parent, false);
        Holder holder = new Holder(v);
        if (getItemViewType(pos) == TYPE_BLANK) {
            v.setVisibility(View.INVISIBLE);
        } else {
            v.setVisibility(View.VISIBLE);
        }
        return holder;
    }

    public synchronized void swap(int fromPosition, int toPosition) {
        try {
            Collections.swap(this.application.getSelectedImages(), fromPosition, toPosition);
            notifyItemMoved(fromPosition, toPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
