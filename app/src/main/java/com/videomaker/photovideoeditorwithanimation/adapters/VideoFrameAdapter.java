package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.activity.VideoCreateActivity;
import com.videomaker.photovideoeditorwithanimation.util.ScalingUtilities;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.bumptech.glide.Glide;

import java.io.FileOutputStream;

public class VideoFrameAdapter extends Adapter<VideoFrameAdapter.Holder> {
    private VideoCreateActivity activity;
    private MyApplication application;
    private int[] drawable = new int[]{R.drawable.no_frame, R.drawable.frame1, R.drawable.frame2, R.drawable.frame3,
            R.drawable.frame4, R.drawable.frame5, R.drawable.frame6, R.drawable.frame7, R.drawable.frame8,
            R.drawable.frame9, R.drawable.frame10, R.drawable.frame11, R.drawable.frame12, R.drawable.frame13,
            R.drawable.frame14, R.drawable.frame15,};
    private LayoutInflater inflater;
    private int lastPos = 0;

    class Holder extends ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        private ImageView ivThumb;
        View mainView;
        TextView tvThemeName;

        Holder(View v) {
            super(v);
            this.cbSelect = v.findViewById(R.id.cbSelect);
            this.ivThumb = v.findViewById(R.id.ivThumb);
            this.tvThemeName = v.findViewById(R.id.tvThemeName);
            this.clickableView = v.findViewById(R.id.clickableView);
            this.mainView = v;
        }
    }

    public VideoFrameAdapter(VideoCreateActivity activity) {
        this.activity = activity;
        this.application = MyApplication.getInstance();
        this.inflater = LayoutInflater.from(activity);
    }

    public int getItemCount() {
        return this.drawable.length;
    }

    private int getItem(int pos) {
        return this.drawable[pos];
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") final int pos) {
        final int themes = getItem(pos);
        holder.ivThumb.setScaleType(ScaleType.FIT_XY);
        Glide.with(this.application).load(themes).into(holder.ivThumb);
        holder.cbSelect.setChecked(themes == this.activity.getFrame());
        holder.tvThemeName.setVisibility(View.INVISIBLE);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                if (themes != activity.getFrame()) {
                    activity.setFrame(themes);
                    if (themes != R.drawable.no_frame) {
                        notifyItemChanged(lastPos);
                        notifyItemChanged(pos);
                        lastPos = pos;
                        FileUtils.deleteFile(FileUtils.frameFile);
                        try {
                            Bitmap bm = ScalingUtilities.scaleCenterCrop(BitmapFactory.decodeResource(activity.getResources(), themes), MyApplication.VIDEO_WIDTH, MyApplication.VIDEO_HEIGHT);
                            FileOutputStream outStream = new FileOutputStream(FileUtils.frameFile);
                            bm.compress(CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                            bm.recycle();
                            System.gc();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        notifyItemChanged(lastPos);
                        notifyItemChanged(pos);
                        lastPos = pos;
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        return new Holder(this.inflater.inflate(R.layout.items_video_frame, parent, false));
    }
}
