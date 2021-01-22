package com.example.motoapp.ui.garage.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;
import com.example.motoapp.activities.TrackerInBackground;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.RecyclerAdapter;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TrackerFragment extends Fragment implements RecyclerViewClickListner {

    private RecyclerAdapter localAdapter;
    private RecyclerView recyclerView;
    private DatabaseAdapter adapter;

    private boolean isStarted;
    private boolean isPaused;
    private boolean isStopped;

    protected FloatingActionButton backFABhandler;
    protected FloatingActionButton startpauseFABhandler;
    protected FloatingActionButton savepauseFABhandler;

    private FragmentActivity localIntent;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        localIntent = getActivity();
        adapter = new DatabaseAdapter(localIntent);

        this.isStarted = false;
        this.isPaused = false;
        this.isStopped = true;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tracker, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.mapList);
        //Here pass list with available maps - tempData
        localAdapter = new RecyclerAdapter(localIntent, new ArrayList<String>(), this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));

        backFABhandler = (FloatingActionButton)view.findViewById(R.id.backToMenuButton);
        startpauseFABhandler = (FloatingActionButton)view.findViewById(R.id.startpauseButton);
        savepauseFABhandler = (FloatingActionButton)view.findViewById(R.id.stopsaveButton);

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
            @Override
            public void onClick(View view) {
                if(isStopped) {
                    //Should start new map
                    TrackerInBackground.shouldContinue = true;
                    TrackerInBackground.shouldFinish = false;
                    Intent backgroundIntent = new Intent(getContext(), TrackerInBackground.class);
                    localIntent.startService(backgroundIntent);
                    isStopped = false;
                    isStarted = true;
                }
                else {
                    if (isStarted) {
                        //Should pause current map
                        isStarted = false;
                        isPaused = true;
                        TrackerInBackground.shouldContinue = false;
                        Log.i("SingleMotoActivity: ", "STOPPED: ");
                    }
                    else {
                        if(isPaused) {
                            //Should restart current map
                            isStarted = true;
                            isPaused = false;
                            TrackerInBackground.shouldContinue = true;
                            Log.i("SingleMotoActivity: ", "RESTARTED: ");
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
                    Log.i("SingleMotoActivity: ", "FINISHED");
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}