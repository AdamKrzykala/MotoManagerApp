package com.example.motoapp.ui.garage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.motoapp.R;

public class SingleMotoActivity extends AppCompatActivity {

    private Intent localIntent;
    private Bundle localBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_moto);

        localIntent = getIntent();
        localBundle = localIntent.getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}