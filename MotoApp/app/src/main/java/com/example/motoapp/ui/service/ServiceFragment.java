package com.example.motoapp.ui.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.RecyclerAdapterMoto;
import com.example.motoapp.adapters.RecyclerViewClickListner;

public class ServiceFragment extends Fragment implements RecyclerViewClickListner {

    private Intent singleMotoIntent;
    private FragmentActivity localIntent;

    private RecyclerAdapterMoto localAdapter;
    private DatabaseAdapter.GarageAnswer localViehicles;
    private RecyclerView recyclerView;

    private DatabaseAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        localIntent = getActivity();
        adapter = new DatabaseAdapter(localIntent);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }//onCreateView


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if((requestCode== 101 ) && (resultCode== Activity.RESULT_OK))
            {
                Bundle resultBundle= data.getExtras();

                //Add to local database
                adapter.addVehicle(resultBundle);
            }

            if((requestCode == 102) && (resultCode == Activity.RESULT_OK))
            {
                //Exit from single moto activity

            }

            localViehicles = adapter.getVehicles(true);
            localAdapter.updateAdapter(localViehicles.names, localViehicles.mths);
            localAdapter.notifyDataSetChanged();
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), "No data - error",
                    Toast.LENGTH_LONG).show();
        }
    }//onActivityResult

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleMotoIntent = new Intent(getActivity(), SingleMotoActivityService.class);

        localViehicles = adapter.getVehicles(true);

        //Recycler View Settings
        recyclerView = (RecyclerView) view.findViewById(R.id.serviceList);
        localAdapter = new RecyclerAdapterMoto(localIntent, localViehicles.names, localViehicles.mths, this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));

    }//onViewCreated

    @Override
    public void onItemClick(int position) {
        Bundle sendBundle = new Bundle();
        sendBundle.putInt("index", localViehicles.indexes.get(position));
        sendBundle.putString("name", localViehicles.names.get(position));
        sendBundle.putString("producent", localViehicles.producents.get(position));
        sendBundle.putString("model", localViehicles.models.get(position));
        sendBundle.putInt("year", Integer.parseInt(String.valueOf(localViehicles.years.get(position))));
        singleMotoIntent.putExtras(sendBundle);
        startActivityForResult(singleMotoIntent, 102);
    }
}