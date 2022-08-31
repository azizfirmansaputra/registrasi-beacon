package com.basarnas.registrasibeacon.mainmenu.sertifikat;

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

import com.basarnas.registrasibeacon.MainActivity;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SertifikatFragment extends Fragment {
    TextView TVDaftarSertifikat;
    RecyclerView RVSertifikat;
    LinearLayout LLTidakAdaSertifikat;

    ArrayList<Sertifikat> arrayListSertifikat;
    SertifikatAdapter sertifikatAdapter;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.fragment_sertifikat, container, false);

        TVDaftarSertifikat      = view.findViewById(R.id.TVDaftarSertifikat);
        RVSertifikat            = view.findViewById(R.id.RVSertifikat);
        LLTidakAdaSertifikat    = view.findViewById(R.id.LLTidakAdaSertifikat);

        mainActivity            = (MainActivity)getContext();
        arrayListSertifikat     = new ArrayList<>();
        sertifikatAdapter       = new SertifikatAdapter(mainActivity, arrayListSertifikat);

        RVSertifikat.setHasFixedSize(true);
        RVSertifikat.setItemViewCacheSize(20);
        RVSertifikat.setAdapter(sertifikatAdapter);
        RVSertifikat.setItemAnimator(new DefaultItemAnimator());
        RVSertifikat.setLayoutManager(new LinearLayoutManager(mainActivity));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.sertifikat));
        mainActivity.getDataSertifikat();

        App.generateToken();
        getDataSertifikat();
        return view;
    }

    private void getDataSertifikat() {
        if(!Preferences.getDataSertifikat().isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataSertifikat());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object           = jsonArray.getJSONObject(i);
                    String IDBeacon             = object.getString(Configs.Parameter_ID_Beacon.toUpperCase());
                    String callSign             = object.getString(Configs.Parameter_Call_Sign.toUpperCase());
                    String tanggalBeacon        = object.getString(Configs.Parameter_Tgl_Beacon.toUpperCase());
                    String tanggalKadaluarsa    = object.getString(Configs.Parameter_Tgl_Kadaluarsa_HU.toUpperCase());
                    String keterangan_Status    = object.getString(Configs.Parameter_Keterangan_Status.toUpperCase());

                    arrayListSertifikat.add(new Sertifikat(IDBeacon, callSign, tanggalBeacon, tanggalKadaluarsa, keterangan_Status));
                    sertifikatAdapter.notifyItemChanged(i);

                    TVDaftarSertifikat.setVisibility(View.VISIBLE);
                    LLTidakAdaSertifikat.setVisibility(View.GONE);
                    RVSertifikat.setVisibility(View.VISIBLE);
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public SertifikatFragment() {}
}