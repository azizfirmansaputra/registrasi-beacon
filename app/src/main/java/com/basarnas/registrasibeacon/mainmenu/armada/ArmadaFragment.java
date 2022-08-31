package com.basarnas.registrasibeacon.mainmenu.armada;

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
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArmadaFragment extends Fragment {
    TextView TVDaftarPenggunaanPersonal;
    RecyclerView RVArmada;
    LinearLayout LLTidakAdaArmada;
    FloatingActionButton FABAddArmada;

    ArrayList<Armada> arrayListArmada;
    ArmadaAdapter armadaAdapter;
    MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view                   = inflater.inflate(R.layout.fragment_armada, container, false);

        TVDaftarPenggunaanPersonal  = view.findViewById(R.id.TVDaftarPenggunaanPersonal);
        RVArmada                    = view.findViewById(R.id.RVArmada);
        LLTidakAdaArmada            = view.findViewById(R.id.LLTidakAdaArmada);
        FABAddArmada                = view.findViewById(R.id.FABAddArmada);

        mainActivity                = (MainActivity)getContext();
        arrayListArmada             = new ArrayList<>();
        armadaAdapter               = new ArmadaAdapter(mainActivity, arrayListArmada);

        RVArmada.setHasFixedSize(true);
        RVArmada.setItemViewCacheSize(20);
        RVArmada.setAdapter(armadaAdapter);
        RVArmada.setItemAnimator(new DefaultItemAnimator());
        RVArmada.setLayoutManager(new LinearLayoutManager(mainActivity));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.armada));
        mainActivity.getDataArmada();

        App.generateToken();
        getDataArmada();
        addDataArmada();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.getDataArmada();
        new Handler(Looper.getMainLooper()).postDelayed(this::getDataArmada, 1000);
    }

    private void getDataArmada() {
        arrayListArmada.clear();

        if(!Preferences.getDataArmada().isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataArmada());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object       = jsonArray.getJSONObject(i);
                    String ID               = object.getString(Configs.Parameter_ID.toUpperCase());
                    String namaPenggunaan   = object.getString(Configs.Parameter_Nm_Penggunaan.toUpperCase());
                    String spesifik         = object.getString(Configs.Parameter_Spesifik.toUpperCase());
                    String tipe             = object.getString(Configs.Parameter_Tipe.toUpperCase());

                    arrayListArmada.add(new Armada(ID, namaPenggunaan, spesifik, tipe));
                    armadaAdapter.notifyItemChanged(i);

                    TVDaftarPenggunaanPersonal.setVisibility(View.VISIBLE);
                    LLTidakAdaArmada.setVisibility(View.GONE);
                    RVArmada.setVisibility(View.VISIBLE);
                }
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void addDataArmada() {
        FABAddArmada.setOnClickListener(view -> startActivity(new Intent(mainActivity, ArmadaActivity.class)));
    }

    public ArmadaFragment() {}
}