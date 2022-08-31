package com.basarnas.registrasibeacon.mainmenu.profil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.basarnas.registrasibeacon.MainActivity;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.SplashActivity;
import com.basarnas.registrasibeacon.mainmenu.profil.tab.ProfilAdapter;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.UbahProfilActivity;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ProfilFragment extends Fragment {
    ImageView IVProfil;
    TextView TVNamaPengguna, TVNomorTelepon, TVAlamatEmail, TVTentang, TVLogout;
    ImageButton IBEdit;
    TabLayout TLProfil;
    ViewPager2 VPProfil;

    ArrayList<String> arrayListTab;
    MainActivity mainActivity;
    Dialogs dialogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view       = inflater.inflate(R.layout.fragment_profil, container, false);

        IVProfil        = view.findViewById(R.id.IVProfil);
        TVNamaPengguna  = view.findViewById(R.id.TVNamaPengguna);
        TVNomorTelepon  = view.findViewById(R.id.TVNomorTelepon);
        TVAlamatEmail   = view.findViewById(R.id.TVAlamatEmail);
        TVTentang       = view.findViewById(R.id.TVTentang);
        TVLogout        = view.findViewById(R.id.TVLogout);
        IBEdit          = view.findViewById(R.id.IBEdit);
        TLProfil        = view.findViewById(R.id.TLProfil);
        VPProfil        = view.findViewById(R.id.VPProfil);

        mainActivity    = (MainActivity)getContext();
        dialogs         = new Dialogs(mainActivity);
        arrayListTab    = new ArrayList<>();

        arrayListTab.add(getString(R.string.pengguna));
        arrayListTab.add(getString(R.string.perusahaan));

        mainActivity.getSupportActionBar().setTitle(getString(R.string.profil));
        App.generateToken();
        setTextDataProfil();
        setTabLayout();
        logoutProfil();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.getDataProfil();
        new Handler(Looper.getMainLooper()).postDelayed(this::setTextDataProfil, 500);
    }

    public void setTextDataProfil() {
        if(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Photo).equals("null")){
            IVProfil.setImageResource(R.drawable.img_profile);
        }
        else{
            Glide.with(mainActivity).asBitmap().load(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Link_Photo)).error(R.drawable.img_profile).into(IVProfil);
        }

        TVNamaPengguna.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Nama));
        TVNomorTelepon.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Seluler));
        TVAlamatEmail.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Email));
        IBEdit.setOnClickListener(view -> startActivity(new Intent(mainActivity, UbahProfilActivity.class)));
        TVTentang.setOnClickListener(view -> startActivity(new Intent(mainActivity, TentangActivity.class)));
    }

    private void setTabLayout() {
        VPProfil.setAdapter(new ProfilAdapter(mainActivity, arrayListTab));
        new TabLayoutMediator(TLProfil, VPProfil, (tab, position) -> tab.setText(arrayListTab.get(position))).attach();
    }

    private void logoutProfil() {
        TVLogout.setOnClickListener(view -> {
            dialogs.setMessage(getString(R.string.apakah_yakin_ingin_keluar_));
            dialogs.setDeterminate(false);
            dialogs.setCancelable(true);
            dialogs.show();

            dialogs.setNegativeButton(getString(R.string.tidak), view1 -> dialogs.dismiss());
            dialogs.setPositiveButton(getString(R.string.ya), view1 -> {
                mainActivity.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().apply();
                startActivity(new Intent(mainActivity, SplashActivity.class));
                dialogs.dismiss();
                App.generateToken();
                mainActivity.finish();
            });
        });
    }

    public ProfilFragment() {}
}