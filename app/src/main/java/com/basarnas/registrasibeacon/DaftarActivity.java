package com.basarnas.registrasibeacon;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Files;
import com.basarnas.registrasibeacon.tools.Permission;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DaftarActivity extends AppCompatActivity {
    LinearLayout LLContactPerson, LLCompanyProfile;
    TextInputLayout TILUsername, TILNamaContactPerson, TILAlamatContactPerson, TILNomorTeleponContactPerson, TILMobilePhone, TILEmailContactPerson;
    TextInputLayout TILNamaCompanyProfile, TILAlamatCompanyProfile, TILNomorTeleponCompanyProfile, TILFax, TILEmailCompanyProfile;
    TextInputEditText TIETUsername, TIETNamaContactPerson, TIETAlamatContactPerson, TIETNomorTeleponContactPerson, TIETMobilePhone, TIETEmailContactPerson;
    TextInputEditText TIETNamaCompanyProfile, TIETAlamatCompanyProfile, TIETNomorTeleponCompanyProfile, TIETFax, TIETEmailCompanyProfile;
    Button btnSelanjutnya, btnSebelumnya, btnRegister;
    Spinner spinnerJenisPerusahaan, spinnerArmada;
    TextView TVSuratPenunjukan, TVBrowse, TVLogin;

    ArrayList<String> listJenisPerusahaan, listArmada;
    ActivityResultLauncher<String> selectFile;
    String fileName, filePath;
    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        LLContactPerson                 = findViewById(R.id.LLContactPerson);
        LLCompanyProfile                = findViewById(R.id.LLCompanyProfile);
        TILUsername                     = findViewById(R.id.TILUsername);
        TILNamaContactPerson            = findViewById(R.id.TILNamaContactPerson);
        TILAlamatContactPerson          = findViewById(R.id.TILAlamatContactPerson);
        TILNomorTeleponContactPerson    = findViewById(R.id.TILNomorTeleponContactPerson);
        TILMobilePhone                  = findViewById(R.id.TILMobilePhone);
        TILEmailContactPerson           = findViewById(R.id.TILEmailContactPerson);
        TILNamaCompanyProfile           = findViewById(R.id.TILNamaCompanyProfile);
        TILAlamatCompanyProfile         = findViewById(R.id.TILAlamatCompanyProfile);
        TILNomorTeleponCompanyProfile   = findViewById(R.id.TILNomorTeleponCompanyProfile);
        TILFax                          = findViewById(R.id.TILFax);
        TILEmailCompanyProfile          = findViewById(R.id.TILEmailCompanyProfile);
        TIETUsername                    = findViewById(R.id.TIETUsername);
        TIETNamaContactPerson           = findViewById(R.id.TIETNamaContactPerson);
        TIETAlamatContactPerson         = findViewById(R.id.TIETAlamatContactPerson);
        TIETNomorTeleponContactPerson   = findViewById(R.id.TIETNomorTeleponContactPerson);
        TIETMobilePhone                 = findViewById(R.id.TIETMobilePhone);
        TIETEmailContactPerson          = findViewById(R.id.TIETEmailContactPerson);
        TIETNamaCompanyProfile          = findViewById(R.id.TIETNamaCompanyProfile);
        TIETAlamatCompanyProfile        = findViewById(R.id.TIETAlamatCompanyProfile);
        TIETNomorTeleponCompanyProfile  = findViewById(R.id.TIETNomorTeleponCompanyProfile);
        TIETFax                         = findViewById(R.id.TIETFax);
        TIETEmailCompanyProfile         = findViewById(R.id.TIETEmailCompanyProfile);
        btnSelanjutnya                  = findViewById(R.id.btnSelanjutnya);
        btnSebelumnya                   = findViewById(R.id.btnSebelumnya);
        btnRegister                     = findViewById(R.id.btnRegister);
        spinnerJenisPerusahaan          = findViewById(R.id.spinnerJenisPerusahaan);
        spinnerArmada                   = findViewById(R.id.spinnerArmada);
        TVSuratPenunjukan               = findViewById(R.id.TVSuratPenunjukan);
        TVBrowse                        = findViewById(R.id.TVBrowse);
        TVLogin                         = findViewById(R.id.TVLogin);

        dialogs                         = new Dialogs(DaftarActivity.this);
        listJenisPerusahaan             = new ArrayList<>();
        listArmada                      = new ArrayList<>();

        filePath                        = "";
        fileName                        = "";

        listJenisPerusahaan.add(getString(R.string.perorangan));
        listJenisPerusahaan.add(getString(R.string.perusahaan));

        listArmada.add(getString(R.string.pesawat));
        listArmada.add(getString(R.string.kapal));
        listArmada.add(getString(R.string.personal));
        listArmada.add(getString(R.string.semua));

        spinnerJenisPerusahaan.setPadding(0, 0, 0, 0);
        spinnerJenisPerusahaan.setAdapter(new ArrayAdapter<>(DaftarActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listJenisPerusahaan));

        spinnerArmada.setPadding(0, 0, 0, 0);
        spinnerArmada.setAdapter(new ArrayAdapter<>(DaftarActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listArmada));

        App.generateToken();
        validationContactPerson();
        processSelectSuratPenunjukan();
    }

    private void validationContactPerson() {
        btnRegister.setVisibility(View.GONE);
        TVLogin.setOnClickListener(view -> finishAfterTransition());

        addTextChangedListener(TIETUsername);
        addTextChangedListener(TIETMobilePhone);
        addTextChangedListener(TIETNamaContactPerson);
        addTextChangedListener(TIETEmailContactPerson);
        addTextChangedListener(TIETAlamatContactPerson);
        addTextChangedListener(TIETNomorTeleponContactPerson);

        addTextChangedListener(TIETFax);
        addTextChangedListener(TIETNamaCompanyProfile);
        addTextChangedListener(TIETEmailCompanyProfile);
        addTextChangedListener(TIETAlamatCompanyProfile);
        addTextChangedListener(TIETNomorTeleponCompanyProfile);
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(textInputEditText.getId() == R.id.TIETUsername){
                    checkUsername();
                }
                else if(textInputEditText.getId() == R.id.TIETNomorTeleponContactPerson){
                    if(TIETNomorTeleponContactPerson.getText().length() < 10){
                        TILNomorTeleponContactPerson.setErrorEnabled(true);
                        TILNomorTeleponContactPerson.setError(getString(R.string.nomor_telepon_tidak_valid));
                    }
                    else{
                        TILNomorTeleponContactPerson.setErrorEnabled(false);
                        TILNomorTeleponContactPerson.setError(null);
                    }
                }
                else if(textInputEditText.getId() == R.id.TIETMobilePhone){
                    if(TIETMobilePhone.getText().length() <= 10){
                        TILMobilePhone.setErrorEnabled(true);
                        TILMobilePhone.setError(getString(R.string.mobile_phone_tidak_valid));
                    }
                    else{
                        TILMobilePhone.setErrorEnabled(false);
                        TILMobilePhone.setError(null);
                    }
                }
                else if(textInputEditText.getId() == R.id.TIETEmailContactPerson){
                    if(!Patterns.EMAIL_ADDRESS.matcher(TIETEmailContactPerson.getText().toString()).matches()){
                        TILEmailContactPerson.setErrorEnabled(true);
                        TILEmailContactPerson.setError(getString(R.string.email_tidak_valid_));
                    }
                    else{
                        TILEmailContactPerson.setErrorEnabled(false);
                        TILEmailContactPerson.setError(null);
                    }
                }
                else if(textInputEditText.getId() == R.id.TIETNomorTeleponCompanyProfile){
                    if(TIETNomorTeleponCompanyProfile.getText().length() < 10){
                        TILNomorTeleponCompanyProfile.setErrorEnabled(true);
                        TILNomorTeleponCompanyProfile.setError(getString(R.string.nomor_telepon_tidak_valid));
                    }
                    else{
                        TILNomorTeleponCompanyProfile.setErrorEnabled(false);
                        TILNomorTeleponCompanyProfile.setError(null);
                    }
                }
                else if(textInputEditText.getId() == R.id.TIETEmailCompanyProfile){
                    if(!Patterns.EMAIL_ADDRESS.matcher(TIETEmailCompanyProfile.getText().toString()).matches()){
                        TILEmailCompanyProfile.setErrorEnabled(true);
                        TILEmailCompanyProfile.setError(getString(R.string.email_tidak_valid_));
                    }
                    else{
                        TILEmailCompanyProfile.setErrorEnabled(false);
                        TILEmailCompanyProfile.setError(null);
                    }
                }

                checkErrorContactPerson();
                checkErrorCompanyProfile();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkUsername() {
        AndroidNetworking.post(Configs.getAPI(Configs.CheckUsername))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addBodyParameter(Configs.Parameter_Username, TIETUsername.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject   = new JSONObject(response);
                            boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                            if(status){
                                TILUsername.setErrorEnabled(true);
                                TILUsername.setError(getString(R.string.username_telah_digunakan));
                            }
                            else{
                                TILUsername.setErrorEnabled(false);
                                TILUsername.setError(null);
                            }
                            checkErrorContactPerson();
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        TILUsername.setError(getString(R.string.terjadi_kesalahan));
                        TILUsername.setErrorEnabled(true);
                        checkErrorContactPerson();
                    }
                });
    }

    private void checkErrorContactPerson() {
        if(TIETUsername.getText().toString().trim().isEmpty() || TIETNamaContactPerson.getText().toString().trim().isEmpty() ||
                TIETMobilePhone.getText().toString().trim().isEmpty() || TIETEmailContactPerson.getText().toString().trim().isEmpty() ||
                TIETAlamatContactPerson.getText().toString().trim().isEmpty() || TIETNomorTeleponContactPerson.getText().toString().trim().isEmpty()){

            btnSelanjutnya.setBackgroundColor(getColor(R.color.background_navy_disabled));
            btnSelanjutnya.setEnabled(false);
        }
        else{
            if(!TILUsername.isErrorEnabled() && !TILNomorTeleponContactPerson.isErrorEnabled() && !TILMobilePhone.isErrorEnabled() && !TILEmailContactPerson.isErrorEnabled()){
                btnSelanjutnya.setBackgroundColor(getColor(R.color.background_navy));
                btnSelanjutnya.setEnabled(true);

                selanjutnya();
                sebelumnya();
                openFile();
                register();
            }
            else{
                btnSelanjutnya.setBackgroundColor(getColor(R.color.background_navy_disabled));
                btnSelanjutnya.setEnabled(false);
            }
        }
    }

    private void selanjutnya() {
        btnSelanjutnya.setOnClickListener(view -> {
            btnRegister.setVisibility(View.VISIBLE);
            LLContactPerson.setVisibility(View.GONE);
            LLCompanyProfile.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.SlideOutLeft).duration(100).playOn(LLContactPerson);
            YoYo.with(Techniques.SlideInRight).duration(100).playOn(LLCompanyProfile);
        });
    }

    private void sebelumnya() {
        btnSebelumnya.setOnClickListener(view -> {
            btnRegister.setVisibility(View.GONE);
            LLCompanyProfile.setVisibility(View.GONE);
            LLContactPerson.setVisibility(View.VISIBLE);

            YoYo.with(Techniques.SlideInLeft).duration(100).playOn(LLContactPerson);
            YoYo.with(Techniques.SlideOutRight).duration(100).playOn(LLCompanyProfile);
        });
    }

    private void openFile() {
        TVBrowse.setOnClickListener(view -> {
            if(Permission.READ_EXTERNAL_STORAGE(DaftarActivity.this)){
                selectFile.launch("application/pdf");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == Permission.READ_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectFile.launch("application/pdf");
        }
    }

    private void register() {
        btnRegister.setOnClickListener(view -> {
            dialogs.setMessage(getString(R.string.sedang_menyimpan_data));
            dialogs.setDeterminate(false);
            dialogs.setCancelable(false);
            dialogs.show();

            AndroidNetworking.post(Configs.getAPI(Configs.RegistrasiUser))
                    .addHeaders(Configs.Auth, Preferences.getToken())
                    .addBodyParameter(Configs.Parameter_Username, TIETUsername.getText().toString())
                    .addBodyParameter(Configs.Parameter_Nama, TIETNamaContactPerson.getText().toString())
                    .addBodyParameter(Configs.Parameter_Alamat, TIETAlamatContactPerson.getText().toString())
                    .addBodyParameter(Configs.Parameter_Telepon, TIETNomorTeleponContactPerson.getText().toString())
                    .addBodyParameter(Configs.Parameter_Mobile, TIETMobilePhone.getText().toString())
                    .addBodyParameter(Configs.Parameter_Email, TIETEmailContactPerson.getText().toString())
                    .addBodyParameter(Configs.Parameter_Nama_Perusahaan, TIETNamaCompanyProfile.getText().toString())
                    .addBodyParameter(Configs.Parameter_Alamat_Perusahaan, TIETAlamatCompanyProfile.getText().toString())
                    .addBodyParameter(Configs.Parameter_Telp_Perusahaan, TIETNomorTeleponCompanyProfile.getText().toString())
                    .addBodyParameter(Configs.Parameter_Fax_Perusahaan, TIETFax.getText().toString())
                    .addBodyParameter(Configs.Parameter_Email_Perusahaan, TIETEmailCompanyProfile.getText().toString())
                    .addBodyParameter(Configs.Parameter_Penempatan_Perusahaan, String.valueOf(spinnerArmada.getSelectedItemPosition() + 1))
                    .addBodyParameter(Configs.Parameter_Jns_Perusahaan, String.valueOf(spinnerJenisPerusahaan.getSelectedItemPosition() + 1))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    if(!filePath.isEmpty() && !fileName.isEmpty()){
                                        uploadSuratPenunjukan();
                                    }
                                    else{
                                        dialogs.setMessage(getString(R.string.data_berhasil_disimpan_username_dan_password_akan_dikirim_ke_email_anda));
                                        dialogs.setPositiveButton(getString(R.string.ok), view -> {
                                            dialogs.dismiss();
                                            finishAfterTransition();
                                        });
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

    private void uploadSuratPenunjukan() {
        AndroidNetworking.upload(Configs.getAPI(Configs.Upload))
                .addHeaders(Configs.Auth, Preferences.getToken())
                .addMultipartParameter(Configs.Parameter_ID, TIETUsername.getText().toString())
                .addMultipartParameter(Configs.Parameter_Jenis, Configs.Value_Surat_Penunjukan)
                .addMultipartFile(Configs.Parameter_File_Upload, new File(filePath))
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
                                dialogs.setMessage(getString(R.string.data_berhasil_disimpan_username_dan_password_akan_dikirim_ke_email_anda));
                                dialogs.setDeterminate(false);

                                dialogs.setPositiveButton(getString(R.string.ok), view -> {
                                    dialogs.dismiss();
                                    finishAfterTransition();
                                });
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

    private void checkErrorCompanyProfile() {
        if(TIETNamaCompanyProfile.getText().toString().trim().isEmpty() || TIETAlamatCompanyProfile.getText().toString().trim().isEmpty() ||
                TIETNomorTeleponCompanyProfile.getText().toString().trim().isEmpty() || TIETFax.getText().toString().trim().isEmpty() ||
                TIETEmailCompanyProfile.getText().toString().trim().isEmpty()){

            btnRegister.setBackgroundColor(getColor(R.color.background_orange_disabled));
            btnRegister.setEnabled(false);
        }
        else{
            if(!TILNomorTeleponCompanyProfile.isErrorEnabled() && !TILEmailCompanyProfile.isErrorEnabled()){
                btnRegister.setBackgroundColor(getColor(R.color.background_orange));
                btnRegister.setEnabled(true);
            }
            else{
                btnRegister.setBackgroundColor(getColor(R.color.background_orange_disabled));
                btnRegister.setEnabled(false);
            }
        }
    }

    private void processSelectSuratPenunjukan() {
        selectFile = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
           if(result != null){
               fileName = Files.getFileName(DaftarActivity.this, result);
               filePath = Files.getFilePath(DaftarActivity.this, result);

               TVSuratPenunjukan.setText(fileName);
           }
        });
    }
}