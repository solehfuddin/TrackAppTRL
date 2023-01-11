package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.MultiSelectSpFrame;
import com.sofudev.trackapptrl.Data.Data_fragment_bestproduct;
import com.sofudev.trackapptrl.R;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_framesp_multi extends RecyclerView.Adapter<Adapter_framesp_multi.ViewHolder> {
    private Context context;
    private List<Data_fragment_bestproduct> item;
    private ArrayList<Data_fragment_bestproduct> returnItem = new ArrayList<>();
    private MultiSelectSpFrame callback;

    public Adapter_framesp_multi(Context context, List<Data_fragment_bestproduct> item, MultiSelectSpFrame callback) {
        this.context = context;
        this.item = item;
        this.callback = callback;

        UniversalFontComponents.init(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand, parent, false);

        return new Adapter_framesp_multi.ViewHolder(v);
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

        int count = Integer.parseInt(dt.getProduct_qty());
        if (count > 0)
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#f90606"));
        }

        viewHolder.imgSelect.setVisibility(item.get(i).isChecked() ? View.VISIBLE : View.INVISIBLE);
        viewHolder.linearSelect.setBackgroundResource(item.get(i).isChecked() ? R.drawable.border_select : R.drawable.border_black);

        viewHolder.bind(item.get(i));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        }

        void bind(final Data_fragment_bestproduct item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Integer.parseInt(item.getProduct_qty()) > 0)
                    {
                        item.setChecked(!item.isChecked());
                        imgSelect.setVisibility(item.isChecked() ? View.VISIBLE : View.INVISIBLE);
                        linearSelect.setBackgroundResource(item.isChecked() ? R.drawable.border_select : R.drawable.border_black);
                        if (item.isChecked()){
                            returnItem.add(item);
                        }
                        else
                        {
                            returnItem.remove(item);
                        }
                    }
                    else
                    {
                        Toasty.warning(context, "Item tidak tersedia saat ini", Toast.LENGTH_SHORT).show();
                    }

                    callback.passResult(returnItem);
                }
            });
        }
    }
}
