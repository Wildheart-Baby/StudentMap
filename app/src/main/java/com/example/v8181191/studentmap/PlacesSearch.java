package com.example.v8181191.studentmap;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;



public class PlacesSearch extends AppCompatActivity implements SearchBoxFragment.SearchListener, SearchResultsFragment.LocationListener, LocationFragment.OnFragmentInteractionListener{

    private final String TAG = "StudentMapApp";
    String searchString;
    SearchResultsFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_search);

    }

    @Override
    public void onReceiveSearch(String search) {
        fragment = (SearchResultsFragment)getFragmentManager().findFragmentById(R.id.frgSearchResults);
        searchString = search;
        if(fragment !=null){
            fragment.searchPlaces(search);
        }
    }

    @Override
    public void onReceiveLocationId(String location, Double userLat, Double userLng) {

        LocationFragment LocFragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(LocationFragment.ARG_PLACE, location);
        args.putDouble("ARG_USERLAT", userLat);
        args.putDouble("ARG_USERLNG", userLng);

        LocFragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, LocFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
