package com.example.v8181191.studentmap;

import android.graphics.Bitmap;

/**
 * Created by Wildheart on 28/02/2019.
 */

public class PlaceItems {
    private Double lat, lng, rating;
    private String name, description, photo, open, place_id, place_type, number_ratings, address;
    private Long sunrise, sunset;
    private Bitmap  placePhoto;

    public String getPlaceName() {return name;}
    public void setPlaceName(String name) {this.name = name;}

    public String getPlaceAddress() {return address;}
    public void setPlaceAddress(String address) {this.address = address;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getPlacePhoto() {return photo;}
    public void setPlacePhoto(String photo) {this.photo = photo;}

    public String getPlaceType() {return place_type;}
    public void setPlaceType(String place_type) {this.place_type = place_type;}

    public String getOpenTimes() {return open;}
    public void setOpenTimes(String open) {this.open = open;}

    public Double getLat() {return lat;}
    public void setLat(Double lat) {this.lat = lat;}

    public Double getLng() {return lng;}
    public void setLng(Double lng) {this.lng = lng;}

    public String getPlaceId() {return place_id;}
    public void setPlaceId(String place_id) {this.place_id = place_id;}

    public String getNumberRatings() {return number_ratings;}
    public void setNumberRatings(String number_ratings) {this.number_ratings = number_ratings;}

    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}

    public Bitmap getPhoto() {return placePhoto;}
    public void setPhoto(Bitmap placePhoto) {this.placePhoto = placePhoto;}

}
