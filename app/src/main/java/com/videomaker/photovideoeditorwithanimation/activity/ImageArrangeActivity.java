package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.adapters.ArrangeImageAdapter;
import com.videomaker.photovideoeditorwithanimation.data.ImageData;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.videomaker.photovideoeditorwithanimation.view.EmptyRecyclerView;

public class ImageArrangeActivity extends AppCompatActivity {

    private MyApplication application;
    private ArrangeImageAdapter arrangeImageAdapter;
    public boolean isFromPreview = false;
    private EmptyRecyclerView rvSelectedImages;
    private Toolbar toolbar;
    LinearLayout linearAds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_arrange);
        isFromPreview = getIntent().hasExtra(ImageSelectionActivity.EXTRA_FROM_PREVIEW);
        application = MyApplication.getInstance();
        application.isEditModeEnable = true;
        bindView();
        init();
        linearAds = findViewById(R.id.linearAds);

    }




    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (arrangeImageAdapter != null) {
            arrangeImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void bindView() {
        rvSelectedImages = findViewById(R.id.rvVideoAlbum);
        toolbar = findViewById(R.id.toolbar);
    }

    private void init() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        arrangeImageAdapter = new ArrangeImageAdapter(this);
        rvSelectedImages.setLayoutManager(gridLayoutManager);
        rvSelectedImages.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImages.setEmptyView(findViewById(R.id.list_empty));
        rvSelectedImages.setAdapter(arrangeImageAdapter);
        new ItemTouchHelper(_ithCallback).attachToRecyclerView(rvSelectedImages);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    Callback _ithCallback = new Callback() {
        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            Log.e("action", "actoinState " + actionState);
            if (actionState == 0) {
                arrangeImageAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            return Callback.makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        }

        @Override
        public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
            arrangeImageAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            application.min_pos = Math.min(application.min_pos, Math.min(fromPos, toPos));
            MyApplication.isBreak = true;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 1001) {
            if (data.getExtras() != null) {
                String strpath = String.valueOf(data.getExtras().get("uri_new"));
                ImageData imgs = new ImageData();
                imgs.setImagePath(strpath);
                application.ReplaceSelectedImage(imgs, data.getExtras().getInt("position"));
                if (arrangeImageAdapter != null) {
                    arrangeImageAdapter.notifyItemChanged(data.getExtras().getInt("position"));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        menu.removeItem(R.id.menu_clear);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isFromPreview) {
                    super.onBackPressed();
                    break;
                }
                done();
                break;
            case R.id.menu_done:
                done();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void done() {
        application.isEditModeEnable = false;
        if (isFromPreview) {
            setResult(RESULT_OK);
            finish();
        } else {
            loadPreviewActivity();
        }
    }

    private void loadPreviewActivity() {
        Intent intent = new Intent(this, VideoCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (isFromPreview) {
            done();
        } else {
            super.onBackPressed();
        }
    }
}
