package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_home_category;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_homecategory extends RecyclerView.Adapter<Adapter_homecategory.ViewHolder_Homecategory>{

    private Context context;
    private List<Data_home_category> list;

    public Adapter_homecategory(Context context, List<Data_home_category> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Adapter_homecategory.ViewHolder_Homecategory onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_homecategory_item, parent, false);
        ViewHolder_Homecategory viewHolder_homecategory = new ViewHolder_Homecategory(view);
        return viewHolder_homecategory;
    }

    @Override
    public void onBindViewHolder(ViewHolder_Homecategory holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        Picasso.with(context).load(list.get(position).getImage()).placeholder(R.drawable.bumper).into(holder.image);

        /* On Image click
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Position : " + position, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder_Homecategory extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView image;
        UniversalFontTextView title;

        private ViewHolder_Homecategory(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.recy_homecategory_linearlayout);
            image        = (ImageView) itemView.findViewById(R.id.recy_homecategory_image);
            title        = (UniversalFontTextView) itemView.findViewById(R.id.recy_homecategory_title);
        }
    }
}
