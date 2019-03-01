package com.example.v8181191.studentmap;

/**
 * Created by Wildheart on 28/02/2019.
 */

public class WeatherItems {
    private Double temp;
    private String description, icon;
    private Long sunrise, sunset;

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getIcon() {return icon;}
    public void setIcon(String icon) {this.icon = icon;}

    public Double getTemp() {return temp;}
    public void setTemp(Double temp) {this.temp = temp;}

    public Long getSunrise() {return sunrise;}
    public void setSunrise(Long sunrise) {this.sunrise = sunrise;}

    public Long getSunset() {return sunset;}
    public void setSunset(Long sunset) {this.sunset = sunset;}
}
