package com.basarnas.registrasibeacon.mainmenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TabNotifikasiAdapter extends FragmentStateAdapter {
    ArrayList<String> arrayListTab;

    public TabNotifikasiAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<String> arrayListTab) {
        super(fragmentActivity);

        this.arrayListTab = arrayListTab;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0 :
                return new KadaluarsaRegistrasiFragment();
            case 1 :
                return new KadaluarsaBateraiFragment();
        }
        return new KadaluarsaRegistrasiFragment();
    }

    @Override
    public int getItemCount() {
        return arrayListTab.size();
    }
}