package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sofudev.trackapptrl.Data.Data_detail_deposit;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * Created by sholeh on 30/06/2020.
 */

public class Adapter_detail_deposit extends RecyclerView.Adapter<Adapter_detail_deposit.ViewHolder> {

    private Context context;
    private List<Data_detail_deposit> item;

    public Adapter_detail_deposit(Context context, List<Data_detail_deposit> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public Adapter_detail_deposit.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_deposit, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Data_detail_deposit data = item.get(i);

        holder.txtJenis.setText(data.getJenis_pembayaran());
        holder.txtTanggal.setText(data.getInsert_date());
        holder.txtDeskripsi.setText(data.getKeterangan());
        holder.txtNominal.setText(CurencyFormat(String.valueOf(data.getSaldo())));

        if (data.getJenis_pembayaran().equals("DEPOSIT"))
        {
            holder.txtNominal.setTextColor(Color.parseColor("#45ac2d"));
            holder.txtJenis.setTextColor(Color.parseColor("#45ac2d"));
            holder.txtJenis.setText(data.getJenis_pembayaran() + " (" + data.getBank_name() + ") ");
        }
        else
        {
            holder.txtNominal.setTextColor(Color.parseColor("#f90606"));
            holder.txtJenis.setTextColor(Color.parseColor("#f90606"));
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtJenis, txtTanggal, txtDeskripsi, txtNominal;

        public ViewHolder(View itemView) {
            super(itemView);

            txtJenis   = itemView.findViewById(R.id.item_detaildeposit_txtjenis);
            txtTanggal = itemView.findViewById(R.id.item_detaildeposit_txtdate);
            txtDeskripsi = itemView.findViewById(R.id.item_detaildeposit_txtdescription);
            txtNominal =itemView.findViewById(R.id.item_detaildeposit_txtprice);
        }
    }
}
