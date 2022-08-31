package com.basarnas.registrasibeacon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.tools.App;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Preferences;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LupaPasswordActivity extends AppCompatActivity {
    TextInputEditText TIETEmail;
    Button btnResetPassword;
    TextView TVError;

    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_password);

        TIETEmail           = findViewById(R.id.TIETEmail);
        btnResetPassword    = findViewById(R.id.btnResetPassword);
        TVError             = findViewById(R.id.TVError);

        dialogs             = new Dialogs(LupaPasswordActivity.this);

        addTextChangedListener();
        App.generateToken();
        resetPassword();
    }

    private void addTextChangedListener() {
        TIETEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!Patterns.EMAIL_ADDRESS.matcher(charSequence.toString()).matches()){
                    btnResetPassword.setBackgroundColor(getColor(R.color.background_orange_disabled));
                    btnResetPassword.setEnabled(false);

                    TVError.setVisibility(View.VISIBLE);
                    TVError.setText(getString(R.string.email_tidak_valid_));
                }
                else{
                    btnResetPassword.setBackgroundColor(getColor(R.color.background_orange));
                    btnResetPassword.setEnabled(true);
                    TVError.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void resetPassword() {
        btnResetPassword.setOnClickListener(view -> {
            TVError.setVisibility(View.GONE);

            AndroidNetworking.post(Configs.getAPI(Configs.ForgotPassword))
                    .addHeaders(Configs.Auth, Preferences.getToken())
                    .addBodyParameter(Configs.Parameter_Email, TIETEmail.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonObject   = new JSONObject(response);
                                boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                if(status){
                                    dialogs.setMessage(getString(R.string.password_berhasil_direset_dan_telah_dikirim_ke_email_anda));
                                    dialogs.setDeterminate(false);
                                    dialogs.setCancelable(false);
                                    dialogs.show();

                                    dialogs.setPositiveButton(getString(R.string.ok), view1 -> {
                                        dialogs.dismiss();
                                        finishAfterTransition();
                                    });
                                }
                                else{
                                    TVError.setVisibility(View.VISIBLE);
                                    TVError.setText(getString(R.string.password_gagal_direset_email_tidak_sesuai));
                                }
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            TVError.setVisibility(View.VISIBLE);
                            TVError.setText(getString(R.string.terjadi_kesalahan));
                        }
                    });
        });
    }
}