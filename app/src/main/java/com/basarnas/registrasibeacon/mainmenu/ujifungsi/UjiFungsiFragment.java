package com.basarnas.registrasibeacon.mainmenu.ujifungsi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    RecyclerView RVUjiFungsi;
    LinearLayout LLTidakAdaUjiFungsi;
    FloatingActionButton FABAjukanUjiFungsi;

    ArrayList<UjiFungsi> arrayListUjiFungsi;
    UjiFungsiAdapter ujiFungsiAdapter;
    MainActivity mainActivity;
    Dialogs dialogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_uji_fungsi, container, false);

        TVDaftarUjiFungsi   = view.findViewById(R.id.TVDaftarUjiFungsi);
        RVUjiFungsi         = view.findViewById(R.id.RVUjiFungsi);
        LLTidakAdaUjiFungsi = view.findViewById(R.id.LLTidakAdaUjiFungsi);
        FABAjukanUjiFungsi  = view.findViewById(R.id.FABAjukanUjiFungsi);

        mainActivity        = (MainActivity)getContext();
        arrayListUjiFungsi  = new ArrayList<>();
        dialogs             = new Dialogs(mainActivity);
        ujiFungsiAdapter    = new UjiFungsiAdapter(mainActivity, arrayListUjiFungsi);

        RVUjiFungsi.setHasFixedSize(true);
        RVUjiFungsi.setItemViewCacheSize(20);
        RVUjiFungsi.setAdapter(ujiFungsiAdapter);
        RVUjiFungsi.setItemAnimator(new DefaultItemAnimator());
        RVUjiFungsi.setLayoutManager(new LinearLayoutManager(mainActivity));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.uji_fungsi));
        mainActivity.getDataUjiFungsi();

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
                    RVUjiFungsi.setVisibility(View.VISIBLE);
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void ajukanUjiFungsi() {
        FABAjukanUjiFungsi.setOnClickListener(view -> {
            dialogs.setPositiveButton(getString(R.string.ok), view1 -> dialogs.dismiss());
            dialogs.setDeterminate(false);

            AndroidNetworking.post(Configs.getAPI(Configs.AjukanUjiFungsi))
                    .addHeaders(Configs.Auth, Preferences.getToken())
                    .addBodyParameter(Configs.Parameter_ID, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID.toUpperCase()))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    dialogs.setMessage(getString(R.string.uji_fungsi_berhasil_diajukan));
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

                        @Override
                        public void onError(ANError anError) {
                            dialogs.setMessage(getString(R.string.terjadi_kesalahan));
                            dialogs.show();
                        }
                    });
        });
    }

    public UjiFungsiFragment() {}
}