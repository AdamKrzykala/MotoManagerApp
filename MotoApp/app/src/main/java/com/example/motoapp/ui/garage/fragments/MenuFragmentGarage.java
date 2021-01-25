package com.example.motoapp.ui.garage.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.motoapp.R;
import com.example.motoapp.adapters.OnFragmentInteractionListener;

import java.io.File;

public class MenuFragmentGarage extends Fragment {

    private ImageButton trackerButtonHandler;
    private ImageButton cameraButtonHandler;
    private ImageButton galleryButtonHandler;
    private ImageButton serviceButtonHandler;
    private ImageButton trashbuttonHandler;
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
        return inflater.inflate(R.layout.fragment_menu_garage, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(listener.getString("name"));

        trackerButtonHandler = (ImageButton)view.findViewById(R.id.addButton);
        cameraButtonHandler = (ImageButton)view.findViewById(R.id.triggerButton);
        galleryButtonHandler = (ImageButton)view.findViewById(R.id.manualButton);
        serviceButtonHandler = (ImageButton)view.findViewById(R.id.registerButton);
        trashbuttonHandler = (ImageButton)view.findViewById(R.id.garageButton);
        backButtonHandler = (ImageButton)view.findViewById(R.id.backButton);

        trackerButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                NavHostFragment.findNavController(MenuFragmentGarage.this)
                        .navigate(R.id.action_MenuFragment_to_TrackerFragment);
            }
        });

        cameraButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MenuFragmentGarage.this)
                        .navigate(R.id.action_MenuFragment_to_CameraFragment);
            }
        });

        galleryButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(sdDir + "/moto" + String.valueOf(listener.getString("index")));

                if (file.isDirectory()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.fromFile(file));
                    intent.setType("image/*");
                    startActivity(intent);
                }

            }
        });

        serviceButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request to move to service
                listener.moveVehicleToService();
            }
        });

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });

        trashbuttonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Request to delete
                listener.onFragmentInteraction(501);
            }
        });
    }
}