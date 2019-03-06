package com.example.v8181191.studentmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Wildheart on 03/03/2019.
 */

public class PlacesAdapter extends BaseAdapter{

    static class ViewHolder {
        TextView placeName, rating, theratings, opentimes, address, placetype;
        ImageView stars, placephoto;
    }

    ArrayList<PlaceItems> placeList;
    Context context;
    String photo;

    public PlacesAdapter(Context context, ArrayList<PlaceItems> list){
        this.context = context;
        placeList = list;
    }

    @Override
    public int getCount() {
        return placeList.size();
    }

    @Override
    public Object getItem(int position) {
        return placeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlaceItems placeListItems = placeList.get(position);

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_result_item, null);

            ViewHolder holder = new ViewHolder();
            holder.placeName = convertView.findViewById(R.id.txtName);
            holder.rating = convertView.findViewById(R.id.txtRating);
            holder.placetype = convertView.findViewById(R.id.txtPlaceType);
            holder.theratings = convertView.findViewById(R.id.txtNumberRated);
            holder.opentimes = convertView.findViewById(R.id.txtPlaceTimes);
            holder.stars = convertView.findViewById(R.id.imgStarRating);
            holder.address = convertView.findViewById(R.id.txtAddress);
            holder.placephoto = convertView.findViewById(R.id.imgPhoto);

            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.placeName.setText(placeListItems.getPlaceName());
        String theRating = String.valueOf(placeListItems.getRating());
        holder.placetype.setText(placeListItems.getPlaceType());
        holder.rating.setText(theRating);
        holder.address.setText(placeListItems.getPlaceAddress());
        holder.theratings.setText("("+placeListItems.getNumberRatings()+")");
        photo = placeListItems.getPlacePhoto();

        photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=125&maxheight=82&photoreference=" + photo + "&key=AIzaSyAMOEaHPdbKbeFf2hpcZVncKv47drjHCaw";
        Glide.with(context).load(photo).into(holder.placephoto);

        if (placeListItems.getOpenTimes() == "true"){
            holder.opentimes.setText("Open Now");
        } else if (placeListItems.getOpenTimes() == "false"){
            holder.opentimes.setText("Closed");
        } else {
            holder.opentimes.setText("No listed time");
        }

        if (placeListItems.getRating() ==5.0 ){
            int coverid=context.getResources().getIdentifier("stars_five", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=4.5 ){
            int coverid=context.getResources().getIdentifier("stars_four_half", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=4.0 ){
            int coverid=context.getResources().getIdentifier("stars_four", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=3.5 ){
            int coverid=context.getResources().getIdentifier("stars_three_half", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=3.0 ){
            int coverid=context.getResources().getIdentifier("stars_three", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=2.5 ){
            int coverid=context.getResources().getIdentifier("stars_two_half", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=2.0 ){
            int coverid=context.getResources().getIdentifier("stars_two", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=1.5 ){
            int coverid=context.getResources().getIdentifier("stars_one_half", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=1.0 ){
            int coverid=context.getResources().getIdentifier("stars_one", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        } else if (placeListItems.getRating() >=0.5 ){
            int coverid=context.getResources().getIdentifier("stars_half", "drawable", context.getPackageName());
            holder.stars.setImageResource(coverid);
        }
        if (position % 2 == 0) {
            convertView.setBackgroundColor(Color.parseColor("#CAC9C5"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#C0C0BB"));
        }


        Log.i("StudMap", "done");
        return convertView;
    }
}
