package com.example.v8181191.studentmap;

/**
 * Created by Wildheart on 21/03/2019.
 */

public class FavouriteItems {

    String name, photo, open, place_id, place_type, address;
    Double rating;
    int cost, number_ratings;

    public FavouriteItems(String name, String photo, String open, String place_id, String place_type, String address, Double rating, int cost,int number_ratings)
    {
        this.name = name;
        this.photo = photo;
        this.open = open;
        this.place_id = place_id;
        this.place_type = place_type;
        this.address = address;
        this.rating = rating;
        this.cost = cost;
        this.number_ratings = number_ratings;
    }

    public String getPlaceName() {return name;}
    public void setPlaceName(String name) {this.name = name;}

    public String getPhoto() {return photo;}
    public void setPhoto(String photo) {this.photo = photo;}

    public String getOpen() {return open;}
    public void setOpen(String open) {this.open = open;}

    public String getAddress() {return address;}
    public void setAddress(String address) {this.address = address;}

    public String getPlace_id() {return place_id;}
    public void setPlace_id(String place_id) {this.place_id = place_id;}

    public String getPlace_type() {return place_type;}
    public void setPlace_type(String place_type) {this.place_type = place_type;}

    public Double getRating() {return rating;}
    public void setRating(Double rating) {this.rating = rating;}

    public int getCost() {return cost;}
    public void setCost(int cost) {this.cost = cost;}

    public int getNumber_ratings() {return number_ratings;}
    public void setNumber_Rating(int number_ratings) {this.number_ratings = number_ratings;}

}
