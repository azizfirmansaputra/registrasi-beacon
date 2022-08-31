package com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.UbahProfilActivity;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UbahPenggunaFragment extends Fragment {
    public TextInputLayout TILNama, TILAlamat, TILNomorTelepon, TILMobilePhone, TILEmail;
    public static TextInputEditText TIETNama, TIETAlamat, TIETNomorTelepon, TIETMobilePhone, TIETEmail;

    UbahProfilActivity ubahProfilActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view           = inflater.inflate(R.layout.fragment_ubah_pengguna, container, false);

        TILNama             = view.findViewById(R.id.TILNama);
        TILAlamat           = view.findViewById(R.id.TILAlamat);
        TILNomorTelepon     = view.findViewById(R.id.TILNomorTelepon);
        TILMobilePhone      = view.findViewById(R.id.TILMobilePhone);
        TILEmail            = view.findViewById(R.id.TILEmail);
        TIETNama            = view.findViewById(R.id.TIETNama);
        TIETAlamat          = view.findViewById(R.id.TIETAlamat);
        TIETNomorTelepon    = view.findViewById(R.id.TIETNomorTelepon);
        TIETMobilePhone     = view.findViewById(R.id.TIETMobilePhone);
        TIETEmail           = view.findViewById(R.id.TIETEmail);

        ubahProfilActivity  = (UbahProfilActivity)getContext();

        setTextDataPengguna();
        validationPengguna();
        return view;
    }

    private void setTextDataPengguna() {
        TIETNama.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Nama));
        TIETAlamat.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Alamat));
        TIETNomorTelepon.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Telp));
        TIETMobilePhone.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Seluler));
        TIETEmail.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_Email));
    }

    private void validationPengguna() {
        addTextChangedListener(TIETNama);
        addTextChangedListener(TIETEmail);
        addTextChangedListener(TIETAlamat);
        addTextChangedListener(TIETMobilePhone);
        addTextChangedListener(TIETNomorTelepon);
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(textInputEditText.getId() == R.id.TIETNomorTelepon){
                    if(TIETNomorTelepon.getText().length() < 10){
                        TILNomorTelepon.setErrorEnabled(true);
                        TILNomorTelepon.setError(getString(R.string.nomor_telepon_tidak_valid));
                    }
                    else{
                        TILNomorTelepon.setErrorEnabled(false);
                        TILNomorTelepon.setError(null);
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
                else if(textInputEditText.getId() == R.id.TIETEmail){
                    if(!Patterns.EMAIL_ADDRESS.matcher(TIETEmail.getText().toString()).matches()){
                        TILEmail.setErrorEnabled(true);
                        TILEmail.setError(getString(R.string.email_tidak_valid_));
                    }
                    else{
                        TILEmail.setErrorEnabled(false);
                        TILEmail.setError(null);
                    }
                }
                checkErrorPengguna();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkErrorPengguna() {
        if(TIETNama.getText().toString().trim().isEmpty() || TIETAlamat.getText().toString().trim().isEmpty() ||
                TIETNomorTelepon.getText().toString().trim().isEmpty() || TIETMobilePhone.getText().toString().trim().isEmpty() ||
                TIETEmail.getText().toString().trim().isEmpty()){

            ubahProfilActivity.pengguna = false;
        }
        else{
            ubahProfilActivity.pengguna = !TILNomorTelepon.isErrorEnabled() && !TILMobilePhone.isErrorEnabled() && !TILMobilePhone.isErrorEnabled() && !TILEmail.isErrorEnabled();
        }
        ubahProfilActivity.setEnableSimpan();
    }

    public UbahPenggunaFragment() {}
}