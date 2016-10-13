package com.alexcarstensen.thebrandapp.Helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by plex on 10/13/16.
 */

public class PermissionHelper
{

    public static Boolean askPermission(Activity activity, String permission, int requestCode)
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                {
                    return true;
                }
                else
                {
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
