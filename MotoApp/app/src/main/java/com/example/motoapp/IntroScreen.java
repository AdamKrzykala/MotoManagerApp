package com.example.motoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.tv.TvContract;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroScreen extends AppCompatActivity {

    private static int SLEEP_TIMER = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro_screen);
        getSupportActionBar().hide();
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }

    private void RunAnimation()
    {
        //Slideshow
        ImageView slideshow = (ImageView) findViewById(R.id.imageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) slideshow.getDrawable();

        animationDrawable.start();
    }

    private class LogoLauncher extends Thread {
        public void run(){
            RunAnimation();
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(IntroScreen.this, MainActivity.class);
            startActivity(intent);
            IntroScreen.this.finish();
        }
    }
}