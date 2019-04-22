package com.example.v8181191.studentmap;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wildheart on 13/03/2019.
 */

public class LocationFunctions {

    ArrayList<LatLng> mapPoints;
    String url;
    LatLng mapPoint;
    Double mpLat, mpLng;
    Polyline line;
    Location location;

    public Double TotalDistance(Double PlaceLat, Double UserLat, Double PlaceLng, Double UserLng){          //function that calculates the distance between the user and the coordinates passed to the function
        double dLat = Math.toRadians(PlaceLat - UserLat);
        double dLon = Math.toRadians(PlaceLng - UserLng);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(UserLat)) * Math.cos(Math.toRadians(54.570792)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c2 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        //6378.1 is gravity of earth
        Double distance =  c2*6378.1;
        distance = distance * 0.621371;
        Double totalDistance = distance * 1609.34;

        //return totalDistance;
        return distance;
    }


    public void GetDirections(Double UserLat, Double UserLng, Double markerLat, Double markerLng, Context context){  getPath(UserLat, UserLng, markerLat, markerLng, context); }

        public void getPath(Double UserLat, Double UserLng, final Double markerLat, final Double markerLng, Context context) {

            mapPoints = new ArrayList<LatLng>();//sets up an array list called map points
            mapPoints.clear();//clear the map points array

            url = "https://maps.googleapis.com/maps/api/directions/json?origin="+UserLat+","+UserLng+"&destination="+markerLat+","+markerLng+"&mode=walking&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
                //the url that uses the current location of the user and the locaton of the map marker to get the direction steps for the path

            //Log.i("Location Functions", url);
            RequestQueue queue = Volley.newRequestQueue(context);                                   //sets up a reference to the volley library queue

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Location Functions", response);
                    try {
                        // Tranform the string into a json object
                        final JSONObject json = new JSONObject(response);
                        JSONArray routeArray = json.getJSONArray("routes");
                        JSONObject route = routeArray.getJSONObject(0);
                        JSONArray legArray = route.getJSONArray("legs");
                        JSONObject leg = legArray.getJSONObject(0);
                        JSONArray stepsArray = leg.getJSONArray("steps");


                        for (int i = 0; i < stepsArray.length(); i++){                              //for loop to add the gps coordinates to an array
                            //placeListItems = new PlaceItems();
                            mpLat = stepsArray.getJSONObject(i).getJSONObject("start_location").getDouble("lat");
                            mpLng = stepsArray.getJSONObject(i).getJSONObject("start_location").getDouble("lng");
                            mapPoint = new LatLng(mpLat,mpLng);
                            mapPoints.add(mapPoint);

                        }
                            mapPoint = new LatLng(markerLat, markerLng);
                            mapPoints.add(mapPoint);

                        if( line != null)
                        {
                            line.remove();                                                      //clears the poly line from the map
                        }

                        List<PatternItem> pattern = Arrays.asList(new Dot(), new Gap(10));
                        line = CampusMap.mMap.addPolyline(new PolylineOptions()
                                .addAll(mapPoints)
                                .width(20)
                                .pattern(pattern)
                                .color(Color.rgb(00,221,255)));
                            //Log.i("Location Functions", ""+mapPoints.size());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("Location Function","That didn't work!");
                }
            });
            queue.add(stringRequest);
        }
}
