package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_item_sp;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_detail_sp extends RecyclerView.Adapter<Adapter_detail_sp.ViewHolder> {
    private Context context;
    private List<Data_item_sp> item;

    public Adapter_detail_sp(Context context, List<Data_item_sp> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_detail_sp, parent, false);
        return new Adapter_detail_sp.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtItemCode.setText(item.get(position).getItemCode());
        holder.txtItemDesc.setText(item.get(position).getDescription());
        holder.txtItemQty.setText(item.get(position).getQty() + " " + item.get(position).getUmoCode());
        holder.txtItemPrice.setText("Rp. " + CurencyFormat(String.valueOf(item.get(position).getDefaulPrice())));
        holder.txtItemSubtotal.setText("Rp. " + CurencyFormat(String.valueOf(item.get(position).getAmount())));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.##";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemCode, txtItemDesc, txtItemQty, txtItemPrice, txtItemSubtotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemCode = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailsp_txtitemname);
            txtItemDesc = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailsp_txtitemdesc);
            txtItemQty  = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailsp_txtitemqty);
            txtItemPrice= (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailsp_titleharga);
            txtItemSubtotal = (UniversalFontTextView) itemView.findViewById(R.id.recycler_detailsp_titlesubtotal);
        }
    }
}
