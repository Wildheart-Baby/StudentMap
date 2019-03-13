package com.example.v8181191.studentmap;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
public class LocationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String ARG_PLACE;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url, ot;
    Double locLat, locLong;
    TextView name, address, opentimes, phonenumber;

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
        }
        //createLocationFragment();
        loadPlaces(mParam1);
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

    public void loadPlaces(String place){
        Log.i("StudMapLF", "running function");
        url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + mParam1 +"&fields=name,formatted_address,geometry,photos,opening_hours,formatted_phone_number&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";

        RequestQueue queue = Volley.newRequestQueue(getActivity()); //sets up a reference to the volley library queue

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject placeData = new JSONObject(response);
                            name.setText(placeData.getJSONObject("result").getString("name"));
                            locLat = placeData.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                            locLong = placeData.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                            address.setText(placeData.getJSONObject("result").getString("formatted_address"));
                            phonenumber.setText(placeData.getJSONObject("result").getString("formatted_phone_number"));//

                            JSONArray openTimes = placeData.getJSONObject("result").getJSONObject("opening_hours").getJSONArray("weekday_text");
                            ot = openTimes.getString(0) + "\n" + openTimes.getString(1) + "\n" + openTimes.getString(2) + "\n" +
                                    openTimes.getString(3) + "\n" +openTimes.getString(4) + "\n" +openTimes.getString(5) + "\n" +
                                    openTimes.getString(6);

                            opentimes.setText(ot);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //json.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);
    }

}
