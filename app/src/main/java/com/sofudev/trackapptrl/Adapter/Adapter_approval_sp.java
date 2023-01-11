package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_sp_header;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_approval_sp extends RecyclerView.Adapter<Adapter_approval_sp.ViewHolder> {
    private Context context;
    private List<Data_sp_header> list;
    private RecyclerViewOnClickListener itemClick;

    public Adapter_approval_sp(Context context, List<Data_sp_header> list, RecyclerViewOnClickListener itemClick) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_approval_sp, parent, false);
        return new Adapter_approval_sp.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtNomorSp.setText(list.get(position).getNoSp());
        holder.txtTipeSp.setText(list.get(position).getTipeSp());
        holder.txtNamaSales.setText(list.get(position).getSales());
        holder.txtTanggal.setText(list.get(position).getDate());
        holder.txtCustname.setText(list.get(position).getCustomerName());
        holder.txtAlamat.setText(list.get(position).getAddress());

        String sts = list.get(position).getStatus();
        String approvalName = list.get(position).getApprovalName() == null ? "-" : list.get(position).getApprovalName();

        if (sts.equals("AM"))
        {
            holder.txtStatus.setText("Sudah disetujui sales area (" + approvalName.toUpperCase() + ")");
            holder.txtStatus.setTextColor(Color.parseColor("#14a895"));
        }
        else if (sts.equals("REJECT AM"))
        {
            holder.txtStatus.setText("Tidak disetujui sales area");
            holder.txtStatus.setTextColor(Color.parseColor("#f64c73"));
        }
        else
        {
            holder.txtStatus.setText("Menunggu konfirmasi sales area");
            holder.txtStatus.setTextColor(Color.parseColor("#f64c73"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtNomorSp, txtTipeSp, txtNamaSales, txtTanggal, txtCustname, txtAlamat, txtStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNomorSp = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txtspnumber);
            txtTipeSp  = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txttipesp);
            txtNamaSales = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txtnamasales);
            txtTanggal = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txttglsp);
            txtCustname= (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txtnamaoptik);
            txtAlamat  = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txtalamatoptik);
            txtStatus  = (UniversalFontTextView) itemView.findViewById(R.id.recycler_spapproval_txtstatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), list.get(getLayoutPosition()).getNoSp());
        }
    }
}
