package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_child_estatement;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_summary_estatement extends RecyclerView.Adapter<Adapter_summary_estatement.ViewHolder> {
    private Context context;
    private List<Data_child_estatement> item;
    private RecyclerViewOnClickListener itemClick;
    DateFormat tglFormat = new DateFormat();

    public Adapter_summary_estatement(Context context, List<Data_child_estatement> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_summary_statement, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data_child_estatement data = item.get(position);

        holder.txtTahun.setText(tglFormat.indo(data.getChildTanggal()));
        holder.txtHarga.setText(CurencyFormat(data.getChildHarga()));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtTahun, txtHarga;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTahun = itemView.findViewById(R.id.item_summary_estatement_txttgl);
            txtHarga = itemView.findViewById(R.id.item_summary_estatement_txtharga);
            layout = itemView.findViewById(R.id.item_summary_estatement_layout);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, getLayoutPosition(), item.get(getLayoutPosition()).getChildInvoice());
        }
    }
}
