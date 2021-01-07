package com.example.motoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewMotoActivity extends AppCompatActivity implements RecyclerViewClickListner{

    private Button quitButton;
    private Button findButton;
    private RecyclerView recyclerView;

    private Spinner spinnerProducent;
    private Spinner spinnerModel;
    private Spinner spinnerYear;
    private Intent localIntent;
    private Bundle localBundle;

    private static final String[] firms = {"KTM", "Husqvarna", "TM Racing", "Honda", "Kawasaki", "Suzuki"};
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
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.globalList);
        localAdapter = new RecyclerAdapter(this, results,this);

        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Producent spinner
        spinnerProducent = (Spinner)findViewById(R.id.spinnerProducent);
        ArrayAdapter<String> adapterFirm = new ArrayAdapter<String>(this, R.layout.spinner_item, firms);
        adapterFirm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProducent.setAdapter(adapterFirm);

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

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LocalViewHolder> {

        List<String> data;
        Context context;
        RecyclerViewClickListner recyclerViewClickListner;

        public RecyclerAdapter(Context ct, List<String> tempData, RecyclerViewClickListner recyclerViewClickListner) {
            this.context = ct;
            this.data = tempData;
            this.recyclerViewClickListner = recyclerViewClickListner;
        }
        @NonNull
        @Override
        public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.row_template, parent, false);
            return new LocalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocalViewHolder holder, int position) {
            holder.stringNameVar.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class LocalViewHolder extends RecyclerView.ViewHolder{

            TextView stringNameVar;

            public LocalViewHolder(@NonNull View itemView) {
                super(itemView);
                stringNameVar = itemView.findViewById(R.id.stringName);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerViewClickListner.onItemClick(getAdapterPosition());
                    }
                });
            }
        }
    }
}