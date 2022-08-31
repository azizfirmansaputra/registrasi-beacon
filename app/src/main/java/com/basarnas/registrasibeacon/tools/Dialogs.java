package com.basarnas.registrasibeacon.tools;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.basarnas.registrasibeacon.R;

import java.util.Locale;

public class Dialogs extends Dialog {
    TextView TVMessage, TVProgress, TVNegativeAction, TVPositiveAction;
    LinearLayout LLAction, LLNegativeAction, LLPositiveAction;
    ProgressBar PBHorizontal;

    int progress;
    boolean determinate;
    String message, txtNegative, txtPositive;
    View.OnClickListener clickListenerNegative, clickListenerPositive;

    public Dialogs(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialogs);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TVMessage           = findViewById(R.id.TVMessage);
        TVProgress          = findViewById(R.id.TVProgress);
        TVNegativeAction    = findViewById(R.id.TVNegativeAction);
        TVPositiveAction    = findViewById(R.id.TVPositiveAction);
        LLAction            = findViewById(R.id.LLAction);
        LLNegativeAction    = findViewById(R.id.LLNegativeAction);
        LLPositiveAction    = findViewById(R.id.LLPositiveAction);
        PBHorizontal        = findViewById(R.id.PBHorizontal);

        TVMessage.setText(message);
        PBHorizontal.setProgress(progress);
        TVNegativeAction.setText(txtNegative);
        TVPositiveAction.setText(txtPositive);
        TVNegativeAction.setOnClickListener(clickListenerNegative);
        TVPositiveAction.setOnClickListener(clickListenerPositive);
        TVProgress.setText(String.format(Locale.getDefault(), "%d%%", progress));

        if(isDeterminate()){
            PBHorizontal.setVisibility(View.VISIBLE);
            TVProgress.setVisibility(View.VISIBLE);
        }
        else{
            PBHorizontal.setVisibility(View.GONE);
            TVProgress.setVisibility(View.GONE);
        }

        checkActionDialogVisibility();
    }

    public void setMessage(String message) {
        this.message = message;

        if(TVMessage != null){
            if(!message.isEmpty()){
                TVMessage.setText(message);
            }
        }
    }

    public void setDeterminate(boolean determinate) {
        this.determinate = determinate;

        if(PBHorizontal != null && TVProgress != null){
            if(determinate){
                PBHorizontal.setVisibility(View.VISIBLE);
                TVProgress.setVisibility(View.VISIBLE);
            }
            else{
                PBHorizontal.setVisibility(View.GONE);
                TVProgress.setVisibility(View.GONE);
            }
        }
    }

    public boolean isDeterminate() {
        return determinate;
    }

    public void setProgress(int progress) {
        this.progress = progress;

        if(PBHorizontal != null && TVProgress != null){
            PBHorizontal.setProgress(progress);
            TVProgress.setText(String.format(Locale.getDefault(), "%d%%", progress));
        }
    }

    public void setNegativeButton(String txtNegative, View.OnClickListener clickListenerNegative) {
        this.txtNegative            = txtNegative;
        this.clickListenerNegative  = clickListenerNegative;

        if(LLAction != null && TVNegativeAction != null){
            if(clickListenerNegative != null){
                LLAction.setVisibility(View.VISIBLE);
                TVNegativeAction.setText(txtNegative);
                LLNegativeAction.setVisibility(View.VISIBLE);
                TVNegativeAction.setOnClickListener(clickListenerNegative);
            }
            checkActionDialogVisibility();
        }
    }

    public void setPositiveButton(String txtPositive, View.OnClickListener clickListenerPositive) {
        this.txtPositive            = txtPositive;
        this.clickListenerPositive  = clickListenerPositive;

        if(LLAction != null && TVPositiveAction != null){
            if(clickListenerPositive != null){
                LLAction.setVisibility(View.VISIBLE);
                TVPositiveAction.setText(txtPositive);
                LLPositiveAction.setVisibility(View.VISIBLE);
                TVPositiveAction.setOnClickListener(clickListenerPositive);
            }
            checkActionDialogVisibility();
        }
    }

    private void checkActionDialogVisibility() {
        if(clickListenerNegative == null){
            LLNegativeAction.setVisibility(View.GONE);
        }

        if(clickListenerPositive == null){
            LLPositiveAction.setVisibility(View.GONE);
        }

        if(clickListenerNegative == null && clickListenerPositive == null){
            LLAction.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        setMessage("");
        setProgress(0);
        setDeterminate(false);
        setPositiveButton("", null);
        setNegativeButton("", null);
    }
}