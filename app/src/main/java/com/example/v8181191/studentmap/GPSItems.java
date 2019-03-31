package com.example.v8181191.studentmap;

/**
 * Created by Wildheart on 27/03/2019.
 */

public class GPSItems {
    Double lat, lng;

    public GPSItems(Double lat, Double lng)
    {
        this.lat = lat;
        this.lng = lng;
    }

    public GPSItems() {
        //this.search=search;

    }

    public Double getLat() {return lat;}
    public void setLat(Double lat) {this.lat = lat;}

    public Double getLng() {return lng;}
    public void setLng(Double lng) {this.lng = lng;}
}
