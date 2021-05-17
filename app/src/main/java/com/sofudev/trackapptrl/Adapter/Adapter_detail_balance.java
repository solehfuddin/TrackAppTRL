package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_detail_balance;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_detail_balance extends RecyclerView.Adapter<Adapter_detail_balance.ViewHolder> {
    private Context context;
    private List<Data_detail_balance> item;

    public Adapter_detail_balance(Context context, List<Data_detail_balance> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_balance, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Data_detail_balance data = item.get(position);

        holder.txtTanggal.setText(data.getTanggal());
        holder.txtDeskripsi.setText(data.getDeskripsi());
        holder.txtNominal.setText(CurencyFormat(String.valueOf(data.getNominal())));
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
        UniversalFontTextView txtTanggal, txtDeskripsi, txtNominal;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTanggal = itemView.findViewById(R.id.item_detailbalance_txtdate);
            txtDeskripsi = itemView.findViewById(R.id.item_detailbalance_txtdescription);
            txtNominal =itemView.findViewById(R.id.item_detailbalance_txtprice);
        }
    }
}
