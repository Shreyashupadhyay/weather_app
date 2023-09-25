package com.example.waetherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout home;
    private ProgressBar loading;
    private TextView cityName , Temp , conditions;
    private RecyclerView weather;
    private TextInputEditText cityedit;
    private ImageView back,icon,seach;
    private ArrayList<Weathermodel> weathermodelArrayList;
    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);
        home = findViewById(R.id.idRlHome);
        loading = findViewById(R.id.idprogrambarloading);
        cityName = findViewById(R.id.idTVcityName);
        Temp = findViewById(R.id.idTVTemp);
        conditions = findViewById(R.id.TVCondition);
        weather = findViewById(R.id.idRvweather);
        cityedit = findViewById(R.id.idEditcity);
        back = findViewById(R.id.idIVblack);
        icon = findViewById(R.id.idIVicon);
        seach = findViewById(R.id.idIVSeach);
        weathermodelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this,weathermodelArrayList);
        weather.setAdapter(weatherAdapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);

        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        cityNames = getCityName(location.getLongitude(), location.getLatitude());

        getWeatherinfo(cityNames);

        seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityedit.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter city Name", Toast.LENGTH_SHORT).show();

                }else{
                    cityName.setText(cityNames);
                    getWeatherinfo(city);
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Please provide the permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude , double latitude){
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());

        try{
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
            for(Address adr : addresses){
                if(adr!=null){
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                    }else
                    {
                        Log.d("TAG","CITY NOT FOUND");
                        Toast.makeText(this, "City not fount ", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return cityName;
    }


    private void getWeatherinfo (String cityyName){
        String url = "http://api.weatherapi.com/v1/forecast.json?key=b32f00e9f1184f129b9111849233101&q="+ cityyName + "&days=1&aqi=no&alerts=no";
        cityName.setText(cityyName);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);

                weathermodelArrayList.clear();
                try {
                    String temperatures = response.getJSONObject("current").getString("temp_c");
                    Temp.setText(temperatures+"°C");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(icon);
                    conditions.setText(condition);
                    if(isDay==1){
                        //morning
                        Picasso.get().load("https://pixabay.com/photos/sky-clouds-outdoors-cloudscape-7149717/").into(back);
                    }else{
                        Picasso.get().load("https://www.freepik.com/free-photo/silhouettes-hills-street-lamps-cloudy-sky-during-beautiful-sunset_12859312.htm#query=weather%20night&position=3&from_view=keyword").into(back);
                    }

                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forcast0 = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forcast0.getJSONArray("hour");
                     for(int i =0;i<hourArray.length();i++){
                         JSONObject hourObj = hourArray.getJSONObject(i);
                         String time = hourObj.getString("time");
                         String temper = hourObj.getString("temp_c");
                         String img = hourObj.getJSONObject("condition").getString("icon");
                         String wind = hourObj.getString("wind_kph");
                         weathermodelArrayList.add(new Weathermodel(time,temper,img,wind));
                     }
                     weatherAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please valid city name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

}