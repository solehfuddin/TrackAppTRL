package com.sofudev.trackapptrl.Form;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_dpodk;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_invoice;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.ForceCloseHandler;
import com.sofudev.trackapptrl.Custom.MultipleSelectDpodk;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.MoneyTextWatcher;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CourierTaskActivity extends AppCompatActivity implements MultipleSelectDpodk {
    Config config = new Config();
    private String GETCOURIER  = config.Ip_address + config.dpodk_getcourierbydate;
    private String GETCOURIERRANGE = config.Ip_address + config.dpodk_getcourierbydaterange;
    private String GETCOURIERSEARCH = config.Ip_address + config.dpodk_getcourierbysearch;
    private String UPDATECOURIERSTS = config.Ip_address + config.dpodk_changestatus;

    RecyclerView recyclerView, recyclerViewInv;
    RecyclerView.LayoutManager recyclerLayoutManager, recyclerLayoutManager1;
    Button btn_filterdate;
    MaterialEditText txt_search, txt_startdate, txt_enddate;
    RippleView rp_search, rp_filterdate;
    ImageButton btn_back, btn_search, btn_filter;
    BootstrapButton btn_prev, btn_next;
    UniversalFontTextView txtcounter;
    CircleProgressBar circleLoading;
    ImageView imgNotfound;
    Switch swInvMode;
    View customView;

    Adapter_courier_dpodk adapter_courier_dpodk;
    Adapter_courier_invoice adapter_courier_invoice;
    List<Data_courier> listData = new ArrayList<>();
    String partySiteId, username, todayDate, start_date, end_date, seachKeyword;
    boolean isName;

    Calendar calendar;
    int day, month, year;
    int offset = 0;
    int start = 1;
    int until = 0;
    int limit = 5;
    int totalData = 0;
    int typeData = 1;
    long lastClick = 0;

    SimpleDateFormat dateFormat;
    CustomLoading customLoading;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_task);
        UniversalFontComponents.init(this);
        Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandler(this));
        customLoading = new CustomLoading(this);

        btn_back        = (ImageButton) findViewById(R.id.courier_task_btnback);
        btn_filter      = (ImageButton) findViewById(R.id.courier_task_btnfilter);
        recyclerView    = (RecyclerView) findViewById(R.id.courier_task_recycleview);
        recyclerViewInv = (RecyclerView) findViewById(R.id.courier_task_recycleviewinv);
        circleLoading   = (CircleProgressBar) findViewById(R.id.courier_task_progressBar);
        btn_prev        = (BootstrapButton) findViewById(R.id.courier_task_btnprev);
        btn_next        = (BootstrapButton) findViewById(R.id.courier_task_btnnext);
        rp_search       = (RippleView) findViewById(R.id.courier_task_rpsearch);
        btn_search      = (ImageButton) findViewById(R.id.courier_task_btnsearch);
        txtcounter      = (UniversalFontTextView) findViewById(R.id.courier_task_txtCounter);
        txt_search      = (MaterialEditText) findViewById(R.id.courier_task_txtSearch);
        imgNotfound     = (ImageView) findViewById(R.id.courier_task_imgnotfound);
        swInvMode       = (Switch) findViewById(R.id.courier_task_swType);

        recyclerView.setHasFixedSize(true);
        recyclerViewInv.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerLayoutManager1 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerViewInv.setLayoutManager(recyclerLayoutManager1);

        txt_search.setHint("Cari berdasarkan nomor dpodk");

        bindingView();
        BackToDashboard();
//        getUsernameData();
        ClickNext();
        ClickPrev();
        ClickFilter();
        handleSearch();

        swInvMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    swInvMode.setText("Dpodk Mode");
                    typeData = 2;
                    limit = 3;

                    txt_search.setHint("Cari berdasarkan nomor invoice");
                }
                else
                {
                    swInvMode.setText("Invoice Mode");
                    typeData = 1;
                    limit = 5;

                    txt_search.setHint("Cari berdasarkan nomor dpodk");
                }

                start = 1;
                offset = 0;
                until = 0;
                until += limit;

                if (start_date != null && end_date != null)
                {
                    getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                }
                else
                {
                    getCourier(partySiteId, todayDate, String.valueOf(typeData));
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        until = 0;
    }

    @SuppressLint("InflateParams")
    private void bindingView(){
        adapter_courier_dpodk = new Adapter_courier_dpodk(getApplicationContext(), listData, 1,false, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d("NOMOR DPODKK", id);
                Intent intent = new Intent(getApplicationContext(), CourierDpodkActivity.class);
                intent.putExtra("idparty", partySiteId);
                intent.putExtra("username", username);
                intent.putExtra("iddpodk", id);
                intent.putExtra("status", 0);
                intent.putExtra("isadmin", false);
                startActivity(intent);
            }
        });

        customView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_dpodk_changests, null);
        adapter_courier_invoice = new Adapter_courier_invoice(getApplicationContext(), listData, 0, 1, new RecyclerViewOnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(View view, final int pos, String id) {
                Log.d("NOMOR INVOICE", id);
                UniversalFontTextView txtOpticName, txtInvoiceNumber, txtDpodkNumber, txtCustNo, txtTotalInv, txtDate, txtTime, txtNote;
                final Spinner spinStatus, spinWarna;
                final BootstrapEditText edtName, edtNotes, edtBayar;
                RippleView btnSave, btnExit;

                txtOpticName = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtopticname);
                txtInvoiceNumber = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtinvnumber);
                txtDpodkNumber = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtdpodknumber);
                txtCustNo      = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtcustno);
                txtTotalInv    = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txttotalinv);
                txtDate        = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txttgl);
                txtTime        = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtjam);
                txtNote        = (UniversalFontTextView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_txtnoteinv);
                spinStatus     = (Spinner) customView.findViewById(R.id.bottom_dialog_dpodkchangests_spinstatus);
                spinWarna      = (Spinner) customView.findViewById(R.id.bottom_dialog_dpodkchangests_spinkertas);
                edtBayar       = (BootstrapEditText) customView.findViewById(R.id.bottom_dialog_dpodkchangests_edtBayar);
                edtName        = (BootstrapEditText) customView.findViewById(R.id.bottom_dialog_dpodkchangests_edtNama);
                edtNotes       = (BootstrapEditText) customView.findViewById(R.id.bottom_dialog_dpodkchangests_edtNote);
                btnSave        = (RippleView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_btnsave);
                btnExit        = (RippleView) customView.findViewById(R.id.bottom_dialog_dpodkchangests_btncancel);

                final TextWatcher customWatcher = new MoneyTextWatcher(edtBayar);

                if (!listData.get(pos).getTotal_inv().contains("."))
                {
                    edtBayar.addTextChangedListener(customWatcher);
                }

                edtName.setText("");
                edtNotes.setText("");

                final BottomDialog bottomDialog = new BottomDialog.Builder(CourierTaskActivity.this)
                        .setTitle("Ubah Status Dpodk")
                        .setCustomView(customView)
                        .build();

                final String[] status = {"TERKIRIM", "RETUR"};
                final String[] warna = {"PUTIH", "MERAH", "TTB", "LAIN-LAIN"};
                String note;
                spinStatus.setAdapter(new ArrayAdapter<>(CourierTaskActivity.this, R.layout.spin_framemodel_item, status));
                spinWarna.setAdapter(new ArrayAdapter<>(CourierTaskActivity.this, R.layout.spin_framemodel_item, warna));

                spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Log.d("Selected Status", item);

                        if (item.equals("RETUR"))
                        {
                            edtName.setEnabled(false);
                            edtName.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);

                            spinWarna.setVisibility(View.INVISIBLE);

                            edtNotes.setEnabled(true);
                            edtNotes.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(edtNotes, InputMethodManager.SHOW_IMPLICIT);

                            edtNotes.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                        }
                        else
                        {
                            edtName.setEnabled(true);
                            edtName.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                            edtName.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(edtName, InputMethodManager.SHOW_IMPLICIT);

                            spinWarna.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        edtName.setEnabled(true);
                    }
                });

                spinWarna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Log.d("Selected Warna", item);

                        if (!item.equals("PUTIH"))
                        {
                            if (!item.equals("MERAH"))
                            {
                                edtNotes.requestFocus();
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(edtNotes, InputMethodManager.SHOW_IMPLICIT);
                            }

                            edtNotes.setEnabled(true);
                            edtNotes.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                        }
                        else
                        {
                            edtNotes.setEnabled(false);
                            edtNotes.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                        }

                        if (item.equals("MERAH"))
                        {
                            edtBayar.setEnabled(true);
                            edtBayar.setTextColor(Color.parseColor("#0275d8"));
                            edtBayar.setBootstrapBrand(DefaultBootstrapBrand.INFO);
//                            edtBayar.requestFocus();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInput(edtBayar, InputMethodManager.SHOW_IMPLICIT);

                            edtBayar.setText(listData.get(pos).getTotal_inv().replace(",", "."));
                            edtBayar.setSelection(edtBayar.getText().length());
                        }
                        else
                        {
                            edtBayar.setEnabled(false);
                            edtBayar.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                            edtBayar.setTextColor(Color.parseColor("#b4b3b3"));
                            edtBayar.setText("0");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                if (listData.get(pos).getNote().isEmpty())
                {
                    note = "-";
                }
                else
                {
                    note = listData.get(pos).getNote().toUpperCase();
                }

                txtOpticName.setText(listData.get(pos).getNama_optik());
                txtInvoiceNumber.setText(listData.get(pos).getNo_inv());
                txtNote.setText(note);
                txtDpodkNumber.setText(listData.get(pos).getNo_trx());
                txtCustNo.setText(listData.get(pos).getCust_no());
                txtTotalInv.setText("Rp. " + CurencyFormat(listData.get(pos).getTotal_inv().replace(",",".")));
                txtDate.setText(listData.get(pos).getTgl_kirim());
                txtTime.setText(listData.get(pos).getJam_berangkat());

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String totalBayar = edtBayar.getText().toString().replace("Rp", "").replace(".", "").trim();
                        Log.d(CourierTaskActivity.class.getSimpleName(), "Total Bayar : " + totalBayar);
                        String warna;

                        if (spinStatus.getSelectedItem().equals("RETUR"))
                        {
                            warna = "LAIN-LAIN";
                            totalBayar = "";
                            isName = true;
                        }
                        else
                        {
                            warna = spinWarna.getSelectedItem().toString();

                            if (edtName.getText().length() > 0)
                            {
                                isName = true;
                            }
                            else
                            {
                                isName = false;
                            }
                        }

                        if (!spinWarna.getSelectedItem().equals("MERAH"))
                        {
                            totalBayar = "";
                        }

                        if (isName)
                        {
                            if (listData.get(pos).getTotal_inv().contains("."))
                            {
                                totalBayar = edtBayar.getText().toString().replace(".", ",");
                            }

                            changeStatus(listData.get(pos).getNo_trx(), listData.get(pos).getNo_inv(), spinStatus.getSelectedItem().toString(),
                                    edtName.getText().toString().toUpperCase().trim(), warna, totalBayar,
                                    edtNotes.getText().toString().toUpperCase().trim(), bottomDialog,
                                    edtName, edtNotes);

                            edtBayar.removeTextChangedListener(customWatcher);
//                            edtBayar.clearFocus();
                        }
                        else
                        {
                            Toasty.warning(getApplicationContext(), "Nama penerima belum diisi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        edtBayar.removeTextChangedListener(customWatcher);
                        bottomDialog.dismiss();
                    }
                });

                bottomDialog.show();
            }
        }, this);

        recyclerView.setAdapter(adapter_courier_dpodk);
        recyclerViewInv.setAdapter(adapter_courier_invoice);
        recyclerViewInv.setVisibility(View.GONE);
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###.##";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void BackToDashboard()
    {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ClickNext()
    {
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset += limit;
                start += limit;
                until += limit;

                if (txt_search.getText().length() == 0)
                {
                    if (start_date != null && end_date != null)
                    {
                        getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                    }
                    else
                    {
                        getCourier(partySiteId, todayDate, String.valueOf(typeData));
                    }
                }
                else
                {
                    getCourierSearch(partySiteId, seachKeyword, String.valueOf(typeData));
                }


                if (totalData < until)
                {
                    btn_next.setEnabled(false);
                }
            }
        });
    }

    private void ClickPrev()
    {
        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offset -= limit;
                start -= limit;
                until -= limit;

                if (txt_search.getText().length() == 0)
                {
                    if (start_date != null && end_date != null)
                    {
                        getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                    }
                    else
                    {
                        getCourier(partySiteId, todayDate, String.valueOf(typeData));
                    }
                }
                else
                {
                    getCourierSearch(partySiteId, seachKeyword, String.valueOf(typeData));
                }

                if (start == 1)
                {
                    btn_prev.setEnabled(false);
                }
                else
                {
                    btn_prev.setEnabled(true);
                }
            }
        });
    }

    private void handleSearch(){
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                seachKeyword = txt_search.getText().toString().trim();
                if (seachKeyword.isEmpty())
                {
                    start = 1;
                    offset = 0;
                    until = 0;
                    until += limit;

                    if (start_date != null && end_date != null)
                    {
                        getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                    }
                    else
                    {
                        getCourier(partySiteId, todayDate, String.valueOf(typeData));
                    }
                }
                else
                {
                    start = 1;
                    offset = 0;
                    until = 0;
                    until += limit;

                    getCourierSearch(partySiteId, seachKeyword, String.valueOf(typeData));
                }
            }
        });

        txt_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    seachKeyword = txt_search.getText().toString().trim();
                    if (seachKeyword.isEmpty())
                    {
                        start = 1;
                        offset = 0;
                        until = 0;
                        until += limit;

                        if (start_date != null && end_date != null)
                        {
                            getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                        }
                        else
                        {
                            getCourier(partySiteId, todayDate, String.valueOf(typeData));
                        }
                    }
                    else
                    {
                        start = 1;
                        offset = 0;
                        until = 0;
                        until += limit;

                        getCourierSearch(partySiteId, seachKeyword, String.valueOf(typeData));
                    }
                }

                return false;
            }
        });
    }

    private void ClickFilter(){
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(CourierTaskActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_trackdate);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                calendar = Calendar.getInstance();

                year  = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day   = calendar.get(Calendar.DATE);

                txt_startdate = (MaterialEditText) dialog.findViewById(R.id.filter_track_startdate);
                txt_enddate   = (MaterialEditText) dialog.findViewById(R.id.filter_track_enddate);
                btn_filterdate= (Button) dialog.findViewById(R.id.filter_track_btnok);
                rp_filterdate = (RippleView) dialog.findViewById(R.id.filter_track_ripple_btnok);

                txt_startdate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (SystemClock.elapsedRealtime() - lastClick < 2000)
                        {
                            return false;
                        }

                        lastClick = SystemClock.elapsedRealtime();

                        showStartDate();
                        return false;
                    }
                });

                txt_enddate.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (SystemClock.elapsedRealtime() - lastClick < 2000)
                        {
                            return false;
                        }

                        lastClick = SystemClock.elapsedRealtime();

                        showEndDate();
                        return false;
                    }
                });

                rp_filterdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstdate, lastdate;

                        firstdate = txt_startdate.getText().toString();
                        lastdate  = txt_enddate.getText().toString();

                        if (firstdate.isEmpty())
                        {
                            txt_startdate.setError("Please choose start date");
                        }
                        else if (lastdate.isEmpty())
                        {
                            txt_enddate.setError("Please choose end date");
                        }
                        else
                        {
                            dialog.dismiss();
                            start = 1;
                            offset = 0;
                            until = 0;
                            until += limit;
//                            showTrackOrderyByDaterange(req_start, id_data, start_date, end_date);
                            getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                        }
                    }
                });
            }
        });
    }

    private void showStartDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CourierTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String mon;
                String day;

                if (month == 1)
                {
                    mon = "JAN";
                }
                else if (month == 2)
                {
                    mon = "FEB";
                }
                else if (month == 3)
                {
                    mon = "MAR";
                }
                else if (month == 4)
                {
                    mon = "APR";
                }
                else if (month == 5)
                {
                    mon = "MAY";
                }
                else if (month == 6)
                {
                    mon = "JUN";
                }
                else if (month == 7)
                {
                    mon = "JUL";
                }
                else if (month == 8)
                {
                    mon = "AUG";
                }
                else if (month == 9)
                {
                    mon = "SEP";
                }
                else if (month == 10)
                {
                    mon = "OCT";
                }
                else if (month == 11)
                {
                    mon = "NOV";
                }
                else
                {
                    mon = "DEC";
                }

                if (dayOfMonth < 10)
                {
                    day = "0" + dayOfMonth;
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }

                txt_startdate.setText(day + "-" + mon + "-" + year);
                start_date = year + "-" + month + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void showEndDate()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(CourierTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = (month + 1);
                String day;
                String mon;

                if (month == 1)
                {
                    mon = "JAN";
                }
                else if (month == 2)
                {
                    mon = "FEB";
                }
                else if (month == 3)
                {
                    mon = "MAR";
                }
                else if (month == 4)
                {
                    mon = "APR";
                }
                else if (month == 5)
                {
                    mon = "MAY";
                }
                else if (month == 6)
                {
                    mon = "JUN";
                }
                else if (month == 7)
                {
                    mon = "JUL";
                }
                else if (month == 8)
                {
                    mon = "AUG";
                }
                else if (month == 9)
                {
                    mon = "SEP";
                }
                else if (month == 10)
                {
                    mon = "OCT";
                }
                else if (month == 11)
                {
                    mon = "NOV";
                }
                else
                {
                    mon = "DEC";
                }

                if (dayOfMonth < 10)
                {
                    day = "0" + dayOfMonth;
                }
                else
                {
                    day = String.valueOf(dayOfMonth);
                }

                txt_enddate.setText(day + "-" + mon + "-" + year);
                end_date = year + "-" + month + "-" + day;
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            partySiteId = bundle.getString("idparty");
            username = bundle.getString("username");
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            todayDate = dateFormat.format(Calendar.getInstance().getTime());

            Log.d("Id party : ", partySiteId);
            Log.d("Today date : ", todayDate);

            until += limit;

//            getCourier(partySiteId, todayDate, String.valueOf(typeData));

            if (txt_search.getText().length() == 0)
            {
                if (start_date != null && end_date != null)
                {
                    getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                }
                else
                {
                    getCourier(partySiteId, todayDate, String.valueOf(typeData));
                }
            }
            else
            {
                getCourierSearch(partySiteId, seachKeyword, String.valueOf(typeData));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUsernameData();
    }

    private void getCourier(final String id, final String tgl, final String type){
        listData.clear();
        circleLoading.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETCOURIER + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                circleLoading.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotfound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewInv.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txtcounter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotfound.setVisibility(View.GONE);

                            if (typeData == 1)
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerViewInv.setVisibility(View.GONE);
                            }
                            else
                            {
                                recyclerViewInv.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                        adapter_courier_invoice.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                imgNotfound.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerViewInv.setVisibility(View.GONE);
                circleLoading.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("date", tgl);
                map.put("id_kurir", id);
                map.put("type", type);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getCourierSearch(final String id, final String keyword, final String type){
        listData.clear();
        circleLoading.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETCOURIERSEARCH + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                circleLoading.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotfound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewInv.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txtcounter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotfound.setVisibility(View.GONE);

                            if (typeData == 1)
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerViewInv.setVisibility(View.GONE);
                            }
                            else
                            {
                                recyclerViewInv.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                        adapter_courier_invoice.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                imgNotfound.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerViewInv.setVisibility(View.GONE);
                circleLoading.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("id_kurir", id);
                map.put("type", type);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getCourierRange(final String id, final String tglSt, final String tglEd, final String type){
        listData.clear();
        circleLoading.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETCOURIERRANGE + "/" + limit + "/" + offset, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                circleLoading.setVisibility(View.GONE);
                btn_next.setEnabled(true);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            btn_prev.setEnabled(false);
                            btn_next.setEnabled(false);
                            imgNotfound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            recyclerViewInv.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_LONG, true).show();
                        }
                        else
                        {
                            Data_courier dt = new Data_courier();
                            dt.setNama_optik(jsonObject.getString("nama_optik"));
                            dt.setCust_no(jsonObject.getString("cust_no"));
                            dt.setNo_inv(jsonObject.getString("no_inv"));
                            dt.setNo_trx(jsonObject.getString("no_trx"));
                            dt.setTotal_inv(jsonObject.getString("total_inv"));
                            dt.setJumlah_dibayar(jsonObject.getString("jumlah_dibayar"));
                            dt.setTgl(jsonObject.getString("tgl"));
                            dt.setWarna_kertas(jsonObject.getString("warna_kertas"));
                            dt.setTgl_kembali(jsonObject.getString("tgl_kembali"));
                            dt.setStatus(jsonObject.getString("status"));
                            dt.setBatal_kirim(jsonObject.getString("batal_kirim"));
                            dt.setNote(jsonObject.getString("note"));
                            dt.setNote_opd(jsonObject.getString("note_opd"));
                            dt.setNoinv_ar(jsonObject.getString("noinv_ar"));
                            dt.setUser(jsonObject.getString("user"));
                            dt.setNama_penerima(jsonObject.getString("nama_penerima"));
                            dt.setKeterangan(jsonObject.getString("keterangan"));
                            dt.setNama_kurir(jsonObject.getString("nama_kurir"));
                            dt.setRute(jsonObject.getString("rute"));
                            dt.setJam_berangkat(jsonObject.getString("jam_berangkat"));
                            dt.setTgl_kirim(jsonObject.getString("tgl_kirim"));

                            totalData = jsonObject.getInt("total_row");

                            String counter = "Show " + start + " - " + until + " from " + totalData + " data";
                            txtcounter.setText(counter);

                            if (start == 1)
                            {
                                btn_prev.setEnabled(false);
                            }
                            else
                            {
                                btn_prev.setEnabled(true);
                            }

                            if (totalData <= until)
                            {
                                btn_next.setEnabled(false);
                            }

                            listData.add(dt);
                            imgNotfound.setVisibility(View.GONE);

                            if (typeData == 1)
                            {
                                recyclerView.setVisibility(View.VISIBLE);
                                recyclerViewInv.setVisibility(View.GONE);
                            }
                            else
                            {
                                recyclerViewInv.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        adapter_courier_dpodk.notifyDataSetChanged();
                        adapter_courier_invoice.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                imgNotfound.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                recyclerViewInv.setVisibility(View.GONE);
                circleLoading.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("dateSt", tglSt);
                map.put("dateEd", tglEd);
                map.put("id_kurir", id);
                map.put("type", type);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void changeStatus(final String noDpodk, final String noInv, final String status, final String penerima,
                              final String kertas, final String totalBayar, final String keterangan,
                              final BottomDialog dialog, final BootstrapEditText edtNama, final BootstrapEditText edtNote){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATECOURIERSTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                        start = 1;
                        offset = 0;
                        until = 0;
                        until += limit;

                        if (txt_search.getText().length() == 0)
                        {
                            if (start_date != null && end_date != null)
                            {
                                getCourierRange(partySiteId, start_date, end_date, String.valueOf(typeData));
                            }
                            else
                            {
                                getCourier(partySiteId, todayDate, String.valueOf(typeData));
                            }
                        }
                        else
                        {
                            getCourierSearch(partySiteId, txt_search.getText().toString(), String.valueOf(typeData));
                        }

                        edtNama.setText("");
                        edtNote.setText("");
                        dialog.dismiss();
                    }
                    else
                    {
                        Toasty.warning(getApplicationContext(), "Gagal menyimpan data, harap coba kembali !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toasty.error(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_trx", noDpodk);
                map.put("noInvoice", noInv);
                map.put("status", status);
                map.put("jumlah_dibayar", totalBayar);
                map.put("nama_penerima", penerima);
                map.put("warna_kertas", kertas);
                map.put("keterangan", keterangan);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void passResultData(ArrayList<Data_courier> courierList) {

    }
}