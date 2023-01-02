package com.sofudev.trackapptrl.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.vipulasri.timelineview.TimelineView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_sp_history;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_sp_history extends RecyclerView.Adapter<Adapter_sp_history.TimeLineViewHolder> {

    private List<Data_sp_history> list;
    private Context context;
    private DateFormat tglFormat = new DateFormat();

    public Adapter_sp_history( Context context, List<Data_sp_history> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_sp_history, viewGroup, false);
        return new TimeLineViewHolder(view, i);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder viewHolder, int i) {
        viewHolder.txtTipeSp.setText(list.get(i).getTipesp());
        viewHolder.txtDateTime.setText(tglFormat.Indotime(list.get(i).getDateout()));
        viewHolder.txtApprove.setText(list.get(i).getApprove());
        viewHolder.txtReject.setText(list.get(i).getReject());
        viewHolder.txtDuration.setText(list.get(i).getDurationunit());

        String status = list.get(i).getStatus();
        String approve = list.get(1).getApprove();
        String approvalName = list.get(i).getApprovalName() == null ? "-" : list.get(i).getApprovalName();

        if (approve.equals("null"))
        {
            String informasi = "SP Sudah Diajukan";
            viewHolder.txtApprove.setText(informasi);
        }
        else
        {
            viewHolder.txtReject.setVisibility(View.GONE);
        }

        switch (status) {
            case "AM":
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#17a2b8"));
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(0, 18, 0, 0);

                viewHolder.txtApprove.setText("Disetujui Sales Area (" + approvalName.toUpperCase() + ")");
                viewHolder.txtApprove.setTextSize(14f);
                viewHolder.txtApprove.setLayoutParams(lp1);
                viewHolder.txtDuration.setVisibility(View.GONE);
                viewHolder.imageView.setImageResource(R.drawable.img_am);
                break;
            case "SAM":
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#17a2b8"));
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 18, 0, 0);

                String statusSam = "PENGAJUAN SP";

                viewHolder.txtApprove.setText(statusSam);
                viewHolder.txtApprove.setTextSize(14f);
                viewHolder.txtApprove.setLayoutParams(lp);
                viewHolder.txtDuration.setVisibility(View.GONE);
//                viewHolder.timelineView.setMarker(ContextCompat.getDrawable(context, R.drawable.dot_orangelight));
                viewHolder.imageView.setImageResource(R.drawable.img_sam);
                break;
            case "AR":
                String statusAr = null;

                if (list.get(i).getApprove().equals("APPROVE"))
                {
                    statusAr = "PENGAJUAN DISETUJUI AR";
                    viewHolder.txtApprove.setTextColor(Color.parseColor("#45ac2d"));
                }
                else if (list.get(i).getApprove().equals("HOLD"))
                {
                    statusAr = "PENGAJUAN DITANGGUHKAN AR" + " (" + list.get(i).getReject() + ")";
                    viewHolder.txtApprove.setTextColor(Color.parseColor("#ff9100"));
                }
                else if (list.get(i).getApprove().equals("REJECT"))
                {
                    statusAr = "PENGAJUAN DITOLAK AR" + " (" + list.get(i).getReject() + ")";
                    viewHolder.txtApprove.setTextColor(Color.parseColor("#de002b"));
                }

                viewHolder.txtApprove.setText(statusAr);
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#28a745"));
                viewHolder.imageView.setImageResource(R.drawable.img_ar);
                break;
            case "WAREHOUSE":
                String statusLog = "PENGAMBILAN BARANG";
                viewHolder.txtApprove.setText(statusLog);
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#ffc107"));
                viewHolder.imageView.setImageResource(R.drawable.img_log);
                break;
            case "CS":
                String statusCs = "PROSES INVOICING";
                viewHolder.txtApprove.setText(statusCs);
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#dc3545"));
                viewHolder.imageView.setImageResource(R.drawable.img_cs);
                break;
            case "Shipping":
                String statusShip = "PENGIRIMAN BARANG";
                viewHolder.txtApprove.setText(statusShip);
                viewHolder.rlHeader.setBackgroundColor(Color.parseColor("#00cec9"));
                viewHolder.imageView.setImageResource(R.drawable.ic_shipping_moto);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        private UniversalFontTextView txtTipeSp, txtDateTime, txtApprove, txtReject, txtDuration;
        private ImageView imageView;
        private TimelineView timelineView;
//        private CardView cardView;
        private RelativeLayout rlHeader;

        private TimeLineViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            txtTipeSp       = itemView.findViewById(R.id.recy_sphistory_txt_tipesp);
            txtDateTime     = itemView.findViewById(R.id.recy_sphistory_txt_datetime);
            txtApprove      = itemView.findViewById(R.id.recy_sphistory_txtapprove);
            txtReject       = itemView.findViewById(R.id.recy_sphistory_txtreject);
            txtDuration     = itemView.findViewById(R.id.recy_sphistory_txtduration);
            imageView       = itemView.findViewById(R.id.recy_sphistory_imageview);
            timelineView    = itemView.findViewById(R.id.recy_sphistory_timeline);
//            cardView        = itemView.findViewById(R.id.recy_sphistory_cardview);
            rlHeader        = itemView.findViewById(R.id.recy_sphistory_rl_Header);

            timelineView.initLine(viewType);
        }
    }
}
