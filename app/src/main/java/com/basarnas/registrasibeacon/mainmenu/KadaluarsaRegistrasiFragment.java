package com.basarnas.registrasibeacon.mainmenu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basarnas.registrasibeacon.MainActivity;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.beranda.Beacon;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class KadaluarsaRegistrasiFragment extends Fragment {
    RecyclerView RVNotifikasi;
    LinearLayout LLTidakAdaNotifikasi;

    NotifikasiActivity notifikasiActivity;
    NotifikasiAdapter notifikasiAdapter;
    ArrayList<Beacon> arrayListBeacon;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.fragment_kadaluarsa_registrasi, container, false);

        RVNotifikasi            = view.findViewById(R.id.RVNotifikasi);
        LLTidakAdaNotifikasi    = view.findViewById(R.id.LLTidakAdaNotifikasi);

        notifikasiActivity      = (NotifikasiActivity)getContext();
        arrayListBeacon         = new ArrayList<>();
        notifikasiAdapter       = new NotifikasiAdapter(notifikasiActivity, arrayListBeacon, getString(R.string.kadaluarsa_registrasi));
        mainActivity            = new MainActivity();

        RVNotifikasi.setHasFixedSize(true);
        RVNotifikasi.setItemViewCacheSize(20);
        RVNotifikasi.setAdapter(notifikasiAdapter);
        RVNotifikasi.setItemAnimator(new DefaultItemAnimator());
        RVNotifikasi.setLayoutManager(new LinearLayoutManager(notifikasiActivity));

        getNotifikasiRegistrasi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.getDataBeacon();
        new Handler(Looper.getMainLooper()).postDelayed(this::getNotifikasiRegistrasi, 1000);
    }

    private void getNotifikasiRegistrasi() {
        arrayListBeacon.clear();

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

                    if(!berlakuSampai.equals("-")){
                        if(new Date().after(new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(berlakuSampai.replace("SEPT", "SEP").replace("AGUST", "AGT")))){
                            arrayListBeacon.add(new Beacon(ID, reg, callSign, noSertifikat, IDBeacon, jenisData, kategori,berlakuSampai, kadaluarsaBaterai, statusBeacon, isVerifikasi, statusUji, formulir, hasil));
                            notifikasiAdapter.notifyItemChanged(i);

                            LLTidakAdaNotifikasi.setVisibility(View.GONE);
                            RVNotifikasi.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            catch(JSONException | ParseException e){
                e.printStackTrace();
            }
        }
    }

    public KadaluarsaRegistrasiFragment() {}
}