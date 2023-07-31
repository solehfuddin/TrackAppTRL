package com.sofudev.trackapptrl.Adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomRecyclerOrderHistoryClick;
import com.sofudev.trackapptrl.Data.Data_orderhistory_optic;
import com.sofudev.trackapptrl.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_orderhistory_optic extends RecyclerView.Adapter<Adapter_orderhistory_optic.HistoryViewHolder> {
    private Context context;
    private List<Data_orderhistory_optic> item;
    private CustomRecyclerOrderHistoryClick itemClick;
    private String orderNumber;

    public Adapter_orderhistory_optic(Context context, List<Data_orderhistory_optic> item, CustomRecyclerOrderHistoryClick itemClick) {
        this.context = context;
        this.item = item;
        this.itemClick = itemClick;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ordersummary, parent, false);
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        Picasso.with(context).load(item.get(position).getIconOrder()).into(holder.img_icon);
        holder.txt_ordernumber.setText(item.get(position).getNomorOrder() + "/" + item.get(position).getNamaPasien());
        holder.txt_date.setText(item.get(position).getTanggalOrder());
        if (item.get(position).getUserLevel() == 3)
        {
            holder.txt_pricetotal.setText("Unavailable");
        }
        else
        {
            holder.txt_pricetotal.setText(item.get(position).getTotalBiaya());
        }

        String status = item.get(position).getStatusOrder();
        holder.txt_status.setText(status);

        if (status.equals("Pending") || status.contains("Pending") || status.contentEquals("Pending") ||
                status.contentEquals("PENDING") || status.equals("PENDING") || status.contains("PENDING"))
        {
            status = "Pending";
            holder.txt_status.setText(status);
            holder.txt_status.setTextColor(Color.parseColor("#ffcc0000"));
        }
        else if (status.equals("Cancel") || status.contains("Cancel") || status.contentEquals("Cancel"))
        {
            holder.txt_status.setText(status);
            holder.txt_status.setTextColor(Color.parseColor("#FF9100"));
        }
        else if (status.equals("FAILURE") || status.contains("FAILURE") || status.contentEquals("FAILURE"))
        {
            holder.txt_status.setText("Failure");
            holder.txt_status.setTextColor(Color.parseColor("#ffcc0000"));
        }
        else if (status.equals("Waiting Order to Complete") || status.contains("Waiting Order to Complete") || status.contentEquals("Waiting Order to Complete"))
        {
            holder.txt_status.setText("Waiting Complete");
            holder.txt_status.setTextColor(Color.parseColor("#ffcc0000"));
        }
        else if (status.equals("Waiting Payment Method") || status.contains("Waiting Payment Method") || status.contentEquals("Waiting Payment Method"))
        {
            holder.txt_status.setText("Waiting Payment");
            holder.txt_status.setTextColor(Color.parseColor("#ffcc0000"));
        }
        else
        {
            holder.txt_status.setTextColor(Color.parseColor("#33cc00"));
        }

        orderNumber = item.get(position).getNomorOrder();

        /*holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPayment(item.get(position).getNomorOrder());

                //Toast.makeText(context, item.get(position).getNomorOrder(), Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void cancelPayment(String id) {
        Config config = new Config();
        String url = config.Ip_address + config.payment_method_cancelBilling;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("responseCode");
                    //String respon = jsonObject.getString("responseDescription");

                    //Toast.makeText(context, status, Toast.LENGTH_SHORT).show();

                    if (status.equals("200") || status.contentEquals("200") || status.contains("200")) {
                        Toast.makeText(context, "Cancel success", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(context, "Cannot cancel payment billing", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Cancel", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        UniversalFontTextView txt_date, txt_ordernumber, txt_pricetotal, txt_status;
        ImageView img_icon;
        Button btn_cancel;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            txt_date        = (UniversalFontTextView) itemView.findViewById(R.id.recycle_ordersummary_txt_date);
            txt_ordernumber = (UniversalFontTextView) itemView.findViewById(R.id.recycle_ordersummary_txt_ordernumber);
            txt_pricetotal  = (UniversalFontTextView) itemView.findViewById(R.id.recycle_ordersummary_txt_pricetotal);
            txt_status      = (UniversalFontTextView) itemView.findViewById(R.id.recycle_ordersummary_txt_status);

            img_icon        = (ImageView) itemView.findViewById(R.id.recycler_ordersummary_img_icon);

            //btn_cancel      = (Button) itemView.findViewById(R.id.recycler_ordersummary_btn_cancel);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            itemClick.onItemClick(v, this.getLayoutPosition(), item.get(pos).getNomorOrder(), item.get(pos).getStatusOrder(),
                    item.get(pos).getNamaPasien(),
                    item.get(pos).getPaymentType());
        }
    }
}
