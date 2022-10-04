package com.basarnas.registrasibeacon.mainmenu.beranda;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.armada.ArmadaActivity;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Files;
import com.basarnas.registrasibeacon.tools.Permission;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BeaconActivity extends AppCompatActivity {
    Button btnSimpan;
    Spinner spinnerPenggunaanUntuk, spinnerJenisBeacon;
    TextView TVTambahPenggunaanUntuk, TVBrowseFileSuratPermohonan, TVBrowseInformasiRegistrasi;
    TextInputLayout TILIDBeacon, TILPembuat, TILModel, TILNomorPersetujuan, TILTanggalKadaluarsaBaterai, TILNamaKontakDaruratUtama, TILTeleponRumahUtama;
    TextInputLayout TILTeleponKantorUtama, TILSelulerUtama, TILNamaKontakDaruratAlternatif, TILTeleponRumahAlternatif, TILTeleponKantorAlternatif;
    TextInputLayout TILSelulerAlternatif, TILFileSuratPermohonan, TILInformasiRegistrasi, TILTanggal, TILNamaTandaTangan;
    TextInputEditText TIETIDBeacon, TIETPembuat, TIETModel, TIETNomorPersetujuan, TIETTanggalKadaluarsaBaterai, TIETNamaKontakDaruratUtama;
    TextInputEditText TIETTeleponRumahUtama, TIETTeleponKantorUtama, TIETSelulerUtama, TIETNamaKontakDaruratAlternatif, TIETTeleponRumahAlternatif;
    TextInputEditText TIETTeleponKantorAlternatif, TIETSelulerAlternatif, TIETFileSuratPermohonan, TIETInformasiRegistrasi, TIETTanggal, TIETNamaTandaTangan;

    String fileNameFileSuratPermohonan, fileNameInformasiRegistrasi, filePathFileSuratPermohonan, filePathInformasiRegistrasi;
    ActivityResultLauncher<String> selectFileSuratPermohonan, selectInformasiRegistrasi;
    DatePickerDialog DPDTanggalKadaluarsaBaterai, DPDTanggal;
    ArrayList<String> listPenggunaanUntuk, listJenisBeacon;
    ActivityResultLauncher<Intent> addPenggunaanUntuk;
    InputMethodManager inputMethodManager;
    Calendar calendar;
    String IDBaeacon;
    Dialogs dialogs;
    int selectFile;

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        btnSimpan                       = findViewById(R.id.btnSimpan);
        spinnerPenggunaanUntuk          = findViewById(R.id.spinnerPenggunaanUntuk);
        spinnerJenisBeacon              = findViewById(R.id.spinnerJenisBeacon);
        TVTambahPenggunaanUntuk         = findViewById(R.id.TVTambahPenggunaanUntuk);
        TVBrowseFileSuratPermohonan     = findViewById(R.id.TVBrowseFileSuratPermohonan);
        TVBrowseInformasiRegistrasi     = findViewById(R.id.TVBrowseInformasiRegistrasi);
        TILIDBeacon                     = findViewById(R.id.TILIDBeacon);
        TILPembuat                      = findViewById(R.id.TILPembuat);
        TILModel                        = findViewById(R.id.TILModel);
        TILNomorPersetujuan             = findViewById(R.id.TILNomorPersetujuan);
        TILTanggalKadaluarsaBaterai     = findViewById(R.id.TILTanggalKadaluarsaBaterai);
        TILNamaKontakDaruratUtama       = findViewById(R.id.TILNamaKontakDaruratUtama);
        TILTeleponRumahUtama            = findViewById(R.id.TILTeleponRumahUtama);
        TILTeleponKantorUtama           = findViewById(R.id.TILTeleponKantorUtama);
        TILSelulerUtama                 = findViewById(R.id.TILSelulerUtama);
        TILNamaKontakDaruratAlternatif  = findViewById(R.id.TILNamaKontakDaruratAlternatif);
        TILTeleponRumahAlternatif       = findViewById(R.id.TILTeleponRumahAlternatif);
        TILTeleponKantorAlternatif      = findViewById(R.id.TILTeleponKantorAlternatif);
        TILSelulerAlternatif            = findViewById(R.id.TILSelulerAlternatif);
        TILFileSuratPermohonan          = findViewById(R.id.TILFileSuratPermohonan);
        TILInformasiRegistrasi          = findViewById(R.id.TILInformasiRegistrasi);
        TILTanggal                      = findViewById(R.id.TILTanggal);
        TILNamaTandaTangan              = findViewById(R.id.TILNamaTandaTangan);
        TIETIDBeacon                    = findViewById(R.id.TIETIDBeacon);
        TIETPembuat                     = findViewById(R.id.TIETPembuat);
        TIETModel                       = findViewById(R.id.TIETModel);
        TIETNomorPersetujuan            = findViewById(R.id.TIETNomorPersetujuan);
        TIETTanggalKadaluarsaBaterai    = findViewById(R.id.TIETTanggalKadaluarsaBaterai);
        TIETNamaKontakDaruratUtama      = findViewById(R.id.TIETNamaKontakDaruratUtama);
        TIETTeleponRumahUtama           = findViewById(R.id.TIETTeleponRumahUtama);
        TIETTeleponKantorUtama          = findViewById(R.id.TIETTeleponKantorUtama);
        TIETSelulerUtama                = findViewById(R.id.TIETSelulerUtama);
        TIETNamaKontakDaruratAlternatif = findViewById(R.id.TIETNamaKontakDaruratAlternatif);
        TIETTeleponRumahAlternatif      = findViewById(R.id.TIETTeleponRumahAlternatif);
        TIETTeleponKantorAlternatif     = findViewById(R.id.TIETTeleponKantorAlternatif);
        TIETSelulerAlternatif           = findViewById(R.id.TIETSelulerAlternatif);
        TIETFileSuratPermohonan         = findViewById(R.id.TIETFileSuratPermohonan);
        TIETInformasiRegistrasi         = findViewById(R.id.TIETInformasiRegistrasi);
        TIETTanggal                     = findViewById(R.id.TIETTanggal);
        TIETNamaTandaTangan             = findViewById(R.id.TIETNamaTandaTangan);

        listPenggunaanUntuk             = new ArrayList<>();
        listJenisBeacon                 = new ArrayList<>();
        dialogs                         = new Dialogs(BeaconActivity.this);
        DPDTanggalKadaluarsaBaterai     = new DatePickerDialog(BeaconActivity.this);
        DPDTanggal                      = new DatePickerDialog(BeaconActivity.this);
        calendar                        = Calendar.getInstance();
        inputMethodManager              = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        listPenggunaanUntuk.add("-- N/A | Rescue");
        listPenggunaanUntuk.add("-- N/A | -");
        listPenggunaanUntuk.add("-- N/A | Personal");

        spinnerPenggunaanUntuk.setPadding(0, 0, 0, 0);
        spinnerPenggunaanUntuk.setAdapter(new ArrayAdapter<>(BeaconActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listPenggunaanUntuk));

        listJenisBeacon.add(getString(R.string.plb));
        spinnerJenisBeacon.setPadding(0, 0, 0, 0);
        spinnerJenisBeacon.setAdapter(new ArrayAdapter<>(BeaconActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listJenisBeacon));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinnerPenggunaanUntuk();
        addPenggunaanUntuk();
        App.generateToken();
        validationData();
        showDialogDate();
        processFile();
        selectFile();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    private void spinnerPenggunaanUntuk() {
        if(!Preferences.getDataArmada().isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataArmada());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object       = jsonArray.getJSONObject(i);

                    String IDArmada         = object.getString(Configs.Parameter_ID.toUpperCase());
                    String namaPenggunaan   = object.getString(Configs.Parameter_Nm_Penggunaan.toUpperCase());
                    String spesifik         = object.getString(Configs.Parameter_Spesifik.toUpperCase());

                    listPenggunaanUntuk.add(String.format("-- %s | %s", namaPenggunaan, spesifik));

                    if(getIntent().hasExtra(Configs.Parameter_Jns_Armada)){
                        if(getIntent().getStringExtra(Configs.Parameter_Jns_Armada).equals(IDArmada)){
                            spinnerPenggunaanUntuk.setSelection(i + 3);
                        }
                    }
                }
                checkDataBeacon();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        else{
            checkDataBeacon();
        }

        TVTambahPenggunaanUntuk.setOnClickListener(view -> addPenggunaanUntuk.launch(new Intent(BeaconActivity.this, ArmadaActivity.class).putExtra(Configs.Parameter_ID, IDBaeacon)));
    }

    private void addPenggunaanUntuk() {
        addPenggunaanUntuk = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK){
                String namaPenggunaan   = result.getData().getStringExtra(Configs.Parameter_Nm_Penggunaan);
                String spesifik         = result.getData().getStringExtra(Configs.Parameter_Spesifik);

                listPenggunaanUntuk.add(String.format("-- %s | %s", namaPenggunaan, spesifik));
                spinnerPenggunaanUntuk.setSelection(listPenggunaanUntuk.size() - 1);
            }
        });
    }

    private void checkDataBeacon() {
        if(!getIntent().hasExtra(Configs.Parameter_ID)){
            getSupportActionBar().setTitle(getString(R.string.form_registrasi_baru));
            setTextDataBeacon("");
            saveBeacon("");
        }
        else{
            getSupportActionBar().setTitle(getString(R.string.form_perubahan_informasi));
            saveBeacon(getIntent().getStringExtra(Configs.Parameter_ID));
            setTextDataBeacon(getIntent().getStringExtra(Configs.Parameter_ID));
        }
    }

    private void setTextDataBeacon(String ID) {
        TIETNamaKontakDaruratUtama.setText(Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Nama.toUpperCase()));
        TIETTeleponRumahUtama.setText(Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Telp.toUpperCase()));
        TIETTeleponKantorUtama.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_telpPerusahaan));
        TIETSelulerUtama.setText(Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Seluler.toUpperCase()));
        TIETTanggal.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date()));

        if(!ID.isEmpty()){
            try{
                JSONArray jsonArray = new JSONArray(Preferences.getDataBeacon());

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    if(object.getString(Configs.Parameter_ID.toUpperCase()).equals(ID)){
                        IDBaeacon           = object.getString(Configs.Parameter_ID_Beacon.toUpperCase());
                        String kadaluarsa   = object.getString(Configs.Parameter_Tgl_Kadaluarsa.toUpperCase());

                        if(!kadaluarsa.equals("-")){
                            String date     = kadaluarsa.replace("SEPT", "SEP").replace("AGUST", "AGT");
                            Date format     = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(date);
                            String newDate  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(format);

                            TIETTanggalKadaluarsaBaterai.setText(newDate);
                        }
                        TIETIDBeacon.setText(IDBaeacon);
                        setTextDataBeaconByID(getIntent().getStringExtra(Configs.Parameter_ID));
                    }
                }
            }
            catch(JSONException | ParseException e){
                e.printStackTrace();
            }
        }
    }

    private void setTextDataBeaconByID(String ID) {
        AndroidNetworking.get(Configs.getAPI(Configs.BeaconByID))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID, ID)
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                JSONArray jsonArray = new JSONArray(jsonObject.getString(Configs.Parameter_Data));
                                JSONObject object   = jsonArray.getJSONObject(0);

                                for(int i = 0; i < listPenggunaanUntuk.size(); i++){
                                    if(listPenggunaanUntuk.get(i).equals("-- " + object.getString(Configs.Parameter_nmArmada).replace("-", "|"))){
                                        spinnerPenggunaanUntuk.setSelection(i);
                                    }
                                }

                                TIETIDBeacon.setText(object.getString(Configs.Parameter_idBeacon).equals("null") ? "" : object.getString(Configs.Parameter_idBeacon));
                                TIETPembuat.setText(object.getString(Configs.Parameter_Pembuat).equals("null") ? "" : object.getString(Configs.Parameter_Pembuat));
                                TIETModel.setText(object.getString(Configs.Parameter_noModel).equals("null") ? "" : object.getString(Configs.Parameter_noModel));
                                TIETNomorPersetujuan.setText(object.getString(Configs.Parameter_noPersetujuan).equals("null") ? "" : object.getString(Configs.Parameter_noPersetujuan));
                                TIETTanggalKadaluarsaBaterai.setText(object.getString(Configs.Parameter_tglKadarluarsa).equals("null") ? "" : object.getString(Configs.Parameter_tglKadarluarsa));

                                TIETNamaKontakDaruratUtama.setText(object.getString(Configs.Parameter_nmKontak1).equals("null") ? "" : object.getString(Configs.Parameter_nmKontak1));
                                TIETTeleponRumahUtama.setText(object.getString(Configs.Parameter_rmKontak1).equals("null") ? "" : object.getString(Configs.Parameter_rmKontak1));
                                TIETTeleponKantorUtama.setText(object.getString(Configs.Parameter_knKontak1).equals("null") ? "" : object.getString(Configs.Parameter_knKontak1));
                                TIETSelulerUtama.setText(object.getString(Configs.Parameter_sel1).equals("null") ? "" : object.getString(Configs.Parameter_sel1));

                                TIETNamaKontakDaruratAlternatif.setText(object.getString(Configs.Parameter_nmKontak2).equals("null") ? "" : object.getString(Configs.Parameter_nmKontak2));
                                TIETTeleponRumahAlternatif.setText(object.getString(Configs.Parameter_rmKontak2).equals("null") ? "" : object.getString(Configs.Parameter_rmKontak2));
                                TIETTeleponKantorAlternatif.setText(object.getString(Configs.Parameter_knKontak2).equals("null") ? "" : object.getString(Configs.Parameter_knKontak2));
                                TIETSelulerAlternatif.setText(object.getString(Configs.Parameter_sel2).equals("null") ? "" : object.getString(Configs.Parameter_sel2));

                                TIETFileSuratPermohonan.setText(object.getString(Configs.Parameter_fileSrtMohon).equals("null") ? "" : object.getString(Configs.Parameter_fileSrtMohon));
                                TIETInformasiRegistrasi.setText(object.getString(Configs.Parameter_fileSrtIzin).equals("null") ? "" : object.getString(Configs.Parameter_fileSrtIzin));
                                TIETTanggal.setText(object.getString(Configs.Parameter_tglBeacon).equals("null") ? "" : object.getString(Configs.Parameter_tglBeacon));
                                TIETNamaTandaTangan.setText(object.getString(Configs.Parameter_TTD).equals("null") ? "" : object.getString(Configs.Parameter_TTD));
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
    }

    private void validationData() {
        addTextChangedListener(TIETTeleponKantorUtama);
        addTextChangedListener(TIETTeleponRumahUtama);
        addTextChangedListener(TIETSelulerUtama);
        addTextChangedListener(TIETIDBeacon);
        addTextChangedListener(TIETPembuat);
        addTextChangedListener(TIETModel);
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(textInputEditText.getId() == R.id.TIETIDBeacon){
                    if(TIETIDBeacon.getText().length() < 15){
                        TILIDBeacon.setErrorEnabled(true);
                        TILIDBeacon.setError(getString(R.string.id_beacon_tidak_valid));
                    }
                    else{
                        if(!getIntent().hasExtra(Configs.Parameter_ID)){
                            checkIDBeacon();
                        }
                        else{
                            if(!TIETIDBeacon.getText().toString().equalsIgnoreCase(IDBaeacon)){
                                checkIDBeacon();
                            }
                        }
                    }
                }
                else if(textInputEditText.getId() == R.id.TIETTeleponKantorUtama){
                    if(TIETTeleponKantorUtama.getText().length() < 10){
                        TILTeleponKantorUtama.setErrorEnabled(true);
                        TILTeleponKantorUtama.setError(getString(R.string.nomor_telepon_tidak_valid));
                    }
                    else{
                        TILTeleponKantorUtama.setErrorEnabled(false);
                        TILTeleponKantorUtama.setError(null);
                    }
                }
                checkError();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkIDBeacon() {
        AndroidNetworking.get(Configs.getAPI(Configs.CheckIDBeacon))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .addQueryParameter(Configs.Parameter_ID, TIETIDBeacon.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                TILIDBeacon.setErrorEnabled(true);
                                TILIDBeacon.setError(getString(R.string.id_beacon_tidak_valid));
                            }
                            else{
                                TILIDBeacon.setErrorEnabled(false);
                                TILIDBeacon.setError(null);
                            }
                            checkError();
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        TILIDBeacon.setError(getString(R.string.terjadi_kesalahan));
                        TILIDBeacon.setErrorEnabled(true);
                        checkError();
                    }
                });
    }

    private void checkError() {
        if(TIETIDBeacon.getText().toString().trim().isEmpty() || TIETPembuat.getText().toString().trim().isEmpty() ||
                TIETModel.getText().toString().trim().isEmpty() || TIETNamaKontakDaruratUtama.getText().toString().trim().isEmpty() ||
                TIETTeleponKantorUtama.getText().toString().trim().isEmpty() || TIETTanggalKadaluarsaBaterai.getText().toString().equals(getString(R.string.dd_mm_yyyy)) ||
                TIETFileSuratPermohonan.getText().toString().isEmpty() || TIETInformasiRegistrasi.getText().toString().isEmpty()){

            btnSimpan.setBackgroundColor(getColor(R.color.background_orange_disabled));
            btnSimpan.setEnabled(false);
        }
        else{
            if(!TILIDBeacon.isErrorEnabled() && !TILTeleponKantorUtama.isErrorEnabled()){
                btnSimpan.setBackgroundColor(getColor(R.color.background_orange));
                btnSimpan.setEnabled(true);
            }
            else{
                btnSimpan.setBackgroundColor(getColor(R.color.background_orange_disabled));
                btnSimpan.setEnabled(false);
            }
        }
    }

    @SuppressLint("NewApi")
    private void showDialogDate() {
        DPDTanggalKadaluarsaBaterai.setCanceledOnTouchOutside(false);
        DPDTanggalKadaluarsaBaterai.setCancelable(true);
        DPDTanggal.setCanceledOnTouchOutside(false);
        DPDTanggal.setCancelable(true);

        TIETTanggalKadaluarsaBaterai.setOnFocusChangeListener((view, b) -> inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0));
        TIETTanggalKadaluarsaBaterai.setShowSoftInputOnFocus(false);
        TIETTanggalKadaluarsaBaterai.setCursorVisible(false);
        TIETTanggalKadaluarsaBaterai.setMovementMethod(null);

        TIETTanggal.setOnFocusChangeListener((view, b) -> inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0));
        TIETTanggal.setShowSoftInputOnFocus(false);
        TIETTanggal.setCursorVisible(false);
        TIETTanggal.setMovementMethod(null);

        TIETTanggalKadaluarsaBaterai.setOnClickListener(view -> {
            if(!TIETTanggalKadaluarsaBaterai.getText().toString().equals(getString(R.string.dd_mm_yyyy))){
                calendar.set(Calendar.YEAR, Integer.parseInt(TIETTanggalKadaluarsaBaterai.getText().toString().substring(6)));
                calendar.set(Calendar.MONTH, Integer.parseInt(TIETTanggalKadaluarsaBaterai.getText().toString().substring(3, 5)) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(TIETTanggalKadaluarsaBaterai.getText().toString().substring(0, 2)));
            }

            DPDTanggalKadaluarsaBaterai.setOnDateSetListener((datePicker, i, i1, i2) -> {
                calendar.set(i, i1, i2);
                TIETTanggalKadaluarsaBaterai.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
                checkError();
            });

            DPDTanggalKadaluarsaBaterai.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            DPDTanggalKadaluarsaBaterai.show();
        });

        TIETTanggal.setOnClickListener(view -> {
            DPDTanggal.setOnDateSetListener((datePicker, i, i1, i2) -> {
                calendar.set(i, i1, i2);
                TIETTanggal.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime()));
            });

            DPDTanggal.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            DPDTanggal.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            DPDTanggal.show();
        });
    }

    private void selectFile() {
        TVBrowseFileSuratPermohonan.setOnClickListener(view -> {
            selectFile = 1;

            if(Permission.READ_EXTERNAL_STORAGE(BeaconActivity.this)){
                selectFileSuratPermohonan.launch("application/pdf");
            }
        });

        TVBrowseInformasiRegistrasi.setOnClickListener(view -> {
            selectFile = 2;

            if(Permission.READ_EXTERNAL_STORAGE(BeaconActivity.this)){
                selectInformasiRegistrasi.launch("application/pdf");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED && selectFile == 1){
            selectFileSuratPermohonan.launch("application/pdf");
        }
        else if(requestCode == Permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED && selectFile == 2){
            selectInformasiRegistrasi.launch("application/pdf");
        }
    }

    private void processFile() {
        selectFileSuratPermohonan = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null){
                fileNameFileSuratPermohonan = Files.getFileName(BeaconActivity.this, result);
                filePathFileSuratPermohonan = Files.getFilePath(BeaconActivity.this, result);

                TIETFileSuratPermohonan.setText(fileNameFileSuratPermohonan);
                checkError();
            }
        });

        selectInformasiRegistrasi = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null){
                fileNameInformasiRegistrasi = Files.getFileName(BeaconActivity.this, result);
                filePathInformasiRegistrasi = Files.getFilePath(BeaconActivity.this, result);

                TIETInformasiRegistrasi.setText(fileNameInformasiRegistrasi);
                checkError();
            }
        });
    }

    private void saveBeacon(String ID) {
        btnSimpan.setOnClickListener(view -> {
            dialogs.setMessage(getString(R.string.sedang_menyimpan_data));
            dialogs.setDeterminate(false);
            dialogs.setCancelable(false);
            dialogs.show();

            try{
                Date tanggalKadaluarsaBaterai   = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(TIETTanggalKadaluarsaBaterai.getText().toString());
                SimpleDateFormat newFormatDate  = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

                AndroidNetworking.post(Configs.getAPI(Configs.AddBeacon))
                        .addHeaders(Configs.Auth, Preferences.getToken())
                        .addBodyParameter(Configs.Parameter_ID, ID)
                        .addBodyParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                        .addBodyParameter(Configs.Parameter_Jenis_Data, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Jns_Armada.toUpperCase()))
                        .addBodyParameter(Configs.Parameter_Jenis_Penempatan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Jns_Armada.toUpperCase()))
                        .addBodyParameter(Configs.Parameter_Penempatan, String.valueOf(spinnerPenggunaanUntuk.getSelectedItemPosition() + 1))
                        .addBodyParameter(Configs.Parameter_ID_Beacon, TIETIDBeacon.getText().toString())
                        .addBodyParameter(Configs.Parameter_Pembuat, TIETPembuat.getText().toString())
                        .addBodyParameter(Configs.Parameter_No_Model, TIETModel.getText().toString())
                        .addBodyParameter(Configs.Parameter_No_Persetujuan, TIETNomorPersetujuan.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tgl_Kadaluarsa, newFormatDate.format(tanggalKadaluarsaBaterai))
                        .addBodyParameter(Configs.Parameter_NM_Kontak_1, TIETNamaKontakDaruratUtama.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Rumah_1, TIETTeleponRumahUtama.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Kantor_1, TIETTeleponKantorUtama.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Seluler_1, TIETSelulerUtama.getText().toString())
                        .addBodyParameter(Configs.Parameter_NM_Kontak_2, TIETNamaKontakDaruratAlternatif.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Rumah_2, TIETTeleponRumahAlternatif.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Kantor_2, TIETTeleponKantorAlternatif.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tlp_Seluler_2, TIETSelulerAlternatif.getText().toString())
                        .addBodyParameter(Configs.Parameter_Tgl_Beacon, TIETTanggal.getText().toString())
                        .addBodyParameter(Configs.Parameter_TTD, TIETNamaTandaTangan.getText().toString())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject   = new JSONObject(response);
                                    boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                    if(status){
                                        uploadFileSuratPermohonan(ID);
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
            }
            catch(ParseException e){
                e.printStackTrace();
            }
        });
    }

    private void uploadFileSuratPermohonan(String ID) {
        AndroidNetworking.upload(Configs.getAPI(Configs.Upload))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addMultipartParameter(Configs.Parameter_ID, ID)
                .addMultipartParameter(Configs.Parameter_Jenis, Configs.Value_Surat_Permohonan)
                .addMultipartFile(Configs.Parameter_File_Upload, new File(filePathFileSuratPermohonan))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    dialogs.setProgress(Integer.parseInt(String.valueOf((bytesUploaded * 100) / totalBytes)));
                    dialogs.setMessage(getString(R.string.sedang_menunggah_file));
                    dialogs.setDeterminate(true);
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                uploadInformasiRegistrasi(ID);
                            }
                            else{
                                dialogsError(getString(R.string.gagal_mengunggah_berkas));
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialogsError(getString(R.string.gagal_mengunggah_berkas));
                    }
                });
    }

    private void uploadInformasiRegistrasi(String ID) {
        AndroidNetworking.upload(Configs.getAPI(Configs.Upload))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addMultipartParameter(Configs.Parameter_ID, ID)
                .addMultipartParameter(Configs.Parameter_Jenis, Configs.Value_Surat_Izin)
                .addMultipartFile(Configs.Parameter_File_Upload, new File(filePathInformasiRegistrasi))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    dialogs.setProgress(Integer.parseInt(String.valueOf((bytesUploaded * 100) / totalBytes)));
                    dialogs.setMessage(getString(R.string.sedang_menunggah_file));
                    dialogs.setDeterminate(true);
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                dialogs.dismiss();
                                finishAfterTransition();
                            }
                            else{
                                dialogsError(getString(R.string.gagal_mengunggah_berkas));
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialogsError(getString(R.string.gagal_mengunggah_berkas));
                    }
                });
    }

    private void dialogsError(String message) {
        dialogs.setPositiveButton(getString(R.string.ok), view1 -> dialogs.dismiss());
        dialogs.setDeterminate(false);
        dialogs.setMessage(message);
    }
}