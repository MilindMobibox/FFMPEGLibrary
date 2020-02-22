package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.activity.VideoAlbumActivity;
import com.videomaker.photovideoeditorwithanimation.data.VideoData;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.ArrayList;

public class VideoAlbumAdapter extends Adapter<VideoAlbumAdapter.Holder> {
    private VideoAlbumActivity videoAlbumActivity;
    private ArrayList<VideoData> mVideoDatas;
    private OnItemClickListener listener;

    class Holder extends ViewHolder {
        private ImageView ivPreview;
        private ImageView imageMenu;
        private TextView tvDuration;
        private TextView tvFileName;
        private TextView tvVideoDate;

        Holder(View v) {
            super(v);
            this.ivPreview = v.findViewById(R.id.list_item_video_thumb);
            this.tvDuration = v.findViewById(R.id.list_item_video_duration);
            this.tvFileName = v.findViewById(R.id.list_item_video_title);
            this.tvVideoDate = v.findViewById(R.id.list_item_video_date);
            this.imageMenu = v.findViewById(R.id.imageMenu);
        }
    }

    public VideoAlbumAdapter(VideoAlbumActivity videoAlbumActivity, ArrayList<VideoData> mVideoDatas, OnItemClickListener listener) {
        this.mVideoDatas = mVideoDatas;
        this.videoAlbumActivity = videoAlbumActivity;
        this.listener = listener;
    }

    public int getItemCount() {
        return this.mVideoDatas.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, @SuppressLint("RecyclerView") final int pos) {
        holder.tvDuration.setText(FileUtils.getDuration(this.mVideoDatas.get(pos).videoDuration));
        Glide.with(this.videoAlbumActivity).load(this.mVideoDatas.get(pos).videoFullPath).into(holder.ivPreview);
        holder.tvFileName.setText(this.mVideoDatas.get(pos).videoName);
        holder.tvVideoDate.setText(DateFormat.getDateInstance().format(this.mVideoDatas.get(pos).dateTaken));
        holder.imageMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listener.onMenuItemClick(v, pos);
            }
        });
        holder.ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(pos);
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int arg1) {
        return new Holder(LayoutInflater.from(this.videoAlbumActivity).inflate(R.layout.item_video_album, parent, false));
    }

    public interface OnItemClickListener {
        void onMenuItemClick(View view, int position);

        void onItemClick(int position);
    }
}
