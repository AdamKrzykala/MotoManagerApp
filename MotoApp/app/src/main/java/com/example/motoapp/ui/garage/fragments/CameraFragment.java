package com.example.motoapp.ui.garage.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.motoapp.BuildConfig;
import com.example.motoapp.R;
import com.example.motoapp.adapters.OnFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CameraFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 228;
    private static final int REQUEST_PERMISSION_CAMERA_AND_WRITE_EXTERNAL = 4192;
    private ImageView imageViewHandler;
    private FloatingActionButton nextButtonHandler;
    private FloatingActionButton backButtonHandler;
    private String currentPhotoPath;
    private OnFragmentInteractionListener listener;

    Uri currentPictureUri = null;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        if(activity instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener)activity;
        } else {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dispatchTakePictureIntent();

        imageViewHandler = (ImageView)view.findViewById(R.id.imageView);
        nextButtonHandler = (FloatingActionButton) view.findViewById(R.id.nextButton);
        backButtonHandler = (FloatingActionButton) view.findViewById(R.id.backButton);

        nextButtonHandler.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(CameraFragment.this)
                        .navigate(R.id.action_CameraFragment_to_MenuFragment);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void dispatchTakePictureIntent() {
        if(getContext().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            && getContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            invokeCamera();
        } else {
            //Request permission
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, REQUEST_PERMISSION_CAMERA_AND_WRITE_EXTERNAL);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA_AND_WRITE_EXTERNAL:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    Log.e("PERMISSIONS: ", "I don't have permissions for camera or for write external");
                }
        }
    }

    private void invokeCamera() {
        Uri pictureUri =  FileProvider.getUriForFile(getContext(),
                getActivity().getApplicationContext().getPackageName() + ".provider",
                createImageFile());

        currentPictureUri = pictureUri;

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }
    
    private File createImageFile() {
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //timestamp makes unique names
        String localPath = "/moto" + listener.getString("index");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp =  sdf.format(new Date());
        File imageFile = new File(picturesDirectory + localPath, "picture" + timeStamp + ".jpg");
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageViewHandler.setImageURI(currentPictureUri);
        }
    }

}