package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.MyApplication;
import com.videomaker.photovideoeditorwithanimation.adapters.ImageByIdAdapter;
import com.videomaker.photovideoeditorwithanimation.adapters.ImageByAlbumAdapter;
import com.videomaker.photovideoeditorwithanimation.adapters.OnItemClickListner;
import com.videomaker.photovideoeditorwithanimation.adapters.ImageSelectionAdapter;
import com.videomaker.photovideoeditorwithanimation.util.Constant;
import com.videomaker.photovideoeditorwithanimation.view.EmptyRecyclerView;
import com.videomaker.photovideoeditorwithanimation.view.ExpandIconView;
import com.videomaker.photovideoeditorwithanimation.view.VerticalSlidingPanel;

public class ImageSelectionActivity extends AppCompatActivity implements VerticalSlidingPanel.PanelSlideListener {

    public static final String EXTRA_FROM_PREVIEW = "extra_from_preview";
    private ImageByIdAdapter imageByIdAdapter;
    private ImageByAlbumAdapter imageByAlbumAdapter;
    private MyApplication application;
    private Button btnClear;
    private ExpandIconView expandIcon;
    int id;
    public boolean isFromPreview = false;
    boolean isPause = false;
    private VerticalSlidingPanel verticalSlidingPanel;
    private View linearHome;
    private RecyclerView rvAlbum;
    private RecyclerView rvAlbumImages;
    private EmptyRecyclerView rvSelectedImage;
    private ImageSelectionAdapter  selectedImageAdapter;
    private Toolbar toolbar;
    private TextView tvImageCount;
    LinearLayout linearAds;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);
        application = MyApplication.getInstance();
        isFromPreview = getIntent().hasExtra(EXTRA_FROM_PREVIEW);
        bindView();
        init();
        addListner();
        linearAds = findViewById(R.id.linearAds);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void bindView() {
        tvImageCount = findViewById(R.id.tvImageCount);
        expandIcon = findViewById(R.id.settings_drag_arrow);
        rvAlbum = findViewById(R.id.rvAlbum);
        rvAlbumImages = findViewById(R.id.rvImageAlbum);
        rvSelectedImage = findViewById(R.id.rvSelectedImagesList);
        verticalSlidingPanel = findViewById(R.id.overview_panel);
        verticalSlidingPanel.setEnableDragViewTouchEvents(true);
        verticalSlidingPanel.setDragView(findViewById(R.id.settings_pane_header));
        verticalSlidingPanel.setPanelSlideListener(this);
        linearHome = findViewById(R.id.linearHome);
        toolbar = findViewById(R.id.toolbar);
        btnClear = findViewById(R.id.btnClear);
    }

    private void init() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageByIdAdapter = new ImageByIdAdapter(this);
        imageByAlbumAdapter = new ImageByAlbumAdapter(this);
        selectedImageAdapter = new ImageSelectionAdapter(this);
        rvAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        rvAlbum.setItemAnimator(new DefaultItemAnimator());
        rvAlbum.setAdapter(imageByIdAdapter);
        rvAlbumImages.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        rvAlbumImages.setItemAnimator(new DefaultItemAnimator());
        rvAlbumImages.setAdapter(imageByAlbumAdapter);
        rvSelectedImage.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
        rvSelectedImage.setAdapter(selectedImageAdapter);
        rvSelectedImage.setEmptyView(findViewById(R.id.list_empty));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvImageCount.setText(String.valueOf(application.getSelectedImages().size()));
    }

    private void addListner() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });
        imageByIdAdapter.setOnItemClickListner(new OnItemClickListner<Object>() {
            @Override
            public void onItemClick(View view, Object o) {
                imageByAlbumAdapter.notifyDataSetChanged();
            }
        });
        imageByAlbumAdapter.setOnItemClickListner(new OnItemClickListner<Object>() {
            @Override
            public void onItemClick(View view, Object o) {
                tvImageCount.setText(String.valueOf(application.getSelectedImages().size()));
                selectedImageAdapter.notifyDataSetChanged();
            }
        });
        selectedImageAdapter.setOnItemClickListner(new OnItemClickListner<Object>() {
            @Override
            public void onItemClick(View view, Object o) {
                tvImageCount.setText(String.valueOf(application.getSelectedImages().size()));
                imageByAlbumAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isPause) {
            isPause = false;
            tvImageCount.setText(String.valueOf(application.getSelectedImages().size()));
            imageByAlbumAdapter.notifyDataSetChanged();
            selectedImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
        isPause = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_selection, menu);
        if (isFromPreview) {
            menu.removeItem(R.id.menu_clear);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_clear:
                clearData();
                break;
            case R.id.menu_done:
                if (application.getSelectedImages().size() > 2) {
                    if (!isFromPreview) {
                        id = 101;
                        loadImageEditActivity();
                        break;
                    }
                    setResult(RESULT_OK);
                    finish();
                    return false;
                } else {
                    Toast.makeText(this, "Select more than 2 Images for create video", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadImageEditActivity() {

            Intent intent = new Intent(ImageSelectionActivity.this, ImageArrangeActivity.class);
            startActivity(intent);

    }

    private void clearData() {
        for (int i = application.getSelectedImages().size() - 1; i >= 0; i--) {
            application.removeSelectedImage(i);
        }
        tvImageCount.setText("0");
        selectedImageAdapter.notifyDataSetChanged();
        imageByAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (verticalSlidingPanel.isExpanded()) {
            verticalSlidingPanel.collapsePane();
        } else if (isFromPreview) {
            setResult(RESULT_OK);
            finish();
        } else {
            application.videoImages.clear();
            application.clearAllSelection();
            super.onBackPressed();
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (expandIcon != null) {
            expandIcon.setFraction(slideOffset, false);
        }
        if (slideOffset >= 0.005f) {
            if (linearHome != null && linearHome.getVisibility() != View.VISIBLE) {
                linearHome.setVisibility(View.VISIBLE);
            }
        } else if (linearHome != null && linearHome.getVisibility() == View.VISIBLE) {
            linearHome.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPanelCollapsed(View panel) {
        if (linearHome != null) {
            linearHome.setVisibility(View.VISIBLE);
        }
        selectedImageAdapter.isExpanded = false;
        selectedImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPanelExpanded(View panel) {
        if (linearHome != null) {
            linearHome.setVisibility(View.GONE);
        }
        selectedImageAdapter.isExpanded = true;
        selectedImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPanelAnchored(View panel) {

    }

    @Override
    public void onPanelShown(View panel) {

    }
}
