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
import com.sofudev.trackapptrl.Data.Data_einvoice;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_einvoice extends RecyclerView.Adapter<Adapter_einvoice.ViewHolder> {
    private Context context;
    private List<Data_einvoice> item;
    RecyclerViewOnClickListener itemClick;
    DateFormat tglFormat = new DateFormat();

    public Adapter_einvoice(Context context, List<Data_einvoice> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_einvoice_overview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Data_einvoice data_einvoice = item.get(position);

        holder.txtCategory.setText(data_einvoice.getInv_category());
        holder.txtInvDate.setText(tglFormat.indoOther(data_einvoice.getInv_date()));
        holder.txtSalesName.setText(data_einvoice.getSales_name());
        holder.txtInvNumber.setText(data_einvoice.getInv_number());
        holder.txtPrice.setText(CurencyFormat(String.valueOf(data_einvoice.getInv_totalprice())));
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

        UniversalFontTextView txtCategory, txtPrice, txtInvNumber, txtInvDate, txtSalesName;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtCategory = itemView.findViewById(R.id.item_einvoice_txtcategory);
            txtPrice    = itemView.findViewById(R.id.item_einvoice_txtprice);
            txtInvNumber= itemView.findViewById(R.id.item_einvoice_txtinvnumber);
            txtInvDate  = itemView.findViewById(R.id.item_einvoice_txtinvdate);
            txtSalesName= itemView.findViewById(R.id.item_einvoice_txtsales);
            constraintLayout = itemView.findViewById(R.id.item_einvoice_constraint);

            itemView.setOnClickListener(this);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClick.onItemClick(view, getLayoutPosition(), item.get(getLayoutPosition()).getInv_number());
                }
            });
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), item.get(getLayoutPosition()).getInv_number());
        }
    }
}
