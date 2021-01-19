package com.example.motoapp.ui.garage.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.motoapp.R;
import com.example.motoapp.ui.garage.SingleMotoActivity;

public class MenuFragment extends Fragment {

    private ImageButton backButtonHandler;

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
        backButtonHandler = (ImageButton)view.findViewById(R.id.backButton);

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
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