package com.example.rumi.tourassistant;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rumi.tourassistant.EventClasses.ForecastListAdapter;
import com.example.rumi.tourassistant.ForecastClasses.ForeCastWeather;
import com.example.rumi.tourassistant.ForecastClasses.ForecastServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFiveDayForecast extends Fragment {

    public static final String BASE_URL = "https://api.openweathermap.org/";
    private Retrofit retrofit;
    private ForecastServiceApi forecastServiceApi;

    private TextView cityName;
    private RecyclerView forecastRecyclerView;
    private ForecastListAdapter forecastListAdapter;
    private LinearLayoutManager llm;
    private Context context;


    public FragmentFiveDayForecast() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        forecastServiceApi = retrofit.create(ForecastServiceApi.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        this.context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_fragment_five_day_forecast, container, false);
        cityName = view.findViewById(R.id.cityNameForForecast);
        forecastRecyclerView = view.findViewById(R.id.recyclerViewForForecast);
        return view;
    }

    public void updateWeatherByCityName(String mycity){

        String API = "80b24926b914740726365bd4a1b57eaf";
        String urlString = String.format("data/2.5/forecast?q=%s&appid=%s",mycity,API);

        forecastServiceApi.getForecastWeather(urlString).enqueue(new Callback<ForeCastWeather>() {
            @Override
            public void onResponse(Call<ForeCastWeather> call, Response<ForeCastWeather> response) {
                if(response.isSuccessful()){
                    ForeCastWeather foreCastWeather = response.body();
                    cityName.setText(foreCastWeather.getCity().getName());
                    forecastListAdapter = new ForecastListAdapter(foreCastWeather.getList());
                    llm = new LinearLayoutManager(context);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    forecastRecyclerView.setLayoutManager(llm);
                    forecastRecyclerView.setAdapter(forecastListAdapter);
                }
            }
            @Override
            public void onFailure(Call<ForeCastWeather> call, Throwable t) {
                Toast.makeText( getContext(), "Failed " , Toast.LENGTH_SHORT).show();
                Log.e("POKA", "onFailure: " + t.getMessage() );
            }
        });

    }
    public void updateWeatherByLocation(Location myLocation){

        String API = "80b24926b914740726365bd4a1b57eaf";
        String urlString = "data/2.5/forecast?lat="+myLocation.getLatitude()+"&lon="+myLocation.getLongitude()+"&appid="+ API ;

        forecastServiceApi.getForecastWeather(urlString).enqueue(new Callback<ForeCastWeather>() {
            @Override
            public void onResponse(Call<ForeCastWeather> call, Response<ForeCastWeather> response) {
                if(response.isSuccessful()){
                    ForeCastWeather foreCastWeather = response.body();
                    cityName.setText(foreCastWeather.getCity().getName());
                    forecastListAdapter = new ForecastListAdapter(foreCastWeather.getList());
                    llm = new LinearLayoutManager(context);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    forecastRecyclerView.setLayoutManager(llm);
                    forecastRecyclerView.setAdapter(forecastListAdapter);
                }
            }
            @Override
            public void onFailure(Call<ForeCastWeather> call, Throwable t) {
                Toast.makeText( getContext(), "Failed " , Toast.LENGTH_SHORT).show();
                Log.e("POKA", "onFailure: " + t.getMessage() );
            }
        });

    }

}
