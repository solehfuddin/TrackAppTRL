package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_courier_invoice;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.CustomLoading;
import com.sofudev.trackapptrl.Custom.MultipleSelectDpodk;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_courier;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.MoneyTextWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class CourierDpodkActivity extends AppCompatActivity implements MultipleSelectDpodk {
    Config config = new Config();
    private String GETCOURIERBYDPODK = config.Ip_address + config.dpodk_getinvbyidtrx;
    private String GETHISTORYCOURIER = config.Ip_address + config.dpodk_historybyidtrx;
    private String GETPROCESSCOURIER = config.Ip_address + config.dpodk_processbyidtrx;
    private String UPDATECOURIERSTS  = config.Ip_address + config.dpodk_changestatus;
    private String SEARCHBYINVNUMBER = config.Ip_address + config.dpodk_searchbyinvnumber;
    private String SEARCHHISTORYBYINV= config.Ip_address + config.dpodk_historybysearchinvnumber;
    private String SEARCHPROCESSBYKEY= config.Ip_address + config.dpodk_processbysearchinvnumber;

    ConstraintLayout constraintSelected;
    RippleView rippleActionBack, rippleActionUpdate;
    UniversalFontTextView txtDpodkNumber, txtSelected, txtActionUpdate;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerLayoutManager;
    CircleProgressBar progressBar;
    ImageView imgNotFound;
    ImageButton btnBack, btnSearch;
    MaterialEditText txtSearch;

    Adapter_courier_invoice adapter_courier_invoice;
    List<Data_courier> listData = new ArrayList<>();
    String partySiteId, username, idDpodk;
    boolean isName, isAdmin;
    int statusDpodk = 0;
    View customView;
    CustomLoading customLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_dpodk);
        customLoading = new CustomLoading(this);

        constraintSelected = (ConstraintLayout) findViewById(R.id.courier_dpodk_moreaction);
        txtDpodkNumber = (UniversalFontTextView) findViewById(R.id.courier_dpodk_txtdpodknumber);
        txtActionUpdate= (UniversalFontTextView) findViewById(R.id.courier_dpodk_actionupdate);
        txtSelected    = (UniversalFontTextView) findViewById(R.id.courier_dpodk_actiontitle);
        recyclerView   = (RecyclerView) findViewById(R.id.courier_dpodk_recycleview);
        progressBar    = (CircleProgressBar) findViewById(R.id.courier_dpodk_progressBar);
        imgNotFound    = (ImageView) findViewById(R.id.courier_dpodk_imgnotfound);
        txtSearch      = (MaterialEditText) findViewById(R.id.courier_dpodk_txtSearch);
        btnBack        = (ImageButton) findViewById(R.id.courier_dpodk_btnback);
        btnSearch      = (ImageButton) findViewById(R.id.courier_dpodk_btnsearch);
        rippleActionBack = (RippleView) findViewById(R.id.courier_dpodk_actionriplebtnback);
        rippleActionUpdate = (RippleView) findViewById(R.id.courier_dpodk_actionriplebtnupdate);


        handleSearch();
        BackToDashboard();
        getUsernameData();
    }

    @SuppressLint("SetTextI18n")
    private void getUsernameData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            partySiteId = bundle.getString("idparty");
            username    = bundle.getString("username");
            idDpodk     = bundle.getString("iddpodk");
            isAdmin     = bundle.getBoolean("isadmin");
            statusDpodk = bundle.getInt("status", 0);
            txtDpodkNumber.setText("#" + idDpodk);

            Log.d("Id party : ", partySiteId);
            Log.d("Id dpodk : ", idDpodk);
            bindingView();

            if (isAdmin)
            {
                getProcessCourierByDpodk(idDpodk);
                txtSearch.setHint("Cari Optik / nomor invoice");
            }
            else
            {
                if (statusDpodk == 0)
                {
                    getCourierByDpodk(idDpodk);
                }
                else
                {
                    getHistoryCourierByDpodk(idDpodk);
                }

                txtSearch.setHint("Cari berdasarkan nomor invoice");
            }
        }
    }

    @SuppressLint("InflateParams")
    private void bindingView()
    {
        customView = LayoutInflater.from(this).inflate(R.layout.bottom_dialog_dpodk_changests, null);

        adapter_courier_invoice = new Adapter_courier_invoice(this, listData, statusDpodk, 0, new RecyclerViewOnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(View view, final int pos, String id) {
                Log.d("NOMOR INVOICE", id);
                if (isAdmin)
                {
                    if (!listData.get(pos).getStatus().equals("null"))
                    {
//                        Toast.makeText(getApplicationContext(), "Dialog untuk ubah nama", Toast.LENGTH_SHORT).show();

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

                        Log.d(CourierDpodkActivity.class.getSimpleName(), "Total inv : " + listData.get(pos).getTotal_inv());

                        final TextWatcher customWatcher = new MoneyTextWatcher(edtBayar);

                        if (!listData.get(pos).getTotal_inv().contains("."))
                        {
                            edtBayar.addTextChangedListener(customWatcher);
                        }

                        edtName.setText(listData.get(pos).getNama_penerima());
                        edtNotes.setText(listData.get(pos).getNote_opd());

                        final BottomDialog bottomDialog = new BottomDialog.Builder(CourierDpodkActivity.this)
                                .setTitle("Ubah Status Dpodk")
                                .setCustomView(customView)
//                            .setCancelable(false)
//                            .autoDismiss(false)
                                .build();

                        final String[] status = {"TERKIRIM", "RETUR"};
                        final String[] warna = {"PUTIH", "MERAH", "TTB", "LAIN-LAIN"};
                        String note;
                        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, status);
                        ArrayAdapter<String> adapterWarna  = new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, warna);
                        //  spinStatus.setAdapter(new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, status));
                        spinStatus.setAdapter(adapterStatus);
                        spinWarna.setAdapter(adapterWarna);
//                        spinWarna.setAdapter(new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, warna));

                        if (listData.get(pos).getStatus() != null)
                        {
                            int statusPos = adapterStatus.getPosition(listData.get(pos).getStatus());
                            spinStatus.setSelection(statusPos);
                        }

                        if (listData.get(pos).getWarna_kertas() != null)
                        {
                            int warnaPos = adapterWarna.getPosition(listData.get(pos).getWarna_kertas());
                            spinWarna.setSelection(warnaPos);
                        }

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

                                    if (spinWarna.getSelectedItem() == "PUTIH")
                                    {
                                        edtNotes.setEnabled(false);
                                        edtNotes.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                                    }
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
//                                edtBayar.requestFocus();
//                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.showSoftInput(edtBayar, InputMethodManager.SHOW_IMPLICIT);

                                    edtBayar.setText(listData.get(pos).getTotal_inv());
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
                        txtTotalInv.setText("Rp. " + CurencyFormat(listData.get(pos).getTotal_inv().replace(",", ".")));
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
                }
                else {
                    if(statusDpodk == 0)
                    {
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

                        Log.d(CourierDpodkActivity.class.getSimpleName(), "Total inv : " + listData.get(pos).getTotal_inv());

                        final TextWatcher customWatcher = new MoneyTextWatcher(edtBayar);

                        if (!listData.get(pos).getTotal_inv().contains("."))
                        {
                            edtBayar.addTextChangedListener(customWatcher);
                        }

                        edtName.setText("");
                        edtNotes.setText("");

                        final BottomDialog bottomDialog = new BottomDialog.Builder(CourierDpodkActivity.this)
                                .setTitle("Ubah Status Dpodk")
                                .setCustomView(customView)
//                            .setCancelable(false)
//                            .autoDismiss(false)
                                .build();

                        final String[] status = {"TERKIRIM", "RETUR"};
                        final String[] warna = {"PUTIH", "MERAH", "TTB", "LAIN-LAIN"};
                        String note;
                        spinStatus.setAdapter(new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, status));
                        spinWarna.setAdapter(new ArrayAdapter<>(CourierDpodkActivity.this, R.layout.spin_framemodel_item, warna));

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

                                    if (spinWarna.getSelectedItem() == "PUTIH")
                                    {
                                        edtNotes.setEnabled(false);
                                        edtNotes.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                                    }
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
//                                edtBayar.requestFocus();
//                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                                imm.showSoftInput(edtBayar, InputMethodManager.SHOW_IMPLICIT);

                                    edtBayar.setText(listData.get(pos).getTotal_inv());
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
                        txtTotalInv.setText("Rp. " + CurencyFormat(listData.get(pos).getTotal_inv().replace(",", ".")));
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
                }
            }
        }, this);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        recyclerView.setAdapter(adapter_courier_invoice);
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

    private void handleSearch(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (txtSearch.getText().toString().isEmpty())
                {
                    if (isAdmin)
                    {
                        getProcessCourierByDpodk(idDpodk);
                    }
                    else
                    {
                        if (statusDpodk == 0)
                        {
                            getCourierByDpodk(idDpodk);
                        }
                        else
                        {
                            getHistoryCourierByDpodk(idDpodk);
                        }
                    }
                }
                else
                {
                    if (isAdmin)
                    {
                        searchProcessCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                    }
                    else
                    {
                        if (statusDpodk == 0)
                        {
                            searchCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                        }
                        else
                        {
                            searchHistoryCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                        }
                    }
                }
            }
        });

        txtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    if (txtSearch.getText().toString().isEmpty())
                    {
                        if (isAdmin)
                        {
                            getProcessCourierByDpodk(idDpodk);
                        }
                        else
                        {
                            if (statusDpodk == 0)
                            {
                                getCourierByDpodk(idDpodk);
                            }
                            else
                            {
                                getHistoryCourierByDpodk(idDpodk);
                            }
                        }
                    }
                    else
                    {
                        if (isAdmin)
                        {
                            searchProcessCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                        }
                        else
                        {
                            if (statusDpodk == 0)
                            {
                                searchCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                            }
                            else
                            {
                                searchHistoryCourierByInvoice(idDpodk, txtSearch.getText().toString().trim());
                            }
                        }
                    }
                }

                return false;
            }
        });
    }

    private void BackToDashboard()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getCourierByDpodk(final String iddpodk)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETCOURIERBYDPODK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_dpodk", iddpodk);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getHistoryCourierByDpodk(final String iddpodk)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETHISTORYCOURIER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_trx", iddpodk);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getProcessCourierByDpodk(final String iddpodk)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, GETPROCESSCOURIER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_trx", iddpodk);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchCourierByInvoice(final String notrx, final String keyword)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, SEARCHBYINVNUMBER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("id_kurir", partySiteId);
                map.put("no_trx", notrx);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchHistoryCourierByInvoice(final String notrx, final String keyword)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, SEARCHHISTORYBYINV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("id_kurir", partySiteId);
                map.put("no_trx", notrx);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void searchProcessCourierByInvoice(final String notrx, final String keyword)
    {
        listData.clear();
        progressBar.setVisibility(View.VISIBLE);
        customLoading.showLoadingDialog();

        StringRequest request = new StringRequest(Request.Method.POST, SEARCHPROCESSBYKEY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                customLoading.dismissLoadingDialog();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("error"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            Toasty.error(getApplicationContext(), "Data not found", Toast.LENGTH_SHORT).show();
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

                            listData.add(dt);
                            imgNotFound.setVisibility(View.GONE);
                        }

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
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imgNotFound.setVisibility(View.VISIBLE);
                customLoading.dismissLoadingDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("keyword", keyword);
                map.put("no_trx", notrx);
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

                        if (isAdmin)
                        {
                            getProcessCourierByDpodk(idDpodk);
                        }
                        else
                        {
                            getCourierByDpodk(idDpodk);
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

    private void multiChangeStatus(final ArrayList<Data_courier> item, final String status, final String penerima,
                                   final String kertas, final String totalBayar, final String keterangan,
                                   final BottomDialog dialog, final BootstrapEditText edtNama, final BootstrapEditText edtNote){
        for (int i = 0; i < item.size(); i++)
        {
            final int finalI = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATECOURIERSTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.names().get(0).equals("success"))
                        {
                            Toasty.success(getApplicationContext(), "Data berhasil disimpan", Toast.LENGTH_SHORT).show();

                            if (finalI + 1 == item.size())
                            {
                                edtNama.setText("");
                                edtNote.setText("");
                                dialog.dismiss();
                                getCourierByDpodk(idDpodk);

                                adapter_courier_invoice.removeTemp();
                                constraintSelected.setVisibility(View.GONE);
                            }
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
                    map.put("no_trx", item.get(finalI).getNo_trx());
                    map.put("noInvoice", item.get(finalI).getNo_inv());
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
    }

    @Override
    public void passResultData(final ArrayList<Data_courier> courierList) {
        String opticName = "";
        String custNo = "";
        String tgl = "";
        String catatan = "-";
        String totalInv = "";
        String invNumber;
        double totalTmp = 0;
        StringBuilder invTmp = new StringBuilder();

        for (int i = 0; i < courierList.size(); i++)
        {
            Log.d(CourierDpodkActivity.class.getSimpleName(), "No invoice : " + courierList.get(i).getNo_inv());

            opticName = courierList.get(i).getNama_optik();
            custNo    = courierList.get(i).getCust_no();
            tgl       = courierList.get(i).getTgl();
            catatan   = courierList.get(i).getNote();

            invTmp.append(courierList.get(i).getNo_inv().concat(","));

            totalTmp += Double.parseDouble(courierList.get(i).getTotal_inv());
        }

        totalInv  = "Rp. " + CurencyFormat(String.valueOf(totalTmp));
        invNumber = invTmp.toString();

        if (courierList.size() > 0)
        {
            invNumber = invNumber.substring(0, invNumber.length() - 1);
        }

        if (courierList.size() > 0)
        {
            constraintSelected.setVisibility(View.VISIBLE);

            String caption = courierList.size() + " Data dipilih";
            txtSelected.setText(caption);
        }
        else
        {
            constraintSelected.setVisibility(View.GONE);
        }

        rippleActionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter_courier_invoice.removeTemp();
                constraintSelected.setVisibility(View.GONE);
            }
        });

        final String finalOpticName = opticName;
        final String finalCustNo = custNo;
        final String finalTgl = tgl;
        final String finalCatatan = catatan;
        final String finalTotalInv = totalInv;
        final String finalInvNumber = invNumber;
        rippleActionUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View multiView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_dpodk_multichange, null);
                UniversalFontComponents.init(getApplicationContext());

                UniversalFontTextView txtOpticName, txtInvoiceNumber, txtCustNo, txtTotalInv, txtDate, txtCatatan;
                final Spinner spinStatus, spinWarna;
                final BootstrapEditText edtName, edtNotes;
                RippleView btnSave, btnExit;

                final BottomDialog bottomDialog = new BottomDialog.Builder(CourierDpodkActivity.this)
                        .setTitle("Ubah Status Dpodk")
                        .setCustomView(multiView)
//                        .setCancelable(false)
//                        .autoDismiss(false)
                        .build();

                txtOpticName = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txtopticname);
                txtInvoiceNumber = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txtinvnumber);
                txtCustNo = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txtcustno);
                txtTotalInv = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txttotalinv);
                txtDate = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txttgl);
                txtCatatan = (UniversalFontTextView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_txtcatatan);
                spinStatus = (Spinner) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_spinstatus);
                spinWarna = (Spinner) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_spinkertas);
                edtName = (BootstrapEditText) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_edtNama);
                edtNotes = (BootstrapEditText) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_edtNote);
                btnSave = (RippleView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_btnsave);
                btnExit = (RippleView) multiView.findViewById(R.id.bottom_dialog_dpodkchangemulti_btncancel);

                txtOpticName.setText(finalOpticName);
                txtInvoiceNumber.setText(finalInvNumber);
                txtCustNo.setText(finalCustNo);
                txtDate.setText(finalTgl);
                txtCatatan.setText(finalCatatan);
                txtTotalInv.setText(finalTotalInv);

                final String[] status = {"TERKIRIM", "RETUR"};
                final String[] warna = {"PUTIH", "MERAH", "TTB", "LAIN-LAIN"};
                spinStatus.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spin_framemodel_item, status));
                spinWarna.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.spin_framemodel_item, warna));

                spinStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String item = adapterView.getItemAtPosition(i).toString();
                        Log.d("Selected Status", item);

                        if (item.equals("RETUR"))
                        {
                            edtName.setText("");
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

                            if (spinWarna.getSelectedItem() == "PUTIH")
                            {
                                edtNotes.setEnabled(false);
                                edtNotes.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                            }
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
                            edtNotes.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(edtNotes, InputMethodManager.SHOW_IMPLICIT);

                            edtNotes.setEnabled(true);
                            edtNotes.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                        }
                        else
                        {
                            edtNotes.setEnabled(false);
                            edtNotes.setBootstrapBrand(DefaultBootstrapBrand.SECONDARY);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String warna;

                        if (spinStatus.getSelectedItem().equals("RETUR"))
                        {
                            warna = "LAIN-LAIN";
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

                        if (isName)
                        {
                            multiChangeStatus(courierList,
                                    spinStatus.getSelectedItem().toString(),edtName.getText().toString().toUpperCase().trim(),
                                    warna, "", edtNotes.getText().toString().toUpperCase().trim(),bottomDialog,
                                    edtName, edtNotes);
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
                        bottomDialog.dismiss();
                    }
                });

                bottomDialog.show();
            }
        });
    }
}