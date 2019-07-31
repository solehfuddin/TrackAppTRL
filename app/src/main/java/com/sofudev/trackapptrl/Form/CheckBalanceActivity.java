package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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

public class CheckBalanceActivity extends AppCompatActivity {
    Config config = new Config();
    String URLCHECKBPRKS = config.Ip_address + config.check_balance_loanbprks;

    ImageView imgBack;
    CardView cardBprks;
    UniversalFontTextView txtBprksName, txtBprksNominal;

    String username, saldo, idparty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        imgBack = findViewById(R.id.activity_checkbalance_btnback);
        cardBprks = findViewById(R.id.activity_checkbalance_cardbprks);
        txtBprksName = findViewById(R.id.activity_checkbalance_txtbprksname);
        txtBprksNominal = findViewById(R.id.activity_checkbalance_txtbprksnominal);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cardBprks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toasty.info(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CheckBalanceActivity.this, DetailBalanceActivity.class);
                intent.putExtra("nominal", saldo);
                intent.putExtra("user_info", idparty);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        getInfo();
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        idparty  = bundle.getString("user_info");

        getBprks(username);
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

    private void getBprks(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKBPRKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String name = jsonObject.getString("namakontak");
                    saldo= jsonObject.getString("saldoLoan");
                    String nominal = CurencyFormat(saldo);

                    txtBprksName.setText(name);
                    txtBprksNominal.setText(nominal);
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
                hashMap.put("username", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
