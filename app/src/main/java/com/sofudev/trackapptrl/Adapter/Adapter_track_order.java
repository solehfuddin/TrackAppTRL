package com.sofudev.trackapptrl.Adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.CustomOnClickListener;
import com.sofudev.trackapptrl.Data.Data_track_order;
import com.sofudev.trackapptrl.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

public class Adapter_track_order extends RecyclerView.Adapter<Adapter_track_order.ViewHolder>{

    //Context context;
    private RecyclerView recyclerView;
    private List<Data_track_order> list;
    private CustomOnClickListener listener;
    private int start = -1;
    private int selectedItem = start;

    public Adapter_track_order(RecyclerView recyclerView, List<Data_track_order> list,
                               CustomOnClickListener listener) {
        //this.context = context;
        this.recyclerView = recyclerView;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_track_order, parent, false);

        final ViewHolder viewHolder = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;

        String order_no  = "Job Number #" + list.get(position).getOrder_number();
        String date_time = list.get(position).getOrder_statusdate() + " " + list.get(position).getOrder_statustime();

        holder.txt_jobnumber.setText(order_no);
        holder.txt_entrydate.setText(list.get(position).getOrder_entrydate());
        holder.txt_custname.setText(list.get(position).getOrder_custname());
        holder.txt_lenscode.setText(list.get(position).getOrder_lenscode());
        holder.txt_reference.setText(list.get(position).getOrder_reference());
        holder.txt_tint.setText(list.get(position).getOrder_tint_descr());
        holder.txt_status.setText(list.get(position).getOrder_status());
        holder.txt_datestatus.setText(date_time);

        holder.txt_sphr.setText(list.get(position).getOrder_sphr());
        holder.txt_cylr.setText(list.get(position).getOrder_cylr());
        holder.txt_addr.setText(list.get(position).getOrder_addr());
        holder.txt_sphl.setText(list.get(position).getOrder_sphl());
        holder.txt_cyll.setText(list.get(position).getOrder_cyll());
        holder.txt_addl.setText(list.get(position).getOrder_addl());

        holder.txt_facet.setText(list.get(position).getOrder_facet());

        if (holder.txt_facet.getText().equals("F") || holder.txt_facet.getText().equals("N"))
        {
            holder.txt_facet.setText("NON FACET");
            holder.txt_facet.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        }
        else
        {
            holder.txt_facet.setText("FACET TIMUR RAYA");
            holder.txt_facet.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
        }

        holder.rl_header.setSelected(false);
        //holder.expandableLayout.collapse(false);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {

        ExpandableLayout expandableLayout;
        RelativeLayout rl_header;
        //ImageView img_header;
        TextView txt_jobnumber, txt_entrydate, txt_custname, txt_lenscode, txt_reference, txt_tint, txt_status,
        txt_datestatus;
        BootstrapLabel txt_facet;
        UniversalFontTextView txt_sphr, txt_cylr, txt_addr, txt_sphl, txt_cyll, txt_addl;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);

            expandableLayout= (ExpandableLayout) itemView.findViewById(R.id.recy_trackorder_expandable_layout);
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);

            txt_jobnumber   = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_jobnumber);
            txt_entrydate   = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_entrydate);
            txt_custname    = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_custname);
            txt_lenscode    = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_lenscode);
            txt_reference   = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_reference);
            txt_tint        = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_tinting);
            txt_status      = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_orderstatus);
            txt_datestatus  = (TextView) itemView.findViewById(R.id.recy_trackorder_txt_datestatus);

            txt_sphr        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_sphr);
            txt_cylr        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_cylr);
            txt_addr        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_addr);
            txt_sphl        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_sphl);
            txt_cyll        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_cyll);
            txt_addl        = (UniversalFontTextView) itemView.findViewById(R.id.recy_trackorder_txt_addl);

            txt_facet       = (BootstrapLabel) itemView.findViewById(R.id.recy_trackorder_txt_facet);

            rl_header       = (RelativeLayout) itemView.findViewById(R.id.recy_trackorder_rl_header);
            //img_header      = (ImageView) itemView.findViewById(R.id.recy_trackorder_img_header);

            rl_header.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(selectedItem);

            if (holder != null)
            {
                holder.rl_header.setSelected(false);
                //holder.expandableLayout.collapse();
                //holder.img_header.setImageResource(R.drawable.collapse_white);
            }

            if (position == selectedItem)
            {
                selectedItem = start;
            }
            else
            {
                rl_header.setSelected(true);
                //expandableLayout.expand();
                //img_header.setImageResource(R.drawable.expand_white);
                selectedItem = position;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state != ExpandableLayout.State.COLLAPSED)
            {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }
}
