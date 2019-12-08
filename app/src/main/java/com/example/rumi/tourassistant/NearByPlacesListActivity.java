package com.example.rumi.tourassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.Nearby.NearByPlaceAdapter;
import com.example.rumi.tourassistant.Nearby.NearbyResponse;
import com.example.rumi.tourassistant.Nearby.NearbyServiceApi;
import com.example.rumi.tourassistant.Nearby.Result;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NearByPlacesListActivity extends AppCompatActivity {


    private Retrofit retrofit;
    public static final String BASE_URL = "https://maps.googleapis.com/maps/api/";
    private NearbyServiceApi serviceApi;


    private RecyclerView recyclerView;
    private NearByPlaceAdapter placeAdapter;
    private  LinearLayoutManager llm;
    private Button btn1;

    public static List<Result> results = new ArrayList<>();

    private String pageToken = "";


    private FusedLocationProviderClient providerClient;


    private String placeType ;
    private int radius;

    private TextView dataFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places_list);

        providerClient = LocationServices.getFusedLocationProviderClient(this);
        recyclerView = findViewById(R.id.nearByrecyclerView);
        btn1 = findViewById(R.id.getMoreResultButton);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        serviceApi = retrofit.create(NearbyServiceApi.class);

        radius = getIntent().getIntExtra("range",100);
        placeType = getIntent().getStringExtra("placeType");

        dataFound = findViewById(R.id.dataFound);

        if(results.size()>0){
            dataFound.setVisibility(View.GONE);
        }else{
            dataFound.setVisibility(View.VISIBLE);
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
        if(checkLocationPermission()){
            getDeviceLastLocation();
        }
        if(results.size()>0){
            dataFound.setVisibility(View.GONE);
        }else{
            dataFound.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkLocationPermission(){
        if(ActivityCompat
                .checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    555
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 555
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getDeviceLastLocation();
        }else{
            Toast.makeText(
                    this,
                    "Permission denied",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void getDeviceLastLocation(){
        if(checkLocationPermission()){
            providerClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                getNearbyPlaces(latitude,longitude);
                            }
                        }
                    });
        }
    }

    private void getNearbyPlaces(double latitude, double longitude) {
        String apiKey = getString(R.string.near_by_api);
        String endUrl =
                String.format("place/nearbysearch/json?location=%f,%f&radius=%d&type=%s&key=%s",latitude,longitude,radius,placeType,apiKey);

        //Toast.makeText(NearByPlacesListActivity.this, latitude+ "/" + longitude , Toast.LENGTH_SHORT).show();
        //Toast.makeText(NearByPlacesListActivity.this, radius+ " / " + placeType , Toast.LENGTH_SHORT).show();

        serviceApi.getNearbyPlaces(endUrl)
                .enqueue(new Callback<NearbyResponse>() {
                    @Override
                    public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                        if(response.isSuccessful()){
                            NearbyResponse nearbyResponse =
                                    response.body();
                           // Toast.makeText(NearByPlacesListActivity.this,  nearbyResponse.getStatus()  , Toast.LENGTH_SHORT).show();
                            if(nearbyResponse.getNextPageToken() != null){
                                btn1.setVisibility(View.VISIBLE);
                                pageToken = nearbyResponse.getNextPageToken();
                            }else{
                                pageToken = "";
                                btn1.setVisibility(View.GONE);
                            }
                            results =
                                    nearbyResponse.getResults();
                            if(placeAdapter == null){
                                placeAdapter = new NearByPlaceAdapter(results);
                                llm = new LinearLayoutManager(NearByPlacesListActivity.this);
                                llm.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(llm);
                                recyclerView.setAdapter(placeAdapter);
                            }else {
                                placeAdapter.updateList(results);
                            }

                            if(nearbyResponse.getStatus().equals("ZERO_RESULTS")){
                                dataFound.setText("Nothing is found\nOn this range");
                            }

                            if(results.size()>0){
                                dataFound.setVisibility(View.GONE);
                            }else{
                                dataFound.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<NearbyResponse> call, Throwable t) {
                        Toast.makeText(NearByPlacesListActivity.this, " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(NearByPlacesListActivity.this, " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(NearByPlacesListActivity.this, " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(NearByPlacesListActivity.this, " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getMoreResults(View view) {
        if(!pageToken.isEmpty()){
            String apiKey = getString(R.string.near_by_api);
            String endUrl =
                    String.format("place/nearbysearch/json?pagetoken=%s&key=%s",pageToken,apiKey);
            serviceApi.getNextPageTokenResult(endUrl)
                    .enqueue(new Callback<NearbyResponse>() {
                        @Override
                        public void onResponse(Call<NearbyResponse> call, Response<NearbyResponse> response) {
                            if(response.isSuccessful()){
                                NearbyResponse nearbyResponse =
                                        response.body();

                                if(nearbyResponse.getNextPageToken() != null){
                                    pageToken = nearbyResponse.getNextPageToken();
                                }else{
                                    pageToken = "";
                                    btn1.setVisibility(View.GONE);
                                }

                                List<Result>newResults = nearbyResponse.getResults();
                                results.addAll(newResults);
                                placeAdapter.updateList(results);
                            }
                        }

                        @Override
                        public void onFailure(Call<NearbyResponse> call, Throwable t) {

                        }
                    });
        }
    }

    public void viewOnMapClicked(View view) {
        startActivity(new Intent(this , MapsActivity.class).putExtra("where",1));
    }
}
