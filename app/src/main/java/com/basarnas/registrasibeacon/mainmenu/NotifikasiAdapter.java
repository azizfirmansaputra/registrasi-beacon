package com.basarnas.registrasibeacon.mainmenu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.basarnas.registrasibeacon.R;
import com.basarnas.registrasibeacon.mainmenu.beranda.Beacon;
import com.basarnas.registrasibeacon.mainmenu.beranda.BeaconActivity;
import com.basarnas.registrasibeacon.tools.Configs;
import com.basarnas.registrasibeacon.tools.Preferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.MyView> {
    private final Context context;
    private final ArrayList<Beacon> arrayListBeacon;
    private final String kadaluarsa;

    public NotifikasiAdapter(Context context, ArrayList<Beacon> arrayListBeacon, String kadaluarsa) {
        this.context            = context;
        this.arrayListBeacon    = arrayListBeacon;
        this.kadaluarsa         = kadaluarsa;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        TextView TVIDBeacon, TVStatus, TVNoSertifikat, TVBerlakuSampai, TVKadaluarsaBaterai, TVDeskripsiStatusTitle, TVDeskripsiStatus, TVPerpanjanganPerubahan;
        CardView CVStatus, CVPerpanjanganPerubahan;
        ImageButton IBBeacon;

        public MyView(@NonNull View itemView) {
            super(itemView);

            TVIDBeacon              = itemView.findViewById(R.id.TVIDBeacon);
            TVStatus                = itemView.findViewById(R.id.TVStatus);
            TVNoSertifikat          = itemView.findViewById(R.id.TVNoSertifikat);
            TVBerlakuSampai         = itemView.findViewById(R.id.TVBerlakuSampai);
            TVKadaluarsaBaterai     = itemView.findViewById(R.id.TVKadaluarsaBaterai);
            TVDeskripsiStatusTitle  = itemView.findViewById(R.id.TVDeskirpsiStatusTitle);
            TVDeskripsiStatus       = itemView.findViewById(R.id.TVDeskripsiStatus);
            TVPerpanjanganPerubahan = itemView.findViewById(R.id.TVPerpanjanganPerubahan);
            CVStatus                = itemView.findViewById(R.id.CVStatus);
            CVPerpanjanganPerubahan = itemView.findViewById(R.id.CVPerpanjanganPerubahan);
            IBBeacon                = itemView.findViewById(R.id.IBBeacon);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_beacon, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        String[] jenisData  = {"ELT", "EPIRB", "PLB"};
        String[] penempatan = {"Pesawat", "Kapal", "Personal", "Semua"};

        holder.CVStatus.setVisibility(View.GONE);
        holder.TVPerpanjanganPerubahan.setText(context.getString(R.string.perpanjang));
        holder.TVIDBeacon.setText(arrayListBeacon.get(position).getIDBeacon());
        holder.TVDeskripsiStatusTitle.setText(String.format("%s :", context.getString(R.string.tanggal)));
        holder.TVBerlakuSampai.setText(String.format("%s : %s", context.getString(R.string.jenis), jenisData[Integer.parseInt(arrayListBeacon.get(position).getJenisData()) - 1]));
        holder.TVNoSertifikat.setText(String.format("%s : %s", context.getString(R.string.call_sign), arrayListBeacon.get(position).getCallSign().equals("null") ? "-" : arrayListBeacon.get(position).getCallSign()));
        holder.TVPerpanjanganPerubahan.setOnClickListener(view -> context.startActivity(new Intent(context, BeaconActivity.class).putExtra(Configs.Parameter_ID, arrayListBeacon.get(position).getID())));
        holder.TVKadaluarsaBaterai.setText(String.format("%s : %s", context.getString(R.string.penempatan), penempatan[Integer.parseInt(Preferences.getData(Preferences.Key_DataLogin, Configs.Parameter_Jns_Armada.toUpperCase())) - 1]));

        holder.IBBeacon.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context, R.style.dialogAction);
            dialog.setContentView(R.layout.view_dialog_action);
            dialog.setCancelable(true);
            dialog.show();

            TextView TVDialogAction1    = dialog.findViewById(R.id.TVDialogAction1);
            TextView TVDialogAction2    = dialog.findViewById(R.id.TVDialogAction2);

            TVDialogAction1.setText(context.getString(R.string.formulir));
            TVDialogAction2.setText(context.getString(R.string.hasil));

            TVDialogAction1.setOnClickListener(view1 -> context.startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(arrayListBeacon.get(position).getFormulir()), "application/pdf")));
            TVDialogAction2.setOnClickListener(view1 -> context.startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse(arrayListBeacon.get(position).getHasil()), "application/pdf")));
        });

        try{
            SimpleDateFormat format     = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
            SimpleDateFormat newFormat  = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            if(kadaluarsa.equals(context.getString(R.string.kadaluarsa_registrasi))){
                holder.TVDeskripsiStatus.setText(newFormat.format(format.parse(arrayListBeacon.get(position).getBerlakuSampai().replace("SEPT", "SEP").replace("AGUST", "AGT"))));
            }
            else{
                holder.TVDeskripsiStatus.setText(newFormat.format(format.parse(arrayListBeacon.get(position).getKadaluarsaBaterai().replace("SEPT", "SEP").replace("AGUST", "AGT"))));
            }
        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayListBeacon.size();
    }
}