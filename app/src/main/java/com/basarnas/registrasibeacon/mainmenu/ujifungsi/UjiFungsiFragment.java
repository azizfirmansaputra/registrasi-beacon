package com.basarnas.registrasibeacon.mainmenu.ujifungsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.MainActivity;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UjiFungsiFragment extends Fragment {
    TextView TVDaftarUjiFungsi;
    CardView CVUjiFungsi;
    CheckBox CBUjiFungsi;
    RecyclerView RVUjiFungsi;
    LinearLayout LLTidakAdaUjiFungsi;
    FloatingActionButton FABAjukanUjiFungsi;

    ArrayList<UjiFungsi> arrayListUjiFungsi;
    ArrayList<String> arrayListIDUjiFungsi;
    UjiFungsiAdapter ujiFungsiAdapter;
    MainActivity mainActivity;
    Dialogs dialogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.fragment_uji_fungsi, container, false);

        TVDaftarUjiFungsi       = view.findViewById(R.id.TVDaftarUjiFungsi);
        CVUjiFungsi             = view.findViewById(R.id.CVUjiFungsi);
        CBUjiFungsi             = view.findViewById(R.id.CBUjiFungsi);
        RVUjiFungsi             = view.findViewById(R.id.RVUjiFungsi);
        LLTidakAdaUjiFungsi     = view.findViewById(R.id.LLTidakAdaUjiFungsi);
        FABAjukanUjiFungsi      = view.findViewById(R.id.FABAjukanUjiFungsi);

        mainActivity            = (MainActivity)getContext();
        arrayListUjiFungsi      = new ArrayList<>();
        arrayListIDUjiFungsi    = new ArrayList<>();
        dialogs                 = new Dialogs(mainActivity);
        ujiFungsiAdapter        = new UjiFungsiAdapter(mainActivity, arrayListUjiFungsi, false, this);

        RVUjiFungsi.setHasFixedSize(true);
        RVUjiFungsi.setItemViewCacheSize(20);
        RVUjiFungsi.setAdapter(ujiFungsiAdapter);
        RVUjiFungsi.setItemAnimator(new DefaultItemAnimator());
        RVUjiFungsi.setLayoutManager(new LinearLayoutManager(mainActivity));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.uji_fungsi));
        mainActivity.getDataUjiFungsi();

        selectAllUjiFungsi();
        App.generateToken();
        getDataUjiFungsi();
        ajukanUjiFungsi();
        return view;
    }

    private void getDataUjiFungsi() {
        if(!Preferences.getDataUjiFungsi().isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataUjiFungsi());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object           = jsonArray.getJSONObject(i);
                    String ID                   = object.getString(Configs.Parameter_ID.toUpperCase());
                    String IDBeacon             = object.getString(Configs.Parameter_ID_Beacon.toUpperCase());
                    String statusUji            = object.getString(Configs.Parameter_Status_Uji.toUpperCase());
                    String callSign             = object.getString(Configs.Parameter_Call_Sign.toUpperCase());
                    String tanggalBeacon        = object.getString(Configs.Parameter_Tgl_Beacon.toUpperCase());

                    arrayListUjiFungsi.add(new UjiFungsi(ID, IDBeacon, statusUji, callSign, tanggalBeacon));
                    ujiFungsiAdapter.notifyItemChanged(i);

                    TVDaftarUjiFungsi.setVisibility(View.VISIBLE);
                    LLTidakAdaUjiFungsi.setVisibility(View.GONE);
                    CVUjiFungsi.setVisibility(View.VISIBLE);
                    RVUjiFungsi.setVisibility(View.VISIBLE);
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void selectAllUjiFungsi() {
        CBUjiFungsi.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                arrayListUjiFungsi.clear();
                arrayListIDUjiFungsi.clear();
                RVUjiFungsi.setAdapter(new UjiFungsiAdapter(mainActivity, arrayListUjiFungsi, true, this));
                getDataUjiFungsi();
            }
            else{
                arrayListUjiFungsi.clear();
                arrayListIDUjiFungsi.clear();
                RVUjiFungsi.setAdapter(new UjiFungsiAdapter(mainActivity, arrayListUjiFungsi, false, this));
                getDataUjiFungsi();
            }
        });
    }

    public void setSelect(boolean select) {
        CBUjiFungsi.setChecked(select);
    }

    private void ajukanUjiFungsi() {
        FABAjukanUjiFungsi.setOnClickListener(view -> {
            if(arrayListIDUjiFungsi.size() == 0){
                dialogs.setPositiveButton(getString(R.string.ok), view1 -> dialogs.dismiss());
                dialogs.setMessage(getString(R.string.pilih_data_yang_akan_diuji));
                dialogs.setCancelable(false);
                dialogs.show();
            }
            else{
                dialogs.setPositiveButton(getString(R.string.ok), view1 -> dialogs.dismiss());
                dialogs.setDeterminate(false);

                for(int i = 0; i < arrayListIDUjiFungsi.size(); i++){
                    processAjukanUjiFungsi(arrayListIDUjiFungsi.get(i), i);
                }
            }
        });
    }

    private void processAjukanUjiFungsi(String ID, int i) {
        AndroidNetworking.post(Configs.getAPI(Configs.AjukanUjiFungsi))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addBodyParameter(Configs.Parameter_ID, ID)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        if(i == arrayListIDUjiFungsi.size() - 1){
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    dialogs.setMessage(getString(R.string.uji_fungsi_berhasil_diajukan));
                                    mainActivity.getDataUjiFungsi();

                                    dialogs.setPositiveButton(getString(R.string.ok), view1 -> {
                                        dialogs.dismiss();
                                        getDataUjiFungsi();
                                    });
                                }
                                else{
                                    dialogs.setMessage(getString(R.string.uji_fungsi_gagal_di_ajukan));
                                }
                                dialogs.show();
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialogs.setMessage(getString(R.string.terjadi_kesalahan));
                        dialogs.show();
                    }
                });
    }

    public UjiFungsiFragment() {}
}