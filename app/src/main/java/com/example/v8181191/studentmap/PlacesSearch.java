package com.example.v8181191.studentmap;

import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.android.gms.actions.SearchIntents;
import com.google.android.gms.common.api.GoogleApiClient;
import java.util.ArrayList;



public class PlacesSearch extends AppCompatActivity implements SearchBoxFragment.SearchListener, SearchResultsFragment.LocationListener, LocationFragment.OnFragmentInteractionListener,  NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private final String TAG = "StudentMapApp";                                                             //sets up the strings and other elements used by the activity
    String searchString;
    SearchResultsFragment fragment;
    DrawerLayout drawer;
    NavigationView navigationView;

    RelativeLayout placesContainer;
    static TextView json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {                                                    //sets up the screen layout being used, along with the toolbar and navigation drawer
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        placesContainer = (RelativeLayout)findViewById(R.id.fragment_container) ;
        checkConnection(); //runs the checkConnection function
        json = (TextView)findViewById(R.id.txtJson);
        //Intent intent = getIntent();
        //if (.equals(intent.getAction())) {}

    }

    @Override
    public void onReceiveSearch(String search) {                            //takes the search term from the search box fragment
        fragment = (SearchResultsFragment)getFragmentManager().findFragmentById(R.id.frgSearchResults); //sets up a reference to the search results fragment
        searchString = search;                                                                          //sets the search term to the searchString variable
        if(fragment !=null){                                                                            //if the fragment exists on the screen
            fragment.searchPlaces(search);                                                              //pass the search term to the search places function in the search results fragment
        }
    }

    @Override
    public void onReceiveLocationId(String location, Double userLat, Double userLng, String name, String placePhoto, String open, String placetype, String address, Double placerating, int cost, int numberratings) {      //takes the location id, of the place clicked on in the search results listview, along with the users latitude and longitude

        LocationFragment LocFragment = new LocationFragment();                              //sets up a reference to the location fragment
        Bundle args = new Bundle();                                                         //sets up the bundle for the fragment
        args.putString("ARG_PLACE_ID", location);                               //puts the location, latitude and longitude into the bundle
        args.putDouble("ARG_USERLAT", userLat);
        args.putDouble("ARG_USERLNG", userLng);
        args.putString("ARG_NAME", name);
        args.putString("ARG_PLACEPHOTO", placePhoto);
        args.putString("ARG_OPEN", open);
        args.putString("ARG_PLACE_TYPE", placetype);
        args.putString("ARG_ADDRESS", address);
        args.putDouble("ARG_PLACE_RATING", placerating);
        args.putInt("ARG_COST", cost);
        args.putInt("ARG_NUMBER_RATINGS", numberratings);
                //String open, String placetype, String address, Double placerating, int cost, int numberratings

        Log.i("SMR2", "place id: "+location + " lat: " + userLat + " lng: " + userLng + " name:" +  name + " photo:" + placePhoto + " open: " + open + " type: " + placetype + " address: " + address + " rating: " + placerating + " cost: " + cost + " number: " + numberratings);

        LocFragment.setArguments(args);                                                     //sets the arguments for the location fragment

        FragmentTransaction transaction = getFragmentManager().beginTransaction();          //sets up a reference to the  location fragment
        transaction.replace(R.id.fragment_container, LocFragment);                          //replace the current layout fragments with the new fragment
        transaction.addToBackStack(null);                                                   //sets the current fragments to behind the location fragment
        transaction.commit();                                                               //push the transaction
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @SuppressWarnings("StatementWithEmptyBody")                         //sets up the links in the navigation drawer to take the user to each of the activities
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_campus_map){
            Intent intent = new Intent(this, CampusMap.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.action_current_Location) {
            Intent intent = new Intent(this, CurrentLocation.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.action_place_search) {
            //Intent intent = new Intent(this, PlacesSearch.class);//opens a new screen when the shopping list is clicked
            //startActivity(intent);
        }else if (id == R.id.action_place_favourites) {
            loadFavourites();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFavourites() {
        fragment = (SearchResultsFragment) getFragmentManager().findFragmentById(R.id.frgSearchResults); //sets up a reference to the search results fragment
        //searchString = search;                                                                          //sets the search term to the searchString variable
        if (fragment != null) {                                                                            //if the fragment exists on the screen
            fragment.loadPlaces();
        }
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            //fab.setVisibility(View.VISIBLE);
        } else {
            message = "Sorry! You are not connected to internet";
            color = Color.RED;
            //fab.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar
                    .make(placesContainer, message, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(color);
            snackbar.show();
        }

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }


}
