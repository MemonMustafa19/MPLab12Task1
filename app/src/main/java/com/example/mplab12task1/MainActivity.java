package com.example.mplab12task1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Location lastLocation;
    TextView textView;
    Geocoder geocoder;
    FusedLocationProviderClient fusedLocationProviderClient;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    public void onClick(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        }
        geocoder = new Geocoder(getBaseContext(), Locale.getDefault());


        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            lastLocation = location;

                            try {
                                List<Address> addresses = geocoder.getFromLocation(
                                        lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
                                Address address = addresses.get(0);
                                s = address.getAddressLine(0);
                                textView.setText(s);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            s = "No location available!";
                        }
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION: {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show();

                }
            }
        }

    }

}
