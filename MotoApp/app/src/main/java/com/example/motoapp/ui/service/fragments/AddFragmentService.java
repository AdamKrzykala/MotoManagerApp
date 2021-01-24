package com.example.motoapp.ui.service.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.artifex.mupdf.viewer.DocumentActivity;
import com.example.motoapp.R;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;

public class AddFragmentService extends Fragment {

    private FloatingActionButton addBtnHandler;
    private EditText name;
    private EditText description;
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
        return inflater.inflate(R.layout.fragment_add_service, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addBtnHandler = (FloatingActionButton)view.findViewById(R.id.addBtn);
        name = (EditText)view.findViewById(R.id.name);
        description = (EditText)view.findViewById(R.id.description);

        addBtnHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saving to database
                String nameToSend = name.getText().toString();
                String descriptionToSend = description.getText().toString();
                listener.addServiceToDatabase(nameToSend, descriptionToSend);
                NavHostFragment.findNavController(AddFragmentService.this)
                        .navigate(R.id.action_AddFragmentService_to_MenuFragmentService);
            }
        });
    }
}