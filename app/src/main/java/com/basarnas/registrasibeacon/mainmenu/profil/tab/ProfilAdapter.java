package com.basarnas.registrasibeacon.mainmenu.profil.tab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ProfilAdapter extends FragmentStateAdapter {
    ArrayList<String> arrayListTab;

    public ProfilAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<String> arrayListTab) {
        super(fragmentActivity);

        this.arrayListTab = arrayListTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0 :
                return new PenggunaFragment();
            case 1 :
                return new PerusahaanFragment();
        }
        return new PenggunaFragment();
    }

    @Override
    public int getItemCount() {
        return arrayListTab.size();
    }
}