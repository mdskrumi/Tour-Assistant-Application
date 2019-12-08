package com.example.rumi.tourassistant;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.WeatherClasses.CurrentWeather;
import com.example.rumi.tourassistant.WeatherClasses.CurrentWeatherServiceApi;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCurrentWeather extends Fragment {

    public static final String BASE_URL = "https://api.openweathermap.org/";
    private Retrofit retrofit;
    private CurrentWeatherServiceApi currentWeatherServiceApi;
    private SimpleDateFormat dateFormatter;

    private TextView cityName , date , temp , mintemp , maxtemp ,  weatherDetail , air , hum , cloud , pressure;

    private ImageView weatherImage;
    private static DecimalFormat df2 = new DecimalFormat(".#");

    public FragmentCurrentWeather() {
        // Required empty public constructor
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        currentWeatherServiceApi = retrofit.create(CurrentWeatherServiceApi.class);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_weather, container, false);

        cityName = view.findViewById(R.id.cityNameForCurrentWeatherTextView);
        date = view.findViewById(R.id.dateForCurrentWeatherTextView);
        temp = view.findViewById(R.id.tempForCurrentWeatherTextView);
        mintemp = view.findViewById(R.id.minTempTextView);
        maxtemp = view.findViewById(R.id.maxTempTextView);
        weatherDetail = view.findViewById(R.id.weatherDetailTextView);
        weatherImage = view.findViewById(R.id.weatherImageView);
        air = view.findViewById(R.id.currentWeatherWindSpeed);
        hum = view.findViewById(R.id.currentWeatherHumidity);
        cloud = view.findViewById(R.id.currentWeatherCloud);
        pressure = view.findViewById(R.id.currentWeatherPressure);


        return view;
    }

    public void updateWeatherByCityName(String mycity){
        String API = "80b24926b914740726365bd4a1b57eaf";
        String urlString = String.format("data/2.5/weather?q=%s&appid=%s",mycity,API);

        currentWeatherServiceApi.getCurrentWeather(urlString).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if(response.isSuccessful()){
                    CurrentWeather currentWeather = response.body();

                    cityName.setText(currentWeather.getName());
                    date.setText(dateFormatter.format(currentWeather.getDt()*1000L));
                    temp.setText(df2.format(currentWeather.getMain().getTemp()-273.15) + "°c");
                    mintemp.setText(Double.toString(Math.round(currentWeather.getMain().getTempMin()-273.15))+ "°c");
                    maxtemp.setText(Double.toString(Math.round(currentWeather.getMain().getTempMax()-273.15))+ "°c");
                    air.setText(Double.toString(Math.round(currentWeather.getWind().getSpeed())) + "km/h");
                    hum.setText(Double.toString(Math.round(currentWeather.getMain().getHumidity()))+"%");
                    cloud.setText(Double.toString(Math.round(currentWeather.getClouds().getAll()))+"%");
                    pressure.setText(Double.toString(Math.round(currentWeather.getMain().getPressure()))+"hpa");
                    weatherDetail.setText(currentWeather.getWeather().get(0).getDescription());
                    Picasso.get().load("https://openweathermap.org/img/w/"+ currentWeather.getWeather().get(0).getIcon()+".png").into(weatherImage);

                }
            }
            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Toast.makeText( getContext(), "Failed " , Toast.LENGTH_SHORT).show();
                Log.e("POKA", "onFailure: " + t.getMessage() );
            }
        });

    }
    public void updateWeatherByLocation(Location myLocation){
        String API = "80b24926b914740726365bd4a1b57eaf";
       // String urlString = String.format("data/2.5/weather?lat=%lf&lon=%lf&appid=%s",myLocation.getLatitude(),myLocation.getLongitude(),API);
        String urlString = "data/2.5/weather?lat="+myLocation.getLatitude()+"&lon="+myLocation.getLongitude()+"&appid="+ API ;

        currentWeatherServiceApi.getCurrentWeather(urlString).enqueue(new Callback<CurrentWeather>() {
            @Override
            public void onResponse(Call<CurrentWeather> call, Response<CurrentWeather> response) {
                if(response.isSuccessful()){
                    CurrentWeather currentWeather = response.body();
                    cityName.setText(currentWeather.getName());
                    date.setText(dateFormatter.format(currentWeather.getDt()*1000L));
                    temp.setText(df2.format(currentWeather.getMain().getTemp()-273.15) + "°c");
                    mintemp.setText(Double.toString(Math.round(currentWeather.getMain().getTempMin()-273.15))+ "°c");
                    maxtemp.setText(Double.toString(Math.round(currentWeather.getMain().getTempMax()-273.15))+ "°c");
                    air.setText(Double.toString(Math.round(currentWeather.getWind().getSpeed())) + "km/h");
                    hum.setText(Double.toString(Math.round(currentWeather.getMain().getHumidity()))+"%");
                    cloud.setText(Double.toString(Math.round(currentWeather.getClouds().getAll()))+"%");
                    pressure.setText(Double.toString(Math.round(currentWeather.getMain().getPressure()))+"hpa");
                    weatherDetail.setText(currentWeather.getWeather().get(0).getDescription());
                    Picasso.get().load("https://openweathermap.org/img/w/"+ currentWeather.getWeather().get(0).getIcon()+".png").into(weatherImage);

                }
            }
            @Override
            public void onFailure(Call<CurrentWeather> call, Throwable t) {
                Toast.makeText( getContext(), "Failed " , Toast.LENGTH_SHORT).show();
                Log.e("POKA", "onFailure: " + t.getMessage() );
            }
        });

    }

}















