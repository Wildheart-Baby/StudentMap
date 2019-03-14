package com.example.v8181191.studentmap;

/**
 * Created by Wildheart on 13/03/2019.
 */

public class LocationFunctions {

    public Double TotalDistance(Double PlaceLat, Double UserLat, Double PlaceLng, Double UserLng){
        double dLat = Math.toRadians(PlaceLat - UserLat);
        double dLon = Math.toRadians(PlaceLng - UserLng);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(UserLat)) * Math.cos(Math.toRadians(54.570792)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c2 = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        //6378.1 is gravity of earth
        Double distance =  c2*6378.1;
        distance = distance * 0.621371;
        Double totalDistance = distance * 1609.34;

        //return totalDistance;
        return distance;
    }
}
