package com.example.motoapp.ui.garage.fragments;

import android.app.Activity;
import android.os.Bundle;
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
import com.example.motoapp.ui.garage.SingleMotoActivity;

public class MenuFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(listener.getString("name"));
        trashbuttonHandler = (ImageButton)view.findViewById(R.id.trashButton);
        backButtonHandler = (ImageButton)view.findViewById(R.id.backButton);

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

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(MenuFragment.this)
//                        .navigate(R.id.action_MenuFragment_to_TrackerFragment);
//            }
//        });
    }
}