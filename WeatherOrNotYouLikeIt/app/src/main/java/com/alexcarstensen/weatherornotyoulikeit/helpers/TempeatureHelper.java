package com.alexcarstensen.weatherornotyoulikeit.helpers;

/**
 * Created by Peter Ring on 28/09/2016.
 */

public class TempeatureHelper {

    public static double KelvinToCelcius(double kelvinTemperature)
    {
        double celciusTemperature = kelvinTemperature - 273.15;

        return celciusTemperature;
    }

    public static double FahrenheitToCelcius(double fahrenheitTemperature)
    {
        double celciusTemperature = (fahrenheitTemperature - 32) / 1.8;

        return celciusTemperature;
    }
}
