package com.example.motoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.motoapp.R;
import com.example.motoapp.adapters.RecyclerAdapter;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMotoActivity extends AppCompatActivity implements RecyclerViewClickListner {

    private Button quitButton;
    private Button findButton;
    private RecyclerView recyclerView;

    private Spinner spinnerProducent;
    private Spinner spinnerModel;
    private Spinner spinnerYear;
    private Intent localIntent;
    private Bundle localBundle;

    private String[] producents = {};
    private String[] models = {};
    private String[] years = {};

    private String currentlySelectedProducent = null;
    private String currentlySelectedModel = null;
    private String currentlySelectedYear = null;

    private List<String> results;
    private RecyclerAdapter localAdapter;
    private boolean isSearchingAllowed = false;

    private FirebaseFirestore dbHandler;

    private List<Bundle> downloadedVehicles;

    public void addingTestValues() {
        String[] producents =
                {"KTM", "KTM", "KTM", "KTM", "KTM", "KTM",
                        "Husqvarna", "Husqvarna", "Husqvarna",
                        "Tm Racing", "Tm Racing", "Tm Racing", "Tm Racing"};
        String[] models =
                {"SXF 250", "SXF 250", "SX 125", "EXC TPI 300", "EXC TPI 300", "RC 390",
                        "TC 250", "TC 250", "FE 350",
                        "MX 125", "MX 144", "MX 144", "FE 250i"};

        Integer[] years =
                {2019, 2020, 2018, 2019, 2020, 2017,
                        2019, 2020, 2016,
                        2016, 2017, 2018, 2018};

        for (Integer i = 0; i < producents.length; i++) {
            addVehicleToFirestore(producents[i], models[i], years[i], "_PATH");
        }
    }

    public void addVehicleToFirestore(String producent, String model, Integer year, String path) {
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("Producent", producent);
        vehicle.put("Model", model);
        vehicle.put("Year", year);
        vehicle.put("Path", path);

        //If producent does not exists
        dbHandler.collection("producents")
                .document(producent)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                Map<String, Object> newObj = new HashMap<>();
                                newObj.put("producent", producent);
                                dbHandler.collection("producents")
                                        .document(producent)
                                        .set(newObj);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                    }
                });

        //If model does not exists
        dbHandler.collection("producents")
                .document(producent)
                .collection("models")
                .document(model)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                Map<String, Object> newObj = new HashMap<>();
                                newObj.put("model", model);
                                dbHandler.collection("producents")
                                        .document(producent)
                                        .collection("models")
                                        .document(model)
                                        .set(newObj);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                    }
                });

        //If vehicle from year does not exists
        dbHandler.collection("producents")
                .document(producent)
                .collection("models")
                .document(model)
                .collection("years")
                .document(String.valueOf(year))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                dbHandler.collection("producents")
                                        .document(producent)
                                        .collection("models")
                                        .document(model)
                                        .collection("years")
                                        .document(String.valueOf(year))
                                        .set(vehicle);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void getProducents() throws InterruptedException {
        dbHandler.collection("producents")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> availableProducents = new ArrayList<String>();

                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) Log.i("FIREBASE: ", "EMPTY");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String)document.getId();
                                Log.i("FIREBASE: ", name);
                                availableProducents.add(name);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                        producents = availableProducents.toArray(new String[0]);
                        ArrayAdapter<String> adapterProducent = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.spinner_item,
                                producents);
                        adapterProducent.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerProducent.setAdapter(adapterProducent);
                    }
                });
    }

    public void getModels(String producent) {
        this.dbHandler.collection("producents")
                .document(producent)
                .collection("models")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> availableModels = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) Log.i("FIREBASE: ", "EMPTY");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.getId();
                                Log.i("FIREBASE: ", name);
                                availableModels.add(name);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                        models = availableModels.toArray(new String[0]);
                        ArrayAdapter<String> adapterModel = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.spinner_item,
                                models);
                        adapterModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerModel.setAdapter(adapterModel);
                    }
                });
    }

    public void getYears(String producent, String model) {
        this.dbHandler.collection("producents")
                .document(producent)
                .collection("models")
                .document(model)
                .collection("years")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> availableYears = new ArrayList<String>();
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) Log.i("FIREBASE: ", "EMPTY");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = (String) document.getId();
                                Log.i("FIREBASE: ", name);
                                availableYears.add(name);
                            }
                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                        years = availableYears.toArray(new String[0]);
                        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(
                                getApplicationContext(),
                                R.layout.spinner_item,
                                years);
                        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerYear.setAdapter(adapterYear);
                    }
                });
    }

    public void getVehicle(String producent, String model, String year) {
        this.dbHandler.collection("producents")
                .document(producent)
                .collection("models")
                .document(model)
                .collection("years")
                .document(year)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                downloadedVehicles.clear();
                                Log.i("FIREBASE: ", "DocumentSnapshot data: " + document.getData());
                                results.clear();
                                Bundle localBundle = new Bundle();

                                localBundle.putString("Producent", (String) document.get("Producent"));
                                localBundle.putString("Model", (String) document.get("Model"));
                                localBundle.putString("Path", (String) document.get("Path"));
                                localBundle.putString("Year", String.valueOf(document.get("Year")));
                                downloadedVehicles.add(localBundle);
                                String newName = document.get("Producent") + " " +
                                        document.get("Model") + " " +
                                        document.get("Year");
                                results.add(newName);
                                localAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("FIREBASE: ", "No such document");
                            }

                        } else {
                            Log.i("FIREBASE: ", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_moto);
        localIntent = getIntent();
        localBundle = localIntent.getExtras();

        results = new ArrayList<String>();
        this.dbHandler = FirebaseFirestore.getInstance();
        downloadedVehicles = new ArrayList<Bundle>();

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
                if(isSearchingAllowed)
                    getVehicle(
                            currentlySelectedProducent,
                            currentlySelectedModel,
                            currentlySelectedYear
                    );
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
        spinnerProducent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentlySelectedProducent = producents[position];
                getModels(currentlySelectedProducent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Model spinner
        spinnerModel = (Spinner)findViewById(R.id.spinnerModel);
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentlySelectedModel = models[position];
                getYears(currentlySelectedProducent, currentlySelectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Year spinner
        spinnerYear = (Spinner)findViewById(R.id.spinnerYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentlySelectedYear = years[position];
                isSearchingAllowed = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            getProducents();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        localBundle = downloadedVehicles.get(position);
        localBundle.putString("vresult", results.get(position));
        localIntent.putExtras(localBundle);
        setResult(Activity.RESULT_OK, localIntent);
        finish();
    }
}