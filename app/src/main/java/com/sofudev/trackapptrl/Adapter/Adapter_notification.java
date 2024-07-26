package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_notification_item;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter_notification extends RecyclerView.Adapter<Adapter_notification.ViewHolder> {
    private Context context;
    private List<Data_notification_item> itemList;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_notification(Context context, List<Data_notification_item> itemList, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.itemList = itemList;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_notification_item item = itemList.get(position);

        holder.constraintImg.setVisibility(View.VISIBLE);

        holder.txtTipeImg.setText(item.getType());
        holder.txtWaktuImg.setText(item.getWaktu());
        holder.txtTitleImg.setText(item.getTitle());
        holder.txtMessageImg.setText(item.getMessage());

        //Validate Image URL Pattern
        if (!URLUtil.isValidUrl(item.getImage()))
        {
            holder.cardviewImg.setVisibility(View.GONE);
        }

        if (item.getIsClick().equals("1"))
        {
            holder.constraintImg.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if (item.getType().equals("Promo"))
        {
            holder.imageviewIconImg.setImageResource(R.drawable.promo);
        }
        else if (item.getType().equals("Transaksi"))
        {
            holder.imageviewIconImg.setImageResource(R.drawable.transaction);
        }
        else
        {
            holder.imageviewIconImg.setImageResource(R.drawable.info);
        }

        if (!item.getImage().isEmpty())
        {
            Picasso.with(context).load(item.getImage()).fit().centerCrop().error(R.drawable.logo_trl3).into(holder.imageviewBannerImg);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RippleView rippleView;
        ConstraintLayout constraintImg;
        CardView cardviewImg;
        ImageView imageviewIconImg, imageviewBannerImg;
        UniversalFontTextView txtTipeImg, txtWaktuImg, txtTitleImg, txtMessageImg;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Layout With Image
            rippleView          = itemView.findViewById(R.id.recycler_notificationImg_ripple);
            constraintImg       = itemView.findViewById(R.id.recycler_notificationImg_constraint);
            cardviewImg         = itemView.findViewById(R.id.recycler_notificationImg_cardimage);
            imageviewIconImg    = itemView.findViewById(R.id.recycler_notificationImg_icon);
            imageviewBannerImg  = itemView.findViewById(R.id.recycler_notificationImg_imgbanner);
            txtTipeImg          = itemView.findViewById(R.id.recycler_notificationImg_txttipe);
            txtWaktuImg         = itemView.findViewById(R.id.recycler_notificationImg_txtwaktu);
            txtTitleImg         = itemView.findViewById(R.id.recycler_notificationImg_txttitle);
            txtMessageImg       = itemView.findViewById(R.id.recycler_notificationImg_txtmessage);

            rippleView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), itemList.get(getLayoutPosition()).getRedirectTo());
        }
    }
}
