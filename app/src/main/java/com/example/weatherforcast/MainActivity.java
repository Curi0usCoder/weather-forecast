package com.example.weatherforcast;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.View;
import android.widget.AdapterView;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Spinner citySpinner;
    ListView weatherList;
    ArrayAdapter<String> weatherAdapter;

    String[] cities = {"Chennai", "Kochi", "Delhi", "Mumbai", "Bangalore", "Kolkata", "Hyderabad", "Pune", "Ahmedabad"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySpinner = findViewById(R.id.city_spinner);
        weatherList = findViewById(R.id.weather_list);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
        citySpinner.setAdapter(cityAdapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cities[position];
                new FetchWeather().execute(selectedCity);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    class FetchWeather extends AsyncTask<String, Void, ArrayList<WeatherItem>> {
        protected ArrayList<WeatherItem> doInBackground(String... params) {
            ArrayList<WeatherItem> forecastList = new ArrayList<>();
            try {
                String city = params[0];
                String apiUrl = "https://api.weatherapi.com/v1/forecast.json?key=2fb9a1303ab4445681f173348251104&q=" +
                        city + "&days=5&aqi=no&alerts=no";

                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                StringBuilder result = new StringBuilder();
                int data = reader.read();
                while (data != -1) {
                    result.append((char) data);
                    data = reader.read();
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray forecastArray = jsonObject.getJSONObject("forecast").getJSONArray("forecastday");

                for (int i = 0; i < forecastArray.length(); i++) {
                    JSONObject dayObject = forecastArray.getJSONObject(i);
                    String date = dayObject.getString("date");
                    JSONObject day = dayObject.getJSONObject("day");
                    String condition = day.getJSONObject("condition").getString("text");
                    String iconUrl = day.getJSONObject("condition").getString("icon");
                    double maxTemp = day.getDouble("maxtemp_c");
                    double minTemp = day.getDouble("mintemp_c");

                    String displayDate;
                    switch (i) {
                        case 0: displayDate = "Today"; break;
                        case 1: displayDate = "Tomorrow"; break;
                        default: displayDate = "Day " + (i + 1); break;
                    }

                    forecastList.add(new WeatherItem(displayDate, condition, iconUrl, maxTemp, minTemp));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return forecastList;
        }

        protected void onPostExecute(ArrayList<WeatherItem> result) {
            WeatherAdapter adapter = new WeatherAdapter(MainActivity.this, result);
            weatherList.setAdapter(adapter);
        }
    }

}
