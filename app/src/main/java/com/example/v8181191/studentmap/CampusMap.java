package com.example.v8181191.studentmap;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
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
import android.view.MenuItem;
import android.view.View;
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

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private final String TAG = "StudentMapApp";
    private android.location.Location mCurrentLocation;
    LatLng llCurrentLocation;
    Marker mCurrLocationMarker;

    Polygon building1, building2, building3, building4, building5, building6, building7, building8, building9, building10, building11, building12, building13, building14;
    Polygon building15, building16, building17, building18, building19,building20, building21, building22, building23, building24, building25, building26, building27;

    String type;

    private static final int COLOR_ORANGE_ARGB = 0xFFF25E21;
    private static final int COLOR_ROAD_ARGB = 0xff4D5156;
    private static final int COLOR_GRASS_ARGB = 0xff6BBF5C;
    private static final int COLOR_BUILDING_ARGB = 0xffEFEC68;
    private static final int POLYGON_STROKE_WIDTH_PX = 0;


    //private LatLngBounds CampusBounds = new LatLngBounds(
    //        new LatLng(54.573043, -1.237703), new LatLng(54.567806, -1.233483));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LatLng campus = new LatLng(54.570254, -1.235165);
                LatLng campus = new LatLng(54.570493, -1.235149);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 16.25f));
            }
        });

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(16.0f);
        mMap.setMaxZoomPreference(20.0f);
        //mMap.setMaxZoomPreference(20.0f);
        //top left 54.573043, -1.237703
        //bottom right 54.567806, -1.233483
        //54.570543, -1.235653
        // Add a marker in Sydney and move the camera

        /*LatLng topLeft = new LatLng(54.573043, -1.237703);
        LatLng bottomRight = new LatLng(54.567806, -1.233483);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(topLeft);
        builder.include(bottomRight);
        builder.build();*/

        //LatLng campus = new LatLng(54.570254, -1.235165);
        LatLng campus = new LatLng(54.570493, -1.235149);

        LatLng home = new LatLng(54.944111, -1.536316);
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
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campus, 16.0f));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        /*LatLngBounds campusBounds = new LatLngBounds(
                new LatLng(54.567788, -1.232546),
                new LatLng(54.572696, -1.237863)
        );*/

        //mMap.setLatLngBoundsForCameraTarget(campusBounds);

         /*GroundOverlayOptions campusMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.campusmap2))
                .position(campus, 410f, 540f);


       GroundOverlayOptions campusMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.campusmap))
                .positionFromBounds(campusBounds);*/

        //mMap.addGroundOverlay(campusMap);
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
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);*/

        mCurrLocationMarker =  mMap.addMarker(new MarkerOptions()
                .position(llCurrentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));

    }

    public void addBuildings(){
        /*mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.570607, -1.237060), new LatLng(54.570145, -1.237157), new LatLng(54.570125, -1.236800), new LatLng(54.570137, -1.236757), new LatLng(54.570193, -1.236738), new LatLng(54.570252, -1.236813), new LatLng(54.570357, -1.236785), new LatLng(54.570566, -1.236550))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeColor(Color.GRAY)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));*/

        /**/

        /*mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.571227, -1.237479), new LatLng(54.571147, -1.236260), new LatLng(54.571564, -1.236154), new LatLng(54.571651, -1.237394))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeColor(Color.GRAY)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));*/


        building1 = mMap.addPolygon(new PolygonOptions()
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
        building5.setTag("5");

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

        building14 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572277, -1.235381), new LatLng(54.572210, -1.234529), new LatLng(54.571706, -1.234650), new LatLng(54.571734, -1.234921), new LatLng(54.572145, -1.234835), new LatLng(54.572182, -1.235400))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building14.setTag("14");

        building15 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.572711, -1.235285), new LatLng(54.572648, -1.234443), new LatLng(54.572260, -1.234534), new LatLng(54.572267, -1.234585), new LatLng(54.572211, -1.234614), new LatLng(54.572215, -1.234703), new LatLng(54.572502, -1.234643), new LatLng(54.572502, -1.234643), new LatLng(54.572497, -1.234711), new LatLng(54.572500, -1.234753), new LatLng(54.572402, -1.234810), new LatLng(54.572401, -1.234822), new LatLng(54.572396, -1.234875), new LatLng(54.572375, -1.234910), new LatLng(54.572383, -1.234970), new LatLng(54.572415, -1.234998), new LatLng(54.572531, -1.234998), new LatLng(54.572536, -1.235029), new LatLng(54.572526, -1.235052), new LatLng(54.572529, -1.235095), new LatLng(54.572544, -1.235159), new LatLng(54.572257, -1.235237), new LatLng(54.572255, -1.235286), new LatLng(54.572320, -1.235284), new LatLng(54.572332, -1.235379))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building15.setTag("15");

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
                .position(new LatLng(54.57221,-1.233581))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));

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

        building20 = mMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(new LatLng(54.571549,-1.232409),
                        new LatLng(54.571538,-1.232201),
                        new LatLng(54.571277,-1.232265),
                        new LatLng(54.571289,-1.23247))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));
        building20.setTag("20");

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


        mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
            @Override
            public void onPolygonClick(Polygon polygon) {
                if (polygon.getTag() != null) {
                    type = polygon.getTag().toString();
                }
                //AlertDialog.Builder builder = new AlertDialog.Builder(CampusMap.this);
                switch (type) {
                    case "1":
                        Toast.makeText(getApplicationContext(), "The Curve", Toast.LENGTH_SHORT).show();
                        break;
                    case "2":
                        Toast.makeText(getApplicationContext(), "The Library", Toast.LENGTH_SHORT).show();
                        break;
                    case "3":
                        Toast.makeText(getApplicationContext(), "Students Union", Toast.LENGTH_SHORT).show();
                        break;
                    case "4":
                        Toast.makeText(getApplicationContext(), "Greig", Toast.LENGTH_SHORT).show();
                        break;
                    case "5":
                        Toast.makeText(getApplicationContext(), "Europa", Toast.LENGTH_SHORT).show();
                        break;
                    case"14":
                        Toast.makeText(getApplicationContext(), "Middlesborough Tower", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

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


        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572798,-1.235474), new LatLng(54.57214,-1.235608), new LatLng(54.572135,-1.235557), new LatLng(54.572793,-1.235407))
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
                .position(new LatLng(54.571362,-1.233289))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(54.571796,-1.236476))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));






        mMap.addCircle(new CircleOptions()
                .center(new LatLng(54.572101,-1.235597))
                .radius(5)
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

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
                .add(new LatLng(54.572644,-1.233675),
                                new LatLng(54.571318,-1.233969),
                                new LatLng(54.571275,-1.233945),
                                new LatLng(54.571198,-1.232769),
                                new LatLng(54.57123,-1.232764),
                                new LatLng(54.571304,-1.23391),
                                new LatLng(54.572639,-1.233607))
                .fillColor(COLOR_ROAD_ARGB)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

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

        //CampusMap.setTheme(R.style.AppTheme.NoActionBar);
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.AppTheme_NoActionBar, true);

       /*

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.572711, -1.235285), new LatLng(54.572648, -1.234443), new LatLng(54.572260, -1.234534), new LatLng(54.572267, -1.234585), new LatLng(54.572223,-1.234605), new LatLng(54.572209, -1.234536), new LatLng(54.571711,-1.234643), new LatLng(54.571734, -1.234927), new LatLng(54.572133, -1.234835), new LatLng(54.572180,-1.235406), new LatLng(54.572278, -1.235383), new LatLng(54.572281, -1.235273), new LatLng(54.572320, -1.235284), new LatLng(54.572333, -1.235376))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeColor(Color.GRAY)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));

        mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(54.568585, -1.234044), new LatLng(54.568548, -1.233550), new LatLng(54.568408, -1.233559), new LatLng(54.568437, -1.233908), new LatLng(54.568301, -1.233946), new LatLng(54.568309, -1.234099))
                .fillColor(COLOR_ORANGE_ARGB)
                .strokeColor(Color.GRAY)
                .strokeWidth(POLYGON_STROKE_WIDTH_PX));*/

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
