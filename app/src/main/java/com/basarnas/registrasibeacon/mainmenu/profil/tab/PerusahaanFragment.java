package com.basarnas.registrasibeacon.mainmenu.profil.tab;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;

import java.util.ArrayList;

public class PerusahaanFragment extends Fragment {
    private TextView TVNama, TVJenisPerusahaan, TVArmada, TVAlamat, TVTelp, TVFax, TVEmail;

    private ArrayList<String> listJenisPerusahaan, listArmada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_perusahaan, container, false);

        TVNama              = view.findViewById(R.id.TVNama);
        TVJenisPerusahaan   = view.findViewById(R.id.TVJenisPerusahaan);
        TVArmada            = view.findViewById(R.id.TVArmada);
        TVAlamat            = view.findViewById(R.id.TVAlamat);
        TVTelp              = view.findViewById(R.id.TVTelp);
        TVFax               = view.findViewById(R.id.TVFax);
        TVEmail             = view.findViewById(R.id.TVEmail);

        listJenisPerusahaan = new ArrayList<>();
        listArmada          = new ArrayList<>();

        listJenisPerusahaan.add(getString(R.string.perorangan));
        listJenisPerusahaan.add(getString(R.string.perusahaan));

        listArmada.add(getString(R.string.pesawat));
        listArmada.add(getString(R.string.kapal));
        listArmada.add(getString(R.string.personal));
        listArmada.add(getString(R.string.semua));

        setTextDataPerusahaan();
        return view;
    }

    private void setTextDataPerusahaan() {
        TVNama.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_nmPerusahaan));
        TVAlamat.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_almtPerusahaan));
        TVTelp.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_telpPerusahaan));
        TVFax.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_faxPerusahaan));
        TVEmail.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_emailPerusahaan));

        TVJenisPerusahaan.setText(listJenisPerusahaan.get(Integer.parseInt(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_jnsPerusahaan)) - 1));
        TVArmada.setText(listArmada.get(Integer.parseInt(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_jnsPenempatan)) - 1));
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler(Looper.getMainLooper()).postDelayed(this::setTextDataPerusahaan, 500);
    }

    public PerusahaanFragment() {}
}