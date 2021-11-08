package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.sofudev.trackapptrl.Adapter.Adapter_ewarranty;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Data.Data_ewarranty;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class EwarrantyActivity extends AppCompatActivity {

    Config config = new Config();
    String URLSEARCH = config.Ip_address + config.ewarranty_getListWarranty;

    ImageView btnBack;
    BootstrapEditText txtNama, txtNohp;
    Button btnSearch;

    RecyclerView recyclerView;
    ImageView imgError;
    CircleProgressBar loader;
    Dialog dialog;

    Adapter_ewarranty adapter_ewarranty;
    List<Data_ewarranty> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewarranty);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btnBack = findViewById(R.id.activity_ewarranty_btnBack);
        txtNama = findViewById(R.id.activity_ewarranty_txtname);
        txtNohp = findViewById(R.id.activity_ewarranty_txtnohp);
        btnSearch = findViewById(R.id.activity_ewarranty_btnSearch);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = txtNama.getText().toString();
                String nohp = txtNohp.getText().toString();

                if (nama.isEmpty())
                {
                    txtNama.setError("Name must fill");
                }
                else if (nohp.isEmpty())
                {
                    txtNohp.setError("Phone Number must fill");
                }
                else
                {
//                    search(nama, nohp);

                    openDialogFilter();
                }
            }
        });
    }

    private void openDialogFilter()
    {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_ewarranty);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height= WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(lp);

        recyclerView = dialog.findViewById(R.id.dialog_ewarranty_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imgError = dialog.findViewById(R.id.dialog_ewarranty_imgError);
        loader   = dialog.findViewById(R.id.dialog_ewarranty_progressBar);

        adapter_ewarranty = new Adapter_ewarranty(this, list);
        recyclerView.setAdapter(adapter_ewarranty);

        search(txtNama.getText().toString(), txtNohp.getText().toString().trim());

        if (!isFinishing())
        {
            dialog.show();
        }
    }

    private void search(final String name, final String phone)
    {
        list.clear();
        loader.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Search output : ", response);
                dialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (!jsonObject.names().get(0).equals("Error"))
                        {
                            loader.setVisibility(View.GONE);
                            imgError.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);

                            String nomorOrder = jsonObject.getString("order_number");
                            String namaPasien = jsonObject.getString("nama_pasien");
                            String nomorHp    = jsonObject.getString("phone_number");
                            String namaOptik  = jsonObject.getString("nama_optik");

                            Data_ewarranty data_ewarranty = new Data_ewarranty();
                            data_ewarranty.setOrderNumber(nomorOrder);
                            data_ewarranty.setName(namaPasien);
                            data_ewarranty.setPhone(nomorHp);
                            data_ewarranty.setOpticName(namaOptik);

                            list.add(data_ewarranty);
                        }
                        else
                        {
                            loader.setVisibility(View.GONE);
                            imgError.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    adapter_ewarranty.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("nama", name);
                hashMap.put("no_hp", phone);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}
