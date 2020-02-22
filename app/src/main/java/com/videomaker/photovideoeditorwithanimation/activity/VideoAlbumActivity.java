package com.videomaker.photovideoeditorwithanimation.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ffmpeglibrary.videolib.FileUtils;
import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.adapters.VideoAlbumAdapter;
import com.videomaker.photovideoeditorwithanimation.data.VideoData;
import com.videomaker.photovideoeditorwithanimation.view.EmptyRecyclerView;
import com.videomaker.photovideoeditorwithanimation.view.SpacesItemDecoration;

import java.io.File;
import java.util.ArrayList;

public class VideoAlbumActivity extends BaseActivity {
    public static final String EXTRA_FROM_VIDEO = "EXTRA_FROM_VIDEO";
    boolean isFromVideo = false;
    VideoAlbumAdapter videoAlbumAdapter;
    private ArrayList<VideoData> mVideoDatas;
    private EmptyRecyclerView rvVideoAlbum;
    private Toolbar toolbar;
    LinearLayout linearAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromVideo = getIntent().hasExtra(EXTRA_FROM_VIDEO);
        setContentView(R.layout.activity_video_album);
        bindView();
        init();
        setUpRecyclerView();
        addListener();
        linearAds = findViewById(R.id.linearAds);

    }


    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void addListener() {
        findViewById(R.id.list_empty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isBreak = false;
                MyApplication.getInstance().setMusicData(null);
                Intent intent = new Intent(VideoAlbumActivity.this, ImageSelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    void viewPlayerActivity(int pos) {
        try {
            final Intent intent = new Intent(VideoAlbumActivity.this, VideoShareActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(Intent.EXTRA_TEXT, mVideoDatas.get(pos).videoFullPath);

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        rvVideoAlbum = findViewById(R.id.rvVideoAlbum);
    }

    private void init() {
        getVideoList();
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpRecyclerView() {
        rvVideoAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvVideoAlbum.setEmptyView(findViewById(R.id.list_empty));
        rvVideoAlbum.setItemAnimator(new DefaultItemAnimator());
        rvVideoAlbum.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen._5sdp)));
        videoAlbumAdapter = new VideoAlbumAdapter(VideoAlbumActivity.this, mVideoDatas, new VideoAlbumAdapter.OnItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int position) {
                PopupMenu popup = new PopupMenu(VideoAlbumActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.menu_video_local, popup.getMenu());
                if (mVideoDatas.size() > 0) {
                    popup.setOnMenuItemClickListener(new MyMenuItemClickListener(mVideoDatas.get(position)));
                    popup.show();
                }
            }

            @Override
            public void onItemClick(int position) {
                viewPlayerActivity(position);
            }
        });
        rvVideoAlbum.setAdapter(videoAlbumAdapter);
    }

    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        VideoData videoData;

        MyMenuItemClickListener(VideoData videoData) {
            this.videoData = videoData;
        }

        public boolean onMenuItemClick(MenuItem menuItem) {
            final int pos = mVideoDatas.indexOf(videoData);
            switch (menuItem.getItemId()) {
                case R.id.action_share_native:
                    File file = new File(mVideoDatas.get(pos).videoFullPath);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("video/*");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, mVideoDatas.get(pos).videoName);
                    shareIntent.putExtra(Intent.EXTRA_TITLE, mVideoDatas.get(pos).videoName);
                    Uri uriVideo = FileProvider.getUriForFile(VideoAlbumActivity.this, getResources().getString(R.string.file_provider_authority), file);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uriVideo);
                    startActivity(Intent.createChooser(shareIntent, "Share Video"));
                    return true;
                case R.id.action_delete:
                    final Dialog dialogCustomeMessage = new Dialog(VideoAlbumActivity.this, R.style.UploadDialog);
                    dialogCustomeMessage.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogCustomeMessage.setContentView(R.layout.custom_dialog_layout);
                    dialogCustomeMessage.setCancelable(true);
                    dialogCustomeMessage.show();

                    TextView txtTileDialog = dialogCustomeMessage.findViewById(R.id.textTitle);
                    TextView txtMessageDialog = dialogCustomeMessage.findViewById(R.id.textDesc);

                    TextView btnNegative = dialogCustomeMessage.findViewById(R.id.btnNegative);
                    TextView btnPositive = dialogCustomeMessage.findViewById(R.id.btnPositive);
                    LinearLayout linearAdsBanner = dialogCustomeMessage.findViewById(R.id.linearAdsBanner);

                    txtTileDialog.setText(getResources().getString(R.string.delete_video_title));
                    txtMessageDialog.setText("Are you sure to delete " + mVideoDatas.get(pos).videoName + " ?");
                    btnPositive.setText(getResources().getString(R.string.delete));
                    btnNegative.setText(getResources().getString(R.string.cancel));

                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCustomeMessage.dismiss();
                            FileUtils.deleteFile(new File(mVideoDatas.remove(pos).videoFullPath));
                            videoAlbumAdapter.notifyItemRemoved(pos);
                        }
                    });
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCustomeMessage.dismiss();
                        }
                    });
                    return true;
                default:
                    return false;
            }
        }
    }

    private void getVideoList() {
        mVideoDatas = new ArrayList<>();
        String[] projection = new String[]{MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.TITLE, MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.DISPLAY_NAME};
        @SuppressLint("Recycle")
        Cursor cur = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                "_data like '%" + FileUtils.APP_DIRECTORY.getAbsolutePath() + "%'", null,
                MediaStore.Video.Media.DATE_TAKEN + " DESC");
        assert cur != null;
        if (cur.moveToFirst()) {
            int bucketColumn = cur.getColumnIndex(MediaStore.Video.Media.DURATION);
            int data = cur.getColumnIndex(MediaStore.Video.Media.DATA);
            int name = cur.getColumnIndex(MediaStore.Video.Media.TITLE);
            int dateTaken = cur.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN);
            do {
                VideoData videoData = new VideoData();
                videoData.videoDuration = cur.getLong(bucketColumn);
                videoData.videoFullPath = cur.getString(data);
                videoData.videoName = cur.getString(name);
                videoData.dateTaken = cur.getLong(dateTaken);
                if (new File(videoData.videoFullPath).exists()) {
                    mVideoDatas.add(videoData);
                }
            } while (cur.moveToNext());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (isFromVideo) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
