package com.example.motoapp.ui.garage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.motoapp.NewMotoActivity;
import com.example.motoapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.ClipData.newIntent;

public class GarageFragment extends Fragment {

    private Intent chooseIntent;
    private FloatingActionButton fabHandler;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_garage, container, false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        try
        {
            if((requestCode== 101 ) && (resultCode== Activity.RESULT_OK))
            {
                Bundle myResultBundle= data.getExtras();
                Double myResult= myResultBundle.getDouble("vresult");
                Toast.makeText(getActivity(), (String)myResult.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(), "No data - error",
                    Toast.LENGTH_LONG).show();
        }}//onActivityResult

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chooseIntent = new Intent(getActivity(), NewMotoActivity.class);
        fabHandler = (FloatingActionButton)(getView().findViewById(R.id.fab));

        fabHandler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double v1 = Double.parseDouble("111");
                Double v2 = Double.parseDouble("222");

                Bundle myDataBundle = new Bundle();
                myDataBundle.putDouble("val1", v1);
                myDataBundle.putDouble("val2", v2);
                chooseIntent.putExtras(myDataBundle);
                startActivityForResult(chooseIntent, 101);
            }
        });
    }
}