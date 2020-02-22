package com.videomaker.photovideoeditorwithanimation.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.universevideomaker.R;


/**
 * Created by DS on 14/03/2017.
 */

public class SplashActivity extends AppCompatActivity {
    protected boolean _active = true;
    protected int _splashTime = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent localIntent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(localIntent);
                    finish();
                }
            }
        };
        splashTread.start();
    }

    @Override
    public void onBackPressed() {

    }
}
