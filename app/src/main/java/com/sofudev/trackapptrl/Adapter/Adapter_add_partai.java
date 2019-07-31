package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.LocalDb.Model.ModelLensPartai;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class Adapter_add_partai extends RecyclerView.Adapter<Adapter_add_partai.ViewHolder> {
    private Context context;
    private List<ModelLensPartai> item;
    private RecyclerViewOnClickListener onClickListener;

    public Adapter_add_partai(Context context, List<ModelLensPartai> item, RecyclerViewOnClickListener onClickListener) {
        this.context = context;
        this.item = item;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_partai_product, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ModelLensPartai data = item.get(position);

        int discount = data.getProductDisc();

        Picasso.with(context).load(data.getProductImage()).resize(100, 100).into(holder.imgItem);
        holder.txtId.setText(String.valueOf(data.getProductId()));
        holder.txtName.setText(data.getProductDesc());
        holder.txtSph.setText(data.getPowerSph());
        holder.txtCyl.setText(data.getPowerCyl());
        holder.txtAdd.setText(data.getPowerAdd());
        holder.txtQty.setText(String.valueOf(data.getProductQty()));
        holder.txtSide.setText(data.getProductSide());
        holder.txtPrice.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductDiscPrice())));
        holder.txtPriceDisc.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.txtPriceDisc.setText("Rp. " + CurencyFormat(String.valueOf(data.getNewProductPrice())));

        if (discount > 0)
        {
            holder.txtDisc.setText(String.valueOf(discount + " % "));
        }
        else
        {
            holder.txtDisc.setVisibility(View.GONE);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgItem, btnMinus, btnPlus;
        UniversalFontTextView txtId, txtName, txtSph, txtCyl, txtAdd, txtQty, txtSide, txtPrice, txtDisc,
                txtPriceDisc, btnRemove;

        public ViewHolder(View itemView) {
            super(itemView);

            imgItem     = itemView.findViewById(R.id.item_partaiproduct_imageView);
            btnMinus    = itemView.findViewById(R.id.item_partaiproduct_btnMinus);
            btnPlus     = itemView.findViewById(R.id.item_partaiproduct_btnPlus);
            txtId       = itemView.findViewById(R.id.item_partaiproduct_txtId);
            txtName     = itemView.findViewById(R.id.item_partaiproduct_txtName);
            txtSph      = itemView.findViewById(R.id.item_partaiproduct_txtSph);
            txtCyl      = itemView.findViewById(R.id.item_partaiproduct_txtCyl);
            txtAdd      = itemView.findViewById(R.id.item_partaiproduct_txtAdd);
            txtQty      = itemView.findViewById(R.id.item_partaiproduct_txtQty);
            txtSide     = itemView.findViewById(R.id.item_partaiproduct_txtSide);
            txtPrice    = itemView.findViewById(R.id.item_partaiproduct_txtPrice);
            txtDisc     = itemView.findViewById(R.id.item_partaiproduct_txtDisc);
            txtPriceDisc= itemView.findViewById(R.id.item_partaiproduct_txtPriceDisc);
            btnRemove   = itemView.findViewById(R.id.item_partaiproduct_btnRemove);

            btnRemove.setOnClickListener(this);
            btnPlus.setOnClickListener(this);
            btnMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v, this.getLayoutPosition(), txtId.getText().toString());
        }
    }
}
