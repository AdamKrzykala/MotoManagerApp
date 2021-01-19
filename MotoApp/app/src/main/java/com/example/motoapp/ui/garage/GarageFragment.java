package com.example.motoapp.ui.garage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.activities.NewMotoActivity;
import com.example.motoapp.R;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.RecyclerAdapter;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GarageFragment extends Fragment implements RecyclerViewClickListner {

    private Intent newMotoIntent;
    private Intent singleMotoIntent;
    private FragmentActivity localIntent;
    private FloatingActionButton fabHandler;

    private RecyclerAdapter localAdapter;
    private List<String> localViehicles;
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
        return inflater.inflate(R.layout.fragment_garage, container, false);
    }//onCreateView


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if((requestCode== 101 ) && (resultCode== Activity.RESULT_OK))
            {
                Bundle resultBundle= data.getExtras();
                String newModel = resultBundle.getString("vresult");
                Toast.makeText(getActivity(), newModel + " added",
                        Toast.LENGTH_LONG).show();
                //Add to local database
                adapter.addVehicle(newModel);
                localViehicles = adapter.getNamesFromGarage();
                localAdapter.updateAdapter(localViehicles);
            }

            if((requestCode == 102) && (resultCode == Activity.RESULT_OK))
            {
                //Exit from single moto activity
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
        newMotoIntent = new Intent(getActivity(), NewMotoActivity.class);
        singleMotoIntent = new Intent(getActivity(), SingleMotoActivity.class);

        localViehicles = adapter.getNamesFromGarage();

        //Recycler View Settings
        recyclerView = (RecyclerView) view.findViewById(R.id.garageList);
        localAdapter = new RecyclerAdapter(localIntent, localViehicles, this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));


        fabHandler = (FloatingActionButton)(getView().findViewById(R.id.fab));

        fabHandler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle sendBundle = new Bundle();

                newMotoIntent.putExtras(sendBundle);
                startActivityForResult(newMotoIntent, 101);
            }
        });
    }//onViewCreated

    @Override
    public void onItemClick(int position) {
        Bundle sendBundle = new Bundle();
        singleMotoIntent.putExtras(sendBundle);
        startActivityForResult(singleMotoIntent, 102);
    }
}