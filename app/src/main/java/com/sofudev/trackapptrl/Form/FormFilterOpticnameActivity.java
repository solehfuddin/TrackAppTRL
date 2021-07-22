package com.sofudev.trackapptrl.Form;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_filter_optic;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.DashboardActivity;
import com.sofudev.trackapptrl.Data.Data_opticname;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class FormFilterOpticnameActivity extends AppCompatActivity {

    Config config = new Config();
    String URLSHOWALLOPTIC      = config.Ip_address + config.filter_optic_showall;
    String URLSHOWOPTICBYNAME   = config.Ip_address + config.filter_optic_showbyname;
    String URLGETBYSHIPNUMBER   = config.Ip_address + config.filter_optic_getbyshipnumber;
    private String URLDELIVERYCOUNTER = config.Ip_address + config.deliverytrack_counter;

    List<Data_opticname> data_opticnames = new ArrayList<>();
    Adapter_filter_optic adapter_filter_optic;
    Data_opticname data;

    Button select_btn;
    RippleView btn_back, btn_search, btn_select, btn_prev, btn_next;
    MaterialEditText txt_search;
    BootstrapButton prev_btn, next_btn;
    UniversalFontTextView txt_counter;
    ListView listView;
    View progress;
    ImageView imgView_opticname;
    RelativeLayout rl_optic;

    MCrypt mCrypt;

    String idparty, condition, sActive, sPast, opticName, salesName;
    int sTotal;
    Integer req_start = 0, totalrow, lastitem, item;
    long lastclick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_filter_opticname);

        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));

        btn_back    = (RippleView) findViewById(R.id.form_filter_opticname_ripplebtnback);
        btn_search  = (RippleView) findViewById(R.id.form_filter_opticname_ripplebtnsearch);
        btn_select  = (RippleView) findViewById(R.id.form_filter_opticname_ripplebtnselect);
        txt_search  = (MaterialEditText) findViewById(R.id.form_filter_opticname_txtsearch);
        btn_prev    = (RippleView) findViewById(R.id.form_filter_opticname_ripplebtnprev);
        btn_next    = (RippleView) findViewById(R.id.form_filter_opticname_ripplebtnnext);
        txt_counter = (UniversalFontTextView) findViewById(R.id.form_filter_opticname_txtCounter);
        listView    = (ListView) findViewById(R.id.form_filter_opticname_listview);
        select_btn  = (Button) findViewById(R.id.form_filter_opticname_btnselect);
        prev_btn    = (BootstrapButton) findViewById(R.id.form_filter_opticname_btnprev);
        next_btn    = (BootstrapButton) findViewById(R.id.form_filter_opticname_btnnext);
        rl_optic    = (RelativeLayout) findViewById(R.id.form_filter_opticname_rl);

        adapter_filter_optic = new Adapter_filter_optic(getApplicationContext(), data_opticnames);
        listView.setAdapter(adapter_filter_optic);

        progress    = ((LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_progress, null, false);
        mCrypt      = new MCrypt();

        getUsernameData();
        disableSelect();
        disableNext();
        disablePrev();
        backToDashboard();
        selectOptic();
        searchOptic();
        showNextOptic();
        showPrevOptic();
        openTrackOrder();

        data_opticnames.clear();
        showAllOptic(req_start);
    }

    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            condition = bundle.getString("cond");
            salesName = bundle.getString("sales");

            Log.d("Sales Name chooseopt : ", salesName);
        }
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

    private void disableSelect()
    {
        btn_select.setEnabled(false);
        select_btn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
    }

    private void enableSelect()
    {
        btn_select.setEnabled(true);
        select_btn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
    }

    private void disablePrev()
    {
        btn_prev.setEnabled(false);
        prev_btn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
    }

    private void enablePrev()
    {
        btn_prev.setEnabled(true);
        prev_btn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
    }

    private void disableNext()
    {
        btn_next.setEnabled(false);
        next_btn.setBackgroundColor(getResources().getColor(R.color.bootstrap_gray_light));
    }

    private void enableNext()
    {
        btn_next.setEnabled(true);
        next_btn.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
    }

    private void showNextOptic()
    {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclick < 1500)
                {
                    return;
                }

                lastclick = SystemClock.elapsedRealtime();

                try {
                    Thread.sleep(300);

                    if (txt_search.getText().length() == 0)
                    {
                        lastitem = 0;
                        req_start = req_start + 8;

                        item     = totalrow % 8;
                        lastitem = req_start + item;

                        enablePrev();
                        disableSelect();
                        listView.addHeaderView(progress);
                        showAllOptic(req_start);
                    }
                    else
                    {
                        lastitem = 0;
                        req_start = req_start + 8;

                        item     = totalrow % 8;
                        lastitem = req_start + item;

                        enablePrev();
                        disableSelect();
                        listView.addHeaderView(progress);
                        showOpticByName(txt_search.getText().toString(),req_start);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showPrevOptic()
    {
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastclick < 1500)
                {
                    return;
                }

                lastclick = SystemClock.elapsedRealtime();

                try {
                    Thread.sleep(300);

                    if (txt_search.getText().length() == 0)
                    {
                        req_start = req_start - 8;

                        if (req_start == 0)
                        {
                            disablePrev();
                        }

                        disableSelect();
                        listView.addHeaderView(progress);
                        showAllOptic(req_start);
                    }
                    else
                    {
                        req_start = req_start - 8;

                        if (req_start == 0)
                        {
                            disablePrev();
                        }

                        disableSelect();
                        listView.addHeaderView(progress);
                        showOpticByName(txt_search.getText().toString(), req_start);
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void selectOptic()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (condition.equals("ESTMENT"))
                {
                    idparty = data_opticnames.get(position).getIdParty();
                    opticName = data_opticnames.get(position).getCustname();
//                    Toast.makeText(getApplicationContext(), idparty, Toast.LENGTH_SHORT).show();
                }
                else if (condition.equals("DELIVTRACK"))
                {
                    idparty = data_opticnames.get(position).getCustname();
                    countData(data_opticnames.get(position).getCustname());
                }
                else if (condition.equals("LENSSALES"))
                {
                    idparty = data_opticnames.get(position).getIdParty();
                    opticName = data_opticnames.get(position).getCustname();
//                    Toasty.info(getApplicationContext(), "Id party : " + idparty, Toast.LENGTH_SHORT).show();
                }
                else if (condition.equals("CARTSALES"))
                {
                    idparty = data_opticnames.get(position).getIdParty();
                    opticName = data_opticnames.get(position).getCustname();
                }
                else if (condition.equals("BATCHSALES"))
                {
                    idparty = data_opticnames.get(position).getIdParty();
                    opticName = data_opticnames.get(position).getCustname();
                }
                else
                {
                    idparty = data_opticnames.get(position).getUsername();
//                    Toast.makeText(getApplicationContext(), idparty, Toast.LENGTH_SHORT).show();
                }

                if (idparty.isEmpty())
                {
                    disableSelect();
                }
                else
                {
                    enableSelect();
                }
            }
        });
    }

    private void countData(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLDELIVERYCOUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d("Output", response);

                    sActive = "Active ("+ jsonObject.getInt("countactive") +")";
                    sPast = "Past (" + jsonObject.getInt("countpast") +")";
                    sTotal = jsonObject.getInt("countactive") + jsonObject.getInt("countpast");

                    Log.d("Active", sActive);
                    Log.d("Past", sPast);
                    Log.d("Total", String.valueOf(sTotal));
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
                map.put("optic", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void openTrackOrder()
    {
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (condition.equals("OTRACK"))
                {
                    Intent intent = new Intent(getApplicationContext(), FormTrackOrderActivity.class);
                    intent.putExtra("idparty", idparty);
                    startActivity(intent);
                }
                else if (condition.equals("ESTMENT"))
                {
                    Intent intent = new Intent(getApplicationContext(), EstatementActivity.class);
                    intent.putExtra("username", idparty);
                    intent.putExtra("opticname", opticName);
                    startActivity(intent);
                }
                else if (condition.equals("DELIVTRACK"))
                {
                    Intent intent = new Intent(getApplicationContext(), FormDeliveryTrackingActivity.class);
                    intent.putExtra("username", idparty);
                    intent.putExtra("activetitle", sActive);
                    intent.putExtra("pasttitle", sPast);
                    intent.putExtra("totalprocess", String.valueOf(sTotal));
                    startActivity(intent);
                }
                else if (condition.equals("LENSSALES"))
                {
                    showByShipnumber(idparty, 0);
                }
                else if (condition.equals("CARTSALES"))
                {
                    showByShipnumber(idparty, 1);
                }
                else if (condition.equals("BATCHSALES"))
                {
                    showByShipnumber(idparty, 2);
                }
            }
        });
    }

    private void showErrorImage()
    {
        imgView_opticname = new ImageView(getApplicationContext());
        imgView_opticname.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        imgView_opticname.setLayoutParams(lp);
        rl_optic.addView(imgView_opticname);
    }

    private void searchOptic()
    {
        txt_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                req_start = 0;
                disableSelect();

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String check = txt_search.getText().toString();
                    if (check.isEmpty())
                    {
                        Toasty.warning(getApplicationContext(), "Please fill optic name", Toast.LENGTH_SHORT, true).show();
                    }
                    else
                    {
                        showOpticByName(check, req_start);
                        disablePrev();
                    }
                }

                return false;
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                String check = txt_search.getText().toString();
                req_start = 0;
                disableSelect();

                if (check.isEmpty())
                {
                    Toasty.warning(getApplicationContext(), "Please fill optic name", Toast.LENGTH_SHORT, true).show();
                }
                else
                {
                    showOpticByName(check, req_start);
                    disablePrev();
                }
            }
        });
    }

    private void showByShipnumber(final String username, final int category)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETBYSHIPNUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Toasty.info(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (category == 0)
                        {
                            Intent intent = new Intent(getApplicationContext(), FormOrderLensActivity.class);
                            intent.putExtra("idparty", jsonObject.getString("idparty"));
                            intent.putExtra("opticname", jsonObject.getString("custname"));
                            intent.putExtra("province", jsonObject.getString("province"));
                            intent.putExtra("usernameInfo", jsonObject.getString("username"));
                            intent.putExtra("city", jsonObject.getString("city"));
                            intent.putExtra("level", "1");
                            intent.putExtra("flag", jsonObject.getString("flag"));
                            intent.putExtra("idSp", "0");
                            intent.putExtra("isSp", "0");
                            intent.putExtra("noHp", "0");
                            intent.putExtra("sales", salesName);
                            startActivity(intent);
                        }
                        else if (category == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), AddCartProductActivity.class);
                            intent.putExtra("idparty", jsonObject.getString("idparty"));
                            intent.putExtra("opticname", jsonObject.getString("custname"));
                            intent.putExtra("province", jsonObject.getString("province"));
                            intent.putExtra("province_address", jsonObject.getString("address"));
                            intent.putExtra("usernameInfo", jsonObject.getString("username"));
                            intent.putExtra("level", "1");
                            intent.putExtra("city", jsonObject.getString("city"));
                            intent.putExtra("flag", jsonObject.getString("flag"));
                            intent.putExtra("sales", salesName);
                            startActivityForResult(intent, 2);
                        }
                        else if (category == 2)
                        {
                            Intent intent = new Intent(getApplicationContext(), FormBatchOrderActivity.class);
                            intent.putExtra("idparty", jsonObject.getString("idparty"));
                            intent.putExtra("opticname", jsonObject.getString("custname"));
                            intent.putExtra("province", jsonObject.getString("province"));
                            intent.putExtra("usernameInfo", jsonObject.getString("username"));
                            intent.putExtra("province_address", jsonObject.getString("province"));
                            intent.putExtra("city", jsonObject.getString("city"));
                            intent.putExtra("idSp", "0");
                            intent.putExtra("isSp", 0);
                            intent.putExtra("flag", jsonObject.getString("flag"));
                            intent.putExtra("level", "1");
                            intent.putExtra("sales", salesName);
                            startActivity(intent);
                        }

                        Log.d("Idparty : ", jsonObject.getString("idparty"));
                        Log.d("Opticname : ", jsonObject.getString("custname"));
                        Log.d("Province : ", jsonObject.getString("province"));
                        Log.d("Username : ", jsonObject.getString("username"));
                        Log.d("City : ", jsonObject.getString("city"));
                        Log.d("Flag : ", jsonObject.getString("flag"));
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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void showAllOptic(int record)
    {
        adapter_filter_optic.notifyDataSetChanged();
        data_opticnames.clear();

        StringRequest stringRequest = new StringRequest(URLSHOWALLOPTIC + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listView.removeHeaderView(progress);
                String info  = "idparty";
                String info1 = "username";
                String info2 = "custname";
                String info3 = "status";
                String info4 = "total_row";
                int start, until;

                try {
                    String encrypt_info  = MCrypt.bytesToHex(mCrypt.encrypt(info));
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String encrypt_info4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));

                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id       = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info)));
                            String username = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                            String custname = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));
                            String status   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info3)));
                            String total    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info4)));

                            totalrow = Integer.parseInt(total);

                            start    = req_start + 1;
                            until    = jsonArray.length() + req_start;

                            String counter = "Show " + start + " - " + until + " from "+ totalrow + " records";
                            txt_counter.setText(counter);

                            data = new Data_opticname();
                            data.setIdParty(id);
                            data.setUsername(username);
                            data.setCustname(custname);
                            data.setStatus(status);

                            data_opticnames.add(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (totalrow > 8)
                    {
                        enableNext();
                    }
                    else
                    {
                        disableNext();
                        disablePrev();
                    }

                    adapter_filter_optic.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getApplicationContext(), "Failed to encrypt message", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void showOpticByName(final String key, int record)
    {
        adapter_filter_optic.notifyDataSetChanged();
        data_opticnames.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLSHOWOPTICBYNAME + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listView.removeHeaderView(progress);
                rl_optic.removeView(imgView_opticname);
                String info6 = "idparty";
                String info1 = "username";
                String info2 = "custname";
                String info3 = "status";
                String info4 = "total_row";
                String info5 = "invalid";
                int start, until;

                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String encrypt_info2 = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String encrypt_info3 = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String encrypt_info4 = MCrypt.bytesToHex(mCrypt.encrypt(info4));
                    String encrypt_info5 = MCrypt.bytesToHex(mCrypt.encrypt(info5));
                    String encrypt_info6 = MCrypt.bytesToHex(mCrypt.encrypt(info6));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            if (jsonObject.names().get(0).equals(encrypt_info5))
                            {
                                showErrorImage();
                                String info = "No record found";
                                txt_counter.setText(info);
                                disableNext();
                                disablePrev();
                                Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                            }
                            else
                            {
                                String username = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                                String custname = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info2)));
                                String status   = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info3)));
                                String total    = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info4)));
                                String id       = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info6)));

                                data            = new Data_opticname();
                                data.setIdParty(id);
                                data.setUsername(username);
                                data.setCustname(custname);
                                data.setStatus(status);

                                totalrow        = Integer.parseInt(total);

                                start           = req_start + 1;
                                until           = jsonArray.length() + req_start;

                                String counter  = "Show " + start + " - " + until + " from " + totalrow + " records";

                                txt_counter.setText(counter);

                                if (totalrow > 8)
                                {
                                    enableNext();
                                }
                                else
                                {
                                    disableNext();
                                    disablePrev();
                                }

                                if (totalrow.equals(until))
                                {
                                    disableNext();
                                }

                                data_opticnames.add(data);
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapter_filter_optic.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Toasty.warning(getApplicationContext(), "Failed to encrypt data", Toast.LENGTH_SHORT, true).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("custname", key);

                return hashMap;
            }
        };

        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
