package com.basarnas.registrasibeacon.tools;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidNetworking.initialize(getApplicationContext());
        Preferences.setSP(getApplicationContext());
        generateToken();
    }

    public static void generateToken() {
        AndroidNetworking.post(Configs.getAPI(Configs.Token))
                .addBodyParameter(Configs.Parameter_Username, Configs.Value_Username)
                .addBodyParameter(Configs.Parameter_Password, Configs.Value_Password)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                Preferences.setToken(jsonObject.getString(Configs.Parameter_Token));
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {}
                });
    }
}