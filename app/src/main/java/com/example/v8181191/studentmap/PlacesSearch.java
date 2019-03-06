package com.example.v8181191.studentmap;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;



public class PlacesSearch extends AppCompatActivity implements SearchBoxFragment.SearchListener, SearchResultsFragment.OnFragmentInteractionListener/*, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks*/{

    private final String TAG = "StudentMapApp";
    String url, placePhoto, placeOpenTimes, placeType, kept;
    GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    Double locLat, locLong;
    ArrayList<PlaceItems> placeList;
    TextView json;

    ListView results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_search);
        //this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        /*mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();

        json = findViewById(R.id.txtJson);
        results = findViewById(R.id.lvResults);*/

    }

   /* public void getPlaces(String url){

        placeList = new ArrayList<PlaceItems>();//sets up an array list called placeList
        placeList.clear();//clear the placeList array

        RequestQueue queue = Volley.newRequestQueue(this); //sets up a reference to the volley library queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject placeData = new JSONObject(response);
                            //json.setText(response);
                            JSONArray places = placeData.getJSONArray("results");

                            for (int i = 0; i < places.length(); i++){

                            PlaceItems placeListItems = new PlaceItems();
                            //json.setText(""+places.length());
                            try {
                                kept = places.getJSONObject(i).getString("vicinity");
                                kept = kept.substring(0, kept.indexOf(","));
                            } catch(org.json.JSONException exception){
                                kept = "none";
                            }

                            placeListItems.setPlaceAddress(kept);
                            placeListItems.setLat(places.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                            placeListItems.setLng(places.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                            placeListItems.setPlaceName(places.getJSONObject(i).getString("name"));
                            placeListItems.setPlaceId(places.getJSONObject(i).getString("id"));

                            try {
                                JSONArray photoItemArray = places.getJSONObject(i).getJSONArray("photos");
                                for (int j=0; j < photoItemArray.length(); j++){
                                    placePhoto = photoItemArray.getJSONObject(j).getString("photo_reference");
                                }

                            } catch(org.json.JSONException exception){
                                    placePhoto = "none";
                            }

                            String pn = placePhoto;
                            Log.i("StudMap", ""+pn);
                            placeListItems.setPlacePhoto(placePhoto);

                            try {
                                placeOpenTimes = places.getJSONObject(i).getJSONObject("opening_hours").getString("open_now");
                                } catch(org.json.JSONException exception){
                            }

                            Log.i("StudMap", ""+placeOpenTimes);
                            placeListItems.setOpenTimes(placeOpenTimes);

                            //placeListItems.setOpenTimes(places.getJSONObject(i).getJSONObject("opening_hours").getString("open_now"));
                            //placePhoto = places.getJSONObject(i).getJSONObject("photos").getJSONObject("0").getString("photo_reference");

                            try {
                                placeListItems.setRating(places.getJSONObject(i).getDouble("rating"));
                            } catch(org.json.JSONException exception){
                                placeListItems.setRating(0.0);
                            }

                            kept = places.getJSONObject(i).getJSONArray("types").getString(0);
                            //placeType = placeType.substring(placeType.     .IndexOf('_') + 1);
                            kept = kept.substring(kept.lastIndexOf('_')+1);
                            placeType = kept.substring(0, 1).toUpperCase() + kept.substring(1);
                            placeListItems.setPlaceType(placeType);
                            Log.i("StudMap", placeType);

                            try {
                                placeListItems.setNumberRatings(places.getJSONObject(i).getString("user_ratings_total"));
                            } catch (JSONException e) {
                                placeListItems.setNumberRatings("0");
                            }
                            placeList.add(placeListItems);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                json.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
        PlacesAdapter plce = new PlacesAdapter(this, placeList);
        results.setAdapter(plce);

    }*/

    @Override
    public void onRecieveSearch(String url) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*@Override
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }


        //LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        locLat = this.mCurrentLocation.getLatitude();
        locLong = this.mCurrentLocation.getLongitude();
        //url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=food&location=" + locLat + "," + locLong + "&rankby=distance&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        //json.setText(url);
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + locLat + "," + locLong + "&radius=1500&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?openNow=true&keyword=food&location=" + locLat + "," + locLong + "&rankby=distance&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        getPlaces(url);

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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        CharSequence text = "onConnectionFailed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClinet connection has failed");
    }*/
}
