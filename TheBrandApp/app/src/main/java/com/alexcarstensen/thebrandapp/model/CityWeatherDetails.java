package com.alexcarstensen.thebrandapp.model;



/**
 * Created by Peter Ring on 27/09/2016.
 */

public class CityWeatherDetails
{


    private Coordinates coord;
    private Sys sys;

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    private Weather[] weather;
    private Main main;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private int dt;
    private int id;
    private String name;
    private String cod;

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }



    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    public void setMain(Main main) {
        this.main = main;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public int getDt() {
        return dt;
    }

    public int getId() {
        return id;
    }

    public Main getMain() {
        return main;
    }

    public Rain getRain() {
        return rain;
    }

    public String getCod() {
        return cod;
    }


    public Sys getSys() {
        return sys;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }


}