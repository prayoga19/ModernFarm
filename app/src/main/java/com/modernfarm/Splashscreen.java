package com.modernfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splashscreen extends AppCompatActivity {

    private  static  int SPLASH_SCREEN = 5000;
    Animation topAnim, botAnim;
    ImageView image;
    TextView judul;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Sembunyikan Appbar / biar fullscreen
        getSupportActionBar().hide();

        //Animasi
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.logo);
        judul = findViewById(R.id.appstext);

        image.setAnimation(topAnim);
        judul.setAnimation(botAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent (Splashscreen.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);
    }
}