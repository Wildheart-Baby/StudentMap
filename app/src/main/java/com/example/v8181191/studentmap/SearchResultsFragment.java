package com.example.v8181191.studentmap;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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

    private final String TAG = "StudentMapApp";
    String url, placePhoto, placeOpenTimes, placeType, kept;
    GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    Double locLat, locLong;
    ArrayList<PlaceItems> placeList;
    TextView json;
    ListView results;

    private OnFragmentInteractionListener mListener;

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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
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

        json = view.findViewById(R.id.txtJson);
        results = view.findViewById(R.id.lvResults);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        //mGoogleApiClient.connect();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getPlaces(String url){

        placeList = new ArrayList<PlaceItems>();//sets up an array list called placeList
        placeList.clear();//clear the placeList array

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext()); //sets up a reference to the volley library queue

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
                                    if(kept.contains(","))  {
                                        kept = kept.substring(0, kept.indexOf(","));
                                    }
                                } catch(org.json.JSONException exception){
                                    kept = "none";
                                }
                                Log.i("StudMap", i + " " + kept);
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
                                //Log.i("StudMap", ""+pn);
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
        PlacesAdapter plce = new PlacesAdapter(getActivity().getApplicationContext(), placeList);
        results.setAdapter(plce);

    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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

        Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        CharSequence text = "onConnectionFailed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(getActivity().getApplicationContext(), text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClinet connection has failed");
    }

}
