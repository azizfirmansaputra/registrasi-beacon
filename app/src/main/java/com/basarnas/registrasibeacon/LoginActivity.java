package com.basarnas.registrasibeacon;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText TIETUsername, TIETPassword;
    TextView TVError, TVPanduanPengguna, TVLupaPassword, TVDaftar;
    Button btnLogin;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TIETUsername        = findViewById(R.id.TIETUsername);
        TIETPassword        = findViewById(R.id.TIETPassword);
        TVError             = findViewById(R.id.TVError);
        TVPanduanPengguna   = findViewById(R.id.TVPanduanPengguna);
        TVLupaPassword      = findViewById(R.id.TVLupaPassword);
        TVDaftar            = findViewById(R.id.TVDaftar);
        btnLogin            = findViewById(R.id.btnLogin);

        dialog              = new Dialog(LoginActivity.this, R.style.dialogPanduanPengguna);

        loginUser();
        showPanduanPengguna();
        openUserLupaPassword();
        openDaftarPenggunaBaru();
    }

    private void loginUser() {
        addTextChangedListener(TIETUsername);
        addTextChangedListener(TIETPassword);

        btnLogin.setOnClickListener(view -> {
            TVError.setVisibility(View.GONE);
            App.generateToken();

            AndroidNetworking.post(Configs.getAPI(Configs.Login))
                    .addHeaders(Configs.Auth, Preferences.getToken())
                    .addBodyParameter(Configs.Parameter_Username, TIETUsername.getText().toString())
                    .addBodyParameter(Configs.Parameter_Password, TIETPassword.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    JSONObject object   = new JSONObject(jsonObject.getString(Configs.Parameter_Data));

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    Preferences.setDataLogin(object.toString());
                                    Preferences.setLogin(true);
                                    finishAfterTransition();
                                }
                                else{
                                    TVError.setText(getString(R.string.username_atau_password_salah));
                                    TVError.setVisibility(View.VISIBLE);
                                }
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            TVError.setText(getString(R.string.terjadi_kesalahan));
                            TVError.setVisibility(View.VISIBLE);
                        }
                    });
        });
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TIETUsername.getText().toString().trim().isEmpty() || TIETPassword.getText().toString().trim().isEmpty()){
                    btnLogin.setBackgroundColor(getColor(R.color.background_orange_disabled));
                    btnLogin.setEnabled(false);
                }
                else{
                    btnLogin.setBackgroundColor(getColor(R.color.background_orange));
                    btnLogin.setEnabled(true);
                }

                TVError.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void showPanduanPengguna() {
        TVPanduanPengguna.setOnClickListener(view -> {
            dialog.setContentView(R.layout.view_panduan_pengguna);
            dialog.setCancelable(true);
            dialog.show();

            ImageButton IBClose         = dialog.findViewById(R.id.IBClose);
            TextView TVNomorTelepon     = dialog.findViewById(R.id.TVNomorTelepon);
            TextView TVNomorHandphone   = dialog.findViewById(R.id.TVNomorHandphone);
            TextView TVAlamatEmail      = dialog.findViewById(R.id.TVAlamatEmail);
            Button btnDownloadPanduan   = dialog.findViewById(R.id.btnDownloadPanduan);

            IBClose.setOnClickListener(view1 -> dialog.dismiss());
            TVNomorTelepon.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + "(021)65701116"))));
            TVNomorHandphone.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + "628121370037"))));
            TVAlamatEmail.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:" + getString(R.string.achmadtoha_basarnas_go_id)))));
            btnDownloadPanduan.setOnClickListener(view1 -> startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(getString(R.string.url_buku_panduan)), "application/pdf")));
        });
    }

    private void openUserLupaPassword() {
        TVLupaPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, LupaPasswordActivity.class)));
    }

    private void openDaftarPenggunaBaru() {
        TVDaftar.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, DaftarActivity.class)));
    }
}