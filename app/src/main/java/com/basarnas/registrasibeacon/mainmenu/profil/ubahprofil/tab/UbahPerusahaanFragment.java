package com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.tab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.profil.ubahprofil.UbahProfilActivity;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

@SuppressLint("StaticFieldLeak")
public class UbahPerusahaanFragment extends Fragment {
    public TextInputLayout TILNama, TILAlamat, TILTelp, TILFax, TILEmail;
    public static TextInputEditText TIETNama, TIETAlamat, TIETTelp, TIETFax, TIETEmail;
    public static Spinner spinnerJenisPerusahaan, spinnerArmada;

    ArrayList<String> listJenisPerusahaan, listArmada;
    UbahProfilActivity ubahProfilActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view               = inflater.inflate(R.layout.fragment_ubah_perusahaan, container, false);

        TILNama                 = view.findViewById(R.id.TILNama);
        TILAlamat               = view.findViewById(R.id.TILAlamat);
        TILTelp                 = view.findViewById(R.id.TILTelp);
        TILFax                  = view.findViewById(R.id.TILFax);
        TILEmail                = view.findViewById(R.id.TILEmail);
        TIETNama                = view.findViewById(R.id.TIETNama);
        TIETAlamat              = view.findViewById(R.id.TIETAlamat);
        TIETTelp                = view.findViewById(R.id.TIETTelp);
        TIETFax                 = view.findViewById(R.id.TIETFax);
        TIETEmail               = view.findViewById(R.id.TIETEmail);
        spinnerJenisPerusahaan  = view.findViewById(R.id.spinnerJenisPerusahaan);
        spinnerArmada           = view.findViewById(R.id.spinnerArmada);

        ubahProfilActivity      = (UbahProfilActivity)getContext();
        listJenisPerusahaan     = new ArrayList<>();
        listArmada              = new ArrayList<>();

        listJenisPerusahaan.add(getString(R.string.perorangan));
        listJenisPerusahaan.add(getString(R.string.perusahaan));

        listArmada.add(getString(R.string.pesawat));
        listArmada.add(getString(R.string.kapal));
        listArmada.add(getString(R.string.personal));
        listArmada.add(getString(R.string.semua));

        spinnerJenisPerusahaan.setPadding(0, 0, 0, 0);
        spinnerJenisPerusahaan.setAdapter(new ArrayAdapter<>(ubahProfilActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listJenisPerusahaan));

        spinnerArmada.setPadding(0, 0, 0, 0);
        spinnerArmada.setAdapter(new ArrayAdapter<>(ubahProfilActivity, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listArmada));

        setTextDataPerusahaan();
        validationPerusahaan();
        return view;
    }

    private void setTextDataPerusahaan() {
        TIETNama.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_nmPerusahaan));
        TIETAlamat.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_almtPerusahaan));
        TIETTelp.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_telpPerusahaan));
        TIETFax.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_faxPerusahaan));
        TIETEmail.setText(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_emailPerusahaan));

        spinnerJenisPerusahaan.setSelection(Integer.parseInt(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_jnsPerusahaan)) - 1);
        spinnerArmada.setSelection(Integer.parseInt(Preferences.getData(Preferences.Key_DataProfil, Configs.Parameter_jnsPenempatan)) - 1);
    }

    private void validationPerusahaan() {
        addTextChangedListener(TIETFax);
        addTextChangedListener(TIETNama);
        addTextChangedListener(TIETTelp);
        addTextChangedListener(TIETEmail);
        addTextChangedListener(TIETAlamat);
    }

    private void addTextChangedListener(TextInputEditText textInputEditText) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(textInputEditText.getId() == R.id.TIETTelp){
                    if(TIETTelp.getText().length() < 10){
                        TILTelp.setErrorEnabled(true);
                        TILTelp.setError(getString(R.string.nomor_telepon_tidak_valid));
                    }
                    else{
                        TILTelp.setErrorEnabled(false);
                        TILTelp.setError(null);
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

                checkErrorPerusahaan();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkErrorPerusahaan() {
        if(TIETNama.getText().toString().trim().isEmpty() || TIETAlamat.getText().toString().trim().isEmpty() ||
                TIETTelp.getText().toString().trim().isEmpty() || TIETFax.getText().toString().trim().isEmpty() ||
                TIETEmail.getText().toString().trim().isEmpty()){

            ubahProfilActivity.perusahaan = false;
        }
        else{
            ubahProfilActivity.perusahaan = !TILTelp.isErrorEnabled() && !TILEmail.isErrorEnabled();
        }
        ubahProfilActivity.setEnableSimpan();
    }

    public UbahPerusahaanFragment() {}
}