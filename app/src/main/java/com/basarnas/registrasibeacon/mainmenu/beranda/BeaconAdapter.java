package com.basarnas.registrasibeacon.mainmenu.beranda;

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
import com.basarnas.registrasibeacon.tools.Configs;

import java.util.ArrayList;

public class BeaconAdapter extends RecyclerView.Adapter<BeaconAdapter.MyView> {
    private final Context context;
    private final ArrayList<Beacon> arrayListBeacon;

    public BeaconAdapter(Context context, ArrayList<Beacon> arrayListBeacon) {
        this.context            = context;
        this.arrayListBeacon    = arrayListBeacon;
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
        holder.TVDeskripsiStatusTitle.setText(context.getString(R.string.status_));
        holder.TVIDBeacon.setText(arrayListBeacon.get(position).getIDBeacon());
        holder.TVBerlakuSampai.setText(String.format("%s\n%s", context.getString(R.string.berlaku_s_d), arrayListBeacon.get(position).getBerlakuSampai()));
        holder.TVKadaluarsaBaterai.setText(String.format("%s\n%s", context.getString(R.string.kadaluarsa_baterai), arrayListBeacon.get(position).getKadaluarsaBaterai()));
        holder.TVNoSertifikat.setText(String.format("%s : %s", context.getString(R.string.sertifikat), arrayListBeacon.get(position).getNoSertifikat().equals("null") ? "-" : arrayListBeacon.get(position).getNoSertifikat()));
        holder.TVPerpanjanganPerubahan.setOnClickListener(view -> context.startActivity(new Intent(context, BeaconActivity.class).putExtra(Configs.Parameter_ID, arrayListBeacon.get(position).getID())));

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

        if(Integer.parseInt(arrayListBeacon.get(position).getIsVerifikasi()) == 1 && Integer.parseInt(arrayListBeacon.get(position).getStatusUji()) >= 1){
            holder.CVStatus.setVisibility(View.INVISIBLE);
            holder.TVDeskripsiStatus.setText(context.getString(R.string.terdaftar));
        }
        else if(Integer.parseInt(arrayListBeacon.get(position).getIsVerifikasi()) == 1 && Integer.parseInt(arrayListBeacon.get(position).getStatusUji()) == 0){
            holder.TVStatus.setText(context.getString(R.string.lima_puluh_persen));
            holder.CVStatus.setVisibility(View.VISIBLE);
            holder.TVDeskripsiStatus.setText(context.getString(R.string.registrasi_baru_data_sesuai));
            holder.CVStatus.setCardBackgroundColor(context.getColor(R.color.background_green));
        }
        else if(Integer.parseInt(arrayListBeacon.get(position).getIsVerifikasi()) == 0){
            holder.TVStatus.setText(context.getString(R.string.nol_persen));
            holder.CVStatus.setVisibility(View.VISIBLE);
            holder.TVDeskripsiStatus.setText(context.getString(R.string.registrasi_baru_belum_diverifikasi));
            holder.CVStatus.setCardBackgroundColor(context.getColor(R.color.background_gray_dark));
        }
        else if(Integer.parseInt(arrayListBeacon.get(position).getIsVerifikasi()) == 2){
            holder.CVStatus.setVisibility(View.INVISIBLE);
            holder.TVDeskripsiStatus.setText(context.getString(R.string.silahkan_perbaiki_data_anda));
        }
    }

    @Override
    public int getItemCount() {
        return arrayListBeacon.size();
    }
}