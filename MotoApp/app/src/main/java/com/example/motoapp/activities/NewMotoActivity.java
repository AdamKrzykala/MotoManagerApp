package com.example.motoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.motoapp.R;
import com.example.motoapp.adapters.RecyclerAdapter;
import com.example.motoapp.adapters.RecyclerViewClickListner;

import java.util.ArrayList;
import java.util.List;

public class NewMotoActivity extends AppCompatActivity implements RecyclerViewClickListner {

    private Button quitButton;
    private Button findButton;
    private RecyclerView recyclerView;

    private Spinner spinnerProducent;
    private Spinner spinnerModel;
    private Spinner spinnerYear;
    private Intent localIntent;
    private Bundle localBundle;

    private static final String[] producents = {"KTM", "Husqvarna", "TM Racing", "Honda", "Kawasaki", "Suzuki"};
    private static final String[] models = {"SX", "TX"};
    private static final String[] years = {"1990", "2000", "2020"};

    private List<String> results;
    private RecyclerAdapter localAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moto);
        localIntent = getIntent();
        localBundle = localIntent.getExtras();

        results = new ArrayList<String>();

        //Buttons in interface
        quitButton = (Button) findViewById(R.id.exitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findButton = (Button) findViewById(R.id.searchButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                results.clear();
                results.add("KTM SXF 250 2018");
                localAdapter.notifyDataSetChanged();
                //Getting items from remote database
            }
        });

        //Recycler View Settings
        recyclerView = (RecyclerView) findViewById(R.id.globalList);
        localAdapter = new RecyclerAdapter(this, results,this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Producent spinner
        spinnerProducent = (Spinner)findViewById(R.id.spinnerProducent);
        ArrayAdapter<String> adapterProducent = new ArrayAdapter<String>(this, R.layout.spinner_item, producents);
        adapterProducent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducent.setAdapter(adapterProducent);

        //Model spinner
        spinnerModel = (Spinner)findViewById(R.id.spinnerModel);
        ArrayAdapter<String> adapterModel= new ArrayAdapter<String>(this, R.layout.spinner_item, models);
        adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(adapterModel);

        //Producent spinner
        spinnerYear = (Spinner)findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this, R.layout.spinner_item, years);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
    }

    @Override
    public void onItemClick(int position) {
        localBundle.putString("vresult", results.get(position));
        localIntent.putExtras(localBundle);
        setResult(Activity.RESULT_OK, localIntent);
        finish();
    }
}