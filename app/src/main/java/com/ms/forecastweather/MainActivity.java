package com.ms.forecastweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout homeRl;
    private ProgressBar loadingPb;
    private TextView cityNameTv, tempratureTv, conditionTv;
    private RecyclerView weatherRv;
    private TextInputEditText cityEdit;
    private ImageView backIv, iconeIv, searchIv;
    private ArrayList<WeatherRvModel> weatherRvModelArrayList;
    private WeatherRvAdapter weatherRvAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FloatingActionButton fabHistory,fabBluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make application as Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);


        // Initialize the assign value
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        // Perform ItemselectionListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), BluetoothActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.setting:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.History:
                        startActivity(new Intent(getApplicationContext(), SearchHistoryActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("WeatherForecastHistory");
        // initials controls
        homeRl = findViewById(R.id.IdRLhome);
        loadingPb = findViewById(R.id.IdPbLoading);
        cityNameTv = findViewById(R.id.IdTcCityName);
        tempratureTv = findViewById(R.id.IdTvTemprature);
        conditionTv = findViewById(R.id.IdTvCondition);
        weatherRv = findViewById(R.id.IdRvWeather);
        cityEdit = findViewById(R.id.IdTeCity);
        backIv = findViewById(R.id.IdIvBack);
        iconeIv = findViewById(R.id.IdIvIcon);
        searchIv = findViewById(R.id.IdIvSearch);
        fabBluetooth=findViewById(R.id.IdFabBluetooth);
        fabHistory=findViewById(R.id.IdFabHistory);
        weatherRvModelArrayList = new ArrayList<>();
        weatherRvAdapter = new WeatherRvAdapter(this, weatherRvModelArrayList);
        weatherRv.setAdapter(weatherRvAdapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check for Location Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);

        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
          double longitude=location.getLongitude();
          double latitude=location.getLatitude();
        cityName=getCityName(longitude,latitude);
        getWeatherInformation(cityName);
            fabBluetooth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Bluetooth Clicked", Toast.LENGTH_SHORT).show();

                }
            });
            fabHistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "History Clicked", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(MainActivity.this, SearchHistoryActivity.class);
                    MainActivity.this.startActivity(myIntent);
                }
            });
         searchIv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String city=cityEdit.getText().toString();
                 if(city.isEmpty())
                     Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
             else {
                     cityNameTv.setText(cityName);
                     getWeatherInformation(city);
                 }
             }
         });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission granted..", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Please Provide Permission..", Toast.LENGTH_SHORT).show();
           finish();
            }
        }
    }

    private String getCityName(double Long , double Lat)
{
    String cityName="Not Found";
    Geocoder geocoder=new Geocoder(this, Locale.getDefault());
    try {
        List<Address> addresses=geocoder.getFromLocation(Lat,Long,1);
        for (Address adr : addresses)
        {
            if(adr!=null)
            {
                String city=adr.getLocality();
                if(city!=null && !city.equals(""))
                {
                    cityName = city;
                }
                else {
                    Log.d("TAG", "CITY NOT FOUND");
                    Toast.makeText(this, "Your City Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }catch (IOException e)
    {
        e.printStackTrace();
    }
    return  cityName;
}

    private void addDatatoFirebase(WeatherModel weatherModel) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(weatherModel.getId()).setValue(weatherModel);
               // Toast.makeText(MainActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWeatherInformation(String CityName) {
        String Url = "http://api.weatherapi.com/v1/forecast.json?key=55143957fcaf4f09939212529220704&q=" + CityName + "&days=1&aqi=no&alerts=no";
        cityNameTv.setText(CityName);
        Calendar c = Calendar.getInstance();
        String strCurrentDateTime = c.getTime().toString();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingPb.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherRvModelArrayList.clear();
                try {
                    String temperature= response.getJSONObject("current").getString("temp_c");
                    tempratureTv.setText(temperature+" ℃");
                    int isDay=response.getJSONObject("current").getInt("is_day");
                    String condition= response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon= response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconeIv);
                    conditionTv.setText(condition);
                    if(isDay==1)
                    {
                        //morning
                        Picasso.get().load("https://images.unsplash.com/photo-1564750975191-0ed807751adf?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=776&q=80").into(backIv);
                    }
                    else
                    {
                        //night
                        Picasso.get().load("https://images.unsplash.com/photo-1507400492013-162706c8c05e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=659&q=80").into(backIv);
                    }
                    JSONObject forecastObj=response.getJSONObject("forecast");
                    JSONObject forecast0=forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray=forecast0.getJSONArray("hour");
                    for (int i=0;i<hourArray.length();i++)
                    {
                        JSONObject hourObj=hourArray.getJSONObject(i);
                        String time=hourObj.getString("time");
                        String temper=hourObj.getString("temp_c");
                        String img=hourObj.getJSONObject("condition").getString("icon");
                        String wind=hourObj.getString("wind_kph");

                        WeatherModel weatherModel=new WeatherModel(strCurrentDateTime,CityName,time,temper,img,wind);
                        weatherRvModelArrayList.add(new WeatherRvModel(time,temper,img,wind));
                        weatherRvAdapter.notifyDataSetChanged();
                        addDatatoFirebase(weatherModel);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Please Enter Valid City Name", Toast.LENGTH_SHORT).show();
            }
        });
requestQueue.add(jsonObjectRequest);
    }
}