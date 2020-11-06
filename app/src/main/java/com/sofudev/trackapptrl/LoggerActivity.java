package com.sofudev.trackapptrl;

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
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoggerActivity extends AppCompatActivity implements View.OnClickListener {

    Config config = new Config();
    String CREATELOG = config.Ip_address + config.inser_logger;

    TextView txtLog;
    Button btnKembali, btnLaporkan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logger);

        txtLog = findViewById(R.id.activity_logger_txtlog);
        btnKembali = findViewById(R.id.activity_logger_btnkembali);
        btnLaporkan= findViewById(R.id.activity_logger_btnlapor);

        txtLog.setText(getIntent().getStringExtra("error"));

        btnKembali.setOnClickListener(this);
        btnLaporkan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_logger_btnkembali:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                finish();
                break;

            case R.id.activity_logger_btnlapor:
                insertLog(txtLog.getText().toString());
                break;
        }
    }

    private void insertLog(final String log) {
        StringRequest request = new StringRequest(Request.Method.POST, CREATELOG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("success")) {
                        String info = "Terima kasih pesan anda akan ditindaklanjuti oleh Tim Development kami";
                        Toasty.success(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
                    }
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
                hashMap.put("logger", log);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
