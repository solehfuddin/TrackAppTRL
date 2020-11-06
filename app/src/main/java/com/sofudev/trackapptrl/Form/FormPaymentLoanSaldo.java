package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressCustom;
import es.dmoral.toasty.Toasty;

public class FormPaymentLoanSaldo extends AppCompatActivity {

    Config config = new Config();
    String URLLOANKONFIRM = config.payment_method_loanKonfirmasi;

    Button btnNext, btnPrev;
    UniversalFontTextView txtSisaSaldo, txtBillingId;
    TextView txtTotalBilling;
    ACProgressCustom loading;

    String nomorHp, billingId, totalBill, orderNumber, username, sisaSaldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_loan_saldo);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnNext = findViewById(R.id.form_paymentloan_saldo_btnNext);
        btnPrev = findViewById(R.id.form_paymentloan_saldo_btnBack);

        txtSisaSaldo = findViewById(R.id.form_paymentloan_saldo_txtSisaSaldo);
        txtBillingId = findViewById(R.id.form_paymentloan_saldo_txtKodeBilling);
        txtTotalBilling = findViewById(R.id.form_paymentloan_saldo_txtTotalBill);

        getInfo();

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            finish();
        }
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();

        billingId           = bundle.getString("billingId");
        sisaSaldo           = bundle.getString("sisaSaldo");
        totalBill           = bundle.getString("totalBill");
        nomorHp             = bundle.getString("nomorHp");
        orderNumber         = bundle.getString("orderNumber");
        username            = bundle.getString("username");

        Toasty.info(getApplicationContext(), nomorHp, Toast.LENGTH_SHORT).show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int balance = Integer.parseInt(sisaSaldo);
//                int amount = Integer.parseInt(totalBill);
//
//                int sisa = balance - amount;
//
//                if (sisa < 0) {
//                    Toasty.error(getApplicationContext(), "Insufficient balance", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    setResult(RESULT_OK, new Intent());
//                    getKonfirmOrder(billingId, nomorHp, totalBill);
//                }

                setResult(RESULT_OK, new Intent());
                getKonfirmOrder(billingId, nomorHp, totalBill);

//                Intent intent = new Intent(FormPaymentLoanSaldo.this, FormPaymentLoanOtp.class);
//                intent.putExtra("totalBill", totalBill);
//                intent.putExtra("accountFrom", 1024);
//                intent.putExtra("billingId", billingId);
//                intent.putExtra("nomorHp", "085710035929");
//                intent.putExtra("orderNumber", orderNumber);
//                startActivityForResult(intent, 1101);
            }
        });

        txtBillingId.setText(billingId);
        txtSisaSaldo.setText(sisaSaldo);
        txtTotalBilling.setText(totalBill);
    }

    private void showLoading() {
        loading = new ACProgressCustom.Builder(FormPaymentLoanSaldo.this)
                .useImages(R.drawable.loadernew0, R.drawable.loadernew1, R.drawable.loadernew2,
                        R.drawable.loadernew3, R.drawable.loadernew4, R.drawable.loadernew5,
                        R.drawable.loadernew6, R.drawable.loadernew7, R.drawable.loadernew8, R.drawable.loadernew9)
                /*.useImages(R.drawable.cobaloader)*/
                .speed(60)
                .build();
        loading.show();
    }

    private void getKonfirmOrder(final String billingId, final String nomorHandphone, final String nominal)
    {
        showLoading();
        StringRequest request = new StringRequest(Request.Method.POST, URLLOANKONFIRM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String totalBilling = jsonObject.getString("grossAmount");
                    String accountFrom  = jsonObject.getString("accountFrom");
                    String statusCode   = jsonObject.getString("responseCode");
                    String description  = jsonObject.getString("responseDescription");

                    if (statusCode.equals("00") || statusCode.contains("00") || statusCode.contentEquals("00"))
                    {
                        Intent intent = new Intent(FormPaymentLoanSaldo.this, FormPaymentLoanOtp.class);
                        intent.putExtra("totalBill", totalBilling);
                        intent.putExtra("accountFrom", accountFrom);
                        intent.putExtra("sisaSaldo", sisaSaldo);
                        intent.putExtra("billingId", billingId);
                        intent.putExtra("nomorHp", nomorHandphone);
                        intent.putExtra("orderNumber", orderNumber);
                        intent.putExtra("username", username);
                        startActivityForResult(intent, 1101);
                    }
                    else
                    {
                        Toasty.error(getApplicationContext(), description + " (" + statusCode + ")", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("billingId", billingId);
                hashMap.put("nomorHandphone", nomorHandphone);
                hashMap.put("nominal", nominal);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
