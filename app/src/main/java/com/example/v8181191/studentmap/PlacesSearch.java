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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_search);

    }

    @Override
    public void onReceiveSearch(String search) {
        SearchResultsFragment fragment = (SearchResultsFragment)getFragmentManager().findFragmentById(R.id.frgSearchResults);
        Log.i("StudMapORS", search);
        if(fragment !=null){
            fragment.searchPlaces(search);
        }
    }

    @Override
    public void onReceiveLocationId(String location) {

        LocationFragment LocFragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(LocationFragment.ARG_PLACE, location);
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
