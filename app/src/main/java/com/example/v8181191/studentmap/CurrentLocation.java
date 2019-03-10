package com.example.v8181191.studentmap;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentLocation extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
                                                            com.google.android.gms.location.LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final String TAG = "StudentMapApp";
    private android.location.Location mCurrentLocation;
    LatLng llCurrentLocation;
    Marker mCurrLocationMarker;
    DrawerLayout drawer;
    NavigationView navigationView;
    Double locLat, locLong, weatherCurrentTemp;
    TextView weather;
    Long currentTime, lastChecked, timeDifference, weatherSunrise, weatherSunset;
    String weatherMain, weatherDescription, weatherIcon, theWeather, sunriseTime, sunsetTime, weatherCTemp;
    int coverid;
    ImageView weatherImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentTime = 0L;
        lastChecked = 0L;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        weather = (findViewById(R.id.txtWeather));
        weatherImage = (findViewById(R.id.imgWeather));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
        mMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        locLat = this.mCurrentLocation.getLatitude();
        locLong = this.mCurrentLocation.getLongitude();
        getWeather();
    }

    @Override
    public void onConnectionSuspended(int i) {
        CharSequence text = "onConnectionSuspended executed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mCurrentLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        llCurrentLocation = new LatLng(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude());
        mCurrLocationMarker = mMap.addMarker(new MarkerOptions()
                .position(llCurrentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

        locLat = this.mCurrentLocation.getLatitude();
        locLong = this.mCurrentLocation.getLongitude();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(llCurrentLocation));

        //getWeather();
        //api.openweathermap.org/data/2.5/weather?lat=35&lon=139
    }

    public void getWeather() {
        timeDifference = currentTime - lastChecked;

        if (timeDifference < 600000) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://api.openweathermap.org/data/2.5/weather?lat=" + locLat + "&lon=" + locLong + "&appid=f45711a54b9a5af29393e044008a9538";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject weatherData = new JSONObject(response);

                                JSONArray wthr = weatherData.getJSONArray("weather");
                                JSONObject weather = wthr.getJSONObject(0);
                                weatherMain = weather.getString("main");
                                weatherDescription = weather.getString("description");
                                weatherIcon = weather.getString("icon");

                                JSONObject main = weatherData.getJSONObject("main");
                                weatherCurrentTemp = main.getDouble("temp");

                                JSONObject sys = weatherData.getJSONObject("sys");
                                weatherSunrise = sys.getLong("sunrise");
                                weatherSunset = sys.getLong("sunset");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            weatherCurrentTemp = weatherCurrentTemp - 273.15;
                            weatherCTemp = String.format("%.1f", weatherCurrentTemp);
                            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                            sunriseTime = sdf.format(new Date(weatherSunrise * 1000L));
                            sunsetTime = sdf.format(new Date(weatherSunset * 1000L));

                            theWeather = "Outlook: " + weatherDescription + "\n" +
                                    "Current Temperature: " + weatherCTemp + "°C\n" +
                                    "Sunrise: " + sunriseTime + " Sunset: " + sunsetTime;
                            weather.setText(theWeather);

                            switch (weatherIcon){
                                case "01d":
                                    coverid = getResources().getIdentifier("weather_one_d", "drawable", getPackageName());
                                    break;
                                case "01n":
                                    coverid = getResources().getIdentifier("weather_one_n", "drawable", getPackageName());
                                    break;
                                case "02d":
                                    coverid = getResources().getIdentifier("weather_two_d", "drawable", getPackageName());
                                    break;
                                case "02n":
                                    coverid = getResources().getIdentifier("weather_two_n", "drawable", getPackageName());
                                    break;
                                case "03d":
                                    coverid = getResources().getIdentifier("weather_03d", "drawable", getPackageName());
                                    break;
                                case "03n":
                                    coverid = getResources().getIdentifier("weather_03n", "drawable", getPackageName());
                                    break;
                                case "04d":
                                    coverid = getResources().getIdentifier("weather_04d", "drawable", getPackageName());
                                    break;
                                case "04n":
                                    coverid = getResources().getIdentifier("weather_04n", "drawable", getPackageName());
                                    break;
                                case "09d":
                                    coverid = getResources().getIdentifier("weather_09d", "drawable", getPackageName());
                                    break;
                                case "09n":
                                    coverid = getResources().getIdentifier("weather_09n", "drawable", getPackageName());
                                    break;
                                case "10d":
                                    coverid = getResources().getIdentifier("weather_10d", "drawable", getPackageName());
                                    break;
                                case "10n":
                                    coverid = getResources().getIdentifier("weather_10n", "drawable", getPackageName());
                                    break;
                                case "11d":
                                    coverid = getResources().getIdentifier("weather_11d", "drawable", getPackageName());
                                    break;
                                case "11n":
                                    coverid = getResources().getIdentifier("weather_11n", "drawable", getPackageName());
                                    break;
                                case "13d":
                                    coverid = getResources().getIdentifier("weather_13d", "drawable", getPackageName());
                                    break;
                                case "13n":
                                    coverid = getResources().getIdentifier("weather_13n", "drawable", getPackageName());
                                    break;
                                case "50d":
                                    coverid = getResources().getIdentifier("weather_50d", "drawable", getPackageName());
                                    break;
                                case "50n":
                                    coverid = getResources().getIdentifier("weather_50n", "drawable", getPackageName());
                                    break;
                            }

                            weatherImage.setImageResource(coverid);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   weather.setText("That didn't work!");
                }
            });

// Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
        lastChecked = currentTime;
        timeDifference = 0L;
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        CharSequence text = "onConnectionFailed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClinet connection has failed");
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_campus_map) {
            Intent intent = new Intent(this, CampusMap.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.action_current_Location) {
            //Intent intent = new Intent(this, CurrentLocation.class);//opens a new screen when the shopping list is clicked
            //startActivity(intent);
        } else if (id == R.id.action_place_search) {
            Intent intent = new Intent(this, PlacesSearch.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}