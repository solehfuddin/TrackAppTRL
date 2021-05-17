package com.sofudev.trackapptrl.Form;

import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.premnirmal.textcounter.CounterView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormOrderDashboardActivity extends AppCompatActivity {

    CounterView lbl_draf, lbl_sent, lbl_process, lbl_finish;
    RippleView btn_back;
    Config config = new Config();
    MCrypt mCrypt = new MCrypt();

    String counter_orderpanel = config.Ip_address + config.orderdashboard_informationpanel;
    String id_party;
    int count_draft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_order_dashboard);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btn_back = (RippleView) findViewById(R.id.form_orderdashboard_ripplebtnback);
        lbl_draf = (CounterView) findViewById(R.id.form_orderdashboard_lbldraft);
        lbl_sent = (CounterView) findViewById(R.id.form_orderdashboard_lblsent);
        lbl_process = (CounterView) findViewById(R.id.form_orderdashboard_lblprocess);
        lbl_finish = (CounterView) findViewById(R.id.form_orderdashboard_lblfinish);

        backToDashboard();
        getUsernameData();
        checkDashboardPanelCounter();
        readAllTxtFiles();
    }

    private void backToDashboard()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            id_party = bundle.getString("idparty");
            //Toasty.info(getApplicationContext(), id_party, Toast.LENGTH_SHORT, true).show();
        }
    }

    private void setAutoCounter(CounterView counterView, Integer endValue)
    {
        counterView.setAutoStart(false);
        counterView.setStartValue(0f);
        counterView.setEndValue(endValue);
        counterView.setIncrement(5f);
        counterView.setTimeInterval(5);
        counterView.start();
    }

    private void checkDashboardPanelCounter()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, counter_orderpanel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String total_order      = "total_order";
                String total_shipped    = "total_shipped";
                String total_process    = "total_process";
                String error            = "error";

                try {
                    String encrypt_totalorder   = MCrypt.bytesToHex(mCrypt.encrypt(total_order));
                    String encrypt_totalshipped = MCrypt.bytesToHex(mCrypt.encrypt(total_shipped));
                    String encrypt_totalprocess = MCrypt.bytesToHex(mCrypt.encrypt(total_process));
                    String encrypt_error        = MCrypt.bytesToHex(mCrypt.encrypt(error));

                    try
                    {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(encrypt_error))
                            {
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT, true).show();
                            }
                            else
                            {
                                String dt_totalorder = new String(mCrypt.decrypt(jsonObject.getString(encrypt_totalorder)));
                                String dt_totalshipped = new String(mCrypt.decrypt(jsonObject.getString(encrypt_totalshipped)));
                                String dt_totalprocess = new String(mCrypt.decrypt(jsonObject.getString(encrypt_totalprocess)));

                                if (!dt_totalorder.isEmpty())
                                {
                                    setAutoCounter(lbl_sent, Integer.parseInt(dt_totalorder));
                                }

                                if (!dt_totalshipped.isEmpty())
                                {
                                    setAutoCounter(lbl_finish, Integer.parseInt(dt_totalshipped));
                                }

                                if (!dt_totalprocess.isEmpty())
                                {
                                    setAutoCounter(lbl_process, Integer.parseInt(dt_totalprocess));
                                }
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("optic_id", id_party);
                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void readAllTxtFiles()
    {
        File sourcefile = new File(Environment.getExternalStorageDirectory(), "TRLAPP/OrderTemp");

        if (sourcefile.exists())
        {
            for (File f : sourcefile.listFiles())
            {
                if (f.isFile() && f.getPath().endsWith(".txt"))
                {
                    //String name = f.getName();
                    //Toasty.info(getApplicationContext(), name, Toast.LENGTH_SHORT, true).show();
                    count_draft = count_draft + 1;
                }
            }

            setAutoCounter(lbl_draf, count_draft);
        }
    }
}
