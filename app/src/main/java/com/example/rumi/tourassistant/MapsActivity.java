package com.example.rumi.tourassistant;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rumi.tourassistant.Nearby.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MY MAP ACTIVITY : ";
    private GoogleMap mMap;
    private PlaceAutocompleteFragment placeAutoComplete;
    private final int MY_REQUEST_CODE = 999;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private GeofencingClient geofencingClient;
    private GeofencingRequest.Builder geofencingRequest;
    private PendingIntent pendingIntent = null;
    private List<Geofence> geofences = new ArrayList<>();

    private int where = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        geofencingClient = LocationServices.getGeofencingClient(this);

        placeAutoComplete = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),14));
                Log.i(TAG, "Place: " + place.getLatLng().latitude + " " + place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });




        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        where = getIntent().getIntExtra("where",0);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(checkLocationPermission()) {
            mMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                LatLng myLatLng = new LatLng(location.getLatitude() , location.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng,14));
                            }
                        }
                    });

            if(where==1){
                List<Result> results =  NearByPlacesListActivity.results;
                if(results != null){
                    for(Result r : results){
                        LatLng latLng = new LatLng(r.getGeometry().getLocation().getLat() , r.getGeometry().getLocation().getLng());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(r.getName()).snippet(r.getVicinity()));
                    }

                }
            }
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                View view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.create_geofence_layout,null);

                final EditText geofenceId = view.findViewById(R.id.identifierGeofenceET);
                final Button create , cancel;
                create = view.findViewById(R.id.createGeofenceButton);
                cancel = view.findViewById(R.id.cancelGeofenceButton);

                final AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this)
                        .setView(view)
                        .show();

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(geofenceId.getText().toString().isEmpty()){
                            geofenceId.setError("This Field is Required");
                            return;
                        }
                        String id = geofenceId.getText().toString();
                        Geofence myGeofence = new Geofence.Builder()
                                .setRequestId(id)
                                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER )
                                .setCircularRegion(latLng.latitude,latLng.longitude,200)
                                .setExpirationDuration(24 * 60 * 60 * 1000)
                                .build();
                        geofences.add(myGeofence);
                       // Toast.makeText(MapsActivity.this, " " + geofences.size() , Toast.LENGTH_SHORT).show();

                        if(checkLocationPermission()){
                            geofencingClient.addGeofences(getRequest(),getPendingIntent())
                                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MapsActivity.this, "geofence added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, "onFailure: " + e.getMessage() );
                                        }
                                    });
                        }
                        alertDialog.cancel();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });


            }
        });


        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(1);
    }

    public void changeMapTypeButtonPressed(View view) {
        if(mMap.getMapType()==1){
            mMap.setMapType(2);
        }
        else{
            mMap.setMapType(1);
        }
    }

    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_CODE);
            return false;
        };
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    private GeofencingRequest getRequest(){
        geofencingRequest = new GeofencingRequest.Builder();
        geofencingRequest.addGeofences(geofences);
        geofencingRequest.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        return geofencingRequest.build();
    }

    private PendingIntent getPendingIntent(){
        if(pendingIntent != null){
            return pendingIntent;
        }
        Intent intent = new Intent(MapsActivity.this,GeofenceService.class);
        pendingIntent = PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkLocationPermission() && geofences != null && geofences.size() > 0){
            geofencingClient.addGeofences(getRequest(),getPendingIntent())
                    .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MapsActivity.this, "geofence added successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
