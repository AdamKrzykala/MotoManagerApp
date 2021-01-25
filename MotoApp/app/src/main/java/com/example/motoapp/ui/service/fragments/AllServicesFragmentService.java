package com.example.motoapp.ui.service.fragments;

import android.app.Activity;
import android.os.Bundle;
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
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.example.motoapp.adapters.RecyclerAdapterExtendedServices;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AllServicesFragmentService extends Fragment implements RecyclerViewClickListner {

    private FragmentActivity localIntent;

    private RecyclerAdapterExtendedServices localAdapter;
    private DatabaseAdapter.ServicesAnswer localServices;
    private RecyclerView recyclerView;

    private DatabaseAdapter adapter;

    private FloatingActionButton backButtonHandler;
    private FloatingActionButton shareButtonHandler;

    private OnFragmentInteractionListener listener;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        if(activity instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener)activity;
        } else {

        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        localIntent = getActivity();
        adapter = new DatabaseAdapter(localIntent);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_services_service, container, false);
    }//onCreateView

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localServices = adapter.getServices(Integer.valueOf(listener.getString("index")));
        backButtonHandler = (FloatingActionButton) view.findViewById(R.id.backBtn);
        shareButtonHandler = (FloatingActionButton) view.findViewById(R.id.wrapAddingButton);

        //Recycler View Settings
        recyclerView = (RecyclerView) view.findViewById(R.id.allServicesList);
        localAdapter = new RecyclerAdapterExtendedServices(
                localIntent,
                Integer.valueOf(listener.getString("index")),
                localServices.indexes,
                localServices.names,
                localServices.dates,
                localServices.descriptions,
                this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(AllServicesFragmentService.this)
                        .navigate(R.id.action_AllServicesFragmentService_to_MenuFragmentService);
            }
        });

        shareButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }//onViewCreated

    @Override
    public void onItemClick(int position) {
//
    }
}