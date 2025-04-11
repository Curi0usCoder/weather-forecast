package com.example.weatherforcast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter<WeatherItem> {

    public WeatherAdapter(Context context, ArrayList<WeatherItem> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        WeatherItem item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.weather_item, parent, false);

        TextView date = convertView.findViewById(R.id.date);
        TextView condition = convertView.findViewById(R.id.condition);
        TextView temp = convertView.findViewById(R.id.temp);
        ImageView icon = convertView.findViewById(R.id.icon);

        date.setText(item.date);
        condition.setText(item.condition);
        temp.setText(item.maxTemp + "°/" + item.minTemp + "°");

        Picasso.get().load("https:" + item.iconUrl).into(icon);

        return convertView;
    }
}

