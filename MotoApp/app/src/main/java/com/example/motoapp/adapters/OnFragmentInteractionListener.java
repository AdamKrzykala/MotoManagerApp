package com.example.motoapp.adapters;

import android.content.Intent;

public interface OnFragmentInteractionListener {
    void onFragmentInteraction(int requestCode);
    void addServiceToDatabase(String name, String description);
    String getString(String key);
    void moveVehicleToService();
    void moveVehicleToGarage();
}
