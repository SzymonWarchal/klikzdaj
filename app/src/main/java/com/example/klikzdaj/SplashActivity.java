package com.example.klikzdaj;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class SplashActivity extends AppCompatActivity {

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.app_name);

        Typeface typeface = ResourcesCompat.getFont(this, R.font.creamfont);
        appName.setTypeface(typeface);

        Animation animacja = AnimationUtils.loadAnimation(this, R.anim.animacja);
        appName.setAnimation(animacja);

        new Thread(){
            @Override
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(com.example.klikzdaj.SplashActivity.this,MainActivity.class);
                startActivity(intent);
                com.example.klikzdaj.SplashActivity.this.finish();

            }


        }.start();
    }
}