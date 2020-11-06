package com.sofudev.trackapptrl.Form;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;

public class FormPaymentLoanSuccess extends AppCompatActivity {

    UniversalFontTextView txtBillingId;
    TextView txtTotalBill;
    Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_payment_loan_success);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        txtBillingId = findViewById(R.id.form_paymentloan_success_txtBillingId);
        txtTotalBill = findViewById(R.id.form_paymentloan_success_txtTotalBill);
        btnOk = findViewById(R.id.form_paymentloan_btn_ok);

        getInfo();

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
    }

    private void getInfo()
    {
        Bundle bundle = getIntent().getExtras();

        String billingId = bundle.getString("billingId");
        String totalBill = bundle.getString("totalBill");

        txtBillingId.setText(billingId);
        txtTotalBill.setText(totalBill);
    }
}
