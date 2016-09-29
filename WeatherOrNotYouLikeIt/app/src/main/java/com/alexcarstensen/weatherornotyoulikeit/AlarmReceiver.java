package com.alexcarstensen.weatherornotyoulikeit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexcarstensen.weatherornotyoulikeit.helpers.WeatherService;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Log.d("Alarm receiver ", WeatherService.LOG_LINE + "onReceive() called in alarm context");

        Intent weatherIntent = new Intent(context, WeatherService.class);

        context.startService(weatherIntent);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
