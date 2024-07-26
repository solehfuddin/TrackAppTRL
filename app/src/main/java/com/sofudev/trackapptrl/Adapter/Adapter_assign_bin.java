package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_assignbin_header;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_assign_bin extends RecyclerView.Adapter<Adapter_assign_bin.ViewHolder> {
    private Context context;
    private List<Data_assignbin_header> list;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_assign_bin(Context context, List<Data_assignbin_header> list, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_assign_bin, parent, false);
        return new Adapter_assign_bin.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_assignbin_header item = list.get(position);

        holder.txtAssignId.setText(item.getAssignId());
        holder.txtAssignDate.setText(item.getAssignDate());

        switch (item.getAssignFlag())
        {
            case "UNSEEN" :
                holder.imgIcon.setImageResource(R.drawable.assignbin);
                holder.txtAssignFlag.setText("Belum Dikonfirmasi");
                holder.txtAssignFlag.setTextColor(Color.parseColor("#ff9100"));
                break;
            case "RETUR" :
                holder.imgIcon.setImageResource(R.drawable.return_bin);
                holder.txtAssignFlag.setText("Belum Dikonfirmasi");
                holder.txtAssignFlag.setTextColor(Color.parseColor("#ff9100"));
                break;
            case "SEEN" :
                holder.imgIcon.setImageResource(R.drawable.assignbin);
                holder.txtAssignFlag.setText("Sudah Dikonfirmasi");
                holder.txtAssignFlag.setTextColor(Color.parseColor("#14a895"));
                break;
            case "REJECT":
                holder.imgIcon.setImageResource(R.drawable.return_bin);
                holder.txtAssignFlag.setText("Tidak Disetujui Admin Gudang");
                holder.txtAssignFlag.setTextColor(Color.parseColor("#f90606"));
                break;
            default:
                holder.imgIcon.setImageResource(R.drawable.return_bin);
                holder.txtAssignFlag.setText("Sudah Dikonfirmasi");
                holder.txtAssignFlag.setTextColor(Color.parseColor("#14a895"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RippleView rippleView;
        UniversalFontTextView txtAssignId, txtAssignSales, txtAssignDate, txtAssignFlag, txtAssignBy;
        ImageView imgIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rippleView = itemView.findViewById(R.id.recycler_assignbin_ripple);
            txtAssignId = itemView.findViewById(R.id.recycler_assignbin_txtinvnumber);
            txtAssignSales = itemView.findViewById(R.id.recycler_assignbin_txtnamasales);
            txtAssignDate = itemView.findViewById(R.id.recycler_assignbin_txttglsp);
            txtAssignFlag = itemView.findViewById(R.id.recycler_assignbin_txtstatus);
            txtAssignBy = itemView.findViewById(R.id.recycler_assignbin_txtlaststatus);
            imgIcon     = itemView.findViewById(R.id.recycler_assignbin_icpackage);

            rippleView.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), list.get(getLayoutPosition()).getAssignId());
        }
    }
}
