package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_home_product;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter_homeproduct extends RecyclerView.Adapter<Adapter_homeproduct.ViewHolder_homeproduct>{
    private Context context;
    private List<Data_home_product> list;

    public Adapter_homeproduct(Context context, List<Data_home_product> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Adapter_homeproduct.ViewHolder_homeproduct onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_homeproduct_item, parent, false);
        ViewHolder_homeproduct viewHolder_homeproduct = new ViewHolder_homeproduct(view);
        return viewHolder_homeproduct;
    }

    @Override
    public void onBindViewHolder(ViewHolder_homeproduct holder, int position) {
        holder.txt_title.setText(list.get(position).getTitle());
        Picasso.with(context).load(list.get(position).getImage()).placeholder(R.drawable.bumper).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder_homeproduct extends RecyclerView.ViewHolder{

        ImageView img;
        UniversalFontTextView txt_title;

        private ViewHolder_homeproduct(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.recy_homeproduct_image);
            txt_title = (UniversalFontTextView) itemView.findViewById(R.id.recy_homeproduct_txttitle);
        }
    }
}
