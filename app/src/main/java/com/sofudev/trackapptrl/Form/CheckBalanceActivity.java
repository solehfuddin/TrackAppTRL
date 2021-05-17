package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
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
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class CheckBalanceActivity extends AppCompatActivity {
    Config config = new Config();
    String URLCHECKBPRKS = config.Ip_address + config.check_balance_loanbprks;
    String URLCHECKDEPOSIT = config.Ip_address + config.depo_getsaldo;

    ACProgressFlower loading;
    ImageView imgBack;
    CardView cardBprks, cardDeposit;
    UniversalFontTextView txtBprksName, txtBprksNominal, txtDepositName, txtDepositNominal;

    String username, saldo, idparty, name, nominal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        imgBack = findViewById(R.id.activity_checkbalance_btnback);
        cardBprks = findViewById(R.id.activity_checkbalance_cardbprks);
        cardDeposit = findViewById(R.id.activity_checkbalance_carddeposit);
        txtBprksName = findViewById(R.id.activity_checkbalance_txtbprksname);
        txtDepositName = findViewById(R.id.activity_checkbalance_txtdepositname);
        txtBprksNominal = findViewById(R.id.activity_checkbalance_txtbprksnominal);
        txtDepositNominal = findViewById(R.id.activity_checkbalance_txtdepositnominal);

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

//        showLoading();
        getInfo();
    }

    private void showLoading()
    {
        loading = new ACProgressFlower.Builder(CheckBalanceActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoading();
        getInfo();
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();

        username = bundle.getString("username");
        idparty  = bundle.getString("user_info");

        getBprks(username);
//        getSaldoDepo(username);
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

                    name = jsonObject.getString("namakontak");
                    saldo= jsonObject.getString("saldoLoan");
                    String nominal = CurencyFormat(saldo);

                    txtBprksName.setText(name);
                    txtBprksNominal.setText(nominal);

                    getSaldoDepo(name, username);
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

    private void getSaldoDepo(final String name, final String shipNumber){
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    nominal = jsonObject.getString("sisa_saldo");
                    String val = CurencyFormat(nominal);

                    txtDepositName.setText(name);
                    txtDepositNominal.setText(val);

                    cardDeposit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CheckBalanceActivity.this, DetailDepositActivity.class);
                            intent.putExtra("nominal", nominal);
                            intent.putExtra("user_info", idparty);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    });
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
                hashMap.put("ship_number", shipNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
