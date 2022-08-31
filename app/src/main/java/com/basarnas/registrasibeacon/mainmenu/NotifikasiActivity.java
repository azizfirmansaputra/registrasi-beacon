package com.basarnas.registrasibeacon.mainmenu;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.basarnas.registrasibeacon.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class NotifikasiActivity extends AppCompatActivity {
    TabLayout TLNotifikasi;
    ViewPager2 VPNotifikasi;

    ArrayList<String> arrayListTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        TLNotifikasi            = findViewById(R.id.TLNotifikasi);
        VPNotifikasi            = findViewById(R.id.VPNotifikasi);

        arrayListTab            = new ArrayList<>();

        arrayListTab.add(getString(R.string.kadaluarsa_registrasi));
        arrayListTab.add(getString(R.string.kadaluarsa_baterai));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.notifikasi));
        setTabLayout();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabLayout() {
        VPNotifikasi.setAdapter(new TabNotifikasiAdapter(NotifikasiActivity.this, arrayListTab));
        new TabLayoutMediator(TLNotifikasi, VPNotifikasi, (tab, position) -> tab.setText(arrayListTab.get(position))).attach();
    }
}