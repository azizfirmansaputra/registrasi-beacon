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

public class PenggunaFragment extends Fragment {
    private TextView TVNama, TVAlamat, TVNomorTelepon, TVMobilePhone, TVEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_pengguna, container, false);

        TVNama          = view.findViewById(R.id.TVNama);
        TVAlamat        = view.findViewById(R.id.TVAlamat);
        TVNomorTelepon  = view.findViewById(R.id.TVNomorTelepon);
        TVMobilePhone   = view.findViewById(R.id.TVMobilePhone);
        TVEmail         = view.findViewById(R.id.TVEmail);

        setTextDataPengguna();
        return view;
    }

    private void setTextDataPengguna() {
        TVNama.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Nama));
        TVAlamat.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Alamat));
        TVNomorTelepon.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Telp));
        TVMobilePhone.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Seluler));
        TVEmail.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Email));
    }

    @Override
    public void onResume() {
        super.onResume();

        new Handler(Looper.getMainLooper()).postDelayed(this::setTextDataPengguna, 500);
    }

    public PenggunaFragment() {}
}