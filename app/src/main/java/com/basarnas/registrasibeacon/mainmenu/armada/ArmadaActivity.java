package com.basarnas.registrasibeacon.mainmenu.armada;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArmadaActivity extends AppCompatActivity {
    Spinner spinnerPenggunaan, spinnerSpesifik, spinnerTipe;
    TextInputLayout TILPenggunaan, TILSpesifik, TILTipe, TILDataTambahan;
    TextInputEditText TIETPenggunaan, TIETSpesifik, TIETTipe, TIETDataTambahan;
    Button btnSimpan;

    ArrayList<String> listPenggunaan, listSpesifik, listTipe;
    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_armada);

        spinnerPenggunaan   = findViewById(R.id.spinnerPenggunaan);
        spinnerSpesifik     = findViewById(R.id.spinnerSpesifik);
        spinnerTipe         = findViewById(R.id.spinnerTipe);
        TILPenggunaan       = findViewById(R.id.TILPenggunaan);
        TILSpesifik         = findViewById(R.id.TILSpesifik);
        TILTipe             = findViewById(R.id.TILTipe);
        TILDataTambahan     = findViewById(R.id.TILDataTambahan);
        TIETPenggunaan      = findViewById(R.id.TIETPenggunaan);
        TIETSpesifik        = findViewById(R.id.TIETSpesifik);
        TIETTipe            = findViewById(R.id.TIETTipe);
        TIETDataTambahan    = findViewById(R.id.TIETDataTambahan);
        btnSimpan           = findViewById(R.id.btnSimpan);

        listPenggunaan      = new ArrayList<>();
        listSpesifik        = new ArrayList<>();
        listTipe            = new ArrayList<>();
        dialogs             = new Dialogs(ArmadaActivity.this);

        listPenggunaan.add(getString(R.string.n_a));
        listPenggunaan.add(getString(R.string.komersial));
        listPenggunaan.add(getString(R.string.militer));
        listPenggunaan.add(getString(R.string.non_komersial));
        listPenggunaan.add(getString(R.string.pemerintah));
        listPenggunaan.add(getString(R.string.komersial_dan_non_komersial));
        listPenggunaan.add(getString(R.string.privat));
        listPenggunaan.add(getString(R.string.lainnya));

        listSpesifik.add(getString(R.string.berkemah));
        listSpesifik.add(getString(R.string.berburu));
        listSpesifik.add(getString(R.string.memancing));
        listSpesifik.add(getString(R.string.lainnya));

        listTipe.add(getString(R.string.kendaraan_darat));
        listTipe.add(getString(R.string.pesawat));
        listTipe.add(getString(R.string.kapal));
        listTipe.add(getString(R.string.tidak_ada));
        listTipe.add(getString(R.string.lainnya));

        spinnerPenggunaan.setPadding(0, 0, 0, 0);
        spinnerPenggunaan.setAdapter(new ArrayAdapter<>(ArmadaActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listPenggunaan));

        spinnerSpesifik.setPadding(0, 0, 0, 0);
        spinnerSpesifik.setAdapter(new ArrayAdapter<>(ArmadaActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listSpesifik));

        spinnerTipe.setPadding(0, 0, 0, 0);
        spinnerTipe.setAdapter(new ArrayAdapter<>(ArmadaActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listTipe));

        TILPenggunaan.setVisibility(View.GONE);
        TILSpesifik.setVisibility(View.GONE);
        TILTipe.setVisibility(View.GONE);

        btnSimpan.setBackgroundColor(getColor(R.color.background_orange));
        btnSimpan.setEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        checkArmada();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkArmada() {
        if(getIntent().hasExtra(Configs.Parameter_ID)){
            getSupportActionBar().setTitle(getString(R.string.ubah_penggunaan_personal));

            setTextDataArmada(getIntent().getStringExtra(Configs.Parameter_ID));
            saveArmada(getIntent().getStringExtra(Configs.Parameter_ID));
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.penggunaan_personal));
            saveArmada("");
        }

        setOnItemSelectedListener(spinnerPenggunaan);
        setOnItemSelectedListener(spinnerSpesifik);
        setOnItemSelectedListener(spinnerTipe);
        addTextChangedListener(TIETPenggunaan);
        addTextChangedListener(TIETSpesifik);
        addTextChangedListener(TIETTipe);
    }

    private void setTextDataArmada(String ID) {
        try{
            JSONArray jsonArray = new JSONArray(Preferences.getDataArmada());

            String[] penggunaan = {"N/A", "Komersial", "Militer", "Non-Komersial", "Pemerintah", "Komersial dan Non-Komersial", "Private", "Lainnya"};
            String[] spesifik   = {"Berkemah", "Berburu", "Memancing", "Lainnya"};
            String[] tipe       = {"Kendaraan Darat", "Pesawat", "Kapal", "Tidak Ada", "Lainnya"};

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);

                if(object.getString(Configs.Parameter_ID.toUpperCase()).equals(ID)){
                    if(Arrays.toString(penggunaan).contains(object.getString(Configs.Parameter_Nm_Penggunaan.toUpperCase()))){
                        for(int j = 0; j < penggunaan.length; j++){
                            if(penggunaan[j].equals(object.getString(Configs.Parameter_Nm_Penggunaan.toUpperCase()))){
                                spinnerPenggunaan.setSelection(j);
                            }
                        }
                    }
                    else{
                        spinnerPenggunaan.setSelection(penggunaan.length - 1);
                        TIETPenggunaan.setText(object.getString(Configs.Parameter_Nm_Penggunaan.toUpperCase()));
                    }

                    if(Arrays.toString(spesifik).contains(object.getString(Configs.Parameter_Spesifik.toUpperCase()))){
                        for(int j = 0; j < spesifik.length; j++){
                            if(spesifik[j].equals(object.getString(Configs.Parameter_Spesifik.toUpperCase()))){
                                spinnerSpesifik.setSelection(j);
                            }
                        }
                    }
                    else{
                        spinnerSpesifik.setSelection(spesifik.length - 1);
                        TIETSpesifik.setText(object.getString(Configs.Parameter_Spesifik.toUpperCase()));
                    }

                    if(Arrays.toString(tipe).contains(object.getString(Configs.Parameter_Tipe.toUpperCase()))){
                        for(int j = 0; j < tipe.length; j++){
                            if(tipe[j].equals(object.getString(Configs.Parameter_Tipe.toUpperCase()))){
                                spinnerTipe.setSelection(j);
                            }
                        }
                    }
                    else{
                        spinnerTipe.setSelection(tipe.length - 1);
                        TIETTipe.setText(object.getString(Configs.Parameter_Tipe.toUpperCase()));
                    }
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void setOnItemSelectedListener(Spinner spinner) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getId() == R.id.spinnerPenggunaan){
                    if(listPenggunaan.get(i).equals(getString(R.string.lainnya))){
                        TILPenggunaan.setVisibility(View.VISIBLE);
                    }
                    else{
                        TILPenggunaan.setVisibility(View.GONE);
                        TIETPenggunaan.getText().clear();
                    }
                }
                else if(spinner.getId() == R.id.spinnerSpesifik){
                    if(listSpesifik.get(i).equals(getString(R.string.lainnya))){
                        TILSpesifik.setVisibility(View.VISIBLE);
                    }
                    else{
                        TILSpesifik.setVisibility(View.GONE);
                        TIETSpesifik.getText().clear();
                    }
                }
                else if(spinner.getId() == R.id.spinnerTipe){
                    if(listTipe.get(i).equals(getString(R.string.lainnya))){
                        TILTipe.setVisibility(View.VISIBLE);
                    }
                    else{
                        TILTipe.setVisibility(View.GONE);
                        TIETTipe.getText().clear();
                    }
                }
                checkVisibiltyInputEditText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkVisibiltyInputEditText();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkVisibiltyInputEditText() {
        if((TILPenggunaan.getVisibility() == View.VISIBLE && TIETPenggunaan.getText().toString().trim().isEmpty()) ||
                (TILSpesifik.getVisibility() == View.VISIBLE && TIETSpesifik.getText().toString().trim().isEmpty()) ||
                (TILTipe.getVisibility() == View.VISIBLE && TIETTipe.getText().toString().trim().isEmpty())){

            btnSimpan.setBackgroundColor(getColor(R.color.background_orange_disabled));
            btnSimpan.setEnabled(false);
        }
        else{
            btnSimpan.setBackgroundColor(getColor(R.color.background_orange));
            btnSimpan.setEnabled(true);
        }
    }

    private void saveArmada(String ID) {
        btnSimpan.setOnClickListener(view -> {
            dialogs.setMessage(getString(R.string.sedang_menyimpan_data));
            dialogs.setDeterminate(false);
            dialogs.setCancelable(false);
            dialogs.show();

            AndroidNetworking.post(Configs.getAPI(Configs.AddArmada))
                    .addHeaders(Configs.Auth, Preferences.getToken())
                    .addBodyParameter(Configs.Parameter_ID, ID)
                    .addBodyParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                    .addBodyParameter(Configs.Parameter_Penggunaan, String.valueOf(spinnerPenggunaan.getSelectedItemPosition()))
                    .addBodyParameter(Configs.Parameter_Spesifik, String.valueOf(spinnerSpesifik.getSelectedItemPosition() + 1))
                    .addBodyParameter(Configs.Parameter_Tipe, String.valueOf(spinnerTipe.getSelectedItemPosition() + 1))
                    .addBodyParameter(Configs.Parameter_Penggunaan_Lain, TIETPenggunaan.getText().toString())
                    .addBodyParameter(Configs.Parameter_Spesifik_Lain, TIETSpesifik.getText().toString())
                    .addBodyParameter(Configs.Parameter_Tipe_Lain, TIETTipe.getText().toString())
                    .addBodyParameter(Configs.Parameter_Data_Tamb, TIETDataTambahan.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    if(getIntent().hasExtra(Configs.Parameter_ID)){
                                        String namaPenggunaan   = spinnerPenggunaan.getSelectedItem().toString();
                                        String spesifik         = spinnerSpesifik.getSelectedItem().toString();

                                        if(String.valueOf(spinnerPenggunaan.getSelectedItemPosition()).equals(getString(R.string.lainnya))){
                                            namaPenggunaan      = TIETPenggunaan.getText().toString();
                                        }

                                        if(String.valueOf(spinnerSpesifik.getSelectedItemPosition()).equals(getString(R.string.lainnya))){
                                            spesifik            = TIETSpesifik.getText().toString();
                                        }

                                        dialogs.dismiss();
                                        setResult(Activity.RESULT_OK, new Intent().putExtra(Configs.Parameter_Nm_Penggunaan, namaPenggunaan).putExtra(Configs.Parameter_Spesifik, spesifik));
                                        finish();
                                    }
                                    else{
                                        dialogs.dismiss();
                                        finishAfterTransition();
                                    }
                                }
                                else{
                                    dialogsError(getString(R.string.data_gagal_disimpan));
                                }
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            dialogsError(getString(R.string.terjadi_kesalahan));
                        }
                    });
        });
    }

    private void dialogsError(String message) {
        dialogs.setPositiveButton(getString(R.string.ok), view1 -> dialogs.dismiss());
        dialogs.setDeterminate(false);
        dialogs.setMessage(message);
    }
}