package com.example.motoapp.ui.garage.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;
import com.example.motoapp.activities.MapActivity;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.example.motoapp.adapters.RecyclerAdapterMap;
import com.example.motoapp.services.TrackerInBackground;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.RecyclerAdapterMoto;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TrackerFragment extends Fragment
        implements RecyclerViewClickListner, OnFragmentInteractionListener {

    private RecyclerAdapterMap localAdapter;
    private RecyclerView recyclerView;
    private DatabaseAdapter adapter;

    private EditText newMth;

    private boolean isStarted;
    private boolean isPaused;
    private boolean isStopped;

    protected FloatingActionButton backFABhandler;
    protected FloatingActionButton startpauseFABhandler;
    protected FloatingActionButton savepauseFABhandler;

    private Button updateButtonHandler;
    Integer index;

    private DatabaseAdapter.RunsAnswer localMaps;

    private FragmentActivity localIntent;
    Intent backgroundIntent;
    private OnFragmentInteractionListener listener;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        if(activity instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener)activity;
        } else {

        }
    }

    public void createTest() {
        adapter = new DatabaseAdapter(localIntent);
        adapter.addNewRun(Integer.valueOf("1"));
        String currentTable = adapter.getCurrentTable();
        adapter.addPointToCurrentRun(currentTable, 30.045, 50.345);
        adapter.addPointToCurrentRun(currentTable, 30.044, 52.346);
        adapter.addPointToCurrentRun(currentTable, 32.043, 50.348);
        adapter.addPointToCurrentRun(currentTable, 32.043, 51.349);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        localIntent = getActivity();
        adapter = new DatabaseAdapter(localIntent);

        this.index = Integer.valueOf(listener.getString("index"));
        this.isStarted = false;
        this.isPaused = false;
        this.isStopped = true;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "ACCESS_FINE_LOCATION permission NOT GRANTED",
                            Toast.LENGTH_LONG).show();
                }
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.globalList);
        //createTest();
        localMaps = adapter.getRuns(index);

        //Here pass list with available maps - tempData
        localAdapter = new RecyclerAdapterMap(localIntent, localMaps.dates, this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));
        newMth = (EditText) view.findViewById(R.id.editMTH);

        backFABhandler = (FloatingActionButton)view.findViewById(R.id.backToMenuButton);
        startpauseFABhandler = (FloatingActionButton)view.findViewById(R.id.startpauseButton);
        savepauseFABhandler = (FloatingActionButton)view.findViewById(R.id.stopsaveButton);
        savepauseFABhandler.setVisibility(View.INVISIBLE);
        updateButtonHandler = (Button)view.findViewById(R.id.updateButton);

        updateButtonHandler.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                String textTemp = newMth.getText().toString();
                if(textTemp != null && isNumeric(textTemp)) {
                    adapter.updateMth(index, Integer.valueOf(textTemp));
                    Toast.makeText(getActivity(), "Mth changed", 3000).show();
                }
                else {
                    Toast.makeText(getActivity(), "Wrong input", 3000).show();
                }
                newMth.setText("");
            }
        });

        backFABhandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TrackerInBackground.shouldContinue = false;
                TrackerInBackground.shouldFinish = true;
                NavHostFragment.findNavController(TrackerFragment.this)
                        .navigate(R.id.action_TrackerFragment_to_MenuFragment);
            }
        });

        startpauseFABhandler.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if(isStopped) {
                    //Add new run
                    adapter.addNewRun(Integer.valueOf(listener.getString("index")));
                    //Should start new map
                    TrackerInBackground.shouldContinue = true;
                    TrackerInBackground.shouldFinish = false;
                    //Add row to maps table
                    //Add table and pass table name to tracker
                    backgroundIntent = new Intent(getContext(), TrackerInBackground.class);
                    localIntent.startService(backgroundIntent);
                    isStopped = false;
                    isStarted = true;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.pause_button, getContext().getTheme()));
                    }
                    else {
                        startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.pause_button));
                    }
                    savepauseFABhandler.setVisibility(View.VISIBLE);
                }
                else {
                    if (isStarted) {
                        //Should pause current map
                        isStarted = false;
                        isPaused = true;
                        TrackerInBackground.shouldContinue = false;
                        //Pause process
                        //Change icon
                        Log.i("Location: ", "STOPPED: ");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.start_button, getContext().getTheme()));
                        }
                        else {
                            startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.start_button));
                        }
                    }
                    else {
                        if(isPaused) {
                            //Should restart current map
                            isStarted = true;
                            isPaused = false;
                            TrackerInBackground.shouldContinue = true;
                            //Start process
                            //Change icon
                            Log.i("Location: ", "RESTARTED: ");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.pause_button, getContext().getTheme()));
                            }
                            else {
                                startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.pause_button));
                            }
                        }
                    }
                }
            }
        });

        savepauseFABhandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStopped) {
                    isStopped = true;
                    isStarted = false;
                    isPaused = false;
                    TrackerInBackground.shouldContinue = false;
                    TrackerInBackground.shouldFinish = true;
                    //Actualize preview on page with maps
                    localIntent.stopService(backgroundIntent);
                    Log.i("Location: ", "FINISHED");
                    savepauseFABhandler.setVisibility(View.INVISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.start_button, getContext().getTheme()));
                    }
                    else {
                        startpauseFABhandler.setImageDrawable(getResources().getDrawable(R.drawable.start_button));
                    }
                    localMaps = adapter.getRuns(index);
                    localAdapter.updateAdapter(localMaps.dates);
                    localAdapter.notifyDataSetChanged();
                }
            }
        });
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //startActivity(new Intent(getActivity(), MapActivity.class));
    }

    @Override
    public void onItemClick(int position) {
        //Start position
        Intent mIntent = new Intent(getActivity(), MapActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("source", localMaps.allocations.get(position));
        mIntent.putExtras(mBundle);
        startActivity(mIntent);
    }

    @Override
    public void onFragmentInteraction(int requestCode) {

    }

    @Override
    public void addServiceToDatabase(String name, String description) {

    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public void moveVehicleToService() {

    }

    @Override
    public void moveVehicleToGarage() {

    }
}