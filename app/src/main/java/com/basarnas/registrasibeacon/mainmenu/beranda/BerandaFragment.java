package com.basarnas.registrasibeacon.mainmenu.beranda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basarnas.registrasibeacon.MainActivity;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.armada.ArmadaFragment;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BerandaFragment extends Fragment {
    TextView TVDataPenggunaanPersonal;
    RecyclerView RVBeacon;
    LinearLayout LLTidakAdaBeacon;
    FloatingActionButton FABAddBeacon;

    ArrayList<Beacon> arrayListBeacon;
    BeaconAdapter beaconAdapter;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                   = inflater.inflate(R.layout.fragment_beranda, container, false);

        TVDataPenggunaanPersonal    = view.findViewById(R.id.TVDataPenggunaanPersonal);
        RVBeacon                    = view.findViewById(R.id.RVBeacon);
        LLTidakAdaBeacon            = view.findViewById(R.id.LLTidakAdaBeacon);
        FABAddBeacon                = view.findViewById(R.id.FABAddBeacon);

        mainActivity                = (MainActivity)getContext();
        arrayListBeacon             = new ArrayList<>();
        beaconAdapter               = new BeaconAdapter(mainActivity, arrayListBeacon);

        RVBeacon.setHasFixedSize(true);
        RVBeacon.setItemViewCacheSize(20);
        RVBeacon.setAdapter(beaconAdapter);
        RVBeacon.setItemAnimator(new DefaultItemAnimator());
        RVBeacon.setLayoutManager(new LinearLayoutManager(mainActivity));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.beranda));
        mainActivity.getDataBeacon();

        dataPenggunaanPersonal();
        getDataBeacon();
        addDataBeacon();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.getDataBeacon();
        new Handler(Looper.getMainLooper()).postDelayed(this::getDataBeacon, 1000);
    }

    private void dataPenggunaanPersonal() {
        TVDataPenggunaanPersonal.setOnClickListener(view -> {
            mainActivity.loadFragment(new ArmadaFragment());
            mainActivity.setSelectNavigation(R.id.bnvArmada);
        });
    }

    private void getDataBeacon() {
        arrayListBeacon.clear();
        mainActivity.notifikasiCount = 0;

        if(!Preferences.getDataBeacon().isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataBeacon());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object           = jsonArray.getJSONObject(i);
                    String ID                   = object.getString(Configs.Parameter_ID.toUpperCase());
                    String reg                  = object.getString(Configs.Parameter_Reg.toUpperCase());
                    String callSign             = object.getString(Configs.Parameter_Call_Sign.toUpperCase());
                    String noSertifikat         = object.getString(Configs.Parameter_Nomor_Regis.toUpperCase());
                    String IDBeacon             = object.getString(Configs.Parameter_ID_Beacon.toUpperCase());
                    String jenisData            = object.getString(Configs.Parameter_Jenis_Data.toUpperCase());
                    String kategori             = object.getString(Configs.Parameter_Kategori.toUpperCase());
                    String berlakuSampai        = object.getString(Configs.Parameter_Tgl_Kadaluarsa_HU.toUpperCase());
                    String kadaluarsaBaterai    = object.getString(Configs.Parameter_Tgl_Kadaluarsa.toUpperCase());
                    String statusBeacon         = object.getString(Configs.Parameter_Status.toUpperCase());
                    String isVerifikasi         = object.getString(Configs.Parameter_Is_Verifikasi.toUpperCase());
                    String statusUji            = object.getString(Configs.Parameter_Status_Uji.toUpperCase());
                    String formulir             = object.getString(Configs.Parameter_Formulir.toUpperCase());
                    String hasil                = object.getString(Configs.Parameter_Hasil.toUpperCase());

                    arrayListBeacon.add(new Beacon(ID, reg, callSign, noSertifikat, IDBeacon, jenisData, kategori,berlakuSampai, kadaluarsaBaterai, statusBeacon, isVerifikasi, statusUji, formulir, hasil));
                    beaconAdapter.notifyItemChanged(i);

                    LLTidakAdaBeacon.setVisibility(View.GONE);
                    RVBeacon.setVisibility(View.VISIBLE);

                    if(!berlakuSampai.equals("-")){
                        if(new Date().after(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(berlakuSampai.replace("SEPT", "SEP").replace("AGUST", "AGU")))){
                            mainActivity.notifikasiCount ++;
                        }
                    }

                    if(!kadaluarsaBaterai.equals("-")){
                        if(new Date().after(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(kadaluarsaBaterai.replace("SEPT", "SEP").replace("AGUST", "AGU")))){
                            mainActivity.notifikasiCount ++;
                        }
                    }

                    mainActivity.setupBadgeNotifikasi();
                }
            }
            catch(JSONException | ParseException e){
                e.printStackTrace();
            }
        }
    }

    private void addDataBeacon() {
        FABAddBeacon.setOnClickListener(view -> startActivity(new Intent(mainActivity, BeaconActivity.class)));
    }

    public BerandaFragment() {}
}