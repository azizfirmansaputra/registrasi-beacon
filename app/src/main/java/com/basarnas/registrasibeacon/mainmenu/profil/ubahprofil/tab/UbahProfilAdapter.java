package com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class UbahProfilAdapter extends FragmentStateAdapter {
    ArrayList<String> arrayListTab;

    public UbahProfilAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<String> arrayListTab) {
        super(fragmentActivity);

        this.arrayListTab = arrayListTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0 :
                return new UbahPenggunaFragment();
            case 1 :
                return new UbahPerusahaanFragment();
        }
        return new UbahPenggunaFragment();
    }

    @Override
    public int getItemCount() {
        return arrayListTab.size();
    }
}