package com.example.motoapp.ui.garage.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.motoapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView imageViewHandler;
    private Button nextButtonHandler;
    private Button backButtonHandler;
    private String currentPhotoPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.dispatchTakePictureIntent();
        return inflater.inflate(R.layout.fragment_camera, container, false);
        //return null;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageViewHandler = (ImageView)view.findViewById(R.id.imageView);
        nextButtonHandler = (Button)view.findViewById(R.id.nextButton);
        backButtonHandler = (Button)view.findViewById(R.id.backButton);

        nextButtonHandler.setOnClickListener(new View.OnClickListener() {
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri outPutFileUri = Uri.fromFile(photoFile);
            if(photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outPutFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewHandler.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg",storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


}