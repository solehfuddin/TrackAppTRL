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

import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.Data.Data_frame_brand;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_framesp_qty extends RecyclerView.Adapter<Adapter_framesp_qty.ViewHolder> {
    private Context context;
    private List<Data_fragment_bestproduct> item;
    private RecyclerViewOnClickListener itemClick;

    private int checkPos = -1;

    public Adapter_framesp_qty(Context context, List<Data_fragment_bestproduct> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;

        UniversalFontComponents.init(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data_fragment_bestproduct dt = item.get(i);

        viewHolder.txtItemId.setText(dt.getProduct_id());
        viewHolder.txtItemCode.setText(dt.getProduct_code());
        viewHolder.txtItemName.setText(dt.getProduct_name());
        viewHolder.txtItemImg.setText(dt.getProduct_image());
        viewHolder.txtItemPrice.setText(dt.getProduct_realprice());
        viewHolder.txtItemDisc.setText(dt.getProduct_discprice());
        viewHolder.txtItemBrand.setText(dt.getProduct_brand());
        viewHolder.txtCounter.setText(dt.getProduct_qty());
        viewHolder.txtItemWeight.setText(dt.getProduct_weight());

        int count = Integer.valueOf(dt.getProduct_qty());
        if (count > 0)
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#f90606"));
        }

        if (checkPos == -1) {
            viewHolder.imgSelect.setVisibility(View.INVISIBLE);
            viewHolder.linearSelect.setBackgroundResource(R.drawable.border_black);
        }
        else
        {
            if (checkPos == i) {
                viewHolder.imgSelect.setVisibility(View.VISIBLE);
                viewHolder.linearSelect.setBackgroundResource(R.drawable.border_select);
            }
            else
            {
                viewHolder.imgSelect.setVisibility(View.INVISIBLE);
                viewHolder.linearSelect.setBackgroundResource(R.drawable.border_black);
            }
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtItemId, txtItemCode, txtItemName, txtItemImg, txtItemPrice, txtItemDisc,
                                txtItemBrand, txtCounter, txtItemWeight;
        ImageView imgSelect;
        LinearLayout linearSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemId   = itemView.findViewById(R.id.recycler_onhand_txtItemId);
            txtItemName = itemView.findViewById(R.id.recycler_onhand_txtItemCode);
            txtItemCode = itemView.findViewById(R.id.recycler_onhand_txtItemName);
            txtItemImg  = itemView.findViewById(R.id.recycler_onhand_txtItemImg);
            txtItemPrice= itemView.findViewById(R.id.recycler_onhand_txtItemPrice);
            txtItemDisc = itemView.findViewById(R.id.recycler_onhand_txtItemDisc);
            txtItemBrand= itemView.findViewById(R.id.recycler_onhand_txtItemBrand);
            txtCounter  = itemView.findViewById(R.id.recycler_onhand_txtCounter);
            txtItemWeight = itemView.findViewById(R.id.recycler_onhand_txtItemWeight);
            imgSelect   = itemView.findViewById(R.id.recycler_onhand_imgSelect);
            linearSelect= itemView.findViewById(R.id.recycler_onhand_linearselect);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txtItemId.getText().toString());

            imgSelect.setVisibility(View.VISIBLE);
            linearSelect.setBackgroundResource(R.drawable.border_select);
            if (checkPos != getAdapterPosition()) {
                notifyItemChanged(checkPos);
                checkPos = getAdapterPosition();
            }
        }
    }
}
