package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.LocalDb.Model.ModelAddCart;
import com.sofudev.trackapptrl.LocalDb.Model.ModelFrameSp;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_add_framesp extends RecyclerView.Adapter<Adapter_add_framesp.ViewHolder> {
    private Context context;
    private List<ModelFrameSp> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_add_framesp(Context context, List<ModelFrameSp> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, viewGroup, false);
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_framesp, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final ModelFrameSp data = item.get(i);

//        Picasso.with(context).load(data.getProductImage()).resize(100, 100).into(holder.img_product);
        holder.txt_productid.setText(String.valueOf(data.getProductId()));
        holder.txt_productname.setText(data.getProductName());
        holder.txt_productqty.setText(String.valueOf(data.getProductQty()));
        holder.txt_productprice.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductDiscPrice())));

        int stock = data.getProductStock();
        if (stock < 0)
        {
            holder.txt_stockkurang.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.txt_stockkurang.setVisibility(View.GONE);
        }

        int disc = data.getProductDisc();
        if (disc > 0)
        {
            holder.txt_productdisc.setText(String.valueOf(data.getProductDisc() + " % "));
            holder.txt_producdiscprice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txt_producdiscprice.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductPrice())));
//            holder.txt_productdisc.setVisibility(View.GONE);
//            holder.txt_producdiscprice.setVisibility(View.GONE);
        }
        else
        {
            holder.txt_productdisc.setVisibility(View.GONE);
            holder.txt_producdiscprice.setVisibility(View.GONE);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_product, btn_plus, btn_minus;
        UniversalFontTextView txt_productname, txt_productid, txt_productqty, txt_productprice, txt_productdisc,
                txt_producdiscprice, txt_stockkurang, btn_remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.item_cartframesp_imageView);
            btn_plus = itemView.findViewById(R.id.item_cartframesp_btnPlus);
            btn_minus= itemView.findViewById(R.id.item_cartframesp_btnMinus);
            txt_productname = itemView.findViewById(R.id.item_cartframesp_txtName);
            txt_productid = itemView.findViewById(R.id.item_cartframesp_txtId);
            txt_productqty = itemView.findViewById(R.id.item_cartframesp_txtQty);
            txt_productprice = itemView.findViewById(R.id.item_cartframesp_txtPrice);
            txt_producdiscprice = itemView.findViewById(R.id.item_cartframesp_txtPriceDisc);
            txt_productdisc = itemView.findViewById(R.id.item_cartframesp_txtDisc);
            txt_stockkurang = itemView.findViewById(R.id.item_cartframesp_lblStockKurang);
            btn_remove = itemView.findViewById(R.id.item_cartframesp_btnRemove);

            btn_remove.setOnClickListener(this);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txt_productid.getText().toString());
        }
    }
}
