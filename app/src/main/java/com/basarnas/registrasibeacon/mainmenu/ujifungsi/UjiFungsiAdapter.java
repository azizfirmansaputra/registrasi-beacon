package com.basarnas.registrasibeacon.mainmenu.ujifungsi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.basarnas.registrasibeacon.R;

import java.util.ArrayList;

public class UjiFungsiAdapter extends RecyclerView.Adapter<UjiFungsiAdapter.MyView> {
    private final boolean selectAll;
    private final Context context;
    private final ArrayList<UjiFungsi> arrayListUjiFungsi;
    public final UjiFungsiFragment ujiFungsiFragment;

    public UjiFungsiAdapter(Context context, ArrayList<UjiFungsi> arrayListUjiFungsi, boolean selectAll, UjiFungsiFragment ujiFungsiFragment) {
        this.context            = context;
        this.arrayListUjiFungsi = arrayListUjiFungsi;
        this.selectAll          = selectAll;
        this.ujiFungsiFragment  = ujiFungsiFragment;
    }

    public static class MyView extends RecyclerView.ViewHolder {
        TextView TVIDBeacon, TVKeteranganStatus, TVCallSign, TVTanggalBeacon;
        CardView CVKeteranganStatus;
        CheckBox CBUjiFungsi;

        public MyView(@NonNull View itemView) {
            super(itemView);

            TVIDBeacon          = itemView.findViewById(R.id.TVIDBeacon);
            TVKeteranganStatus  = itemView.findViewById(R.id.TVKeteranganStatus);
            TVCallSign          = itemView.findViewById(R.id.TVCallSign);
            TVTanggalBeacon     = itemView.findViewById(R.id.TVTanggalBeacon);
            CVKeteranganStatus  = itemView.findViewById(R.id.CVKeteranganStatus);
            CBUjiFungsi         = itemView.findViewById(R.id.CBUjiFungsi);
        }
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_uji_fungsi, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        holder.TVIDBeacon.setText(arrayListUjiFungsi.get(position).getIDBeacon());
        holder.TVCallSign.setText(String.format("%s : %s", context.getString(R.string.call_sign), arrayListUjiFungsi.get(position).getCallSign()));
        holder.TVTanggalBeacon.setText(String.format("%s %s", context.getString(R.string.tanggal), arrayListUjiFungsi.get(position).getTanggalBeacon()));

        switch(arrayListUjiFungsi.get(position).getStatusUji()){
            case "3":
                holder.TVKeteranganStatus.setText(context.getString(R.string.belum_siap_uji_fungsi));
                holder.CVKeteranganStatus.setCardBackgroundColor(context.getColor(R.color.background_red));
                break;
            case "4":
                holder.TVKeteranganStatus.setText(context.getString(R.string.siap_uji_fungsi));
                holder.CVKeteranganStatus.setCardBackgroundColor(context.getColor(R.color.background_orange));
                break;
            case "6":
                holder.TVKeteranganStatus.setText(context.getString(R.string.telah_diuji_fungsi));
                holder.CVKeteranganStatus.setCardBackgroundColor(context.getColor(R.color.background_green));
                break;
        }

        holder.CBUjiFungsi.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                ujiFungsiFragment.arrayListIDUjiFungsi.add(arrayListUjiFungsi.get(position).getID());

                if(arrayListUjiFungsi.size() == ujiFungsiFragment.arrayListIDUjiFungsi.size()){
                    ujiFungsiFragment.setSelect(true);
                }
            }
            else{
                ujiFungsiFragment.arrayListIDUjiFungsi.remove(arrayListUjiFungsi.get(position).getID());

                if(ujiFungsiFragment.arrayListIDUjiFungsi.size() == 0){
                    ujiFungsiFragment.setSelect(false);
                }
            }
        });

        holder.CBUjiFungsi.setChecked(selectAll);
    }

    @Override
    public int getItemCount() {
        return arrayListUjiFungsi.size();
    }
}