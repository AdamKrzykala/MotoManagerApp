package com.example.motoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.motoapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NewMotoActivity extends AppCompatActivity {

    private Button quitButton;
    private Button findButton;
    private RecyclerView recyclerView;

    private String values[];
    private RecyclerAdapter localAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moto);
        Intent myLocalIntent = getIntent();
        Bundle myBundle = myLocalIntent.getExtras();
        Double v1 = myBundle.getDouble("val1");
        Double v2 = myBundle.getDouble("val2");
        Double vResult=  v1 + v2;
        myBundle.putDouble("vresult", vResult);
        myLocalIntent.putExtras(myBundle);
        setResult(Activity.RESULT_OK, myLocalIntent);

        quitButton = (Button) findViewById(R.id.exitButton);
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        values = new String[]{"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen",
                "Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen",
                "Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen",
                "Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};

        recyclerView = (RecyclerView) findViewById(R.id.globalList);
        localAdapter = new RecyclerAdapter(this, values);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findButton = (Button) findViewById(R.id.searchButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                localAdapter.notifyDataSetChanged();
            }
        });
    }

    public static String[] localToArray(List<String> input) {
        String[] arr = new String[input.size()];
        for (int i = 0; i < input.size(); i++)
            arr[i] = input.get(i);
        return arr;
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.LocalViewHolder> {

        String data[];
        Context context;

        public RecyclerAdapter(Context ct, String tempData[]) {
            context = ct;
            data = tempData;
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
            holder.stringNameVar.setText(data[position]);
        }

        @Override
        public int getItemCount() {
            return data.length;
        }

        public class LocalViewHolder extends RecyclerView.ViewHolder {

            TextView stringNameVar;

            public LocalViewHolder(@NonNull View itemView) {
                super(itemView);
                stringNameVar = itemView.findViewById(R.id.stringName);
            }
        }
    }
}