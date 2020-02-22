package com.videomaker.photovideoeditorwithanimation.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.activity.VideoCreateActivity;
import com.videomaker.photovideoeditorwithanimation.service.CreateImageService;
import com.videomaker.photovideoeditorwithanimation.themes.THEMES;
import com.example.ffmpeglibrary.videolib.FileUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoAnimationAdapter extends Adapter<VideoAnimationAdapter.Holder> {
    private MyApplication application = MyApplication.getInstance();
    private LayoutInflater inflater;
    private ArrayList<THEMES> list;
    private VideoCreateActivity previewActivity;

    class Holder extends ViewHolder {
        CheckBox cbSelect;
        private View clickableView;
        private ImageView ivThumb;
        View mainView;
        private TextView tvThemeName;

        Holder(View v) {
            super(v);
            this.cbSelect = v.findViewById(R.id.cbSelect);
            this.ivThumb = v.findViewById(R.id.ivThumb);
            this.tvThemeName = v.findViewById(R.id.tvThemeName);
            this.clickableView = v.findViewById(R.id.clickableView);
            this.mainView = v;
        }
    }

    public VideoAnimationAdapter(VideoCreateActivity previewActivity) {
        this.previewActivity = previewActivity;
        this.list = new ArrayList<>(Arrays.asList(THEMES.values()));
        this.inflater = LayoutInflater.from(previewActivity);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt) {
        return new Holder(this.inflater.inflate(R.layout.items_video_frame, paramViewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") final int pos) {
        THEMES themes = this.list.get(pos);
        Glide.with(this.application).load(themes.getThemeDrawable()).into(holder.ivThumb);
        holder.tvThemeName.setText(themes.toString());
        holder.cbSelect.setChecked(themes == this.application.selectedTheme);
        holder.clickableView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (list.get(pos) != application.selectedTheme) {
                    deleteThemeDir(application.selectedTheme.toString());
                    application.videoImages.clear();
                    application.selectedTheme = list.get(pos);
                    application.setCurrentTheme(application.selectedTheme.toString());
                    previewActivity.reset();
                    Intent intent = new Intent(application, CreateImageService.class);
                    intent.putExtra(CreateImageService.EXTRA_SELECTED_THEME, application.getCurrentTheme());
                    application.startService(intent);
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void deleteThemeDir(final String dir) {
        new Thread() {
            public void run() {
                FileUtils.deleteThemeDir(dir);
            }
        }.start();
    }

    public ArrayList<THEMES> getList() {
        return this.list;
    }

    public int getItemCount() {
        return this.list.size();
    }
}
