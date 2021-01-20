package com.example.motoapp.ui.garage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.OnFragmentInteractionListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.View;
import android.widget.Toast;

import com.example.motoapp.R;

public class SingleMotoActivity extends AppCompatActivity implements  OnFragmentInteractionListener{

    private Intent localIntent;
    private Bundle localBundle;
    private Integer motoIndex;
    private String motoName;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_moto);

        localIntent = getIntent();
        localBundle = localIntent.getExtras();
        adapter = new DatabaseAdapter(this);
        motoIndex = (Integer) localBundle.get("index");
        motoName = (String) localBundle.get("name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void deleteVehicle(){
        adapter.deleteVehicle(motoIndex);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onFragmentInteraction(int requestCode) {
        if(requestCode == 501) deleteVehicle();
    }

    @Override
    public String getString(String key) {
        if (key == "name") return motoName;
        return null;
    }

    @Override
    public void moveVehicleToService() {
        adapter.moveToService(motoIndex);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void moveVehicleToGarage() {
        adapter.moveToGarage(motoIndex);
        setResult(RESULT_OK);
        finish();
    }
}