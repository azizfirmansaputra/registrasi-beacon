package com.basarnas.registrasibeacon.tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.basarnas.registrasibeacon.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Preferences {
    private static SharedPreferences SP;

    public static String Key_Token          = "Token";
    public static String Key_Login          = "isLogin";
    public static String Key_DataLogin      = "DataLogin";
    public static String Key_DataBeacon     = "DataBeacon";
    public static String Key_DataArmada     = "DataArmada";
    public static String Key_DataUjiFungsi  = "DataUjiFungsi";
    public static String Key_DataSertifikat = "DataSertifikat";
    public static String Key_DataProfil     = "DataProfil";

    public static void setSP(Context context){
        SP = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static void setToken(String token){
        SP.edit().putString(Key_Token, token).apply();
    }

    public static String getToken(){
        return "Bearer " + SP.getString(Key_Token, "");
    }

    public static void setLogin(boolean isLogin){
        SP.edit().putBoolean(Key_Login, isLogin).apply();
    }

    public static boolean isLogin(){
        return SP.getBoolean(Key_Login, false);
    }

    public static void setDataLogin(String dataLogin){
        SP.edit().putString(Key_DataLogin, dataLogin).apply();
    }

    public static String getData(String key, String parameter){
        JSONObject object;
        String data = "";

        try{
            object  = new JSONObject(SP.getString(key, ""));
            data    = object.getString(parameter);
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        return data;
    }

    public static void setDataBeacon(String dataBeacon){
        SP.edit().putString(Key_DataBeacon, dataBeacon).apply();
    }

    public static String getDataBeacon(){
        return SP.getString(Key_DataBeacon, "");
    }

    public static void setDataArmada(String dataArmada){
        SP.edit().putString(Key_DataArmada, dataArmada).apply();
    }

    public static String getDataArmada(){
        return SP.getString(Key_DataArmada, "");
    }

    public static void setDataUjiFungsi(String dataUjiFungsi){
        SP.edit().putString(Key_DataUjiFungsi, dataUjiFungsi).apply();
    }

    public static String getDataUjiFungsi(){
        return SP.getString(Key_DataUjiFungsi, "");
    }

    public static void setDataSertifikat(String dataSertifikat){
        SP.edit().putString(Key_DataSertifikat, dataSertifikat).apply();
    }

    public static String getDataSertifikat(){
        return SP.getString(Key_DataSertifikat, "");
    }

    public static void setDataProfil(String dataProfil){
        SP.edit().putString(Key_DataProfil, dataProfil).apply();
    }
}