package com.example.rumi.tourassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;


public class NearByPlacesActivity extends AppCompatActivity {


    private Button hotelNB , restaurantNB , bankNB , atmNB , policeNB , hospitalNB , busNB , trainNB , taxiNB , mallNB;
    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by_places);

        spinner = findViewById(R.id.nearByRangeSpn);

        hotelNB = findViewById(R.id.nearByHotelButton);
        restaurantNB = findViewById(R.id.nearByrestaurantButton);
        bankNB = findViewById(R.id.nearByBankButton);
        atmNB = findViewById(R.id.nearByAtmButton);
        policeNB = findViewById(R.id.nearByPoliceButton);
        hospitalNB = findViewById(R.id.nearByHospitalButton);
        busNB = findViewById(R.id.nearByBusButton);
        trainNB = findViewById(R.id.nearBytrainButton);
        taxiNB = findViewById(R.id.nearBytaxiButton);
        mallNB = findViewById(R.id.nearBymallButton);


        hotelNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","cafe"));
            }
        });

        taxiNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","taxi_stand"));
            }
        });

        mallNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","shopping_mall"));
            }
        });


        restaurantNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","restaurant"));
            }
        });


        bankNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","bank"));
            }
        });


        atmNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","atm"));
            }
        });


        policeNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","police"));
            }
        });


        hospitalNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","hospital"));
            }
        });

        busNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","bus_station"));
            }
        });

        trainNB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NearByPlacesActivity.this , NearByPlacesListActivity.class)
                        .putExtra("range" , Integer.parseInt(spinner.getSelectedItem().toString()))
                        .putExtra("placeType","train_station"));
            }
        });

    }
}
