package com.example.v8181191.studentmap;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


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
    AutoCompleteTextView searchBox;
    Button search;
    DatabaseHelper db;
    ArrayList<String> searches;

    public SearchBoxFragment() {
        // Required empty public constructor
    }

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
        searches = new ArrayList<>();

        for (String search : searches) {
            String log = "SearchItems: " + search;
            Log.i("SBF: : ", log);
        }

        setAutoAdapter();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacesSearch.json.setText("");
                searchTerm = searchBox.getText().toString();
                if (searches.contains(searchTerm)) {
                    mListener.onReceiveSearch(searchTerm);
                } else {
                    db.addSearch(new SearchItems(searchTerm));
                    mListener.onReceiveSearch(searchTerm);
                    setAutoAdapter();
                }

            }
        });
        return view;
    }

    public void setAutoAdapter(){
        searches = db.getSearches();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, searches);
        searchBox.setThreshold(1);
        searchBox.setAdapter(adapter);
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

    public interface SearchListener {
        void onReceiveSearch(String search);
    }


}