package com.example.motoapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.motoapp.R;
import com.example.motoapp.adapters.DatabaseAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    Intent localIntent;
    Bundle localBundle;
    GoogleMap gMap;
    DatabaseAdapter adapter;
    String runTable;
    Button backButtonHandler;

    public Polyline polyline = null;
    public List<LatLng> latLngList = new ArrayList<>();
    public List<Marker> markerList = new ArrayList<>();

    int red = 0, green = 0, blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        localIntent = getIntent();
        localBundle = localIntent.getExtras();
        runTable = localBundle.getString("source");
        adapter = new DatabaseAdapter(this);
        backButtonHandler = (Button) findViewById(R.id.bt_back);

        backButtonHandler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.latLngList = adapter.getLocations(runTable);

        //Initialize SupportMapFragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        //Draw Polyline on Map
        if (polyline != null) polyline.remove();
        //CVreate PolylineOptions
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(latLngList).clickable(true);
        polyline = gMap.addPolyline(polylineOptions);
        polyline.setColor(Color.rgb(red,green, blue));
        MarkerOptions firstmarkerOptions = new MarkerOptions().position(latLngList.get(0));
        MarkerOptions lastmarkerOptions = new MarkerOptions().position(latLngList.get(latLngList.size()-1));

        gMap.addMarker(firstmarkerOptions);
        gMap.addMarker(lastmarkerOptions);

        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngList.get(0).latitude,
                latLngList.get(0).longitude), 12.0f));

        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
    }
}