package com.example.motoapp.ui.service.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motoapp.R;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.example.motoapp.adapters.RecyclerAdapterExtendedTriggers;
import com.example.motoapp.adapters.RecyclerViewClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TriggerFragmentService extends Fragment implements RecyclerViewClickListner {

    private FragmentActivity localIntent;

    private RecyclerAdapterExtendedTriggers localAdapter;
    private DatabaseAdapter.TriggersAnswer localTriggers;
    private RecyclerView recyclerView;

    private DatabaseAdapter adapter;

    private FloatingActionButton backButtonHandler;
    private FloatingActionButton wrapAddingButtonHandler;
    private FloatingActionButton addTriggerButtonHandler;
    private EditText editTextToDoHandler;
    private EditText editTextMthHandler;
    private LinearLayout addingLayoutHandler;

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
        return inflater.inflate(R.layout.fragment_trigger_service, container, false);
    }//onCreateView

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localTriggers = adapter.getTriggers(Integer.valueOf(listener.getString("index")));
        backButtonHandler = (FloatingActionButton) view.findViewById(R.id.backBtn);
        wrapAddingButtonHandler = (FloatingActionButton) view.findViewById(R.id.wrapAddingButton);
        addTriggerButtonHandler = (FloatingActionButton) view.findViewById(R.id.addTriggerButton);
        editTextToDoHandler = (EditText) view.findViewById(R.id.editTextToDo);
        editTextMthHandler = (EditText) view.findViewById(R.id.editTextMth);
        addingLayoutHandler = (LinearLayout) view.findViewById(R.id.addingLayout);
        addingLayoutHandler.setVisibility(View.GONE);

        //Recycler View Settings
        recyclerView = (RecyclerView) view.findViewById(R.id.triggersList);
        localAdapter = new RecyclerAdapterExtendedTriggers(
                localIntent,
                Integer.valueOf(listener.getString("index")),
                localTriggers.indexes,
                localTriggers.whatToDo,
                localTriggers.mth,
                localTriggers.done,
                this);
        recyclerView.setAdapter(localAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(localIntent));

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(TriggerFragmentService.this)
                        .navigate(R.id.action_TriggerFragmentService_to_MenuFragmentService);
            }
        });

        wrapAddingButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingLayoutHandler.setVisibility(View.VISIBLE);
            }
        });

        addTriggerButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingLayoutHandler.setVisibility(View.GONE);
                Integer vehID = Integer.valueOf(listener.getString("index"));
                String whatToDo = editTextToDoHandler.getText().toString();
                Integer mth = Integer.valueOf(editTextMthHandler.getText().toString());
                adapter.addTrigger(vehID, whatToDo, mth);
                localAdapter.updateAdapter();
            }
        });
    }//onViewCreated

    @Override
    public void onItemClick(int position) {
//
    }
}