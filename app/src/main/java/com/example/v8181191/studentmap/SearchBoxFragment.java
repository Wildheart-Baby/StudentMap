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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import static com.android.volley.VolleyLog.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link SearchBoxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBoxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SearchListener mListener;
    String searchTerm;
    TextView searchBox;
    Button search;
    DatabaseHelper db;


    public SearchBoxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBoxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBoxFragment newInstance(String param1, String param2) {
        SearchBoxFragment fragment = new SearchBoxFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_search_box, container, false);

        searchBox = view.findViewById(R.id.etSearchTerm);
        search = view.findViewById(R.id.btnSearch);

        List<Search> searches = db.getSearches();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getSearchTerm();
                searchTerm = searchBox.getText().toString();
                db.addFavourite(new Search(searchTerm));
                mListener.onReceiveSearch(searchTerm);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener) {
            mListener = (SearchListener) context;
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


    public interface SearchListener {
        void onReceiveSearch(String search);
    }

    public void getSearchTerm(){
        searchTerm = searchBox.getText().toString();
        Log.i("StudMapSBF", searchTerm);
        mListener.onReceiveSearch(searchTerm);
        //mGoogleApiClient.disconnect();
    }

}