package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_child_estatement;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;


public class Adapter_child_estatement extends RecyclerView.Adapter<Adapter_child_estatement.ViewHolder> {
    private Context context;
    private List<Data_child_estatement> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_child_estatement(Context context, List<Data_child_estatement> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_estatement_child, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Data_child_estatement data = item.get(i);

        holder.txtInvoice.setText(data.getChildInvoice());
        holder.txtTanggal.setText(data.getChildTanggal());
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
        UniversalFontTextView txtInvoice, txtTanggal, txtHarga;
        ImageView imgItem;
        ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtInvoice = itemView.findViewById(R.id.item_child_estatement_txtinv);
            txtTanggal = itemView.findViewById(R.id.item_child_estatement_txttgl);
            txtHarga = itemView.findViewById(R.id.item_child_estatement_txtharga);
            imgItem = itemView.findViewById(R.id.item_child_estatement_imgitem);
            layout = itemView.findViewById(R.id.item_child_estatement_layout);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, getLayoutPosition(), item.get(getLayoutPosition()).getChildInvoice());
        }
    }
}
