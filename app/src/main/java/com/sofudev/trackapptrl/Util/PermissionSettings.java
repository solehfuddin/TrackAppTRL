package com.sofudev.trackapptrl.Util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Fuddins on 03/03/2018.
 */

public class PermissionSettings {

    public void checkPermission(Activity activity, String permission, Integer code)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
            {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
            }
        }
        else
        {
            Log.v("Info", "Permisson already granted");
        }
    }
}
