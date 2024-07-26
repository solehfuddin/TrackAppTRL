package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_master_onhand;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.ZoomImageActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_master_onhandframe extends RecyclerView.Adapter<Adapter_master_onhandframe.ViewHolder> {

    private Context context;
    private List<Data_master_onhand> item;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_master_onhandframe(Context context, List<Data_master_onhand> item, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;

        UniversalFontComponents.init(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_onhand_frame, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Data_master_onhand data = item.get(position);

        int count = Integer.parseInt(data.getQty());

        viewHolder.txtItemCode.setText(data.getItemCode());
        viewHolder.txtItemName.setText(data.getItemDesc());
        viewHolder.txtCounter.setText(data.getQty());

        Picasso.with(context).
                cancelRequest(viewHolder.imgChooser);
        if (item.get(position).getItemImg() != null)
        {
            if (!item.get(position).getItemImg().isEmpty())
            {
                viewHolder.setIsRecyclable(false);
                Picasso.with(context)
                        .load(item.get(position).getItemImg())
                        .fit()
                        .placeholder(R.drawable.progress_zoom)
                        .error(R.drawable.pic_holder)
                        .into(viewHolder.imgChooser);
            }
        }
        else
        {
            viewHolder.setIsRecyclable(true);
            viewHolder.imgChooser.setImageResource(R.drawable.pic_holder);
        }

        if (count > 0)
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#45ac2d"));
        }
        else
        {
            viewHolder.txtCounter.setBackgroundColor(Color.parseColor("#f90606"));
        }

        viewHolder.imgChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.getItemImg().isEmpty()) {
                    Intent intent = new Intent(context, ZoomImageActivity.class);
                    intent.putExtra("image_url", data.getItemImg());
                    intent.putExtra("frame_description", data.getItemDesc());
                    intent.putExtra("frame_id", data.getItemId());
                    context.startActivity(intent);
                }
                else
                {
                    Toasty.warning(context, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return item.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        UniversalFontTextView txtItemCode, txtItemName, txtCounter;
        ImageView imgChooser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtItemCode = itemView.findViewById(R.id.recycler_onhandframe_txtItemCode);
            txtItemName = itemView.findViewById(R.id.recycler_onhandframe_txtItemName);
            txtCounter  = itemView.findViewById(R.id.recycler_onhandframe_txtCounter);

            imgChooser  = itemView.findViewById(R.id.recycler_onhandframe_imgChooser);
        }
    }
}
