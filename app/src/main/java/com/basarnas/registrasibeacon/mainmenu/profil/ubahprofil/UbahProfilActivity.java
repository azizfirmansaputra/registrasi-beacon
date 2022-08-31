package com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab.UbahPenggunaFragment;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab.UbahPerusahaanFragment;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab.UbahProfilAdapter;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Files;
import com.basarnas.registrasibeacon.tools.Permission;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class UbahProfilActivity extends AppCompatActivity {
    ImageView IVFotoProfil;
    TextView TVFotoProfil;
    TabLayout TLUbahProfil;
    TabItem TIPengguna, TIPerusahaan;
    ViewPager2 VPUbahProfil;
    Button btnSimpan;

    ActivityResultLauncher<String> selectFoto;
    ArrayList<String> arrayListTab;
    String fotoPath;
    Dialogs dialogs;

    public boolean pengguna, perusahaan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_profil);

        IVFotoProfil    = findViewById(R.id.IVFotoProfil);
        TVFotoProfil    = findViewById(R.id.TVFotoProfil);
        TLUbahProfil    = findViewById(R.id.TLUbahProfil);
        TIPengguna      = findViewById(R.id.TIPengguna);
        TIPerusahaan    = findViewById(R.id.TIPerusahaan);
        VPUbahProfil    = findViewById(R.id.VPUbahProfil);
        btnSimpan       = findViewById(R.id.btnSimpan);

        arrayListTab    = new ArrayList<>();
        dialogs         = new Dialogs(UbahProfilActivity.this);

        pengguna        = true;
        perusahaan      = true;

        arrayListTab.add(getString(R.string.pengguna));
        arrayListTab.add(getString(R.string.perusahaan));

        btnSimpan.setBackgroundColor(getColor(R.color.background_orange));
        btnSimpan.setEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.ubah_profil));
        processSelectFotoProfil();
        App.generateToken();
        saveUbahProfil();
        setFotoProfil();
        setTabLayout();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    private void processSelectFotoProfil() {
        selectFoto = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if(result != null){
                fotoPath = Files.getFilePath(UbahProfilActivity.this, result);
                uploadFotoProfil();
            }
        });
    }

    private void uploadFotoProfil() {
        dialogs.setCancelable(false);
        dialogs.setDeterminate(true);
        dialogs.show();

        AndroidNetworking.upload(Configs.getAPI(Configs.Upload))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addMultipartParameter(Configs.Parameter_ID, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                .addMultipartParameter(Configs.Parameter_Jenis, Configs.Value_File_Photo)
                .addMultipartFile(Configs.Parameter_File_Upload, new File(fotoPath))
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                    dialogs.setProgress(Integer.parseInt(String.valueOf((bytesUploaded * 100) / totalBytes)));
                    dialogs.setMessage(getString(R.string.sedang_mengunggah_foto));
                })
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                Glide.with(UbahProfilActivity.this).load(fotoPath).into(IVFotoProfil);
                                dialogs.dismiss();
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

    public void setFotoProfil() {
        if(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Photo).equals("null")){
            IVFotoProfil.setImageResource(R.drawable.img_profile);
            TVFotoProfil.setText(getString(R.string.tambah_foto));
        }
        else{
            Glide.with(UbahProfilActivity.this).asBitmap().load(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Link_Photo)).error(R.drawable.img_profile).into(IVFotoProfil);
            TVFotoProfil.setVisibility(View.INVISIBLE);
        }
        selectFotoProfil();
    }

    private void selectFotoProfil() {
        IVFotoProfil.setOnClickListener(view -> {
            if(Permission.READ_EXTERNAL_STORAGE(UbahProfilActivity.this)){
                selectFoto.launch("image/*");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectFoto.launch("image/*");
        }
    }

    private void setTabLayout() {
        VPUbahProfil.setAdapter(new UbahProfilAdapter(UbahProfilActivity.this, arrayListTab));
        new TabLayoutMediator(TLUbahProfil, VPUbahProfil, (tab, position) -> tab.setText(arrayListTab.get(position))).attach();
    }

    private void saveUbahProfil() {
        btnSimpan.setOnClickListener(view -> {
            if(UbahPenggunaFragment.TIETNama != null && UbahPenggunaFragment.TIETAlamat != null && UbahPenggunaFragment.TIETNomorTelepon != null &&
                    UbahPenggunaFragment.TIETMobilePhone != null && UbahPenggunaFragment.TIETEmail != null && UbahPerusahaanFragment.TIETNama != null &&
                    UbahPerusahaanFragment.TIETAlamat != null && UbahPerusahaanFragment.TIETTelp != null && UbahPerusahaanFragment.TIETFax != null &&
                    UbahPerusahaanFragment.TIETEmail != null && UbahPerusahaanFragment.spinnerJenisPerusahaan != null && UbahPerusahaanFragment.spinnerArmada != null){

                dialogs.setMessage(getString(R.string.sedang_menyimpan_data));
                dialogs.setDeterminate(false);
                dialogs.setCancelable(false);
                dialogs.show();

                AndroidNetworking.put(Configs.getAPI(Configs.UpdateUser))
                        .addHeaders(Configs.Auth, Preferences.getToken())
                        .addBodyParameter(Configs.Parameter_ID, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID.toUpperCase()))
                        .addBodyParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                        .addBodyParameter(Configs.Parameter_Nama, UbahPenggunaFragment.TIETNama.getText().toString())
                        .addBodyParameter(Configs.Parameter_Alamat, UbahPenggunaFragment.TIETAlamat.getText().toString())
                        .addBodyParameter(Configs.Parameter_Telepon, UbahPenggunaFragment.TIETNomorTelepon.getText().toString())
                        .addBodyParameter(Configs.Parameter_Mobile, UbahPenggunaFragment.TIETMobilePhone.getText().toString())
                        .addBodyParameter(Configs.Parameter_Email, UbahPenggunaFragment.TIETEmail.getText().toString())
                        .addBodyParameter(Configs.Parameter_Nama_Perusahaan, UbahPerusahaanFragment.TIETNama.getText().toString())
                        .addBodyParameter(Configs.Parameter_Jns_Perusahaan, String.valueOf(UbahPerusahaanFragment.spinnerJenisPerusahaan.getSelectedItemPosition() + 1))
                        .addBodyParameter(Configs.Parameter_Penempatan_Perusahaan, String.valueOf(UbahPerusahaanFragment.spinnerArmada.getSelectedItemPosition() + 1))
                        .addBodyParameter(Configs.Parameter_Alamat_Perusahaan, UbahPerusahaanFragment.TIETAlamat.getText().toString())
                        .addBodyParameter(Configs.Parameter_Telp_Perusahaan, UbahPerusahaanFragment.TIETTelp.getText().toString())
                        .addBodyParameter(Configs.Parameter_Fax_Perusahaan, UbahPerusahaanFragment.TIETFax.getText().toString())
                        .addBodyParameter(Configs.Parameter_Email_Perusahaan, UbahPerusahaanFragment.TIETEmail.getText().toString())
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject   = new JSONObject(response);
                                    boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                    if(status){
                                        dialogs.setMessage(getString(R.string.perubahan_data_berhasil_disimpan));
                                        dialogs.setPositiveButton(getString(R.string.ok), view1 -> {
                                            dialogs.dismiss();
                                            finishAfterTransition();
                                        });
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
        });
    }

    public void setEnableSimpan() {
        if(pengguna && perusahaan){
            btnSimpan.setBackgroundColor(getColor(R.color.background_orange));
            btnSimpan.setEnabled(true);
        }
        else{
            btnSimpan.setBackgroundColor(getColor(R.color.background_orange_disabled));
            btnSimpan.setEnabled(false);
        }
    }
}