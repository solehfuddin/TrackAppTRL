package com.sofudev.trackapptrl.Form;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.kyleduo.switchbutton.SwitchButton;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_uac;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_uac;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Security.MCrypt;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import es.dmoral.toasty.Toasty;

public class FormUACActivity extends AppCompatActivity {

    private List<Data_uac> data_uacs = new ArrayList<>();
    private Adapter_uac adapter_uac;
    Config config = new Config();

    private String FILTER_URL      = config.Ip_address + config.uac_filter_user;
    private String USERDETAILURL   = config.Ip_address + config.uac_detail_user;
    private String UPDATEUACURL    = config.Ip_address + config.uac_update_user;
    private String UPDATEPASSURL   = config.Ip_address + config.uac_update_password;
    private String ADDUACURL       = config.Ip_address + config.uac_add_user;

    private ACProgressFlower loading;
    private MCrypt mCrypt;
    private String info_username;
    private LovelyCustomDialog lovelyCustomDialog;
    LayoutInflater layoutInflater;
    View view;
    ListView listView_auc;
    TextView lbl_errorusername, lbl_errorcustomer, lbl_erroremail;
    BootstrapEditText txt_uac, txt_username, txt_customer, txt_email;
    UniversalFontTextView txt_counter;
    RelativeLayout rl_uac;
    ImageView imageView_uac;
    BootstrapButton btn_submit, btn_reset, btn_next, btn_prev;
    SwitchButton sw_status, sw_level;
    FloatingActionButton fab_add;

    String username, customer, email, status, level;

    View progressLoading, progressInfo;
    Integer req_start = 0, last_item, total_row, item;
    long lastClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_uac);

        Toolbar toolbar = (Toolbar) findViewById(R.id.form_uac_toolbar);
        toolbar.setTitle("User Management");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorToolbar));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView_auc = (ListView) findViewById(R.id.form_uac_listviewuser);
        txt_uac      = (BootstrapEditText) findViewById(R.id.form_uac_txtsearch);
        rl_uac       = (RelativeLayout) findViewById(R.id.form_uac_rl);
        fab_add      = (FloatingActionButton) findViewById(R.id.form_uac_fabadd);
        btn_next     = (BootstrapButton) findViewById(R.id.form_uac_btnnext);
        btn_prev     = (BootstrapButton) findViewById(R.id.form_uac_btnprev);
        txt_counter  = (UniversalFontTextView) findViewById(R.id.form_uac_txtCounter);

        adapter_uac  = new Adapter_uac(this, data_uacs);
        listView_auc.setAdapter(adapter_uac);

        progressLoading = ((LayoutInflater)this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_progress, null, false);
        progressInfo    = ((LayoutInflater)this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_info, null, false);

        disablePrev();
        disableNext();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1500)
                {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                try {
                    Thread.sleep(300);
                    req_start = req_start + 5;
                    enablePrev();

                    item        = total_row % 5;
                    last_item   = req_start + item;

                    listView_auc.addFooterView(progressLoading);
                    filterData(txt_uac.getText().toString(), req_start);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - lastClick < 1500)
                {
                    return;
                }

                lastClick = SystemClock.elapsedRealtime();

                try {
                    Thread.sleep(300);
                    last_item = 0;
                    req_start = req_start - 5;

                    if (req_start == 0)
                    {
                        disablePrev();
                    }

                    listView_auc.addFooterView(progressLoading);
                    filterData(txt_uac.getText().toString(), req_start);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mCrypt = new MCrypt();

        showInitializeImage();
        searchData();
        openDialogUpdate();
        openDialogAdd();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void disablePrev()
    {
        btn_prev.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        btn_prev.setEnabled(false);
    }

    private void enablePrev()
    {
        btn_prev.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        btn_prev.setEnabled(true);
    }

    private void disableNext()
    {
        btn_next.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
        btn_next.setEnabled(false);
    }

    private void enableNext()
    {
        btn_next.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        btn_next.setEnabled(true);
    }

    public void information(String info, String message, int resource, final DefaultBootstrapBrand defaultcolorbtn)
    {
        ImageView img_status;
        UniversalFontTextView txt_information, txt_message;
        final BootstrapButton btn_ok;

        final Dialog dialog = new Dialog(FormUACActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_status);
        dialog.setCancelable(false);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        img_status      = (ImageView) dialog.findViewById(R.id.info_status_imageview);
        txt_information = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtInformation);
        txt_message     = (UniversalFontTextView) dialog.findViewById(R.id.info_status_txtMessage);
        btn_ok          = (BootstrapButton) dialog.findViewById(R.id.info_status_btnOk);

        img_status.setImageResource(resource);
        txt_information.setText(info);
        txt_message.setText(message);
        btn_ok.setBootstrapBrand(defaultcolorbtn);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterData(txt_uac.getText().toString(), req_start);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void filterData(final String Key, int record)
    {
        showLoading();
        data_uacs.clear();
        adapter_uac.notifyDataSetChanged();
        enableNext();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FILTER_URL + String.valueOf(record), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.hide();
                listView_auc.removeFooterView(progressLoading);
                rl_uac.removeView(imageView_uac);
                String info1 = "customer";
                String info2 = "image";
                String info3 = "status";
                String info4 = "username";
                String info5 = "total_data";
                int start, until;

                try {
                    String customer = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    String image    = MCrypt.bytesToHex(mCrypt.encrypt(info2));
                    String status   = MCrypt.bytesToHex(mCrypt.encrypt(info3));
                    String username = MCrypt.bytesToHex(mCrypt.encrypt(info4));
                    String totaldata= MCrypt.bytesToHex(mCrypt.encrypt(info5));
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String data_customer = new String(mCrypt.decrypt(jsonObject.getString(customer)));
                            String data_image    = new String(mCrypt.decrypt(jsonObject.getString(image)));
                            String data_status   = new String(mCrypt.decrypt(jsonObject.getString(status)));
                            String data_total    = new String(mCrypt.decrypt(jsonObject.getString(totaldata)));

                            info_username = new String(mCrypt.decrypt(jsonObject.getString(username)));

                            Data_uac data_uac = new Data_uac();
                            data_uac.setTitle(data_customer);
                            data_uac.setImage(data_image);
                            data_uac.setStatus(data_status);
                            data_uac.setUsername(info_username);

                            total_row = Integer.parseInt(data_total);

                            start   = req_start + 1;
                            until   = jsonArray.length() + req_start;
                            String counter = "show " + start + " - " + until + " from " + total_row + " users";
                            txt_counter.setText(counter);
                            data_uacs.add(data_uac);

                            if (until <= 5 )
                            {
                                disablePrev();
                            }

                            if (total_row.equals(until))
                            {
                                disableNext();
                            }
                        }

                        if (total_row <= 5)
                        {
                            disablePrev();
                            disableNext();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
                        showErrorImage();
                        txt_counter.setText("No record found");
                        disableNext();
                        disablePrev();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toasty.error(getApplicationContext(), "Failed decrypt message", Toast.LENGTH_SHORT).show();
                }

                adapter_uac.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.hide();
                listView_auc.removeFooterView(progressLoading);
                //Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
                information("Error connection", "Can't connect to server, press ok to reconnect ", R.drawable.failed_outline,
                        DefaultBootstrapBrand.WARNING);
                disableNext();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("customer_name", Key);
                return hashMap;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 0, 1f));
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void searchData()
    {
        txt_uac.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    String check = txt_uac.getText().toString();
                    if (check.isEmpty())
                    {
                        Toasty.warning(getApplicationContext(), "Please fill optic name", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        filterData(txt_uac.getText().toString(), req_start);
                    }
                }

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL))
                {
                    req_start = 0;
                }
                return false;
            }
        });
    }

    private void showLoading() {
        loading = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.GREEN)
                .text("Please wait ...")
                .fadeColor(Color.DKGRAY).build();
        loading.show();
    }

    private void showErrorImage()
    {
        imageView_uac = new ImageView(getApplicationContext());
        imageView_uac.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView_uac.setLayoutParams(lp);
        rl_uac.addView(imageView_uac);
    }

    private void showInitializeImage()
    {
        imageView_uac = new ImageView(getApplicationContext());
        imageView_uac.setImageResource(R.drawable.search_optic_sm);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView_uac.setLayoutParams(lp);
        rl_uac.addView(imageView_uac);
    }

    private void openDialogUpdate()
    {
        listView_auc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username_id = data_uacs.get(position).getTitle();
                showDetailUac(username_id);
            }
        });
    }

    private void showDetailUac(final String key)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERDETAILURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                txt_uac.clearFocus();
                String data1 = "username";
                String data2 = "customer";
                String data3 = "email";
                String data4 = "status";
                String data5 = "level";

                try {
                    String detail_username = MCrypt.bytesToHex(mCrypt.encrypt(data1));
                    String detail_customer = MCrypt.bytesToHex(mCrypt.encrypt(data2));
                    String detail_email    = MCrypt.bytesToHex(mCrypt.encrypt(data3));
                    String detail_status   = MCrypt.bytesToHex(mCrypt.encrypt(data4));
                    String detail_level    = MCrypt.bytesToHex(mCrypt.encrypt(data5));

                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            lovelyCustomDialog = new LovelyCustomDialog(FormUACActivity.this);
                            layoutInflater  = getLayoutInflater();
                            view = layoutInflater.inflate(R.layout.form_uac, null);
                            lovelyCustomDialog.setTopColorRes(R.color.bootstrap_brand_danger);
                            lovelyCustomDialog.setTopTitle("Account Settings");
                            lovelyCustomDialog.setIcon(R.drawable.ic_settings);
                            lovelyCustomDialog.setTopTitleColor(Color.WHITE);
                            lovelyCustomDialog.setView(view);
                            lovelyCustomDialog.show();

                            txt_username = (BootstrapEditText) view.findViewById(R.id.form_uac_txtusername);
                            txt_customer = (BootstrapEditText) view.findViewById(R.id.form_uac_txtcustomer);
                            txt_email    = (BootstrapEditText) view.findViewById(R.id.form_uac_txtemail);
                            sw_status    = (SwitchButton) view.findViewById(R.id.form_uac_swtstatus);
                            sw_level     = (SwitchButton) view.findViewById(R.id.form_uac_swtlevel);
                            btn_submit   = (BootstrapButton) view.findViewById(R.id.form_uac_btnsubmit);
                            btn_reset    = (BootstrapButton) view.findViewById(R.id.form_uac_btnreset);
                            lbl_errorusername = (TextView) view.findViewById(R.id.form_uac_errorUsername);
                            lbl_errorcustomer = (TextView) view.findViewById(R.id.form_uac_errorCustomer);
                            lbl_erroremail    = (TextView) view.findViewById(R.id.form_uac_errorEmail);

                            //Decrypt all json data
                            String data_username = new String(mCrypt.decrypt(jsonObject.getString(detail_username)));
                            String data_customer = new String(mCrypt.decrypt(jsonObject.getString(detail_customer)));
                            String data_email    = new String(mCrypt.decrypt(jsonObject.getString(detail_email)));
                            String data_status   = new String(mCrypt.decrypt(jsonObject.getString(detail_status)));
                            String data_level    = new String(mCrypt.decrypt(jsonObject.getString(detail_level)));

                            txt_username.setText(data_username);
                            txt_customer.setText(data_customer);
                            txt_email.setText(data_email);

                            txt_username.setFocusable(false);
                            txt_username.setClickable(false);

                            btn_submit.setText("Update");

                            sw_status.setText(data_status);

                            if (sw_status.getText().equals("I"))
                            {
                                sw_status.setText("");
                                sw_status.setChecked(false);
                            }
                            else if (sw_status.getText().equals("A"))
                            {
                                sw_status.setText("");
                                sw_status.setChecked(true);
                            }

                            sw_level.setText(data_level);

                            if (sw_level.getText().equals("1"))
                            {
                                sw_level.setText("");
                                sw_level.setChecked(true);
                                sw_status.setEnabled(false);
                            }
                            else if (sw_level.getText().equals("0"))
                            {
                                sw_level.setText("");
                                sw_level.setChecked(false);
                            }

                            txt_customer.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (s.length() < 3) {
                                        txt_customer.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                                        lbl_errorcustomer.setText("Customer name less than 3 character");
                                        lbl_errorcustomer.setVisibility(View.VISIBLE);
                                        txt_email.setEnabled(false);
                                    }
                                    else {
                                        txt_customer.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                                        lbl_errorcustomer.setVisibility(View.GONE);
                                        txt_email.setEnabled(true);
                                    }
                                }
                            });

                            txt_email.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!isValidEmail(txt_email.getText().toString().trim())) {
                                        txt_email.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                                        lbl_erroremail.setVisibility(View.VISIBLE);
                                        sw_status.setEnabled(false);
                                        sw_level.setEnabled(false);
                                        btn_submit.setEnabled(false);
                                    }
                                    else
                                    {
                                        txt_email.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                                        lbl_erroremail.setVisibility(View.GONE);
                                        sw_status.setEnabled(true);
                                        sw_level.setEnabled(true);
                                        btn_submit.setEnabled(true);
                                    }
                                }
                            });

                            lovelyCustomDialog.setListener(R.id.form_uac_btnsubmit, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    username = txt_username.getText().toString();
                                    customer = txt_customer.getText().toString();
                                    email    = txt_email.getText().toString();
                                    if (sw_status.isChecked()) {
                                        status = "A";
                                    }
                                    else {
                                        status = "I";
                                    }
                                    if (sw_level.isChecked()) {
                                        level = "1";
                                    }
                                    else {
                                        level = "0";
                                    }

                                    updateDataUac(username, customer, status, level, email);
                                    lovelyCustomDialog.dismiss();
                                }
                            });

                            lovelyCustomDialog.setListener(R.id.form_uac_btnreset, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    username = txt_username.getText().toString();
                                    resetPassword(username);
                                    lovelyCustomDialog.dismiss();
                                }
                            });

                            lovelyCustomDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("user_account", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updateDataUac(final String username, final String customer, final String status, final String level, final String email)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATEUACURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "success";
                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String decrypt_info1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));

                        if (jsonObject.names().get(0).equals(encrypt_info1))
                        {
                            Toasty.success(getApplicationContext(), decrypt_info1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toasty.warning(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                    }
                    filterData(txt_uac.getText().toString(), req_start);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap <String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("customer", customer);
                hashMap.put("status", status);
                hashMap.put("level", level);
                hashMap.put("email", email);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void resetPassword(final String username)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATEPASSURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "success";
                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String decrypt_info1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));
                        if (jsonObject.names().get(0).equals(encrypt_info1))
                        {
                            Toasty.success(getApplicationContext(), decrypt_info1, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void openDialogAdd()
    {
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lovelyCustomDialog = new LovelyCustomDialog(FormUACActivity.this);
                layoutInflater  = getLayoutInflater();
                view = layoutInflater.inflate(R.layout.form_uac, null);
                lovelyCustomDialog.setTopColorRes(R.color.bootstrap_brand_danger);
                lovelyCustomDialog.setTopTitle("Account Settings");
                lovelyCustomDialog.setIcon(R.drawable.ic_settings);
                lovelyCustomDialog.setTopTitleColor(Color.WHITE);
                lovelyCustomDialog.setView(view);
                lovelyCustomDialog.show();

                txt_username = (BootstrapEditText) view.findViewById(R.id.form_uac_txtusername);
                txt_customer = (BootstrapEditText) view.findViewById(R.id.form_uac_txtcustomer);
                txt_email    = (BootstrapEditText) view.findViewById(R.id.form_uac_txtemail);
                sw_status    = (SwitchButton) view.findViewById(R.id.form_uac_swtstatus);
                sw_level     = (SwitchButton) view.findViewById(R.id.form_uac_swtlevel);
                btn_submit   = (BootstrapButton) view.findViewById(R.id.form_uac_btnsubmit);
                btn_reset    = (BootstrapButton) view.findViewById(R.id.form_uac_btnreset);
                lbl_errorusername = (TextView) view.findViewById(R.id.form_uac_errorUsername);
                lbl_errorcustomer = (TextView) view.findViewById(R.id.form_uac_errorCustomer);
                lbl_erroremail    = (TextView) view.findViewById(R.id.form_uac_errorEmail);

                txt_username.setFocusable(true);
                txt_customer.setEnabled(false);
                txt_email.setEnabled(false);
                sw_level.setEnabled(false);
                sw_status.setEnabled(false);
                btn_submit.setEnabled(false);
                btn_reset.setVisibility(View.GONE);

                txt_username.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 3)
                        {
                            txt_username.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            lbl_errorusername.setVisibility(View.GONE);
                            txt_customer.setEnabled(true);
                        }
                        else {
                            txt_username.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            lbl_errorusername.setVisibility(View.VISIBLE);
                            lbl_errorusername.setText("Username less than 3 character");
                            txt_customer.setEnabled(false);
                        }
                    }
                });

                txt_customer.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 3)
                        {
                            txt_customer.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            lbl_errorcustomer.setVisibility(View.GONE);
                            txt_email.setEnabled(true);
                        }
                        else {
                            txt_customer.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            lbl_errorcustomer.setVisibility(View.VISIBLE);
                            lbl_errorcustomer.setText("Customer name less than 3 character");
                            txt_email.setEnabled(false);
                        }
                    }
                });

                txt_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (isValidEmail(txt_email.getText().toString().trim()))
                        {
                            txt_email.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                            lbl_erroremail.setVisibility(View.GONE);
                            sw_status.setEnabled(true);
                            sw_level.setEnabled(true);
                            btn_submit.setEnabled(true);
                        }
                        else
                        {
                            txt_email.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
                            lbl_erroremail.setVisibility(View.VISIBLE);
                            sw_status.setEnabled(false);
                            sw_level.setEnabled(false);
                            btn_submit.setEnabled(false);
                        }
                    }
                });

                lovelyCustomDialog.setListener(R.id.form_uac_btnsubmit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String dt_username = txt_username.getText().toString();
                        String dt_customer = txt_customer.getText().toString();
                        String dt_email    = txt_email.getText().toString().trim();

                        String dt_status;
                        if (sw_status.isChecked())
                        {
                            dt_status = "A";
                        }
                        else {
                            dt_status = "I";
                        }
                        String dt_level;
                        if (sw_level.isChecked())
                        {
                            dt_level = "1";
                        }
                        else {
                            dt_level = "0";
                        }

                        //Validator username
                        if (dt_username.isEmpty())
                        {
                            //txt_username.setError("Username must be filled");
                            lbl_errorusername.setVisibility(View.VISIBLE);
                        }
                        else {
                            lbl_errorusername.setVisibility(View.GONE);
                        }

                        //Validator customer name
                        if (dt_customer.isEmpty())
                        {
                            //txt_customer.setError("Customer must be filled");
                            lbl_errorcustomer.setVisibility(View.VISIBLE);
                        }
                        else {
                            lbl_errorcustomer.setVisibility(View.GONE);
                        }

                        //Validator email
                        if (!isValidEmail(dt_email))
                        {
                            lbl_erroremail.setVisibility(View.VISIBLE);
                        }
                        else{
                            lbl_erroremail.setVisibility(View.GONE);
                        }

                        //Validator status
                        if (!sw_status.isChecked())
                        {
                            Toasty.warning(getApplicationContext(), "Please activated status to new user", Toast.LENGTH_SHORT).show();
                        }
                        //Submit new user
                        else {
                            //lovelyCustomDialog.dismiss();
                            addNewUAC(dt_username, dt_customer, dt_status, dt_level, dt_email);
                        }
                    }
                });

                lovelyCustomDialog.show();
            }
        });
    }

    private void addNewUAC(final String dt_username, final String dt_customer, final String dt_status, final String dt_level, final String dt_email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDUACURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String info1 = "success";
                try {
                    String encrypt_info1 = MCrypt.bytesToHex(mCrypt.encrypt(info1));
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String decrypt_info1 = new String(mCrypt.decrypt(jsonObject.getString(encrypt_info1)));

                        if (jsonObject.names().get(0).equals(encrypt_info1)) {
                            Toasty.success(getApplicationContext(), decrypt_info1, Toast.LENGTH_SHORT, true).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toasty.warning(getApplicationContext(), "Your input username has exist", Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", dt_username);
                hashMap.put("customer", dt_customer);
                hashMap.put("email", dt_email);
                hashMap.put("status", dt_status);
                hashMap.put("level", dt_level);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}
