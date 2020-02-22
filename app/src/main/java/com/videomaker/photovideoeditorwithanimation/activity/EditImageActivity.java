package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.universevideomaker.R;
import com.videomaker.photovideoeditorwithanimation.imageedit.CompressImage;
import com.videomaker.photovideoeditorwithanimation.imageedit.DataBinder;
import com.videomaker.photovideoeditorwithanimation.imageedit.Filter;
import com.videomaker.photovideoeditorwithanimation.imageedit.FilterAdapter;
import com.videomaker.photovideoeditorwithanimation.imageedit.SaveImage;
import com.videomaker.photovideoeditorwithanimation.util.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;

public class EditImageActivity extends AppCompatActivity implements FilterAdapter.FilterCallback, SeekBar.OnSeekBarChangeListener, GPUImageView.OnPictureSavedListener {

    LinearLayout linearBrightness, linearContrast, linearSaturation, linearSharp, linearBlur, linearFilter;
    SeekBar seekbarBrightness, seekbarContrast, seekbarSaturation, seekbarSharp, seekbarVignette;
    TextView textcontrast, textSaturation, textSharp, textVignette, textBrightness;
    FilterAdapter filterAdapter;
    ArrayList<Filter> filterList = new ArrayList<>();
    GPUImageView gpuImageView;
    String imagePath;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewFilter;
    float start;
    float end;
    float value;
    String strFinalPath = "";
    String strFolder = "";
    String strFileName = "";
    LinearLayout linearAds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("Editor");

        linearAds = findViewById(R.id.linearAds);

        recyclerViewFilter = findViewById(R.id.viewFilter_RecyclerView);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerViewFilter.setLayoutManager(linearLayoutManager);
        gpuImageView = findViewById(R.id.jpImage);
        gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_CROP);
        linearFilter = findViewById(R.id.linearFilter);
        linearBrightness = findViewById(R.id.linearBrightness);
        linearContrast = findViewById(R.id.linearContrast);
        linearBlur = findViewById(R.id.linearBlur);
        linearSaturation = findViewById(R.id.linearSaturation);
        linearSharp = findViewById(R.id.linearSharp);
        textBrightness = findViewById(R.id.textBrightness);
        textcontrast = findViewById(R.id.textcontrast);
        textVignette = findViewById(R.id.textVignette);
        textSaturation = findViewById(R.id.textSaturation);
        textSharp = findViewById(R.id.textSharp);
        seekbarBrightness = findViewById(R.id.seekbarBrightness);
        seekbarContrast = findViewById(R.id.seekbarContrast);
        seekbarVignette = findViewById(R.id.sbVigent);
        seekbarSaturation = findViewById(R.id.seekbarSaturation);
        seekbarSharp = findViewById(R.id.seekbarSharp);
        seekbarBrightness.setOnSeekBarChangeListener(this);
        seekbarContrast.setOnSeekBarChangeListener(this);
        seekbarVignette.setOnSeekBarChangeListener(this);
        seekbarSaturation.setOnSeekBarChangeListener(this);
        seekbarSharp.setOnSeekBarChangeListener(this);
        imagePath = getIntent().getStringExtra("uri");
        if (getIntent().getExtras() != null)
            Constant.editedImagePath = getIntent().getExtras().getString("editImage");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(Constant.editedImagePath, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        if (imageHeight > 1000 && imageWidth > 1000 && imageHeight < 2000 && imageWidth < 2000) {
            imageHeight /= 2;
            imageWidth /= 2;
        } else if (imageHeight > 2000 || imageWidth > 2000) {
            if (imageHeight < 3000 || imageWidth < 3000) {
                imageHeight /= 3;
                imageWidth /= 3;
            } else if (imageHeight > 3000 || imageWidth > 3000) {
                if (imageHeight < 4000 || imageWidth < 4000) {
                    imageHeight /= 4;
                    imageWidth /= 4;
                } else if (imageHeight > 4000 || imageWidth > 4000) {
                    imageHeight /= 5;
                    imageWidth /= 5;
                }
            }
        } else if (imageHeight > 1000 || imageWidth > 1000) {
            imageHeight = (int) (((double) imageHeight) / 1.6d);
            imageWidth = (int) (((double) imageWidth) / 1.6d);
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(imageWidth, imageHeight);
        layoutParams.gravity = Gravity.CENTER;
        gpuImageView.setLayoutParams(layoutParams);
        gpuImageView.setImage(  CompressImage.originalBitmap(BitmapFactory.decodeFile(Constant.editedImagePath)));
        changeVisibility(Constant.visibleLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout);
        filterList.clear();
        filterList = DataBinder.fetchFilters();
        filterAdapter = new FilterAdapter(EditImageActivity.this, filterList);
        recyclerViewFilter.setAdapter(filterAdapter);

    }

    public void changeVisibility(int filter, int brightness, int contrast, int vignette, int saturation, int sharp) {
        linearFilter.setVisibility(filter);
        linearBrightness.setVisibility(brightness);
        linearContrast.setVisibility(contrast);
        linearBlur.setVisibility(vignette);
        linearSaturation.setVisibility(saturation);
        linearSharp.setVisibility(sharp);
    }

    public void btnFilter(View view) {
        changeVisibility(Constant.visibleLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout);
    }

    public void btnBrightness(View view) {
        changeVisibility(Constant.goneLayout, Constant.visibleLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout);
    }

    public void btnContrast(View view) {
        changeVisibility(Constant.goneLayout, Constant.goneLayout, Constant.visibleLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout);
    }

    public void btnVignette(View view) {
        changeVisibility(Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.visibleLayout, Constant.goneLayout, Constant.goneLayout);
    }

    public void btnSaturation(View view) {
        changeVisibility(Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.visibleLayout, Constant.goneLayout);
    }

    public void btnSharp(View view) {
        changeVisibility(Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.goneLayout, Constant.visibleLayout);
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbarBrightness:
                start = -0.5f;
                end = 0.5f;
                value = (((end - start) * ((float) progress)) / 100.0f) + start;
                gpuImageView.setFilter(new GPUImageBrightnessFilter(value));
                textBrightness.setText(String.valueOf(progress));
                return;
            case R.id.sbVigent:
                start = 0.0f;
                end = 1.0f;
                value = (((end - start) * ((float) progress)) / 100.0f) + start;
                gpuImageView.setFilter(new GPUImageGaussianBlurFilter(value));
                textVignette.setText(String.valueOf(progress));
                return;
            case R.id.seekbarSaturation:
                start = 0.0f;
                end = 2.0f;
                value = (((end - start) * ((float) progress)) / 100.0f) + start;
                gpuImageView.setFilter(new GPUImageSaturationFilter(value));
                textSaturation.setText(String.valueOf(progress));
                return;
            case R.id.seekbarContrast:
                start = 0.4f;
                end = 2.0f;
                value = (((end - start) * ((float) progress)) / 100.0f) + start;
                gpuImageView.setFilter(new GPUImageContrastFilter(value));
                textcontrast.setText(String.valueOf(progress));
                return;
            case R.id.seekbarSharp:
                start = 0.0f;
                end = 360.0f;
                value = (((end - start) * ((float) progress)) / 100.0f) + start;
                gpuImageView.setFilter(new GPUImageHueFilter(value));
                textSharp.setText(String.valueOf(progress));
                return;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void FilterMethod(int i) {
        gpuImageView.setFilter(DataBinder.applyFilter(i, EditImageActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_done:
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                strFolder = "temp";
                strFileName = System.currentTimeMillis() + ".jpg";
                strFinalPath = String.valueOf(new File(path, strFolder + "/" + strFileName));
                gpuImageView.saveToPictures(strFolder, strFileName, this);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    @Override
    public void onPictureSaved(Uri uri) {
        File from = new File(strFinalPath);
        File to = new File(getBaseContext().getCacheDir(), "temp/" + System.currentTimeMillis() + ".jpg");
        to.getParentFile().mkdirs();
        try {
            SaveImage.copyDirectoryOneLocationToAnotherLocation(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (from.exists()) {
            SaveImage.deleteFileFromMediaStore(getContentResolver(), from);
        }
        Constant.editedImagePath = String.valueOf(to);
        Intent intent = new Intent();
        intent.putExtra("uri_new", Constant.editedImagePath);
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
        setResult(1001, intent);
        closeActivity();
    }

    void closeActivity() {

            finish();

    }
}
