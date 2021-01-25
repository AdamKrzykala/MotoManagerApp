package com.example.motoapp.ui.service.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private FloatingActionButton sendButtonHandler;
    private LinearLayout emailLayoutHandler;
    private EditText emailTextHandler;

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
        backButtonHandler = (FloatingActionButton) view.findViewById(R.id.backBtnService);
        shareButtonHandler = (FloatingActionButton) view.findViewById(R.id.shareButton);
        emailLayoutHandler = (LinearLayout) view.findViewById(R.id.emailLayout);
        sendButtonHandler = (FloatingActionButton) view.findViewById(R.id.sendButton);
        emailTextHandler = (EditText) view.findViewById(R.id.emailText);
        emailLayoutHandler.setVisibility(View.GONE);


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
                emailLayoutHandler.setVisibility(View.VISIBLE);
            }
        });

        sendButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLayoutHandler.setVisibility(View.GONE);
                //Sending email
                shareDataViaEmail();
            }
        });
    }//onViewCreated

    protected void shareDataViaEmail() {
        Log.i("Send email", "");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {emailTextHandler.getText().toString()} );
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Moto Data");

        String stringToSend = null;
        for (Integer i = 0; i < localServices.indexes.size(); i++) {
            stringToSend += String.valueOf(localServices.names.get(i)) + "\n";
            stringToSend += "Data wykonania: " + localServices.dates.get(i) + "\n";
            stringToSend += "Opis: " + localServices.descriptions.get(i) + "\n";
            stringToSend += "\n";
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringToSend);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            localIntent.finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {
//
    }
}