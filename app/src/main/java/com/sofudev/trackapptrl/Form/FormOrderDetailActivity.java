package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormOrderDetailActivity extends AppCompatActivity {
    Config config = new Config();
    UniversalFontTextView txtPasien, txtTanggal, txtProduk, txtHarga, txtStatus, txtNomorOrder, txtDownload;
    RippleView btnDownload;
    ImageView btnBack;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_detail);

        txtPasien   = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_pasien);
        txtTanggal  = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_tanggal);
        txtProduk   = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_produk);
        txtHarga    = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_harga);
        txtStatus   = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_status);
        txtNomorOrder = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_nomororder);
        txtDownload = (UniversalFontTextView) findViewById(R.id.activity_orderdetail_txt_download);

        btnDownload = (RippleView) findViewById(R.id.activity_orderdetail_btn_download);
        btnBack     = (ImageView) findViewById(R.id.activity_orderdetail_btn_back);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/lensa/" + key));

                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/printreceipt/lensaweb/" + key));
                startActivity(openBrowser);
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        key = bundle.getString("key");
        getInformationDetail(key);
    }

    private void getInformationDetail(final String key_order)
    {
//        String URL = config.Ip_address + config.order_history_chooseOrder;
        String URL = config.Ip_address + config.lenshistory_chooselens;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String nomor = "No Order #";

                    txtPasien.setText(jsonObject.getString("nama_pasien"));
                    txtTanggal.setText(jsonObject.getString("tanggal_order"));
                    txtProduk.setText(jsonObject.getString("nama_produk"));
                    txtHarga.setText("Rp. " + CurencyFormat(jsonObject.getString("total_harga")));
                    txtStatus.setText(jsonObject.getString("status_bayar"));
                    txtNomorOrder.setText(nomor + jsonObject.getString("order_number"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("key_order", key_order);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
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
}
