package com.sofudev.trackapptrl.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Data.Data_ewarranty;
import com.sofudev.trackapptrl.R;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class Adapter_ewarranty extends RecyclerView.Adapter<Adapter_ewarranty.ViewHolder> {

    private Context context;
    private List<Data_ewarranty> item;

    public Adapter_ewarranty(Context context, List<Data_ewarranty> item) {
        this.context = context;
        this.item = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ewarranty, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Data_ewarranty data_ewarranty = item.get(position);

        holder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasty.info(context, data_ewarranty.getOrderNumber(), Toast.LENGTH_SHORT).show();
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.timurrayalab.com/dev/Warranty_check_detail/Warranty_check_detail/pdf/" + data_ewarranty.getOrderNumber()));
                context.startActivity(openBrowser);
            }
        });

        holder.txtOrderNumber.setText(data_ewarranty.getOrderNumber());
        holder.txtNamaPasien.setText(data_ewarranty.getName());
        holder.txtPhoneNumber.setText(data_ewarranty.getPhone());
        holder.txtOpticName.setText(data_ewarranty.getOpticName());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btnDownload;
        UniversalFontTextView txtOrderNumber, txtNamaPasien, txtPhoneNumber, txtOpticName;

        public ViewHolder(View itemView) {
            super(itemView);

            btnDownload     = itemView.findViewById(R.id.item_ewarranty_btnDownload);
            txtOrderNumber  = itemView.findViewById(R.id.item_ewarranty_txtordernumber);
            txtNamaPasien   = itemView.findViewById(R.id.item_ewarranty_txtName);
            txtPhoneNumber  = itemView.findViewById(R.id.item_ewarranty_txtphonenumber);
            txtOpticName    = itemView.findViewById(R.id.item_ewarranty_txtopticname);
        }
    }
}
