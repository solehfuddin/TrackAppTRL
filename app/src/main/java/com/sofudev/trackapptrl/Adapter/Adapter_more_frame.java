package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_more_frame extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int IS_LAST = 1;
    private static int NOT_LAST = 2;

    private Context context;
    private List<Data_fragment_bestproduct> item;
    private RecyclerViewOnClickListener itemClick;
    String TAG;

    public Adapter_more_frame(Context context, List<Data_fragment_bestproduct> item, RecyclerViewOnClickListener itemClick, String TAG) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == IS_LAST)
        {
            view = LayoutInflater.from(context).inflate(R.layout.see_more_vertical, parent, false);
            return new Adapter_more_frame.MoreViewHolder(view);
        }
        else
        {
            view = LayoutInflater.from(context).inflate(R.layout.more_item_frame, parent, false);
            return new Adapter_more_frame.SpesialViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == IS_LAST)
        {
            ((Adapter_more_frame.MoreViewHolder) holder).setData(item.get(position));
        }
        else
        {
            ((Adapter_more_frame.SpesialViewHolder) holder).setData(item.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (item.get(position).getProduct_brand().contains("kosong"))
        {
            return IS_LAST;
        }
        else
        {
            return NOT_LAST;
        }
    }

    class SpesialViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView imgProduct;
        TextView txtId;
        UniversalFontTextView txtDisc, txtBrandName, txtProductName, txtStock, txtPrice;

        public SpesialViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.special_item_imgproduct);
            txtId      = itemView.findViewById(R.id.special_item_txtidproduct);
            txtDisc    = itemView.findViewById(R.id.special_item_txtdiscproduct);
            txtBrandName = itemView.findViewById(R.id.special_item_txtbrandproduct);
            txtProductName = itemView.findViewById(R.id.special_item_txtnameproduct);
            txtStock   = itemView.findViewById(R.id.special_item_txtstockproduct);
            txtPrice   = itemView.findViewById(R.id.special_item_txtpriceproduct);

            itemView.setOnClickListener(this);
        }

        void setData(Data_fragment_bestproduct item)
        {
            Picasso.with(context).load(item.getProduct_image()).fit().into(imgProduct);
            String persen = "- " + item.getProduct_discpercent() +" %";

            txtId.setText(item.getProduct_id());
            txtPrice.setText(item.getProduct_realprice());
            txtProductName.setText(item.getProduct_name());
            txtBrandName.setText(item.getProduct_brand());
            txtDisc.setText(persen);

            if (TAG.contains("main"))
            {
                txtPrice.setVisibility(View.GONE);
                txtStock.setVisibility(View.VISIBLE);
            }
            else
            {
                txtStock.setVisibility(View.GONE);
                txtPrice.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), txtId.getText().toString());
        }
    }

    class MoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ConstraintLayout layout;
        String data;

        public MoreViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.see_more_layout);

            itemView.setOnClickListener(this);
        }

        void setData(Data_fragment_bestproduct item)
        {
            data = item.getProduct_code();
        }

        @Override
        public void onClick(View v) {
            itemClick.onItemClick(v, this.getLayoutPosition(), data);
        }
    }
}
