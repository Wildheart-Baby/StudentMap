package com.example.v8181191.studentmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Wildheart on 17/03/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "StudMap.db";

    // Table names
    private static final String TABLE_SEARCHES = "searches";
    private static final String TABLE_FAVOURITES = "favourites";

    // Table Columns names
    private static final String SEARCHES_KEY_ID = "id";
    private static final String SEARCHES_SEARCH_TERM = "search";

    private static final String FAVOURITES_KEY_ID = "id";
    private static final String FAVOURITES_NAME = "name";
    private static final String FAVOURITES_PHOTO = "photo"; //, , , , ,
    private static final String FAVOURITES_OPEN = "open";
    private static final String FAVOURITES_PLACE_ID = "place_id";
    private static final String FAVOURITES_PLACE_TYPE = "place_type";
    private static final String FAVOURITES_NUMBER_RATINGS = "number_ratings";
    private static final String FAVOURITES_ADDRESS = "address";
    private static final String FAVOURITES_COST = "cost"; //int
    private static final String FAVOURITES_RATING = "rating"; //real

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SEARCHES_TABLE = "CREATE TABLE " + TABLE_SEARCHES + "("
                + SEARCHES_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SEARCHES_SEARCH_TERM + " TEXT)";
        db.execSQL(CREATE_SEARCHES_TABLE);

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_FAVOURITES + "("
                + FAVOURITES_KEY_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT," + FAVOURITES_NAME + " TEXT,"
                + FAVOURITES_PHOTO + " TEXT," + FAVOURITES_OPEN + " TEXT,"
                + FAVOURITES_PLACE_ID + " TEXT," + FAVOURITES_PLACE_TYPE + " TEXT,"
                + FAVOURITES_NUMBER_RATINGS + " TEXT," + FAVOURITES_ADDRESS + " TEXT,"
                + FAVOURITES_COST + " INTEGER," + FAVOURITES_RATING + " REAL" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addSearch(SearchItems searchItems){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SEARCHES_SEARCH_TERM, searchItems.getSearch());

        db.insert(TABLE_SEARCHES, null, values);
        db.close();
    }

    public ArrayList<String> getSearches(){
        ArrayList<String> searchesList = new ArrayList<>();
        searchesList.clear();
        String searchQuery = "select * from " + TABLE_SEARCHES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if(c.moveToFirst()){
            do {
                searchesList.add(c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return searchesList;
    }

    public void addFavourite(FavouriteItems favouriteItems){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAVOURITES_NAME, favouriteItems.getPlaceName());
        values.put(FAVOURITES_PHOTO, favouriteItems.getPhoto());
        values.put(FAVOURITES_OPEN, favouriteItems.getOpen());
        values.put(FAVOURITES_PLACE_ID, favouriteItems.getPlace_id());
        values.put(FAVOURITES_PLACE_TYPE, favouriteItems.getPlace_type());
        values.put(FAVOURITES_NUMBER_RATINGS, favouriteItems.getNumber_ratings());
        values.put(FAVOURITES_ADDRESS, favouriteItems.getAddress());
        values.put(FAVOURITES_COST, favouriteItems.getCost());
        values.put(FAVOURITES_RATING, favouriteItems.getRating());
        //values.put(SEARCHES_SEARCH_TERM, searchItems.getSearch());

        db.insert(TABLE_FAVOURITES, null, values);
        db.close();
    }

    public ArrayList<String> getPlaceIds(){
        ArrayList<String> placesIdList = new ArrayList<>();
        placesIdList.clear();
        String searchQuery = "select "+ FAVOURITES_PLACE_ID + " from " + TABLE_FAVOURITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if(c.moveToFirst()){
            do {
                placesIdList.add(c.getString(c.getColumnIndex(FAVOURITES_PLACE_ID)));
            } while ((c.moveToNext()));
        }
        c.close();
        db.close();
        return placesIdList;
    }

    public ArrayList<PlaceItems> getFavourites(){
        ArrayList<PlaceItems> favouritesList = new ArrayList<>();
        favouritesList.clear();
        String searchQuery = "select * from " + TABLE_FAVOURITES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if(c.moveToFirst()){
            do {
                final PlaceItems favouriteListItems = new PlaceItems();
                favouriteListItems.setPlaceName(c.getString(c.getColumnIndex(FAVOURITES_NAME)));
                favouriteListItems.setPlacePhoto(c.getString(c.getColumnIndex(FAVOURITES_PHOTO)));
                favouriteListItems.setOpenTimes(c.getString(c.getColumnIndex(FAVOURITES_OPEN)));
                favouriteListItems.setPlaceId(c.getString(c.getColumnIndex(FAVOURITES_PLACE_ID)));
                favouriteListItems.setPlaceType(c.getString(c.getColumnIndex(FAVOURITES_PLACE_TYPE)));
                favouriteListItems.setNumberRatings(c.getInt(c.getColumnIndex(FAVOURITES_NUMBER_RATINGS)));
                favouriteListItems.setPlaceAddress(c.getString(c.getColumnIndex(FAVOURITES_ADDRESS)));
                favouriteListItems.setCost(c.getInt(c.getColumnIndex(FAVOURITES_COST)));
                favouriteListItems.setRating(c.getDouble(c.getColumnIndex(FAVOURITES_RATING)));
                favouritesList.add(favouriteListItems);
                Log.i("DBH", c.getString(c.getColumnIndex(FAVOURITES_NAME)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        Log.i("DBH", "Added faves");
        return favouritesList;
    }


    }

