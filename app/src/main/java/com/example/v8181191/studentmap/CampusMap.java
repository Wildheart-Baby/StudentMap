package com.example.v8181191.studentmap;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class CampusMap extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;                                                                         //sets up the variables and elements that the activity  uses
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final String TAG = "StudentMapApp";
    private android.location.Location mCurrentLocation;
    LatLng llCurrentLocation;
    Marker mCurrLocationMarker;
    DrawerLayout drawer;
    NavigationView navigationView;
    RelativeLayout campusKey;
    int i;

    LatLng campus;

    Polygon building1, building2, building3, building4, building5, building6, building7, building8, building9, building10, building11, building12, building13, building14;
    Polygon building15, building16, building17, building18, building19,building20, building21, building22, building23, building24, building25, building26, building27;

    String type, d;
    TextView distance_overlay;
    Double locLat, locLong, campusCentre, distance;

    private static final int COLOR_ORANGE_ARGB = 0xFFF25E21;
    private static final int COLOR_ROAD_ARGB = 0xff4D5156;
    private static final int COLOR_GRASS_ARGB = 0xff6BBF5C;
    private static final int COLOR_BUILDING_ARGB = 0xffEFEC68;
    private static final int POLYGON_STROKE_WIDTH_PX = 0;
    private static final int COLOR_PATH_WHITE = 0xffFFFFFF;


    //private LatLngBounds CampusBounds = new LatLngBounds(
    //        new LatLng(54.573043, -1.237703), new LatLng(54.567806, -1.233483));

    @Override
    protected void onCreate(Bundle savedInstanceState) {                                                    //sets up the layout the activity uses along with the toolbar, the navigation drawer, map fragment and floating action button that re-centres the map
        super.onCreate(savedInstanceState);                                                                 //it also builds the gps connection
        setContentView(R.layout.activity_campus_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        campus = new LatLng(54.570792, -1.234907);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        distance_overlay = findViewById(R.id.lblDistanceOverlay);
        campusKey = findViewById(R.id.rlCampusKey);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LatLng campus = new LatLng(54.570254, -1.235165);
                resetCamera();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setClickable(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (mMap != null){

            mapFragment.getView().setClickable(false);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.campus_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_campus_key:
                    ShowCampusMap();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void ShowCampusMap(){                                                                        //function to show a legend for the markers on the map

        new CountDownTimer(3000, 1000) {                                    //sets up a countdown timer for three seconds

            public void onTick(long millisUntilFinished) {
                campusKey.setVisibility(View.VISIBLE);                                                  //whilst the timer is counting down show the campus key
            }

            public void onFinish() {
                campusKey.setVisibility(View.INVISIBLE);                                                //after the timer has finished hide the campus key
            }
        }.start();                                                                                      //start the timer
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {                                                       //when the map is loaded
        mMap = googleMap;                                                                               //sets up a reference to the map
        mMap.setMinZoomPreference(16.30f);                                                              //sets the zoom preferences
        mMap.setMaxZoomPreference(20.0f);

        addBuildings();                                                                                 //add the building polygons

        resetCamera();                                                                                  //set the map camera to the centre of campus

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
    public void onConnected(Bundle bundle) {                                                                //when the gps is connected
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECEIVE_SMS, Manifest.permission.CALL_PHONE}, 1);
            return;                                                                                         //ask for the persmissions the app requires
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {                                                          //if the gps connection is suspended run this function
        CharSequence text = "onConnectionSuspended executed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClient connection has been suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {                                 //if the gps connection fails run this function
        CharSequence text = "onConnectionFailed";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        //toast.show();
        Log.i(TAG, "GoogleApiClinet connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {                      //is the user moves from the last location

        this.mCurrentLocation = location;                                   //get the current location
        if (mCurrLocationMarker != null)                                    //if the map marker exists on screen
        {
            mCurrLocationMarker.remove();                                   //delete the marker from the screen
        }

        llCurrentLocation = new LatLng(this.mCurrentLocation.getLatitude(), this.mCurrentLocation.getLongitude());  //set the current loacation to the LatLng object

        mCurrLocationMarker =  mMap.addMarker(new MarkerOptions()                                                   //add a marker at the current position with a custom location icon
                .position(llCurrentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

        locLat = mCurrentLocation.getLatitude();                                                                    //get the current latitude and longitude to doubles
        locLong = mCurrentLocation.getLongitude();

        distance_calc();                                                                                            //run the distance calculation function

    }

    public void distance_calc(){                                                                                    //this calculates the distance from the centre of campus and the user's current location
        /*double dLat = Math.toRadians(54.570792 - locLat);
        double dLon = Math.toRadians(-1.234907 - locLong);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(locLat)) * Math.cos(Math.toRadians(54.570792)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c2 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        //6378.1 is gravity of earth
        distance =  c2*6378.1;
        distance = distance * 0.621371;
        campusCentre = distance * 1609.34;*/

        LocationFunctions lf = new LocationFunctions();

        campusCentre =  lf.TotalDistance(54.570792, locLat, -1.234907,locLong);                     //checks to see how far from the centre of campus the user is
        Log.i("CM Distance", ""+campusCentre);
        if (campusCentre > 0.14291537){                                                                              //if the user if further than 200 metres from the centre, show an overlay on screen with the distance
            distance_overlay.setVisibility(View.VISIBLE);
            d = String.format("%.2f", campusCentre);
            d = d + " miles from campus";
            distance_overlay.setText(d);
        } else if (campusCentre < 0.14291537){                                                                      //if less than 200 metres, hide the overlay
            distance_overlay.setVisibility(View.INVISIBLE);
            d = String.format("%.0f", campusCentre);
            distance_overlay.setText("");
            //distance_overlay.setShadowLayer(1.0f, -2, 2,   getResources().getColor(R.color.places_text_black_alpha_26));
        }
    }

    public void addBuildings(){

        building1 = mMap.addPolygon(new PolygonOptions()                                            //sets up all the overlay polygons used for the campus map, using latitude and longitude coordinates, along with a colour to fill the polygon
                .clickable(true)
                .add(new LatLng(54.570621,-1.237067),
                        new LatLng(54.570585,-1.236558),
                        new LatLng(54.570579,-1.236541),
                        new LatLng(54.570563,-1.23652),
                        new LatLng(54.570553,-1.236501),
                        new LatLng(54.570536,-1.23649),
                        new LatLng(54.570515,-1.236488),
                        new LatLng(54.570498,-1.236485),
                        new LatLng(54.570478,-1.236501),
                        new LatLng(54.570461,-1.236515),
                        new LatLng(54.570374,-1.236791),
                        new LatLng(54.570271,-1.23682),
                        new LatLng(54.570201,-1.236737),
                        new LatLng(54.57019,-1.236727),
                        new LatLng(54.570176,-1.236723),
                        new LatLng(54.57017,-1.236724),
                        new LatLng(54.570163,-1.236726),
                        new LatLng(54.570155,-1.236731),
                        new LatLng(54.570149,-1.236741),
                        new LatLng(54.570141,-1.236754),
                        new LatLng(54.570134,-1.236786),
                        new LatLng(54.570131,-1.236825),
                        new LatLng(54.570158,-1.23716))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building1.setTag("1");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570381,-1.236945))
                .title("The Curve (T)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.one_marker)));

        building2 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.570127,-1.236443),
                        new LatLng(54.570117,-1.23631),
                        new LatLng(54.57013,-1.236271),
                        new LatLng(54.570138,-1.236235),
                        new LatLng(54.570147,-1.23619),
                        new LatLng(54.570151,-1.236166),
                        new LatLng(54.570152,-1.236139),
                        new LatLng(54.570153,-1.236102),
                        new LatLng(54.570153,-1.236067),
                        new LatLng(54.57015,-1.23602),
                        new LatLng(54.570145,-1.235977),
                        new LatLng(54.570136,-1.23593),
                        new LatLng(54.570128,-1.235899),
                        new LatLng(54.570113,-1.235859),
                        new LatLng(54.570093,-1.235814),
                        new LatLng(54.570081,-1.235786),
                        new LatLng(54.570072,-1.235661),
                        new LatLng(54.569816,-1.235712),
                        new LatLng(54.56982,-1.235807),
                        new LatLng(54.5698,-1.235857),
                        new LatLng(54.569792,-1.23589),
                        new LatLng(54.569789,-1.235912),
                        new LatLng(54.56978,-1.235952),
                        new LatLng(54.569771,-1.235949),
                        new LatLng(54.569768,-1.235969),
                        new LatLng(54.569766,-1.23599),
                        new LatLng(54.569763,-1.236022),
                        new LatLng(54.569762,-1.236054),
                        new LatLng(54.569762,-1.236098),
                        new LatLng(54.569766,-1.236131),
                        new LatLng(54.569768,-1.23617),
                        new LatLng(54.569775,-1.236208),
                        new LatLng(54.56978,-1.236238),
                        new LatLng(54.569789,-1.236265),
                        new LatLng(54.569801,-1.236272),
                        new LatLng(54.569813,-1.236259),
                        new LatLng(54.569822,-1.236289),
                        new LatLng(54.569836,-1.236321),
                        new LatLng(54.569849,-1.236352),
                        new LatLng(54.569862,-1.236374),
                        new LatLng(54.56987,-1.236497))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building2.setTag("2");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569961,-1.236089))
                .title("Library (L)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.two_marker)));

        building3 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.570318,-1.23446),
                        new LatLng(54.569987,-1.234525),
                        new LatLng(54.569996,-1.234686),
                        new LatLng(54.569952,-1.234695),
                        new LatLng(54.569957,-1.234791),
                        new LatLng(54.570005,-1.234781),
                        new LatLng(54.570023,-1.23509),
                        new LatLng(54.569976,-1.235101),
                        new LatLng(54.569981,-1.235192),
                        new LatLng(54.570031,-1.235183),
                        new LatLng(54.570033,-1.23528),
                        new LatLng(54.570067,-1.235304),
                        new LatLng(54.570092,-1.235316),
                        new LatLng(54.570133,-1.235331),
                        new LatLng(54.570158,-1.235334),
                        new LatLng(54.570183,-1.235334),
                        new LatLng(54.570186,-1.235402),
                        new LatLng(54.570189,-1.235412),
                        new LatLng(54.570194,-1.235419),
                        new LatLng(54.570206,-1.235428),
                        new LatLng(54.570223,-1.235425),
                        new LatLng(54.57023,-1.235412),
                        new LatLng(54.570235,-1.235396),
                        new LatLng(54.570234,-1.235322),
                        new LatLng(54.570264,-1.23531),
                        new LatLng(54.570282,-1.235297),
                        new LatLng(54.5703,-1.235282),
                        new LatLng(54.57032,-1.235268),
                        new LatLng(54.570335,-1.235253),
                        new LatLng(54.570358,-1.23523),
                        new LatLng(54.570372,-1.235205))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building3.setTag("3");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570126,-1.234845))
                .title("Student Union")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.three_marker)));

        building4 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.570262,-1.233658),
                        new LatLng(54.569906,-1.233725),
                        new LatLng(54.569932,-1.234109),
                        new LatLng(54.570287,-1.234039))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building4.setTag("4");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570056,-1.23382))
                .title("Greig (G)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.four_marker)));


        building5 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.569732,-1.233157),
                        new LatLng(54.569638,-1.233175),
                        new LatLng(54.56964,-1.233221),
                        new LatLng(54.569517,-1.233253),
                        new LatLng(54.569546,-1.233726),
                        new LatLng(54.569565,-1.233723),
                        new LatLng(54.569592,-1.234145),
                        new LatLng(54.569585,-1.234161),
                        new LatLng(54.569588,-1.234224),
                        new LatLng(54.569597,-1.234238),
                        new LatLng(54.569597,-1.234252),
                        new LatLng(54.569581,-1.234255),
                        new LatLng(54.569611,-1.234707),
                        new LatLng(54.569631,-1.234722),
                        new LatLng(54.569651,-1.234732),
                        new LatLng(54.56967,-1.234734),
                        new LatLng(54.569695,-1.234735),
                        new LatLng(54.569711,-1.234731),
                        new LatLng(54.569725,-1.234719),
                        new LatLng(54.569743,-1.234707),
                        new LatLng(54.569769,-1.234678),
                        new LatLng(54.569735,-1.234162),
                        new LatLng(54.569772,-1.234156),
                        new LatLng(54.569749,-1.233686),
                        new LatLng(54.56977,-1.233681))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building5.setTag("5"); //54.569639,-1.23419
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569639,-1.23419))
                .title("Europa (IT/OL)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.five_marker)));

        building6 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.569421,-1.235129),
                        new LatLng(54.569396,-1.234773),
                        new LatLng(54.569368,-1.23478),
                        new LatLng(54.569357,-1.234582),
                        new LatLng(54.569387,-1.234573),
                        new LatLng(54.569357,-1.234207),
                        new LatLng(54.56927,-1.234237),
                        new LatLng(54.569263,-1.234182),
                        new LatLng(54.569193,-1.234198),
                        new LatLng(54.569224,-1.234611),
                        new LatLng(54.569287,-1.234604),
                        new LatLng(54.569298,-1.234791),
                        new LatLng(54.569231,-1.234805),
                        new LatLng(54.569253,-1.235165))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building6.setTag("6");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569286,-1.234944))
                .title("Victoria (V)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.six_marker)));


        building7 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.568586,-1.23404),
                        new LatLng(54.56855,-1.233552),
                        new LatLng(54.568416,-1.233573),
                        new LatLng(54.568438,-1.233902),
                        new LatLng(54.568297,-1.233938),
                        new LatLng(54.568308,-1.234096))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building7.setTag("7");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.568462,-1.233892))
                .title("Mercuria (MC)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.seven_marker)));

        building8 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.569396,-1.235629),
                        new LatLng(54.569266,-1.235654),
                        new LatLng(54.569263,-1.23562),
                        new LatLng(54.569208,-1.235594),
                        new LatLng(54.569184,-1.23559),
                        new LatLng(54.569163,-1.235589),
                        new LatLng(54.569106,-1.235606),
                        new LatLng(54.569111,-1.235684),
                        new LatLng(54.569,-1.235707),
                        new LatLng(54.569061,-1.236632),
                        new LatLng(54.569092,-1.236627),
                        new LatLng(54.569108,-1.236836),
                        new LatLng(54.569426,-1.236921),
                        new LatLng(54.569404,-1.236572),
                        new LatLng(54.569459,-1.23656))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building8.setTag("8");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569124,-1.236141))
                .title("Olympia (OLY)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.eight_marker)));

        building9 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.569619,-1.237895),
                        new LatLng(54.569565,-1.237118),
                        new LatLng(54.569157,-1.236999),
                        new LatLng(54.569175,-1.237271),
                        new LatLng(54.569113,-1.237287),
                        new LatLng(54.5691,-1.237149),
                        new LatLng(54.569069,-1.237149),
                        new LatLng(54.569062,-1.237113),
                        new LatLng(54.568954,-1.237131),
                        new LatLng(54.568985,-1.237684),
                        new LatLng(54.569017,-1.237677),
                        new LatLng(54.569039,-1.238082),
                        new LatLng(54.569094,-1.238073),
                        new LatLng(54.569098,-1.238131),
                        new LatLng(54.569163,-1.238116),
                        new LatLng(54.569149,-1.237813),
                        new LatLng(54.569213,-1.237805),
                        new LatLng(54.569223,-1.237976))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building9.setTag("9");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569286,-1.237451))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nine_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571083,-1.235672))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ten_marker)));

        building11 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571661,-1.237391),
                        new LatLng(54.571582,-1.236176),
                        new LatLng(54.571452,-1.236201),
                        new LatLng(54.571429,-1.235949),
                        new LatLng(54.571378,-1.23596),
                        new LatLng(54.571339,-1.235501),
                        new LatLng(54.570969,-1.235569),
                        new LatLng(54.571,-1.23608),
                        new LatLng(54.571274,-1.236033),
                        new LatLng(54.571284,-1.236226),
                        new LatLng(54.571144,-1.236255),
                        new LatLng(54.571232,-1.237479))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building11.setTag("11");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.57124,-1.236662))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.eleven_marker)));

        building12 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571736,-1.236298),
                        new LatLng(54.571728,-1.236158),
                        new LatLng(54.572017,-1.23608),
                        new LatLng(54.572004,-1.235913),
                        new LatLng(54.571971,-1.23592),
                        new LatLng(54.571939,-1.235583),
                        new LatLng(54.571756,-1.235615),
                        new LatLng(54.571754,-1.23559),
                        new LatLng(54.571534,-1.235633),
                        new LatLng(54.571555,-1.235863),
                        new LatLng(54.571648,-1.235835),
                        new LatLng(54.57166,-1.235992),
                        new LatLng(54.571627,-1.236001),
                        new LatLng(54.571656,-1.236308))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building12.setTag("12");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571729,-1.235879))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twelve_marker)));


        building14 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572277, -1.235381), new LatLng(54.572210, -1.234529), new LatLng(54.571706, -1.234650), new LatLng(54.571734, -1.234921), new LatLng(54.572145, -1.234835), new LatLng(54.572182, -1.235400))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building14.setTag("14");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571886,-1.234682))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.fourteen_marker)));

        building15 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572711, -1.235285), new LatLng(54.572648, -1.234443), new LatLng(54.572260, -1.234534), new LatLng(54.572267, -1.234585), new LatLng(54.572211, -1.234614), new LatLng(54.572215, -1.234703), new LatLng(54.572502, -1.234643), new LatLng(54.572502, -1.234643), new LatLng(54.572497, -1.234711), new LatLng(54.572500, -1.234753), new LatLng(54.572402, -1.234810), new LatLng(54.572401, -1.234822), new LatLng(54.572396, -1.234875), new LatLng(54.572375, -1.234910), new LatLng(54.572383, -1.234970), new LatLng(54.572415, -1.234998), new LatLng(54.572531, -1.234998), new LatLng(54.572536, -1.235029), new LatLng(54.572526, -1.235052), new LatLng(54.572529, -1.235095), new LatLng(54.572544, -1.235159), new LatLng(54.572257, -1.235237), new LatLng(54.572255, -1.235286), new LatLng(54.572320, -1.235284), new LatLng(54.572332, -1.235379))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building15.setTag("15");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.572517,-1.234878))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.fifteen_marker)));

        building16 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572608,-1.234326),
                        new LatLng(54.572588,-1.234087),
                        new LatLng(54.572615,-1.23408),
                        new LatLng(54.572587,-1.233735),
                        new LatLng(54.572473,-1.233762),
                        new LatLng(54.57248,-1.233883),
                        new LatLng(54.572357,-1.233912),
                        new LatLng(54.572348,-1.233788),
                        new LatLng(54.571623,-1.233958),
                        new LatLng(54.571647,-1.234238),
                        new LatLng(54.571793,-1.234208),
                        new LatLng(54.571811,-1.234424),
                        new LatLng(54.571833,-1.234419),
                        new LatLng(54.571838,-1.234505))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building16.setTag("16");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.572195,-1.234094))
                .title("Centuria (H)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.sixteen_marker)));

        building17 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572578,-1.233587),
                        new LatLng(54.572509,-1.232644),
                        new LatLng(54.572157,-1.232719),
                        new LatLng(54.572199,-1.233203),
                        new LatLng(54.57234,-1.233177),
                        new LatLng(54.572361,-1.233505),
                        new LatLng(54.572398,-1.233544),
                        new LatLng(54.572479,-1.233525),
                        new LatLng(54.572485,-1.233611))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building17.setTag("17");

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.572395,-1.233284))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.seventeen_marker)));

        building18 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572298,-1.233651),
                        new LatLng(54.572285,-1.233494),
                        new LatLng(54.572119,-1.233523),
                        new LatLng(54.572131,-1.23369))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building18.setTag("18");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.572162,-1.233581))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.eighteen_marker)));

        building19 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571847,-1.23374),
                        new LatLng(54.571769,-1.232743),
                        new LatLng(54.571614,-1.232775),
                        new LatLng(54.571674,-1.233566),
                        new LatLng(54.571668,-1.233781))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building19.setTag("19");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571697,-1.233249))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nineteen_marker)));

        building20 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571549,-1.232409),
                        new LatLng(54.571538,-1.232201),
                        new LatLng(54.571277,-1.232265),
                        new LatLng(54.571289,-1.23247))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building20.setTag("20");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571347,-1.232328))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twenty_marker)));

        building21 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571257,-1.234309),
                        new LatLng(54.571205,-1.233609),
                        new LatLng(54.570961,-1.233663),
                        new LatLng(54.570967,-1.233733),
                        new LatLng(54.570995,-1.233729),
                        new LatLng(54.571038,-1.234352))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building21.setTag("21");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571058,-1.233947))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twenty_one_marker)));


        building22 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571071,-1.233139),
                        new LatLng(54.57106,-1.232927),
                        new LatLng(54.570695,-1.233018),
                        new LatLng(54.570698,-1.233058),
                        new LatLng(54.570629,-1.233075),
                        new LatLng(        54.570629,-1.233098),
                        new LatLng(54.570594,-1.23311),
                        new LatLng(54.570589,-1.233506),
                        new LatLng(54.570815,-1.233463),
                        new LatLng(54.5708,-1.233207))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building22.setTag("22");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570711,-1.233218))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twenty_two_marker)));

        building23 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.570882,-1.233593),
                        new LatLng(54.570875,-1.233491),
                        new LatLng(54.5707,-1.233533),
                        new LatLng(54.570701,-1.233627),
                        new LatLng(54.57068,-1.233631),
                        new LatLng(54.570683,-1.233682),
                        new LatLng(54.570706,-1.233677),
                        new LatLng(54.570714,-1.233775),
                        new LatLng(54.570859,-1.233745),
                        new LatLng(54.570849,-1.2336))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building23.setTag("23");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570717,-1.233619))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twenty_three_marker)));


        building24 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.570901,-1.23443),
                        new LatLng(54.570895,-1.23435),
                        new LatLng(54.570856,-1.234354),
                        new LatLng(54.570851,-1.23428),
                        new LatLng(54.570641,-1.234316),
                        new LatLng(54.570653,-1.234481),
                        new LatLng(54.570763,-1.234461),
                        new LatLng(54.570786,-1.23464),
                        new LatLng(54.570862,-1.234628),
                        new LatLng(54.57085,-1.234445))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building24.setTag("24");
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570768,-1.234372))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.twenty_four_marker)));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570365,-1.233085),
                        new LatLng(54.570419,-1.233073),
                        new LatLng(54.570696,-1.237038),
                        new LatLng(54.570627,-1.237057))
                .fillColor(COLOR_PATH_WHITE)
                .strokeColor(COLOR_PATH_WHITE)
                .strokeWidth(2));

        //building site
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571628,-1.234692),
                        new LatLng(54.57165,-1.234992),
                        new LatLng(54.571467,-1.23532),
                        new LatLng(54.57059,-1.235491),
                        new LatLng(54.570565,-1.235067),
                        new LatLng(54.570944,-1.23503),
                        new LatLng(54.571044,-1.23488),
                        new LatLng(54.571031,-1.234644),
                        new LatLng(54.571305,-1.234597),
                        new LatLng(54.571317,-1.234744))
                .fillColor(COLOR_BUILDING_ARGB)
                .strokeWidth(4)
                .strokeColor(Color.BLACK));

        mMap.addCircle(new CircleOptions()
                .center(new LatLng(54.572101,-1.235597))
                .radius(5)
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571622,-1.233804),
                        new LatLng(54.571554,-1.232808),
                        new LatLng(54.571306,-1.232862),
                        new LatLng(54.571339,-1.233503),
                        new LatLng(54.571322,-1.233506),
                        new LatLng(54.571329,-1.233599),
                        new LatLng(54.571346,-1.233596),
                        new LatLng(54.571362,-1.233848))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571422,-1.233287))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571809, -1.236381))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571916, -1.233205))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570976, -1.233346))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570033,-1.233407))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569819,-1.235263))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.569718,-1.2371))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.568568,-1.237474))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.568716,-1.234848))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.570431,-1.237236))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571407,-1.234379))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));

                mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571648,-1.236563),
                        new LatLng(54.571668,-1.236559),
                        new LatLng(54.571677,-1.236655),
                        new LatLng(54.57203,-1.236572),
                        new LatLng(54.571999,-1.236134),
                        new LatLng(54.571738,-1.236203),
                        new LatLng(54.57175,-1.236321),
                        new LatLng(54.571653,-1.236347),
                        new LatLng(54.571662,-1.236483),
                        new LatLng(54.571642,-1.236488))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

         mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572053,-1.236574),
                        new LatLng(54.572034,-1.236254),
                        new LatLng(54.572056,-1.236235),
                        new LatLng(54.572056,-1.236195),
                        new LatLng(54.572063,-1.236153),
                        new LatLng(54.572072,-1.236101),
                        new LatLng(54.572084,-1.236063),
                        new LatLng(54.572101,-1.236018),
                        new LatLng(54.572123,-1.235948),
                        new LatLng(54.572151,-1.23589),
                        new LatLng(54.572177,-1.235827),
                        new LatLng(54.572229,-1.235735),
                        new LatLng(54.572287,-1.23662),
                        new LatLng(54.572337,-1.237361),
                        new LatLng(54.572154,-1.237403),
                        new LatLng(54.571643,-1.237528),
                        new LatLng(54.571636,-1.237441),
                        new LatLng(54.571681,-1.237432),
                        new LatLng(54.57164,-1.236661))
                 .fillColor(COLOR_PATH_WHITE)
                 .strokeWidth(0));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571828,-1.232742),
                        new LatLng(54.571916,-1.232724),
                        new LatLng(54.571989,-1.233691),
                        new LatLng(54.571954,-1.233699),
                        new LatLng(54.571955,-1.233739),
                        new LatLng(54.571911,-1.233761))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571214,-1.233197),
                        new LatLng(54.570869,-1.23327),
                        new LatLng(54.570891,-1.233467),
                        new LatLng(54.570954,-1.233452),
                        new LatLng(54.571004,-1.23348),
                        new LatLng(54.571163,-1.23344),
                        new LatLng(54.571154,-1.233302),
                        new LatLng(54.571222,-1.233291))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.569935,-1.237588),
                        new LatLng(54.569682,-1.237675),
                        new LatLng(54.569633,-1.236874),
                        new LatLng(54.569633,-1.236774),
                        new LatLng(54.569636,-1.236741),
                        new LatLng(54.569643,-1.236721),
                        new LatLng(54.569648,-1.236704),
                        new LatLng(54.569657,-1.236689),
                        new LatLng(54.56967,-1.236673),
                        new LatLng(54.569681,-1.236665),
                        new LatLng(54.569691,-1.236658),
                        new LatLng(54.569703,-1.236651),
                        new LatLng(54.569731,-1.236644),
                        new LatLng(54.569873,-1.236615))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.569771,-1.233127),
                    new LatLng(54.569803,-1.233709),
                    new LatLng(54.570266,-1.2336),
                    new LatLng(54.570232,-1.233118),
                    new LatLng(54.569832,-1.2332),
                    new LatLng(54.569827,-1.233116))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570638,-1.237264),
                        new LatLng(54.570584,-1.237273),
                        new LatLng(54.570586,-1.23735),
                        new LatLng(54.570267,-1.237413),
                        new LatLng(54.570252,-1.237181),
                        new LatLng(54.570572,-1.237127),
                        new LatLng(54.570575,-1.237181),
                        new LatLng(54.570634,-1.237176))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571647,-1.234626),
                        new LatLng(54.571605,-1.233964),
                        new LatLng(54.571369,-1.234004),
                        new LatLng(54.571366,-1.233969),
                        new LatLng(54.571339,-1.233977),
                        new LatLng(54.57137,-1.234323),
                        new LatLng(54.571056,-1.234401),
                        new LatLng(54.571069,-1.234562),
                        new LatLng(54.57138,-1.2345),
                        new LatLng(54.571393,-1.234674))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        //grass area
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572241,-1.235827),
                        new LatLng(54.57223,-1.235817),
                        new LatLng(54.572156,-1.235984),
                        new LatLng(54.572132,-1.236062),
                        new LatLng(54.572117,-1.23611),
                        new LatLng(54.572109,-1.236134),
                        new LatLng(54.572105,-1.23616),
                        new LatLng(54.5721,-1.236189),
                        new LatLng(54.572097,-1.236216),
                        new LatLng(54.572097,-1.236251),
                        new LatLng(54.5721,-1.236286),
                        new LatLng(54.57211,-1.236326),
                        new LatLng(54.572138,-1.236389),
                        new LatLng(54.572174,-1.236462),
                        new LatLng(54.572195,-1.236502),
                        new LatLng(54.572226,-1.23655),
                        new LatLng(54.572285,-1.236623))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570923,-1.236054),
                        new LatLng(54.57089,-1.23558),
                        new LatLng(54.570721,-1.235611),
                        new LatLng(54.570709,-1.235658),
                        new LatLng(54.570737,-1.23609))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571704,-1.237429),
                new LatLng(54.571652,-1.236674),
                new LatLng(54.571672,-1.236711),
                new LatLng(54.571754,-1.236754),
                new LatLng(54.571808,-1.236776),
                new LatLng(54.571864,-1.236811),
                new LatLng(54.571915,-1.236843),
                new LatLng(54.571955,-1.236872),
                new LatLng(54.571993,-1.236914),
                new LatLng(54.572021,-1.236945),
                new LatLng(54.572037,-1.236967),
                new LatLng(54.572049,-1.236982),
                new LatLng(54.57205,-1.237004),
                new LatLng(54.572042,-1.237039),
                new LatLng(54.572034,-1.237081),
                new LatLng(54.572006,-1.237127),
                new LatLng(54.571969,-1.237166),
                new LatLng(54.571938,-1.237212),
                new LatLng(54.571908,-1.237248),
                new LatLng(54.571883,-1.237298),
                new LatLng(54.571863,-1.237342),
                new LatLng(54.571841,-1.237365),
                new LatLng(54.571826,-1.237377),
                new LatLng(54.571806,-1.237397),
                new LatLng(54.571782,-1.237409),
                new LatLng(54.571753,-1.237419))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(0));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571678,-1.236661),
                    new LatLng(54.572051,-1.236573),
                    new LatLng(54.57203,-1.236254),
                    new LatLng(54.572063,-1.236249),
                    new LatLng(54.572068,-1.23627),
                    new LatLng(54.572077,-1.236299),
                    new LatLng(54.572089,-1.236336),
                    new LatLng(54.572098,-1.236368),
                    new LatLng(54.572106,-1.236393),
                    new LatLng(54.572115,-1.236415),
                    new LatLng(54.572131,-1.236453),
                    new LatLng(54.572139,-1.236468),
                    new LatLng(54.572143,-1.236546),
                    new LatLng(54.572147,-1.236613),
                    new LatLng(54.572152,-1.236709),
                    new LatLng(54.572147,-1.236745),
                    new LatLng(54.572137,-1.236786),
                    new LatLng(54.572129,-1.236821),
                    new LatLng(54.572122,-1.236843),
                    new LatLng(54.572115,-1.236874),
                    new LatLng(54.572102,-1.236881),
                    new LatLng(54.57208,-1.236866),
                    new LatLng(54.572056,-1.236861),
                    new LatLng(54.572047,-1.236871),
                    new LatLng(54.57203,-1.236888),
                    new LatLng(54.572018,-1.236897),
                    new LatLng(54.571954,-1.236835),
                    new LatLng(54.571885,-1.236785),
                    new LatLng(54.571805,-1.236737),
                    new LatLng(54.571767,-1.236726),
                    new LatLng(54.571724,-1.236713),
                    new LatLng(54.571701,-1.236701),
                    new LatLng(54.571687,-1.236686))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(0));

        mMap.addPolygon(new PolygonOptions()
                        .add(new LatLng(54.571638,-1.237535),
                            new LatLng(54.571637,-1.237509),
                            new LatLng(54.571642,-1.237495),
                            new LatLng(54.571647,-1.237481),
                            new LatLng(54.571649,-1.237474),
                            new LatLng(54.571696,-1.237463),
                            new LatLng(54.57174,-1.237457),
                            new LatLng(54.571769,-1.237448),
                            new LatLng(54.57179,-1.237443),
                            new LatLng(54.57181,-1.237428),
                            new LatLng(54.571842,-1.237404),
                            new LatLng(54.571865,-1.237372),
                            new LatLng(54.571883,-1.237347),
                            new LatLng(54.571902,-1.237308),
                            new LatLng(54.571922,-1.237275),
                            new LatLng(54.57195,-1.237233),
                            new LatLng(54.571982,-1.237195),
                            new LatLng(54.571997,-1.237177),
                            new LatLng(54.572023,-1.237153),
                            new LatLng(54.57204,-1.237135),
                            new LatLng(54.572058,-1.23711),
                            new LatLng(54.572073,-1.237095),
                            new LatLng(54.572089,-1.237082),
                            new LatLng(54.572107,-1.237088),
                            new LatLng(54.572135,-1.237096),
                            new LatLng(54.572158,-1.237107),
                            new LatLng(54.572181,-1.237121),
                            new LatLng(54.572203,-1.237134),
                            new LatLng(54.57222,-1.23715),
                            new LatLng(54.572239,-1.237174),
                            new LatLng(54.572253,-1.237198),
                            new LatLng(54.572269,-1.237228),
                            new LatLng(54.572279,-1.237259),
                            new LatLng(54.572285,-1.237279),
                            new LatLng(54.572287,-1.237308),
                            new LatLng(54.572293,-1.237339),
                            new LatLng(54.572294,-1.237381))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(0));



        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570479,-1.235537),
                    new LatLng(54.570336,-1.235568),
                    new LatLng(54.570338,-1.235542),
                    new LatLng(54.570342,-1.235514),
                    new LatLng(54.570348,-1.235481),
                    new LatLng(54.570356,-1.23545),
                    new LatLng(54.570361,-1.235429),
                    new LatLng(54.57037,-1.235404),
                    new LatLng(54.57038,-1.235383),
                    new LatLng(54.570389,-1.235361),
                    new LatLng(54.570401,-1.235353),
                    new LatLng(54.570418,-1.235352),
                    new LatLng(54.570433,-1.235359),
                    new LatLng(54.570447,-1.235374),
                    new LatLng(54.570455,-1.235397),
                    new LatLng(54.570473,-1.235483))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeColor(COLOR_PATH_WHITE)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570492,-1.235653),
                    new LatLng(54.570494,-1.235678),
                    new LatLng(54.570494,-1.235694),
                    new LatLng(54.570495,-1.235729),
                    new LatLng(54.570495,-1.235765),
                    new LatLng(54.570496,-1.235791),
                    new LatLng(54.570496,-1.235808),
                    new LatLng(54.570494,-1.235826),
                    new LatLng(54.570494,-1.235855),
                    new LatLng(54.570492,-1.235882),
                    new LatLng( 54.570492,-1.235898),
                    new LatLng(54.570489,-1.235924),
                    new LatLng(54.570488,-1.23594),
                    new LatLng(54.570487,-1.235957),
                    new LatLng(54.570485,-1.23597),
                    new LatLng(54.570484,-1.235982),
                    new LatLng(54.570482,-1.236003),
                    new LatLng(54.570473,-1.235999),
                    new LatLng(54.570465,-1.235991),
                    new LatLng(54.570459,-1.235983),
                    new LatLng(54.570452,-1.235977),
                    new LatLng(54.570447,-1.235972),
                    new LatLng(54.570441,-1.235967),
                    new LatLng(54.570431,-1.235954),
                    new LatLng(54.570423,-1.235945),
                    new LatLng(54.570414,-1.23593),
                    new LatLng(54.570406,-1.235918),
                    new LatLng(54.570398,-1.235902),
                    new LatLng(54.570392,-1.235892),
                    new LatLng(54.570386,-1.235879),
                    new LatLng(54.570378,-1.235862),
                    new LatLng(54.570371,-1.235845),
                    new LatLng(54.570365,-1.235828),
                    new LatLng(54.570359,-1.235809),
                    new LatLng(54.570353,-1.235788),
                    new LatLng(54.570351,-1.235774),
                    new LatLng(54.570347,-1.235758),
                    new LatLng(54.570344,-1.235737),
                    new LatLng(54.570341,-1.235725),
                    new LatLng(54.570338,-1.235702),
                    new LatLng(54.570335,-1.23568))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeColor(COLOR_PATH_WHITE)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572325,-1.237231),
                        new LatLng(54.572295,-1.237235),
                        new LatLng(54.572285,-1.237208),
                        new LatLng(54.572275,-1.237185),
                        new LatLng(54.572265,-1.237164),
                        new LatLng(54.572257,-1.237153),
                        new LatLng(54.57225,-1.237141),
                        new LatLng(54.572238,-1.237126),
                        new LatLng(54.572226,-1.237112),
                        new LatLng(54.572212,-1.2371),
                        new LatLng(54.572198,-1.23709),
                        new LatLng(54.572187,-1.237083),
                        new LatLng(54.572175,-1.237077),
                        new LatLng(54.572162,-1.237071),
                        new LatLng(54.57215,-1.237063),
                        new LatLng(54.572138,-1.23706),
                        new LatLng(54.572124,-1.237046),
                        new LatLng(54.57211,-1.237028),
                        new LatLng(54.572117,-1.236967),
                        new LatLng(54.572133,-1.236928),
                        new LatLng(54.572157,-1.236826),
                        new LatLng(54.57217,-1.236767),
                        new LatLng(54.572182,-1.236719),
                        new LatLng(54.572198,-1.236663),
                        new LatLng(54.572216,-1.23661),
                        new LatLng(54.572232,-1.236632),
                        new LatLng(54.572255,-1.236652),
                        new LatLng(54.572272,-1.236666),
                        new LatLng(54.572288,-1.236679))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(0));


        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.569043,-1.236782),
                        new LatLng(54.568938,-1.235172),
                        new LatLng(54.568331,-1.235312),
                        new LatLng(54.568446,-1.236926))
                .fillColor(COLOR_GRASS_ARGB)
                .strokeWidth(0));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.568266,-1.235244),
                        new LatLng(54.569108,-1.235118),
                        new LatLng(54.569061,-1.234391),
                        new LatLng(54.568675,-1.234458),
                        new LatLng(54.568689,-1.234648),
                        new LatLng(54.568232,-1.234748))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.569551,-1.235185),
                    new LatLng(54.569778,-1.235153),
                    new LatLng(54.569795,-1.235464),
                    new LatLng(54.569971,-1.235434),
                    new LatLng(54.569902,-1.234251),
                    new LatLng(54.569787,-1.234278),
                    new LatLng(54.569831,-1.235077),
                    new LatLng(54.569742,-1.235094),
                    new LatLng(54.569738,-1.235025),
                    new LatLng(54.569772,-1.235016),
                    new LatLng(54.569757,-1.234786),
                    new LatLng(54.569625,-1.234806),
                    new LatLng(54.569644,-1.23504),
                    new LatLng(54.569704,-1.23503),
                    new LatLng(54.569707,-1.235103),
                    new LatLng(54.569546,-1.235128))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.568965,-1.23821),
                        new LatLng(54.568917,-1.237338),
                        new LatLng(54.568837,-1.237351),
                        new LatLng(54.568834,-1.237278),
                        new LatLng(54.568655,-1.237317),
                        new LatLng(54.568638,-1.237104),
                        new LatLng(54.568449,-1.237155),
                        new LatLng(54.56845,-1.237235),
                        new LatLng(54.568413,-1.237249),
                        new LatLng(54.568472,-1.238245),
                        new LatLng(54.568708,-1.23817),
                        new LatLng(54.568687,-1.237809),
                        new LatLng(54.568869,-1.237781),
                        new LatLng(54.568896,-1.23823))
                .strokeColor(COLOR_ROAD_ARGB)
                .strokeWidth(2));

        //roads
        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570814,-1.238413),
        new LatLng(54.570724,-1.237158),
        new LatLng(54.570716,-1.237139),
        new LatLng(54.570709,-1.237129),
        new LatLng(54.570703,-1.237123),
        new LatLng(54.570696,-1.237115),
        new LatLng(54.570686,-1.237113),
        new LatLng(54.570672,-1.237111),
        new LatLng(54.570662,-1.237115),
        new LatLng(54.570654,-1.237115),
        new LatLng(54.570646,-1.237125),
        new LatLng(54.570641,-1.237134),
        new LatLng(54.570635,-1.237155),
        new LatLng(54.570726,-1.238444))
                        .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572555,-1.232531),
                        new LatLng(54.571251,-1.232809),
                        new LatLng(54.569413,-1.233191),
                        new LatLng(54.569558,-1.235352),
                        new LatLng(54.569509,-1.235363),
                        new LatLng(54.569349,-1.233212),
                        new LatLng(54.568631,-1.233351),
                        new LatLng(54.568684,-1.234197),
                        new LatLng(54.568645,-1.234201),
                        new LatLng(54.568585,-1.233354),
                        new LatLng(54.567861,-1.233506),
                        new LatLng(54.568204,-1.238469),
                        new LatLng(54.568142,-1.238492),
                        new LatLng(54.567811,-1.233517),
                        new LatLng(54.5678,-1.233391),
                        new LatLng(54.568547,-1.233232),
                        new LatLng(54.568494,-1.232493),
                        new LatLng(54.568554,-1.232489),
                        new LatLng(54.568604,-1.233228),
                        new LatLng(54.569339,-1.233089),
                        new LatLng(54.569283,-1.23227),
                        new LatLng(54.569332,-1.232262),
                        new LatLng(54.569399,-1.233083),
                        new LatLng(54.570361,-1.232877),
                        new LatLng(54.570311,-1.232065),
                        new LatLng(54.570338,-1.232063),
                        new LatLng(54.570398,-1.232874),
                        new LatLng(54.571192,-1.232704),
                        new LatLng(54.571134,-1.231849),
                        new LatLng(54.571165,-1.231845),
                        new LatLng(54.571225,-1.2327),
                        new LatLng(54.572542,-1.232416))
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572798,-1.235474), new LatLng(54.57214,-1.235608), new LatLng(54.572135,-1.235557), new LatLng(54.572793,-1.235407))
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572644,-1.233675),
                        new LatLng(54.571318,-1.233969),
                        new LatLng(54.571275,-1.233945),
                        new LatLng(54.571198,-1.232769),
                        new LatLng(54.57123,-1.232764),
                        new LatLng(54.571304,-1.23391),
                        new LatLng(54.572639,-1.233607))
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));


        Resources.Theme theme = super.getTheme();                                     //gets the current theme used by the activity
        theme.applyStyle(R.style.CampusPreload, true);                          //set the theme of the activity to the campus preload theme
        //navigationView.setBackgroundResource(R.drawable.drawer_background);
        navigationView.setBackgroundColor(getResources().getColor(R.color.CampusMapKey));           //sets the background of the navigation drawer to overwrite the initial them image, to make the navigation drawer usable

    }

    public void resetCamera(){                                                      //resets the camera to the initial gps coordinates and zppm level
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 16.5f));      //function that resets the map camera to the centre of campus
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {                    //sets up the navigation menu
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_campus_map){
            //Intent intent = new Intent(this, MainActivity.class);//opens a new screen when the shopping list is clicked
            //startActivity(intent);
        } else if (id == R.id.action_current_Location) {
            Intent intent = new Intent(this, CurrentLocation.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        } else if (id == R.id.action_place_search) {
            Intent intent = new Intent(this, PlacesSearch.class);//opens a new screen when the shopping list is clicked
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
