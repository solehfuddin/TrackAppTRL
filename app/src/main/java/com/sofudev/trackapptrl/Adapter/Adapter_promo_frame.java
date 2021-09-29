package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_promo_frame extends RecyclerView.Adapter<Adapter_promo_frame.ViewHolder> {
    private Context context;
    private List<Data_fragment_bestproduct> item;
    private RecyclerViewOnClickListener itemClick;
    String TAG;

    public Adapter_promo_frame(Context context, List<Data_fragment_bestproduct> item, RecyclerViewOnClickListener itemClick, String TAG) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
        this.TAG = TAG;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.promo_item_frame, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(item.get(position));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgProduct;
        TextView txtId;
        UniversalFontTextView txtDisc, txtBrandName, txtProductName, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.promo_item_imgproduct);
            txtId = itemView.findViewById(R.id.promo_item_txtidproduct);
            txtDisc = itemView.findViewById(R.id.promo_item_txtdiscproduct);
            txtBrandName = itemView.findViewById(R.id.promo_item_txtbrandproduct);
            txtProductName = itemView.findViewById(R.id.promo_item_txtnameproduct);
            txtPrice = itemView.findViewById(R.id.promo_item_txtpriceproduct);

            itemView.setOnClickListener(this);
        }

        void setData(Data_fragment_bestproduct item)
        {
            Picasso.with(context).load(item.getProduct_image()).fit().into(imgProduct);
            String persen = "- " + item.getProduct_discpercent() + " %";

            if (item.getProduct_discpercent().equals("0"))
            {
                txtDisc.setVisibility(View.GONE);
            }
            else
            {
                txtDisc.setVisibility(View.VISIBLE);
                txtDisc.setText(persen);
            }

            txtId.setText(item.getProduct_id());
            txtPrice.setText(item.getProduct_realprice());
            txtProductName.setText(item.getProduct_name());
            txtBrandName.setText(item.getProduct_brand());

            if (TAG.contains("main"))
            {
                txtPrice.setVisibility(View.GONE);
            }
            else
            {
                txtPrice.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, this.getLayoutPosition(), txtId.getText().toString());
        }
    }
}
