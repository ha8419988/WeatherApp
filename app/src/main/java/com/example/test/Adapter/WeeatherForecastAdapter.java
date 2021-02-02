package com.example.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Common.Common;
import com.example.test.Model.WeatherForecastResult;
import com.example.test.R;
import com.squareup.picasso.Picasso;

public class WeeatherForecastAdapter extends RecyclerView.Adapter<WeeatherForecastAdapter.MyViewHolder> {
    Context context;
    WeatherForecastResult weatherForecastResult;

    public WeeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//load icon
        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(position).weather.get(0).getIcon()).append(".png").toString()).into(holder.img);
    holder.txt_date_time.setText(new StringBuilder(Common.convertToDate(weatherForecastResult.list.get(position).dt)));
    holder.txt_des.setText(new StringBuilder(weatherForecastResult.list.get(position).weather.get(0).getDescription()));
    holder.txt_temp.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C"));

    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date_time, txt_des, txt_temp;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_date_time = itemView.findViewById(R.id.txt_date);
            txt_des = itemView.findViewById(R.id.txt_des);
            txt_temp = itemView.findViewById(R.id.temp1);
            img = itemView.findViewById(R.id.img_weather);
        }
    }
}
