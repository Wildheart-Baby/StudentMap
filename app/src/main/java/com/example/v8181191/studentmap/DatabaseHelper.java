package com.example.v8181191.studentmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wildheart on 17/03/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "shopsInfo";

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

    public void addFavourite(Search search){
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(SEARCHES_SEARCH_TERM, search.getSearch());

        db.insert(TABLE_SEARCHES, null, values);
        db.close();
    }

    public List<Search>  getSearches(){
        List<Search> searchesList = new ArrayList<>();
        String searchQuery = "select * from " + TABLE_SEARCHES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(searchQuery, null);

        if(c.moveToFirst()){
            do {
                Search search = new Search();
                search.setSearch(c.getString(1));
                searchesList.add(search);
            } while (c.moveToNext());
        }
        return searchesList;
    }

}

