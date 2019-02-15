package com.example.v8181191.studentmap;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

public class CampusMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final String TAG = "StudentMapApp";
    private android.location.Location mCurrentLocation;
    LatLng llCurrentLocation;
    Marker mCurrLocationMarker;


    //private LatLngBounds CampusBounds = new LatLngBounds(
    //        new LatLng(54.573043, -1.237703), new LatLng(54.567806, -1.233483));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int width = mapFragment.getView().getMeasuredWidth();

        int height = mapFragment.getView().getMeasuredHeight();


        Log.d("CampusMap", "Width: " + width+ " Height: "+height);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.setMaxZoomPreference(20.0f);
        //top left 54.573043, -1.237703
        //bottom right 54.567806, -1.233483
        //54.570543, -1.235653
        // Add a marker in Sydney and move the camera

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e("StudentMap", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("StudentMap", "Can't find style. Error: ", e);
        }

        /*LatLng topLeft = new LatLng(54.573043, -1.237703);
        LatLng bottomRight = new LatLng(54.567806, -1.233483);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(topLeft);
        builder.include(bottomRight);
        builder.build();*/

        LatLng campus = new LatLng(54.570254, -1.235165);
        //LatLng campus = new LatLng(54.570532, -1.235156);
        LatLng library = new LatLng(54.569941, -1.236059);
        LatLng curve = new LatLng(54.570381, -1.236883);
        LatLng olympia = new LatLng(54.569268, -1.236071);
        LatLng centuria = new LatLng(54.569390, -1.237555);
        LatLng cook = new LatLng(54.572374, -1.232863);
        LatLng eleven = new LatLng(54.571396, -1.236835);
        //54.569390, -1.237555

        //mMap.addMarker(new MarkerOptions().position(library).title("Library"));
        //mMap.addMarker(new MarkerOptions().position(curve).title("Curve"));
        //mMap.addMarker(new MarkerOptions().position(olympia).title("Olympia"));
        //mMap.addMarker(new MarkerOptions().position(centuria).title("Centuria"));
        //mMap.addMarker(new MarkerOptions().position(cook).title("Cook"));
        //mMap.addMarker(new MarkerOptions().position(eleven).title("11"));



        //mMap.moveCamera(CameraUpdateFactory.newLatLng(campus));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 16.25f));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        LatLngBounds campusBounds = new LatLngBounds(
                new LatLng(54.567788, -1.232546),
                new LatLng(54.572696, -1.237863)
        );

        GroundOverlayOptions campusMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.campusmap2))
                .position(campus, 410f, 540f);


        /*GroundOverlayOptions campusMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.campusmap))
                .positionFromBounds(campusBounds);*/

        mMap.addGroundOverlay(campusMap);
        addBuildings();

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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //mMap.setMyLocationEnabled(true);

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
    }

    @Override
    public void onLocationChanged(Location location) {

        this.mCurrentLocation = location;
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }


        llCurrentLocation = new LatLng(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(llCurrentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));

    }

    public void addBuildings(){
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570607, -1.237060), new LatLng(54.570145, -1.237157), new LatLng(54.570125, -1.236800), new LatLng(54.570137, -1.236757), new LatLng(54.570193, -1.236738), new LatLng(54.570252, -1.236813), new LatLng(54.570357, -1.236785), new LatLng(54.570566, -1.236550))
                .fillColor(R.color.buildingOrange)
                .strokeColor(Color.GRAY));

        /*mMap.addCircle(new CircleOptions()
            .center(new LatLng(54.570175, -1.236823))
            .radius(5)
            .fillColor(R.color.buildingOrange));*/
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571227, -1.237479), new LatLng(54.571147, -1.236260), new LatLng(54.571564, -1.236154), new LatLng(54.571651, -1.237394))
                .fillColor(R.color.buildingOrange)
                .strokeColor(Color.GRAY));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572711, -1.235285), new LatLng(54.572648, -1.234443), new LatLng(54.572260, -1.234534), new LatLng(54.572267, -1.234585), new LatLng(54.572223,-1.234605), new LatLng(54.572209, -1.234536), new LatLng(54.571711,-1.234643), new LatLng(54.571734, -1.234927), new LatLng(54.572133, -1.234835), new LatLng(54.572180,-1.235406), new LatLng(54.572278, -1.235383), new LatLng(54.572281, -1.235273), new LatLng(54.572320, -1.235284), new LatLng(54.572333, -1.235376))
                .fillColor(R.color.buildingOrange)
                .strokeColor(Color.GRAY));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.568585, -1.234044), new LatLng(54.568548, -1.233550), new LatLng(54.568408, -1.233559), new LatLng(54.568437, -1.233908), new LatLng(54.568301, -1.233946), new LatLng(54.568309, -1.234099))
                .fillColor(R.color.buildingOrange)
                .strokeColor(Color.GRAY));

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
