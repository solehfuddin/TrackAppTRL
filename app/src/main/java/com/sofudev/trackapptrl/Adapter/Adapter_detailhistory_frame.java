package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_detailhistory_frame;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_detailhistory_frame extends RecyclerView.Adapter<Adapter_detailhistory_frame.ViewHolder> {
    private Context context;
    private List<Data_detailhistory_frame> item;

    public Adapter_detailhistory_frame(Context context, List<Data_detailhistory_frame> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_detailhistory_frame, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data_detailhistory_frame data = item.get(position);

        String price = "Rp. " + CurencyFormat(String.valueOf(data.getPrice()));
        String disc  = String.valueOf(data.getDisc()) + " % ";
        String qty   = String.valueOf(data.getQty()) + " Pcs ";
        String amount= "Rp. " + CurencyFormat(String.valueOf(data.getAmount()));

        holder.txtItem.setText(data.getItemCode());
        holder.txtPrice.setText(price);
        holder.txtDiscount.setText(disc);
        holder.txtQty.setText(qty);
        holder.txtAmount.setText(amount);
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
        UniversalFontTextView txtItem, txtPrice, txtDiscount, txtQty, txtAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            txtItem     = itemView.findViewById(R.id.recycle_detailhistory_frame_txtitem);
            txtPrice    = itemView.findViewById(R.id.recycle_detailhistory_frame_txtprice);
            txtDiscount = itemView.findViewById(R.id.recycle_detailhistory_frame_txtdisc);
            txtQty      = itemView.findViewById(R.id.recycle_detailhistory_frame_txtqty);
            txtAmount   = itemView.findViewById(R.id.recycle_detailhistory_frame_txtamount);
        }
    }
}
