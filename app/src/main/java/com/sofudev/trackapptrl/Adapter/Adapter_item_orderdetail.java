package com.sofudev.trackapptrl.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_item_orderdetail;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_item_orderdetail extends RecyclerView.Adapter<Adapter_item_orderdetail.ViewHolder> {

    private Context context;
    private List<Data_item_orderdetail> item;

    public Adapter_item_orderdetail(Context context, List<Data_item_orderdetail> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_detail, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Data_item_orderdetail data = item.get(i);

        String disc = data.getDiskon() + " %";
        String tintPrice = "-";
        int cat     = data.getCategory();

        if (cat == 3)
        {
            int discSale = data.getDiskonFlashSale();
            int tint = data.getTinting();

            if (discSale > 0)
            {
                disc = disc + " + " + discSale + "% (" + data.getTitleFlashSale() + ")";
            }
            else
            {
                if (data.getDiskon() > 0)
                {
                    disc = data.getDiskon() + "%";
                }
                else
                {
                    disc = "-";
                }
            }

            if (tint > 0) { tintPrice = "Rp. " + CurencyFormat(String.valueOf(tint)); } else { tintPrice = "-"; }
        }
        else if (cat == 2)
        {
            String note = data.getTitleFlashSale();

            if (!note.equals("null"))
            {
                disc = disc + " (" + note + ") ";
            }
        }

        holder.txtKodeItem.setText(data.getItem_code());
        holder.txtDeskripsi.setText(data.getDeskripsi());
        holder.txtJumlah.setText(String.valueOf(data.getJumlah()));
        holder.txtDiskon.setText(disc);
        holder.txtTinting.setText(tintPrice);

        if (data.getCategory() == 3)
        {
            holder.txtTotal.setText("Unavailable");
        }
        else
        {
            holder.txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(data.getTotalAll())));
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
        UniversalFontTextView txtKodeItem, txtDeskripsi, txtJumlah, txtDiskon, txtTinting, txtTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtKodeItem = itemView.findViewById(R.id.item_orderdetail_txtKodeItem);
            txtDeskripsi= itemView.findViewById(R.id.item_orderdetail_txtDeskripsi);
            txtJumlah   = itemView.findViewById(R.id.item_orderdetail_txtJumlah);
            txtDiskon   = itemView.findViewById(R.id.item_orderdetail_txtDiskon);
            txtTinting  = itemView.findViewById(R.id.item_orderdetail_txtTinting);
            txtTotal    = itemView.findViewById(R.id.item_orderdetail_txtTotal);
        }
    }
}
