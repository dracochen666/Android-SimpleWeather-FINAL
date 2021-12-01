package com.example.simpleweather.Model;

import java.io.Serializable;

public class Weather implements Serializable {
    public String temp = "";
    public String icon = "";
    public String city = "";
    public String toString(){
        return "city: " + city + " temp: " + temp + " icon: " + icon;
    }

}
