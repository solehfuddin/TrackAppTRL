package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_framesp extends RecyclerView.Adapter<Adapter_framesp.ViewHolder> {

    private Context context;
    private List<Data_fragment_bestproduct> item;
    private RecyclerViewOnClickListener itemClick;

    private int checkPos = -1;

    public Adapter_framesp(Context context, List<Data_fragment_bestproduct> item, RecyclerViewOnClickListener onClick) {
        this.context = context;
        this.item = item;
        this.itemClick = onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_framesp, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Picasso.with(context).load(item.get(i).getProduct_image()).into(viewHolder.img_product);
        viewHolder.txt_productid.setText(item.get(i).getProduct_id());
        viewHolder.txt_productname.setText(item.get(i).getProduct_name());
        viewHolder.txt_brand.setText(item.get(i).getProduct_brand());
        viewHolder.txt_realprice.setText(item.get(i).getProduct_realprice());

        int discount = Integer.parseInt(item.get(i).getProduct_discpercent());
        if (discount > 0)
        {
            String disc = String.valueOf(discount) + " %";
            viewHolder.txt_discpercent.setVisibility(View.VISIBLE);
            viewHolder.txt_discpercent.setText(disc);
        }
        else
        {
            viewHolder.txt_discpercent.setVisibility(View.GONE);
        }

        int qty = Integer.parseInt(item.get(i).getProduct_qty());
        if (qty > 0)
        {
            String tersedia = "Stok Tersedia";
            viewHolder.txt_available.setText(tersedia);
            viewHolder.txt_available.setTextColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            String habis = "Stok Habis";
            viewHolder.txt_available.setText(habis);
            viewHolder.txt_available.setTextColor(Color.parseColor("#f90606"));
        }

        if (checkPos == -1)
        {
            viewHolder.img_select.setVisibility(View.INVISIBLE);
            viewHolder.linear_select.setBackgroundResource(R.drawable.border_black);
        }
        else
        {
            if (checkPos == i)
            {
                viewHolder.img_select.setVisibility(View.VISIBLE);
                viewHolder.linear_select.setBackgroundResource(R.drawable.border_select);
            }
            else
            {
                viewHolder.img_select.setVisibility(View.INVISIBLE);
                viewHolder.linear_select.setBackgroundResource(R.drawable.border_black);
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }
//
//    private String removeRupiah(String price)
//    {
//        String output;
//        output = price.replace("Rp ", "").replace("Rp", "").replace(".","").trim();
//
//        return output;
//    }
//
//    private String removeDiskon(String diskon)
//    {
//        String output;
//        output = diskon.replace("%", "").trim();
//
//        return output;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img_product, img_select;
        UniversalFontTextView txt_productid, txt_discpercent, txt_productname, txt_realprice, txt_brand,
                txt_available;
        LinearLayout linear_select;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            linear_select = itemView.findViewById(R.id.list_itemframesp_linearSelect);
            img_product   = itemView.findViewById(R.id.list_itemframesp_imgProduct);
            img_select    = itemView.findViewById(R.id.list_itemframesp_imgSelect);
            txt_productid = itemView.findViewById(R.id.list_itemframesp_txtProductId);
            txt_discpercent = itemView.findViewById(R.id.list_itemframesp_txtDiscPercent);
            txt_productname = itemView.findViewById(R.id.list_itemframesp_txtProductName);
            txt_realprice = itemView.findViewById(R.id.list_itemframesp_txtProductPrice);
            txt_brand     = itemView.findViewById(R.id.list_itemframesp_txtBrand);
            txt_available = itemView.findViewById(R.id.list_itemframesp_txtProductAvail);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txt_productid.getText().toString());

            img_select.setVisibility(View.VISIBLE);
            linear_select.setBackgroundResource(R.drawable.border_select);
            if (checkPos != getAdapterPosition())
            {
                notifyItemChanged(checkPos);
                checkPos = getAdapterPosition();
            }
        }
    }
}
