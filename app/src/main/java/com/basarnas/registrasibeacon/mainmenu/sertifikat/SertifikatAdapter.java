package com.basarnas.registrasibeacon.mainmenu.sertifikat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.basarnas.registrasibeacon.R;

import java.util.ArrayList;

public class SertifikatAdapter extends RecyclerView.Adapter<SertifikatAdapter.MyView> {
    private final Context context;
    private final ArrayList<Sertifikat> arrayListSertifikat;

    public SertifikatAdapter(Context context, ArrayList<Sertifikat> arrayListSertifikat) {
        this.context                = context;
        this.arrayListSertifikat    = arrayListSertifikat;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        TextView TVIDBeacon, TVKeteranganStatus, TVCallSign, TVTanggalBeacon, TVTanggalKadaluarsa;
        CardView CVKeteranganStatus;

        public MyView(@NonNull View itemView) {
            super(itemView);

            TVIDBeacon          = itemView.findViewById(R.id.TVIDBeacon);
            TVKeteranganStatus  = itemView.findViewById(R.id.TVKeteranganStatus);
            TVCallSign          = itemView.findViewById(R.id.TVCallSign);
            TVTanggalBeacon     = itemView.findViewById(R.id.TVTanggalBeacon);
            TVTanggalKadaluarsa = itemView.findViewById(R.id.TVTanggalKadaluarsa);
            CVKeteranganStatus  = itemView.findViewById(R.id.CVKeteranganStatus);
        }
    }

    @NonNull
    @Override
    public SertifikatAdapter.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sertifikat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SertifikatAdapter.MyView holder, int position) {
        holder.TVIDBeacon.setText(arrayListSertifikat.get(position).getIDBeacon());

        holder.TVKeteranganStatus.setText(arrayListSertifikat.get(position).getKeteranganStatus().toLowerCase());
        holder.TVCallSign.setText(String.format("%s : %s", context.getString(R.string.call_sign), arrayListSertifikat.get(position).getCallSign()));

        holder.TVTanggalBeacon.setText(String.format("%s %s", context.getString(R.string.tanggal), arrayListSertifikat.get(position).getTanggalBeacon()));
        holder.TVTanggalKadaluarsa.setText(String.format("%s %s", context.getString(R.string.kadaluarsa), arrayListSertifikat.get(position).getTanggalKadaluarsa()));

        if(arrayListSertifikat.get(position).getKeteranganStatus().equalsIgnoreCase("active")){
            holder.CVKeteranganStatus.setCardBackgroundColor(context.getColor(R.color.background_green));
        }
        else if(arrayListSertifikat.get(position).getKeteranganStatus().equalsIgnoreCase("expired")){
            holder.CVKeteranganStatus.setCardBackgroundColor(context.getColor(R.color.background_gray_dark));
        }
    }

    @Override
    public int getItemCount() {
        return arrayListSertifikat.size();
    }
}