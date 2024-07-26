package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_delivery_iteminv;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_einvoice;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Data.Data_item_einvoice;
import com.sofudev.trackapptrl.R;
import com.vipulasri.ticketview.TicketView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DetailEinvoiceActivity extends AppCompatActivity {

    private Config config = new Config();
    private String URLDETAIL = config.Ip_address + config.einvoice_getDetail;
    private String URLDETAILPAID = config.Ip_address + config.einvoicepaid_getDetail;
    private String TAG = DetailEinvoiceActivity.class.getSimpleName();

    TicketView ticketView;
    RelativeLayout rlInvNumber, rlPriceTotal, rlPurchaseOrder, rlInvDate, rlShipAmount, rlTotalAmount;
    UniversalFontTextView txtInvNumber, txtInvDate, txtTotalPrice, txtTotalAmount, txtPurchaseOrder,
            txtShipAmount, txtPaid, txtKeterangan, txtTitle;
    RippleView rpBack, rpDownload, rpViewPdf;
    ShimmerRecyclerView shimmer;
    RecyclerView recyclerView;

    DateFormat tglFormat = new DateFormat();
    String invNumber;
    Boolean isPaid = false;

    Adapter_detail_einvoice adapter_detail_einvoice;
    List<Data_item_einvoice> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UniversalFontComponents.init(this);
        setContentView(R.layout.activity_detail_einvoice);

        ticketView = findViewById(R.id.ticketView);
        txtTitle = findViewById(R.id.form_detaileinv_txttitle);
        txtInvNumber = findViewById(R.id.form_detaileinv_txtinvnumber);
        txtInvDate = findViewById(R.id.form_detaileinv_txtinvdate);
        txtPurchaseOrder = findViewById(R.id.form_detaileinv_txtpurchaseorder);
        txtTotalPrice = findViewById(R.id.form_detaileinv_txtpricetotal);
        txtTotalAmount = findViewById(R.id.form_detaileinv_txttotalamount);
        txtShipAmount = findViewById(R.id.form_detaileinv_txtshipamount);
        txtPaid = findViewById(R.id.form_detaileinv_txtpaid);
        txtKeterangan = findViewById(R.id.form_detaileinv_txtketerangan);
        rpBack = findViewById(R.id.form_detaileinv_ripplebtnback);
        rpViewPdf = findViewById(R.id.form_detaileinv_ripplebtnview);
        rpDownload = findViewById(R.id.form_detaileinv_ripplebtndownload);
        rlInvNumber = findViewById(R.id.form_detaileinv_progressinv);
        rlPriceTotal = findViewById(R.id.form_detaileinv_progresspricetotal);
        rlPurchaseOrder = findViewById(R.id.form_detaileinv_progressponumber);
        rlInvDate = findViewById(R.id.form_detaileinv_progressinvdate);
        rlShipAmount = findViewById(R.id.form_detaileinv_progressshipamount);
        rlTotalAmount = findViewById(R.id.form_detaileinv_progressstotalamount);
        shimmer = findViewById(R.id.form_einvoice_shimmerview);
        recyclerView = findViewById(R.id.form_detaileinv_recyclerview);

        shimmer.setDemoLayoutManager(ShimmerRecyclerView.LayoutMangerType.GRID);
        shimmer.showShimmerAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter_detail_einvoice = new Adapter_detail_einvoice(this, list);
        recyclerView.setAdapter(adapter_detail_einvoice);

        getDataIntent();

        if (isPaid) {
            txtPaid.setVisibility(View.VISIBLE);
            txtTitle.setText("Einvoice Lunas");
            ticketView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.ticket_einvoice_paid_height);
        } else {
            txtPaid.setVisibility(View.GONE);
            txtTitle.setText("Einvoice");
            ticketView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.ticket_einvoice_height);
        }

        rlInvNumber.setVisibility(View.VISIBLE);
        rlPriceTotal.setVisibility(View.VISIBLE);
        rlPurchaseOrder.setVisibility(View.VISIBLE);
        rlInvDate.setVisibility(View.VISIBLE);
        rlShipAmount.setVisibility(View.VISIBLE);
        rlTotalAmount.setVisibility(View.VISIBLE);
        txtInvNumber.setVisibility(View.GONE);
        txtTotalPrice.setVisibility(View.GONE);
        txtPurchaseOrder.setVisibility(View.GONE);
        txtInvDate.setVisibility(View.GONE);
        txtTotalAmount.setVisibility(View.GONE);
        txtShipAmount.setVisibility(View.GONE);

        rpBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rpViewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link;
                String title;

                if (isPaid){
                    link = "index.php/PrintReceipt/einvoicelunas/" + invNumber;
                    title = "Invoice Lunas #" + invNumber;
                }
                else
                {
                    link = "index.php/PrintReceipt/einvoice/" + invNumber;
                    title = "Invoice #" + invNumber;
                }

                Intent intent = new Intent(getApplicationContext(), FormPDFViewerActivity.class);
                intent.putExtra("data", config.Ip_address + link);
                intent.putExtra("title", title);
                intent.putExtra("source", "url");
                startActivity(intent);
            }
        });

        rpDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String link;
                if (isPaid){
                    link = "http://180.250.96.154/trl-webs/index.php/PrintReceipt/einvoicelunas/" + invNumber;
                }else
                {
                    link = "http://180.250.96.154/trl-webs/index.php/PrintReceipt/einvoice/" + invNumber;
                }

                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(openBrowser);
            }
        });
    }

    private void getDataIntent(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            invNumber = bundle.getString("INVNUMBER");
            isPaid = bundle.getBoolean("ispaid", false);
            Log.d(TAG, "USERNAME : " + invNumber);
            Log.d(TAG, "ISPAID : " + isPaid);

            getDetailInv(invNumber);
        }
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0,00";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void getDetailInv(final String invNumber){
        list.clear();
        String url;
        if (isPaid){
            url = URLDETAILPAID;
        }else {
            url = URLDETAIL;
        }

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);

                        String free = "Free";

                        if (object.names().get(0).equals("invalid")){
                            Log.d(TAG, "Data tidak ditemukan");

                            rlInvNumber.setVisibility(View.GONE);
                            rlPriceTotal.setVisibility(View.GONE);
                            rlPurchaseOrder.setVisibility(View.GONE);
                            rlInvDate.setVisibility(View.GONE);
                            rlShipAmount.setVisibility(View.GONE);
                            rlTotalAmount.setVisibility(View.GONE);
                            txtInvNumber.setVisibility(View.VISIBLE);
                            txtTotalPrice.setVisibility(View.VISIBLE);
                            txtPurchaseOrder.setVisibility(View.VISIBLE);
                            txtInvDate.setVisibility(View.VISIBLE);
                            txtTotalAmount.setVisibility(View.VISIBLE);
                            txtShipAmount.setVisibility(View.VISIBLE);

                            txtInvNumber.setText("-");
                            txtTotalPrice.setText("-");
                            txtShipAmount.setText(free);

                            shimmer.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            JSONArray dataArr = object.getJSONArray("data");
                            JSONObject singleObj = object.getJSONObject("single");

                            for (int j = 0; j < dataArr.length(); j++)
                            {
                                JSONObject itemObj = dataArr.getJSONObject(j);

                                String flag = itemObj.getString("dis_line");
                                String title = itemObj.getString("line_desc");

                                String price = "IDR " + CurencyFormat(itemObj.getString("unit_standard_price"));

                                Data_item_einvoice dt = new Data_item_einvoice();
                                dt.setPrice(price);
                                dt.setTitle(title);
                                dt.setFlag(flag);

                                list.add(dt);
                            }

                            String invDate = singleObj.getString("inv_date");
                            String purcOrder = singleObj.getString("purchase_order");
                            String totalPrice = singleObj.getString("total_price");

                            if (isPaid){
                                String[] temp = singleObj.getString("receipt_date").split("-");
                                String receiptDate = tglFormat.ValueUserDate(Integer.parseInt(temp[2]),
                                        Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[0]));

                                String keterangan = singleObj.getString("bank_name") + " (" + receiptDate + ")";
                                txtKeterangan.setVisibility(View.VISIBLE);
                                txtKeterangan.setText(keterangan);
                            }else {
                                txtKeterangan.setVisibility(View.GONE);
                            }

                            String[] temp = invDate.split("-");
                            invDate = tglFormat.ValueUserDate(Integer.parseInt(temp[2]),
                                    Integer.parseInt(temp[1]) - 1, Integer.parseInt(temp[0]));

                            String total = "IDR " + CurencyFormat(totalPrice);

                            int shipment = singleObj.getInt("amount_freight");

                            if (shipment > 0)
                            {
                                String ship = "IDR " + CurencyFormat(String.valueOf(shipment));
                                txtShipAmount.setText(ship);
                            }
                            else
                            {
                                txtShipAmount.setText(free);
                            }

                            shimmer.hideShimmerAdapter();
                            shimmer.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            adapter_detail_einvoice.notifyDataSetChanged();

                            txtInvNumber.setText(invNumber);
                            txtPurchaseOrder.setText(purcOrder);
                            txtInvDate.setText(invDate);
                            txtTotalPrice.setText(total);
                            txtTotalAmount.setText(total);

                            rlInvNumber.setVisibility(View.GONE);
                            rlPriceTotal.setVisibility(View.GONE);
                            rlPurchaseOrder.setVisibility(View.GONE);
                            rlInvDate.setVisibility(View.GONE);
                            rlShipAmount.setVisibility(View.GONE);
                            rlTotalAmount.setVisibility(View.GONE);
                            txtInvNumber.setVisibility(View.VISIBLE);
                            txtTotalPrice.setVisibility(View.VISIBLE);
                            txtPurchaseOrder.setVisibility(View.VISIBLE);
                            txtInvDate.setVisibility(View.VISIBLE);
                            txtTotalAmount.setVisibility(View.VISIBLE);
                            txtShipAmount.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("inv_number", invNumber);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}