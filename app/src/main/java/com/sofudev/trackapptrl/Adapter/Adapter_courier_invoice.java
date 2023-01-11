package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.MultipleSelectDpodk;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class Adapter_courier_invoice extends RecyclerView.Adapter<Adapter_courier_invoice.ViewHolder> {
    private Context context;
    private List<Data_courier> list;
    ArrayList<Data_courier> returnList = new ArrayList<>();
    private RecyclerViewOnClickListener itemClick;
    MultipleSelectDpodk callback;
    private int status;
    private int dpodkMode;

    public Adapter_courier_invoice(Context context, List<Data_courier> list, int status, int dpodkMode, RecyclerViewOnClickListener itemClick, MultipleSelectDpodk callback) {
        this.context = context;
        this.list = list;
        this.itemClick = itemClick;
        this.status = status;
        this.dpodkMode = dpodkMode;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_courier_task_invoice, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtInvoiceNumber.setText(list.get(position).getNo_inv());
        holder.txtDpodkNumber.setText("Nomor Dpodk : " + list.get(position).getNo_trx());
        holder.txtNamaOptic.setText(list.get(position).getNama_optik());

        String sts;
        if (status == 0)
        {
            sts = "Diproses kurir";
        }
        else
        {
            if (list.get(position).getStatus().equals("TERKIRIM"))
            {
                String penerima = "";
                if (list.get(position).getNama_penerima().equals("null"))
                {
                    penerima = "-";
                }
                else
                {
                    penerima = list.get(position).getNama_penerima();
                }

                sts = "DITERIMA : " + penerima;

                holder.txtStatus.setTextColor(Color.parseColor("#45ac2d"));
                holder.txtStatus.setTypeface(null, Typeface.BOLD);
            }
            else
            {
                if (list.get(position).getStatus().equals("null"))
                {
                    sts = "Sedang Diproses Kurir";

                    holder.txtStatus.setTextColor(Color.parseColor("#f90606"));
                }
                else
                {
                    String tmpSts = list.get(position).getStatus() + " : " + list.get(position).getNote_opd().toUpperCase();

                    if (tmpSts.length() >= 23)
                    {
                        sts = tmpSts.concat("...");
                    }
                    else
                    {
                        sts = tmpSts;
                    }

                    holder.txtStatus.setTextColor(Color.parseColor("#ff9100"));
                }
                holder.txtStatus.setTypeface(null, Typeface.BOLD);
            }

            holder.txtStatusTitle.setVisibility(View.GONE);
        }

        holder.txtStatus.setText(sts);
        holder.txtTanggal.setText(list.get(position).getTgl_kirim());
        holder.txtTotalInv.setText(CurencyFormat(list.get(position).getTotal_inv().replace(",", ".")));

        if (dpodkMode == 0)
        {
            if (status == 0)
            {
                holder.cbSelector.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.cbSelector.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.cbSelector.setVisibility(View.GONE);
        }

        holder.cbSelector.setOnCheckedChangeListener(null);

        holder.cbSelector.setChecked(list.get(position).isChecked());

        if (returnList.isEmpty())
        {
            holder.cbSelector.setChecked(false);
        }

        holder.cbSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    String namaOptik = list.get(position).getNama_optik();
                    boolean cek = checkedOptik(returnList, namaOptik);

                    if (cek)
                    {
                        list.get(position).setChecked(true);
                        returnList.add(list.get(position));
                    }
                    else
                    {
                        Toasty.warning(context, "Harap pilih optik yang sama", Toast.LENGTH_SHORT).show();
                        list.get(position).setChecked(false);
                        holder.cbSelector.setChecked(false);
                    }
                }
                else
                {
                    returnList.remove(list.get(position));
                    list.get(position).setChecked(false);
                }

                callback.passResultData(returnList);
            }
        });

        int drawable;

        if (status == 0)
        {
            drawable = R.drawable.ic_package;
        }
        else
        {
            drawable = R.drawable.delivered_courier;
        }
        holder.imgIcon.setImageResource(drawable);
    }

    private boolean checkedOptik(ArrayList<Data_courier> list, String namaOptik){
        for (int i = 0; i < list.size(); i++)
        {
            Log.d(Adapter_courier_track.class.getSimpleName(), "Nama Optik [" + i + "] : " + list.get(i).getNama_optik());
            if (!list.get(i).getNama_optik().equals(namaOptik))
            {
                return false;
            }
        }

        return true;
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.##";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    public void removeTemp(){
        returnList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        UniversalFontTextView txtDpodkNumber, txtInvoiceNumber, txtNamaOptic, txtStatus, txtStatusTitle, txtTanggal, txtTotalInv;
        ImageView imgIcon;
        CheckBox cbSelector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDpodkNumber      = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txtdpodknumber);
            txtInvoiceNumber    = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txtinvnumber);
            txtNamaOptic        = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txtto);
            txtStatus           = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txtstatus);
            txtStatusTitle      = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txttitlestatus);
            txtTanggal          = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txttitletgl);
            txtTotalInv         = (UniversalFontTextView) itemView.findViewById(R.id.recycler_courierinv_txttotalinv);
            imgIcon             = (ImageView) itemView.findViewById(R.id.recycler_courierinv_icpackage);
            cbSelector          = (CheckBox) itemView.findViewById(R.id.recycler_courierinv_check);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(view, getLayoutPosition(), list.get(getLayoutPosition()).getNo_inv());
        }
    }
}
