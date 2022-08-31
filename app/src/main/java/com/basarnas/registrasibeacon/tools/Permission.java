package com.basarnas.registrasibeacon.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class Permission {
    public static int READ_EXTERNAL_STORAGE = 0;

    public static boolean READ_EXTERNAL_STORAGE(Activity activity){
        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
            return false;
        }
        else{
            return true;
        }
    }
}