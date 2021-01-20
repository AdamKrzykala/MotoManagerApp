package com.example.motoapp.ui.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.RecyclerAdapter;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ServiceFragment extends Fragment implements RecyclerViewClickListner {

    private Intent singleMotoIntent;
    private FragmentActivity localIntent;

    private RecyclerAdapter localAdapter;
    private DatabaseAdapter.GarageAnswer localViehicles;
    private RecyclerView recyclerView;

    DatabaseAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        localIntent = getActivity();
        adapter = new DatabaseAdapter(localIntent);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if((requestCode == 102) && (resultCode == Activity.RESULT_OK))
            {
                //Exit from single moto activity
                localViehicles = adapter.getVehicles(true);

                localAdapter.updateAdapter(localViehicles.names);
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), "No data - error",
                    Toast.LENGTH_LONG).show();
        }
    }//onActivityResult


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localViehicles = adapter.getVehicles(true);

        //Recycler View Settings
        recyclerView = (RecyclerView) view.findViewById(R.id.garageList);
        localAdapter = new RecyclerAdapter(localIntent, localViehicles.names, this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));
    }

    @Override
    public void onItemClick(int position) {

    }
}