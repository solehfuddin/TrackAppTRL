package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_searchnow;
import com.sofudev.trackapptrl.R;

import java.util.List;

public class Adapter_searchnow extends RecyclerView.Adapter<Adapter_searchnow.ViewHolder> {

    Context context;
    List<Data_searchnow> item;
    private RecyclerViewOnClickListener onClickListener;

    public Adapter_searchnow(Context context, List<Data_searchnow> item, RecyclerViewOnClickListener onItemClick) {
        this.context = context;
        this.item = item;
        this.onClickListener = onItemClick;
    }

    @Override
    public Adapter_searchnow.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_searchnow, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_searchnow.ViewHolder holder, int position) {
        holder.txtKeyword.setText(item.get(position).getKeyword());
        holder.txtid.setText(String.valueOf(item.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        UniversalFontTextView txtKeyword, txtid;
        LinearLayout rl;
        ImageView imgSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            txtKeyword = itemView.findViewById(R.id.item_searchnow_txtkeyword);
            txtid = itemView.findViewById(R.id.item_searchnow_txtid);
            rl = itemView.findViewById(R.id.item_searchnow_layout);
            imgSelect  = itemView.findViewById(R.id.item_searchnow_btnselect);

            imgSelect.setOnClickListener(this);
            rl.setOnClickListener(this);

            rl.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        rl.setBackgroundColor(Color.parseColor("#93c6fd"));
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP)
                    {
                        rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                    else if (event.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        rl.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }

                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            onClickListener.onItemClick(v, this.getLayoutPosition(), txtid.getText().toString());
        }
    }
}
