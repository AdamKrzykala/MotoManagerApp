package com.example.motoapp.ui.service.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.motoapp.R;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.artifex.mupdf.viewer.DocumentActivity;
import com.example.motoapp.ui.garage.fragments.MenuFragmentGarage;

import java.io.File;

public class MenuFragmentService extends Fragment {

    private static final int REQUEST_PERMISSION_READ_EXTERNAL = 4193;
    private ImageButton addButtonHandler;
    private ImageButton triggerButtonHandler;
    private ImageButton manualButtonHandler;
    private ImageButton registerButtonHandler;
    private ImageButton garagebuttonHandler;
    private ImageButton backButtonHandler;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_service, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(listener.getString("name") + " - service");

        addButtonHandler = (ImageButton)view.findViewById(R.id.addButton);
        triggerButtonHandler = (ImageButton)view.findViewById(R.id.triggerButton);
        manualButtonHandler = (ImageButton)view.findViewById(R.id.manualButton);
        registerButtonHandler = (ImageButton)view.findViewById(R.id.registerButton);
        garagebuttonHandler = (ImageButton)view.findViewById(R.id.garageButton);
        backButtonHandler = (ImageButton)view.findViewById(R.id.backButton);

        addButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(MenuFragmentService.this)
                        .navigate(R.id.action_MenuFragmentService_to_AddFragmentService);
            }
        });

        triggerButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MenuFragmentService.this)
                        .navigate(R.id.action_MenuFragmentService_to_TriggerFragmentService);
            }
        });

        registerButtonHandler.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MenuFragmentService.this)
                        .navigate(R.id.action_MenuFragmentService_to_AllServicesFragmentService);
            }
        });

        manualButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Opening PDF
                File dir = new File("content://com.android.providers.downloads.documents/document");
                String fileName = listener.getString("name");
                File fileToOpen = new File(dir,"5");
                String toOpen = "content://com.android.providers.downloads.documents/document/downloads/"
                        + "2" + ".pdf";
                Uri uriToOpen = Uri.parse(toOpen);
                Log.i("URI: ", uriToOpen.toString());
                Intent pdfIntent = new Intent(getActivity(), DocumentActivity.class);
                pdfIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                pdfIntent.setData(uriToOpen);
                startActivity(pdfIntent);
            }
        });

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        garagebuttonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request to move to garage
                listener.moveVehicleToGarage();
            }
        });
    }
}