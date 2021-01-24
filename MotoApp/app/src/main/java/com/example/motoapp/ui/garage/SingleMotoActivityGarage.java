package com.example.motoapp.ui.garage;

import android.content.Intent;
import android.os.Bundle;

import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.OnFragmentInteractionListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.motoapp.R;

public class SingleMotoActivityGarage extends AppCompatActivity implements  OnFragmentInteractionListener{

    private Intent localIntent;
    private Bundle localBundle;
    private Integer motoIndex;
    private String motoName;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_moto_garage);

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
    public void addServiceToDatabase(String name, String description) {

    }

    @Override
    public String getString(String key) {
        if (key == "name") return motoName;
        if (key == "index") return String.valueOf(motoIndex);
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