package com.example.rumi.tourassistant.EventClasses;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rumi.tourassistant.ForecastClasses.ForecastList;
import com.example.rumi.tourassistant.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder> {

    private List<ForecastList> ForecastList;
    private SimpleDateFormat  dateFormatter ;
    private static DecimalFormat df2 = new DecimalFormat(".#");

    public ForecastListAdapter(List<ForecastList> ForecastList) {
        this.ForecastList = ForecastList;
        dateFormatter = new SimpleDateFormat("HH:mm EEE dd-MM-yyyy");
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_recycler_layout,parent,false);
        ForecastViewHolder forecastViewHolder = new ForecastViewHolder(view);
        return forecastViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {

        holder.date.setText(dateFormatter.format(ForecastList.get(position).getDt()*1000L));
        holder.temp.setText(df2.format(ForecastList.get(position).getMain().getTemp()-273.15) + "°c");
        holder.mintemp.setText(Double.toString(Math.round(ForecastList.get(position).getMain().getTempMin()-273.15))+ "°c");
        holder.maxtemp.setText(Double.toString(Math.round(ForecastList.get(position).getMain().getTempMax()-273.15))+ "°c");
        holder.air.setText(Double.toString(Math.round(ForecastList.get(position).getWind().getSpeed())) + "km/h");
        holder.hum.setText(Double.toString(Math.round(ForecastList.get(position).getMain().getHumidity()))+"%");
        holder.cloud.setText(Double.toString(Math.round(ForecastList.get(position).getClouds().getAll()))+"%");
        holder.pressure.setText(Double.toString(Math.round(ForecastList.get(position).getMain().getPressure()))+"hpa");
        holder.weatherDetail.setText(ForecastList.get(position).getWeather().get(0).getDescription());
        Picasso.get().load("https://openweathermap.org/img/w/"+ ForecastList.get(position).getWeather().get(0).getIcon()+".png").into(holder.weatherImage);
    }

    @Override
    public int getItemCount() {
        return ForecastList.size();
    }



    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        private TextView date , temp , mintemp , maxtemp ,  weatherDetail , air , hum , cloud , pressure;
        private ImageView weatherImage;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateForForecastWeatherTextView);
            temp = itemView.findViewById(R.id.tempForForecastWeatherTextView);
            mintemp = itemView.findViewById(R.id.minForecastTempTextView);
            maxtemp = itemView.findViewById(R.id.maxForecastTempTextView);
            weatherDetail = itemView.findViewById(R.id.ForecastweatherDetailTextView);
            weatherImage = itemView.findViewById(R.id.ForecastweatherImageView);
            air = itemView.findViewById(R.id.ForecastWeatherWindSpeed);
            hum = itemView.findViewById(R.id.ForecastWeatherHumidity);
            cloud = itemView.findViewById(R.id.ForecastWeatherCloud);
            pressure = itemView.findViewById(R.id.ForecastWeatherPressure);

        }
    }

    public void updateList(List<ForecastList>list){
        this.ForecastList = list;
    }

}
