package com.example.rumi.tourassistant;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class WeatherActivity extends AppCompatActivity {



    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView citySearchView;

    private FusedLocationProviderClient providerClient;


    private FragmentCurrentWeather currentWeatherPage;
    private FragmentFiveDayForecast fragmentFiveDayForecast;
    private final int MY_REQUEST_CODE = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        providerClient =
                LocationServices.getFusedLocationProviderClient(this);


        tabLayout = findViewById(R.id.weatherTabLayout);
        viewPager = findViewById(R.id.weatherViewPager);
        citySearchView = findViewById(R.id.cityWeatherSeachView);

        currentWeatherPage = new FragmentCurrentWeather();
        fragmentFiveDayForecast = new FragmentFiveDayForecast();


        tabLayout.addTab(tabLayout.newTab().setText("Current"));
        tabLayout.addTab(tabLayout.newTab().setText("5 Day Forecast"));

        tabLayout.setSelectedTabIndicatorColor(Color.rgb(255,255,255));
        tabLayout.setTabTextColors(Color.rgb(255,255,255),Color.rgb(255,255,255));

        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        citySearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentWeatherPage.updateWeatherByCityName(query);
                fragmentFiveDayForecast.updateWeatherByCityName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm ) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
               case 0:
                   currentWeatherPage.updateWeatherByCityName("dhaka");
                   return currentWeatherPage;
                case 1:
                    fragmentFiveDayForecast.updateWeatherByCityName("dhaka");
                    return fragmentFiveDayForecast;

            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }



    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_REQUEST_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(checkLocationPermission()){
                getUserLocation();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
        return;
    }

    private void getUserLocation(){
        if(checkLocationPermission()) {
            providerClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Toast.makeText(WeatherActivity.this, "Location is found" ,  Toast.LENGTH_SHORT).show();
                                currentWeatherPage.updateWeatherByLocation(location);
                                fragmentFiveDayForecast.updateWeatherByLocation(location);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkLocationPermission()){
            getUserLocation();
        }
    }
}
