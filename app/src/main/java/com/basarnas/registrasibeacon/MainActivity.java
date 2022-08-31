package com.basarnas.registrasibeacon;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.mainmenu.armada.ArmadaFragment;
import com.basarnas.registrasibeacon.mainmenu.beranda.BerandaFragment;
import com.basarnas.registrasibeacon.mainmenu.NotifikasiActivity;
import com.basarnas.registrasibeacon.mainmenu.profil.ProfilFragment;
import com.basarnas.registrasibeacon.mainmenu.sertifikat.SertifikatFragment;
import com.basarnas.registrasibeacon.mainmenu.ujifungsi.UjiFungsiFragment;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    BottomNavigationView BNV;
    TextView TVBadgeNotifikasi;

    public int notifikasiCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BNV = findViewById(R.id.BNV);

        BNV.setOnItemSelectedListener(MainActivity.this);
        loadFragment(new BerandaFragment());

        App.generateToken();
        getDataSertifikat();
        getDataUjiFungsi();
        getDataProfil();
        getDataArmada();
        getDataBeacon();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem menuItem   = menu.findItem(R.id.menuNotifikasi);
        View actionView     = menuItem.getActionView();
        TVBadgeNotifikasi   = actionView.findViewById(R.id.TVBadge);

        setupBadgeNotifikasi();
        actionView.setOnClickListener(view -> onOptionsItemSelected(menuItem));

        return super.onCreateOptionsMenu(menu);
    }

    public void setupBadgeNotifikasi() {
        if(TVBadgeNotifikasi != null){
            if(notifikasiCount == 0){
                TVBadgeNotifikasi.setVisibility(View.GONE);
            }
            else{
                if(notifikasiCount <= 9){
                    TVBadgeNotifikasi.setText(String.valueOf(notifikasiCount));
                }
                else{
                    TVBadgeNotifikasi.setText(R.string._9_plus);
                }
                TVBadgeNotifikasi.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuNotifikasi){
            startActivity(new Intent(MainActivity.this, NotifikasiActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if(item.getItemId() == R.id.bnvBeranda){
            fragment = new BerandaFragment();
        }
        else if(item.getItemId() == R.id.bnvArmada){
            fragment = new ArmadaFragment();
        }
        else if(item.getItemId() == R.id.bnvUjiFungsi){
            fragment = new UjiFungsiFragment();
        }
        else if(item.getItemId() == R.id.bnvSertifikat){
            fragment = new SertifikatFragment();
        }
        else if(item.getItemId() == R.id.bnvProfil){
            fragment = new ProfilFragment();
        }
        return loadFragment(fragment);
    }

    public boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.FL, fragment).commit();
            return true;
        }
        return false;
    }

    public void setSelectNavigation(int menuItem) {
        BNV.getMenu().findItem(menuItem).setChecked(true);
    }

    public void getDataBeacon() {
        AndroidNetworking.get(Configs.getAPI(Configs.ListBeacon))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .addQueryParameter(Configs.Parameter_Jenis_Data, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Jns_Armada.toUpperCase()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(Configs.Parameter_Data));

                                Preferences.setDataBeacon(jsonArray.toString());
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

    public void getDataArmada() {
        AndroidNetworking.get(Configs.getAPI(Configs.ListArmada))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(Configs.Parameter_Data));

                                Preferences.setDataArmada(jsonArray.toString());
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

    public void getDataUjiFungsi() {
        AndroidNetworking.get(Configs.getAPI(Configs.UjiFungsi))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(Configs.Parameter_Data));

                                Preferences.setDataUjiFungsi(jsonArray.toString());
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

    public void getDataSertifikat() {
        AndroidNetworking.get(Configs.getAPI(Configs.Sertifikat))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(Configs.Parameter_Data));

                                Preferences.setDataSertifikat(jsonArray.toString());
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

    public void getDataProfil() {
        AndroidNetworking.get(Configs.getAPI(Configs.Register))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID.toUpperCase()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                String message      = jsonObject.getString(Configs.Parameter_Message);
                                JSONArray jsonArray = new JSONArray(message);

                                Preferences.setDataProfil(jsonArray.getString(0));
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