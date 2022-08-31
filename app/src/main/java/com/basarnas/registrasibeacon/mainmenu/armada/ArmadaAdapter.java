package com.basarnas.registrasibeacon.mainmenu.armada;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.beranda.BeaconActivity;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Dialogs;
import com.basarnas.registrasibeacon.tools.Preferences;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class ArmadaAdapter extends RecyclerView.Adapter<ArmadaAdapter.MyView> {
    private final Context context;
    private final ArrayList<Armada> arrayListArmada;

    public ArmadaAdapter(Context context, ArrayList<Armada> arrayListArmada) {
        this.context            = context;
        this.arrayListArmada    = arrayListArmada;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        TextView TVNamaPenggunaan, TVTipe, TVSpesifik, TVTambahBeacon;
        ImageButton IBArmada;

        public MyView(@NonNull View itemView) {
            super(itemView);

            TVNamaPenggunaan    = itemView.findViewById(R.id.TVNamaPenggunaan);
            TVTipe              = itemView.findViewById(R.id.TVTipe);
            TVSpesifik          = itemView.findViewById(R.id.TVSpesifik);
            TVTambahBeacon      = itemView.findViewById(R.id.TVTambahBeacon);
            IBArmada            = itemView.findViewById(R.id.IBArmada);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_armada, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, @SuppressLint("RecyclerView") int position) {
        holder.TVNamaPenggunaan.setText(arrayListArmada.get(position).getNamaPenggunaan());
        holder.TVSpesifik.setText(arrayListArmada.get(position).getSpesifik());
        holder.TVTipe.setText(arrayListArmada.get(position).getTipe());

        holder.IBArmada.setOnClickListener(view -> {
            Dialog dialog   = new Dialog(context, R.style.dialogAction);
            Dialogs dialogs = new Dialogs(context);

            dialog.setContentView(R.layout.view_dialog_action);
            dialog.setCancelable(true);
            dialog.show();

            TextView TVDialogAction1    = dialog.findViewById(R.id.TVDialogAction1);
            TextView TVDialogAction2    = dialog.findViewById(R.id.TVDialogAction2);

            TVDialogAction1.setText(context.getString(R.string.ubah));
            TVDialogAction2.setText(context.getString(R.string.hapus));

            TVDialogAction1.setOnClickListener(view1 -> {
                dialog.dismiss();
                context.startActivity(new Intent(context, ArmadaActivity.class).putExtra(Configs.Parameter_ID, arrayListArmada.get(position).getID()));
            });

            TVDialogAction2.setOnClickListener(view1 -> {
                dialog.dismiss();

                dialogs.setMessage(context.getString(R.string.yakin_ingin_hapus_data_ini));
                dialogs.setDeterminate(false);
                dialogs.setCancelable(true);
                dialogs.show();

                dialogs.setNegativeButton(context.getString(R.string.tidak), view2 -> dialogs.dismiss());
                dialogs.setPositiveButton(context.getString(R.string.ya), view2 -> {
                    dialogs.dismiss();
                    AndroidNetworking.get(Configs.getAPI(Configs.DeleteArmada))
                            .addHeaders(Configs.Auth, Preferences.getToken())
                            .addQueryParameter(Configs.Parameter_ID, arrayListArmada.get(position).getID())
                            .addQueryParameter(Configs.Parameter_ID_Perusahaan, Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_ID_Perusahaan.toUpperCase()))
                            .setPriority(Priority.HIGH)
                            .build()
                            .getAsString(new StringRequestListener() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject   = new JSONObject(response);
                                        boolean status          = Boolean.parseBoolean(jsonObject.getString(Configs.Parameter_Status));

                                        if(status){
                                            arrayListArmada.remove(position);
                                            notifyItemRemoved(position);
                                        }
                                    }
                                    catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    dialogs.setPositiveButton(context.getString(R.string.ok), view3 -> dialogs.dismiss());
                                    dialogs.setMessage(context.getString(R.string.terjadi_kesalahan));
                                    dialogs.show();
                                }
                            });
                });
            });
        });
        holder.TVTambahBeacon.setOnClickListener(view -> context.startActivity(new Intent(context, BeaconActivity.class).putExtra(Configs.Parameter_Jns_Armada, arrayListArmada.get(position).getID())));
    }

    @Override
    public int getItemCount() {
        return arrayListArmada.size();
    }
}