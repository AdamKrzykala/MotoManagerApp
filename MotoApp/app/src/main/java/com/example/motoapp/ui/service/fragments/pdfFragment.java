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

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.artifex.mupdf.viewer.DocumentActivity;
import com.example.motoapp.R;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class pdfFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PDFView viewPDF = (PDFView) view.findViewById(R.id.pdfView);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        String fileName = new StringBuilder()
                .append(listener.getString("producent")).append("_")
                .append(listener.getString("model")).append("_")
                .append(listener.getString("year")).append(".pdf").toString();

        File file = new File(dir, fileName.replaceAll("\\s", ""));

        String toOpen = dir + "/" + fileName.replaceAll("\\s", "");
        Log.i("PATH: ", toOpen);
        //Uri uriToOpen = Uri.parse(toOpen);
        Uri uriToOpen = Uri.fromFile(file);
        Log.i("PATH: ", uriToOpen.toString());
        Log.i("URI: ", uriToOpen.toString());

        viewPDF.fromUri(uriToOpen).load();
    }
}