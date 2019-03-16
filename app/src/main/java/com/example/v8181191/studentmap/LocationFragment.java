package com.example.v8181191.studentmap;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String ARG_PLACE;
    public static Double ARG_USERLAT;
    public static Double ARG_USERLNG;

    LatLng placeMarker;
    private GoogleMap mMap;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url, ot, conPhone;
    Double locLat, locLong, userLat, userLng, totalDistance;
    TextView name, address, opentimes, phonenumber, locdistance;
    LocationFunctions lf;
    String tDistance;
    ImageButton Phone, Maps;

    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PLACE);
            userLat = getArguments().getDouble("ARG_USERLAT");
            userLng = getArguments().getDouble("ARG_USERLNG");

        }
        //createLocationFragment();
        loadPlaces(mParam1, userLat, userLng);
        lf = new LocationFunctions();


    }

    private void createLocationFragment (){
        FragmentManager fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.fragment_container, LocationFragment.newInstance());

        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        name = view.findViewById(R.id.txtName);
        address = view.findViewById(R.id.txtAddress);
        opentimes = view.findViewById(R.id.txtOpenTimes);
        phonenumber = view.findViewById(R.id.txtPhoneNumber);
        locdistance = view.findViewById(R.id.txtMiles);
        Phone = view.findViewById(R.id.imgPhone);
        Maps = view.findViewById(R.id.imgLocation);

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conPhone !=null){
                    makeCall();
                } else {
                    Toast toast = Toast.makeText(getActivity(), "No phone number to call", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMaps(locLat, locLong);
            }
        });

        //supportMap = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mvLocation);
        //supportMap.getMapAsync(this);
        //SupportMapFragment supportMap = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mvLocation);
        //MapFragment supportMap = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mvLocation);

        //supportMap.getMapAsync(this);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(placeMarker));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeMarker));
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(17.0f);
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

    public void startMap(){
        MapFragment supportMap = (MapFragment)getChildFragmentManager().findFragmentById(R.id.mvLocation);
        supportMap.getMapAsync(this);
    }

    public void loadPlaces(String place, final Double userLat, final Double userLng){
        Log.i("StudMapLF", "running function");
        url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + mParam1 +"&fields=name,formatted_address,geometry,photos,opening_hours,formatted_phone_number&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";

        RequestQueue queue = Volley.newRequestQueue(getActivity()); //sets up a reference to the volley library queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("SMR", response);
                        try {

                            JSONObject placeData = new JSONObject(response);
                            name.setText(placeData.getJSONObject("result").getString("name"));
                            locLat = placeData.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            locLong = placeData.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            placeMarker = new LatLng(locLat, locLong);
                            address.setText(placeData.getJSONObject("result").getString("formatted_address"));
                            conPhone = placeData.getJSONObject("result").getString("formatted_phone_number");
                            phonenumber.setText(conPhone);//

                            JSONArray openTimes = placeData.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");
                            ot = openTimes.getString(0) + "\n" + openTimes.getString(1) + "\n" + openTimes.getString(2) + "\n" +
                                    openTimes.getString(3) + "\n" +openTimes.getString(4) + "\n" +openTimes.getString(5) + "\n" +
                                    openTimes.getString(6);
                            totalDistance = lf.TotalDistance(locLat, userLat ,locLong, userLng);
                            tDistance = String.format("%.2f", totalDistance);
                            tDistance = tDistance + " miles from location";
                            Log.i("StudMapLF", tDistance);
                            opentimes.setText(ot);
                            locdistance.setText(tDistance);
                            startMap();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("StudMapError", e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //json.setText("That didn't work!");
                Log.i("StudMap", "Error"+ error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void makeCall(){
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + conPhone + "?call&video=true")); //use ACTION_CALL class  skype:username?call&video=true

        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                    10);
            return;
        }else {     //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                //Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openMaps(Double lat, Double lng){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng+"+&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}
