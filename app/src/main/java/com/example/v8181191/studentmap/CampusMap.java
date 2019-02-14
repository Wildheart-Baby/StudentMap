package com.example.v8181191.studentmap;

import android.content.res.Resources;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class CampusMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //top left 54.573043, -1.237703
        //bottom right 54.567806, -1.233483
        //54.570543, -1.235653
        // Add a marker in Sydney and move the camera

        /*GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_NONE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance( options );
        */

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

        LatLng topLeft = new LatLng(54.573043, -1.237703);
        LatLng bottomRight = new LatLng(54.567806, -1.233483);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(topLeft);
        builder.include(bottomRight);
        builder.build();

        LatLng campus = new LatLng(54.570254, -1.235165);
        LatLng library = new LatLng(54.569941, -1.236059);
        LatLng curve = new LatLng(54.570381, -1.236883);
        LatLng olympia = new LatLng(54.569268, -1.236071);
        LatLng centuria = new LatLng(54.569390, -1.237555);
        LatLng cook = new LatLng(54.572374, -1.232863);
        LatLng eleven = new LatLng(54.571396, -1.236835);
        //54.569390, -1.237555

        mMap.addMarker(new MarkerOptions().position(library).title("Library"));
        mMap.addMarker(new MarkerOptions().position(curve).title("Curve"));
        mMap.addMarker(new MarkerOptions().position(olympia).title("Olympia"));
        mMap.addMarker(new MarkerOptions().position(centuria).title("Centuria"));
        mMap.addMarker(new MarkerOptions().position(cook).title("Cook"));
        mMap.addMarker(new MarkerOptions().position(eleven).title("11"));



        //mMap.moveCamera(CameraUpdateFactory.newLatLng(campus));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 15));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        GroundOverlayOptions campusMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.campusmap))
                .position(campus, 500f, 581f);
        mMap.addGroundOverlay(campusMap);

    }


}
