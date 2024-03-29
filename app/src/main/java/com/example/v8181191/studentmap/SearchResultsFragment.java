package com.example.v8181191.studentmap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchResultsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LocationListener lListener;

    private final String TAG = "StudentMapApp";
    String url, placePhoto, placeOpenTimes, placeType, kept, searchString;
    GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    Double locLat, locLong, placerating;
    ArrayList<PlaceItems> placeList;

    ListView results;
    String placeid, placename, open, placetype, address;
    Boolean firstTime = true;
    int cost, numberratings, cPic, jCount;
    DatabaseHelper db;
    ArrayList<String> favourites;
    Boolean dloadFinished;


    public SearchResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchResultsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultsFragment newInstance(String param1, String param2) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        db = new DatabaseHelper(getContext());

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_results, container, false);

        if (savedInstanceState != null) {
            // Restore last state
            getPlaces(savedInstanceState.getString("search_key"));
            firstTime = savedInstanceState.getBoolean("first_time");
        }
        favourites = new ArrayList<>();
        //json = view.findViewById(R.id.txtJson);
        results = view.findViewById(R.id.lvResults);
        results.requestFocus();
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {//on clicking a shopping list
                if(ConnectivityReceiver.isConnected() == true){
                PlaceItems placeListItems = (PlaceItems) arg0.getItemAtPosition(arg2);//read the item at the list position that has been clicked
                placeid = placeListItems.getPlaceId();//get the id of the location
                placename = placeListItems.getPlaceName();
                placePhoto = placeListItems.getPlacePhoto();
                open = placeListItems.getOpenTimes();
                placetype = placeListItems.getPlaceType();
                address = placeListItems.getPlaceAddress();
                placerating = placeListItems.getRating();
                cost = placeListItems.getCost();
                numberratings = placeListItems.getNumberRatings();
                Log.i("SMR1",placeid + " " + locLat + " " + locLong + " " + placename + " " + placePhoto + " " + open + " " + placetype + " " + address + " " + placerating + " " + cost + " " + numberratings);
                lListener.onReceiveLocationId(placeid, locLat, locLong, placename, placePhoto, open, placetype, address, placerating, cost, numberratings);
                } else { Toast.makeText(getActivity(), "Sorry you are't connected to the intenet", Toast.LENGTH_LONG).show();    }   //shows a toast message informing the user they aren't connected to the internet
            }
        });
        dloadFinished = false;
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search_key", searchString);
        outState.putBoolean("first_time", false);
        //firstTime = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocationListener) {
            lListener = (LocationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        //mGoogleApiClient.connect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoogleApiClient.disconnect();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public void getPlaces(String url){
        searchString = url;
        placeList = new ArrayList<PlaceItems>();//sets up an array list called placeList
        placeList.clear();//clear the placeList array
        RequestQueue queue = Volley.newRequestQueue(getActivity()); //sets up a reference to the volley library queue
        final RequestQueue pictureQueue = Volley.newRequestQueue(getActivity());
        cPic = 0;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject placeData = new JSONObject(response);
                            //json.setText(response);
                            JSONArray places = placeData.getJSONArray("results");
                            final int pics = places.length() - 1;
                            for (int i = 0; i < places.length(); i++){

                                final PlaceItems placeListItems = new PlaceItems();
                                //json.setText(""+places.length());
                                try {
                                    kept = places.getJSONObject(i).getString("vicinity");
                                    if(kept.contains(","))  {
                                        kept = kept.substring(0, kept.indexOf(","));
                                    }
                                } catch(org.json.JSONException exception){
                                    //kept = "none";
                                    kept = places.getJSONObject(i).getString("formatted_address");
                                    Log.i("test", kept);
                                    if(kept.contains(","))  {
                                        kept = kept.substring(0, kept.indexOf(","));
                                    }
                                }

                                placeListItems.setPlaceAddress(kept);
                                placeListItems.setLat(places.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                                placeListItems.setLng(places.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                                placeListItems.setPlaceName(places.getJSONObject(i).getString("name"));
                                placeListItems.setPlaceId(places.getJSONObject(i).getString("place_id"));
                                try {
                                    JSONArray photoItemArray = places.getJSONObject(i).getJSONArray("photos");

                                    for (int j=0; j < photoItemArray.length(); j++){

                                        placePhoto = photoItemArray.getJSONObject(j).getString("photo_reference");
                                        //placePhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=125&maxheight=82&photoreference=" + placePhoto + "&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
                                        /*String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=125&maxheight=82&photoreference=" + placePhoto + "&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
                                        RequestQueue imageQueue = Volley.newRequestQueue(getActivity());

                                        // Initialize a new ImageRequest
                                        ImageRequest imageRequest = new ImageRequest(
                                                imgUrl, // Image URL
                                                new Response.Listener<Bitmap>() { // Bitmap listener
                                                    @Override
                                                    public void onResponse(Bitmap response) {
                                                        // Do something with response
                                                        placeListItems.setPhoto(response);
                                                        cPic ++;
                                                        if (cPic == pics){
                                                            //dloadFinished = true;
                                                            PlacesAdapter plce = new PlacesAdapter(getActivity(), placeList);
                                                            results.setAdapter(plce);
                                                        }

                                                    }
                                                },
                                                0, // Image width
                                                0, // Image height
                                                ImageView.ScaleType.CENTER_CROP, // Image scale type
                                                Bitmap.Config.RGB_565, //Image decode configuration
                                                new Response.ErrorListener() { // Error listener
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        // Do something with error response
                                                        error.printStackTrace();
                                                        //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                                                    }
                                                }
                                        );

                                        // Add ImageRequest to the RequestQueue
                                        imageQueue.add(imageRequest);*/

                                    }

                                } catch(org.json.JSONException exception){
                                    //placePhoto = "none";
                                }

                                String pn = placePhoto;
                                //Log.i("StudMap", ""+pn);
                                placeListItems.setPlacePhoto(placePhoto);

                                try {
                                    placeOpenTimes = places.getJSONObject(i).getJSONObject("opening_hours").getString("open_now");
                                } catch(org.json.JSONException exception){
                                }

                                placeListItems.setOpenTimes(placeOpenTimes);

                                try {
                                    placeListItems.setCost(places.getJSONObject(i).getInt("price_level"));
                                } catch (org.json.JSONException exception){
                                    placeListItems.setCost(0);
                                }
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

                                try {
                                    placeListItems.setNumberRatings(places.getJSONObject(i).getInt("user_ratings_total"));
                                } catch (JSONException e) {
                                    placeListItems.setNumberRatings(0);
                                }
                                placeList.add(placeListItems);
                                PlacesAdapter plce = new PlacesAdapter(getActivity(), placeList);
                                results.setAdapter(plce);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PlacesSearch.json.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        locLat = this.mCurrentLocation.getLatitude();
        locLong = this.mCurrentLocation.getLongitude();
        //url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=food&location=" + locLat + "," + locLong + "&rankby=distance&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        //json.setText(url);
        //url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + locLat + "," + locLong + "&radius=1500&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";

        if(firstTime == true) {
            if(ConnectivityReceiver.isConnected() == true){
                url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?openNow=true&keyword=food&location=" + locLat + "," + locLong + "&rankby=distance&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
                getPlaces(url);
            }
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        CharSequence text = "onConnectionSuspended executed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(getActivity(), text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        CharSequence text = "onConnectionFailed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(getActivity(), text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClinet connection has failed");
    }

    public void searchPlaces(String place){
        Log.i("StudMapSP", "running function");
        locLat = this.mCurrentLocation.getLatitude();
        locLong = this.mCurrentLocation.getLongitude();
        url="https://maps.googleapis.com/maps/api/place/textsearch/json?query="+ place +"&location=" + locLat + "," + locLong + "&rankby=distance&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        Log.i("StudMapSRF", url);
        getPlaces(url);
    }

    public void loadPlaces(){
       placeList = new ArrayList<PlaceItems>();//sets up an array list called placeList
       placeList.clear();//clear the placeList array
       placeList = db.getFavourites();
       Log.i("SRF0", ""+placeList.size());
       /*final int pics = placeList.size();
       cPic = 0;
        for (jCount=0; jCount < placeList.size(); jCount++){
            final PlaceItems placeListItems = new PlaceItems();
            placePhoto = placeList.get(jCount).getPlacePhoto();
            //placePhoto = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=125&maxheight=82&photoreference=" + placePhoto + "&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
            String imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=125&maxheight=82&photoreference=" + placePhoto + "&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
            RequestQueue imageQueue = Volley.newRequestQueue(getActivity());

            // Initialize a new ImageRequest
            ImageRequest imageRequest = new ImageRequest(
                    imgUrl, // Image URL
                    new Response.Listener<Bitmap>() { // Bitmap listener
                        @Override
                        public void onResponse(Bitmap response) {
                            // Do something with response
                            //placeListItems.set(j).setPhoto(response);
                            placeList.get(jCount -1).setPhoto(response);
                            //placeListItems.setPhoto(response);
                            cPic ++;
                            if (cPic == pics){
                                //dloadFinished = true;
                                PlacesAdapter plce = new PlacesAdapter(getActivity(), placeList);
                                results.setAdapter(plce);
                            }

                        }
                    },
                    0, // Image width
                    0, // Image height
                    ImageView.ScaleType.CENTER_CROP, // Image scale type
                    Bitmap.Config.RGB_565, //Image decode configuration
                    new Response.ErrorListener() { // Error listener
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Do something with error response
                            error.printStackTrace();
                            //Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
                        }
                    }
            );

            // Add ImageRequest to the RequestQueue
            imageQueue.add(imageRequest);

        }*/


      PlacesAdapter plce = new PlacesAdapter(getActivity(), placeList);
      results.setAdapter(plce);
    }



    public interface LocationListener {
        void onReceiveLocationId(String search, Double userLat, Double userLng, String name, String placePhoto, String open, String placetype, String address, Double placerating, int cost, int numberratings);
    }



}
