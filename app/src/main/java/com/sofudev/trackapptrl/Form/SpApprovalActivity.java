package com.sofudev.trackapptrl.Form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannedString;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_approval_sp;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_sp;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_detail_sp;
import com.sofudev.trackapptrl.Data.Data_item_sp;
import com.sofudev.trackapptrl.Data.Data_sp_header;
import com.sofudev.trackapptrl.Data.Data_track_order;
import com.sofudev.trackapptrl.Info.InfoOrderHistoryActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ayar.oktay.advancedtextview.AdvancedTextView;
import es.dmoral.toasty.Toasty;

public class SpApprovalActivity extends AppCompatActivity {
    Config config = new Config();
    String URLGETDATA = config.Ip_address + config.ordersp_get_approvalsp;
    String URLSEARCHDATA = config.Ip_address + config.ordersp_search_approvalsp;
    String URLITEMSP  = config.Ip_address + config.ordersp_item_detailsp;
    String URLAPPROVESP = config.Ip_address + config.ordersp_update_approvalsp;
    String URLREJECTSP = config.Ip_address + config.ordersp_update_rejectsp;

    RippleView btnBack, btnSearch, ripple_btnApprove, ripple_btnReject, ripple_btndownload;
    BootstrapButton btnPrev, btnNext;
    MaterialEditText edSearch;
    UniversalFontTextView txtCounter;
    RecyclerView recyclerView;
    CircleProgressBar progressBar;
    RelativeLayout rl_track;
    ImageView img_track;
    RecyclerView.LayoutManager recyclerLayoutManager;

    Adapter_approval_sp adapter_approval_sp;
    Adapter_detail_sp adapter_detail_sp;
    List<Data_sp_header> list = new ArrayList<>();
    List<Data_item_sp> itemSp = new ArrayList<>();

    String sales, areacode;
    int counter = 0, totalData,total_item, flagCon;
    View custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_approval);

        btnBack = findViewById(R.id.form_approvalsp_ripplebtnback);
        btnSearch = findViewById(R.id.form_approvalsp_rpsearch);
        btnPrev = findViewById(R.id.form_approvalsp_btnprev);
        btnNext = findViewById(R.id.form_approvalsp_btnnext);
        edSearch= findViewById(R.id.form_approvalsp_txtJobNumber);
        txtCounter = findViewById(R.id.form_approvalsp_txtCounter);
        recyclerView = findViewById(R.id.form_approvalsp_recycleview);
        rl_track    = findViewById(R.id.form_approvalsp_relativelayout);
        progressBar = findViewById(R.id.form_approvalsp_progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        getData();

        adapter_approval_sp = new Adapter_approval_sp(getApplicationContext(), list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                if (!list.get(pos).getStatus().equals("AM"))
                {
//                    showDialogApproval(pos);
                    showBottomDetail(pos);
                }
                else
                {
                    Toasty.warning(getApplicationContext(), "Sp sudah disetujui", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(adapter_approval_sp);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = String.valueOf(edSearch.getText());

                if (key.length() > 0)
                {
                    getSearchDataApproval(areacode, key, "0");
                }
                else
                {
                    getDataApproval(areacode, String.valueOf(counter));
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flagCon) {
                    case 0:
                        counter = counter - 8;
                        getDataApproval(areacode, String.valueOf(counter));
                        break;

                    case 1:
                        list.clear();
                        counter = counter - 1;

                        if (counter <= total_item) {
                            getSearchDataApproval(areacode, edSearch.getText().toString(), String.valueOf(counter));
                        }
                        break;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flagCon) {
                    case 0:
                        counter = counter + 8;
                        getDataApproval(areacode, String.valueOf(counter));
                        break;

                    case 1:
                        list.clear();
                        counter = counter + 1;

                        if (counter <= total_item) {
                            getSearchDataApproval(areacode, edSearch.getText().toString(), String.valueOf(counter));
                        }
                        break;
                }
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            sales     = bundle.getString("username");
            areacode  = bundle.getString("salesarea");

            Log.d("NAMA SALES", sales);

            getDataApproval(areacode, "0");
        }
    }

    private void showErrorImage()
    {
        img_track = new ImageView(getApplicationContext());
        img_track.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_track.setLayoutParams(lp);
        rl_track.addView(img_track);
    }

    @SuppressLint("SetTextI18n")
    private void showBottomDetail(final int pos) {
        custom = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_dialog_approvalsp, null);
        final BottomDialog bottomDialog = new BottomDialog.Builder(SpApprovalActivity.this)
                .setTitle("No SP : " + list.get(pos).getNoSp())
                .setCustomView(custom)
                .build();

        RecyclerView.LayoutManager recyclerLayoutManager;

        UniversalFontTextView txtTipesp = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtsptipe);
        UniversalFontTextView txtNamasales = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txttitletgl);
        UniversalFontTextView txtTglsp  = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txttgl);
        UniversalFontTextView txtPemesan = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtpemesan);
        UniversalFontTextView txtNoakun = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtaccno);
        UniversalFontTextView txtNamaoptik = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtoptik);
        UniversalFontTextView txtAlamat = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtalamat);
        UniversalFontTextView txtOrdervia = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtvia);
        UniversalFontTextView txtDisc   = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtdisc);
        UniversalFontTextView txtSyarat = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtsyarat);
        UniversalFontTextView txtDp     = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtdp);
        UniversalFontTextView txtTitledp= (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txttitledp);
        UniversalFontTextView txtTitlecicil = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txttitlecicilan);
        TextView txtCicil = (TextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtcicilan);
        UniversalFontTextView txtAlamatKirim = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtalamatkirim);
        UniversalFontTextView txtCatatan = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtcatatan);
        RecyclerView rvItem = (RecyclerView) custom.findViewById(R.id.bottomdialog_approvalsp_rvItem);
        RippleView btnApprove = (RippleView) custom.findViewById(R.id.bottomdialog_approvalsp_rippleBtnApprove);
        RippleView btnReject  = (RippleView) custom.findViewById(R.id.bottomdialog_approvalsp_rippleBtnReject);

        rvItem.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(this);
        rvItem.setLayoutManager(recyclerLayoutManager);

        getItemSp(list.get(pos).getNoSp());

        adapter_detail_sp = new Adapter_detail_sp(this, itemSp);
        rvItem.setAdapter(adapter_detail_sp);

        String disc = "";
        String note = "";
        String alamatKirim = "";
        String kondisi = "";

        int numBulan = (getNumberOfMonth(list.get(pos).getStartInstallment()) + Integer.parseInt(list.get(pos).getInstallment())) - 1;
        String strBulan = numBulan > 12 ? getMonOfNumber(numBulan - 12) : getMonOfNumber(numBulan);

        String defYear = list.get(pos).getDate().substring(list.get(pos).getDate().length() - 4);
        int untilYear  = numBulan > 12 ? Integer.parseInt(defYear + 1) : Integer.parseInt(defYear);

        int cicilanBulanan = (list.get(pos).getGrandTotal() - Integer.parseInt(list.get(pos).getDownPayment())) / Integer.parseInt(list.get(pos).getInstallment());
        String cicilan = "Sebanyak (" + list.get(pos).getInstallment() + ") Kali, Sejumlah Rp. " + CurencyFormat(String.valueOf(cicilanBulanan).replace(",", ".")) + " / bulan <br/>"
                + "Dimulai Sejak Bulan <b>" + list.get(pos).getStartInstallment() + " " + defYear + "</b> Sampai dengan Bulan <b>" + strBulan + " " + untilYear + "</b>";
        if (Integer.parseInt(list.get(pos).getDiscount()) > 0)
        {
            disc = list.get(pos).getDiscount() + " (%)";
        }
        else
        {
            disc = "Rp. " + CurencyFormat(list.get(pos).getDiscountValue().replace(",", "."));
        }

        if (list.get(pos).getConditions().contains("Cicilan"))
        {
            txtDp.setVisibility(View.VISIBLE);
            txtTitledp.setVisibility(View.VISIBLE);

            txtCicil.setVisibility(View.VISIBLE);
            txtTitlecicil.setVisibility(View.VISIBLE);

            kondisi = list.get(pos).getConditions();
        }
        else
        {
            txtDp.setVisibility(View.GONE);
            txtTitledp.setVisibility(View.GONE);

            txtCicil.setVisibility(View.GONE);
            txtTitlecicil.setVisibility(View.GONE);

            kondisi = list.get(pos).getConditions() + " / Rp. " + CurencyFormat(list.get(pos).getDownPayment().replace(",", "."));
        }

        if (list.get(pos).getCatatan().isEmpty())
        {
            note = "-";
        }
        else
        {
            note = list.get(pos).getCatatan();
        }

        if (list.get(pos).getShippingAddress().isEmpty())
        {
            alamatKirim = list.get(pos).getAddress();
        }
        else
        {
            alamatKirim = list.get(pos).getShippingAddress();
        }

        txtTipesp.setText(list.get(pos).getTipeSp());
        txtNamasales.setText(list.get(pos).getSales());
        txtTglsp.setText(list.get(pos).getDate());
        txtPemesan.setText(getPemesanSp(list.get(pos).getShipNumber()));
        txtNoakun.setText(list.get(pos).getShipNumber());
        txtNamaoptik.setText(list.get(pos).getCustomerName());
        txtAlamat.setText(list.get(pos).getAddress() + " - " + list.get(pos).getCity());
        txtOrdervia.setText(list.get(pos).getOrderVia());
        txtDisc.setText(disc);
        txtSyarat.setText(kondisi);
        txtDp.setText("Rp. " + CurencyFormat(list.get(pos).getDownPayment().replace(",", ".")));
        txtAlamatKirim.setText(alamatKirim);
        txtCicil.setText(Html.fromHtml(cicilan));
        txtCatatan.setText(note);

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateApprovalSp(list.get(pos).getNoSp(), sales);
                bottomDialog.dismiss();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogReason(list.get(pos).getNoSp());
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show();
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

    private int getNumberOfMonth(String month){
        switch (month) {
            case "Januari":
                return 1;
            case "Februari":
                return 2;
            case "Maret":
                return 3;
            case "April":
                return 4;
            case "Mei":
                return 5;
            case "Juni":
                return 6;
            case "Juli":
                return 7;
            case "Agustus":
                return 8;
            case "September":
                return 9;
            case "Oktober":
                return 10;
            case "November":
                return 11;
            default:
                return 12;
        }
    }

    private String getMonOfNumber(int mon){
        switch (mon) {
            case 1:
                return "Januari";
            case 2:
                return "Februari";
            case 3:
                return "Maret";
            case 4:
                return "April";
            case 5:
                return "Mei";
            case 6:
                return "Juni";
            case 7:
                return "Juli";
            case 8:
                return "Agustus";
            case 9:
                return "September";
            case 10:
                return "Oktober";
            case 11:
                return "November";
            default:
                return "Desember";
        }
    }

    private String getPemesanSp(String shipNumber){
        switch (shipNumber.substring(0,1)) {
            case "C" :
                return "PERUSAHAAN";
            case "D" :
                return "DOKTER";
            case "R" :
                return "RUMAH SAKIT";
            case "Y" :
                return "YAYASAN";
            default:
                return "OPTIK";
        }
    }

    @SuppressLint("SetTextI18n")
    private void showDialogApproval(final int pos) {
        final UniversalFontTextView txt_jobNumber;
        final AdvancedTextView txt_tipe, txt_nama, txt_kondisi, txt_via;

        final Dialog dialog = new Dialog(SpApprovalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dialog_approvalsp);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int) (width * 0.98f);
        int dialogHeight= (int) (height * 0.65f);
        layoutParams.width = dialogWidth;
        layoutParams.height= dialogHeight;
        dialog.getWindow().setAttributes(layoutParams);

        txt_jobNumber   = dialog.findViewById(R.id.form_dialogapprovalsp_txtspnumber);
        txt_tipe        = dialog.findViewById(R.id.form_dialogapprovalsp_txttipe);
        txt_nama        = dialog.findViewById(R.id.form_dialogapprovalsp_txtnama);
        txt_kondisi     = dialog.findViewById(R.id.form_dialogapprovalsp_txtkondisi);
        txt_via         = dialog.findViewById(R.id.form_dialogapprovalsp_txtvia);

        ripple_btnApprove= dialog.findViewById(R.id.form_dialogapprovalsp_rippleBtnApprove);
        ripple_btnReject = dialog.findViewById(R.id.form_dialogapprovalsp_rippleBtnReject);
        ripple_btndownload = dialog.findViewById(R.id.form_dialogapprovalsp_rippleBtnDownload);
        final String noSp = list.get(pos).getNoSp();
        final String tipe = list.get(pos).getTipeSp();

        txt_jobNumber.setText("SP Number #" + noSp);
        txt_tipe.setText(tipe);
        txt_nama.setText(list.get(pos).getCustomerName());
        txt_kondisi.setText(list.get(pos).getConditions());
        txt_via.setText(list.get(pos).getOrderVia());

        ripple_btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApprovalSp(list.get(pos).getNoSp(), sales);
                dialog.dismiss();
            }
        });

        ripple_btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogReason(list.get(pos).getNoSp());
                dialog.dismiss();
            }
        });

        ripple_btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipe.equals("FRAME") || tipe.equals("Frame"))
                {
//                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://timurrayalab.com/trl-dev/index.php/PrintReceipt/spFrame/" + noSp));
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/PrintReceipt/spFrame/" + noSp));
                    startActivity(openBrowser);
                }
                else
                {

//                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://timurrayalab.com/trl-dev/index.php/PrintReceipt/spLensa/" + noSp));
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/PrintReceipt/spLensa/" + noSp));
                    startActivity(openBrowser);
                }
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
    }

    private void showDialogReason(final String spNumber) {
        final BootstrapEditText edReason;
        final RippleView btnSave, btnCancel;

        final Dialog dialog = new Dialog(SpApprovalActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_reason_rejectsp);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int) (width * 0.98f);
        int dialogHeight= (int) (height * 0.65f);
        layoutParams.width = dialogWidth;
        layoutParams.height= dialogHeight;
        dialog.getWindow().setAttributes(layoutParams);

        edReason   = dialog.findViewById(R.id.form_dialogreasonsp_edtNote);
        btnSave    = dialog.findViewById(R.id.form_dialogreasonsp_rippleBtnApprove);
        btnCancel  = dialog.findViewById(R.id.form_dialogreasonsp_rippleBtnReject);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatRejectSp(spNumber, edReason.getText().toString(), sales);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        if (!isFinishing()){
            dialog.show();
        }
    }

    private void getDataApproval(final String salesarea, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDATA + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 0;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
                            String tgl  = object.getString("date");
                            String type_sp     = object.getString("type_sp");
                            String sales     = object.getString("sales");
                            String customer_name = object.getString("customer_name");
                            String address = object.getString("address");
                            String city  = object.getString("city");
                            String order_via   = object.getString("order_via");
                            String down_payment   = object.getString("down_payment");
                            String discount     = object.getString("discount");
                            String discount_val = object.getString("discount_val");
                            String conditions  = object.getString("conditions");
                            String installment = object.getString("installment");
                            String start_installment = object.getString("start_installment");
                            String shipping_address = object.getString("shipping_address");
                            String photo = object.getString("photo");
                            String path = object.getString("path");
                            String status = object.getString("status");
                            String approvalName = object.getString("approval_name");
                            String shipNumber = object.getString("ship_number");
                            String notes = object.getString("notes");
                            int grandTotal = object.getInt("grandTotal");
                            totalData   = object.getInt("totalrow");

                            Data_sp_header data = new Data_sp_header();
                            data.setNoSp(noSp);
                            data.setDate(tgl);
                            data.setTipeSp(type_sp);
                            data.setSales(sales);
                            data.setCustomerName(customer_name);
                            data.setAddress(address);
                            data.setCity(city);
                            data.setOrderVia(order_via);
                            data.setDownPayment(down_payment);
                            data.setDiscount(discount);
                            data.setDiscountValue(discount_val);
                            data.setConditions(conditions);
                            data.setInstallment(installment);
                            data.setStartInstallment(start_installment);
                            data.setShippingAddress(shipping_address);
                            data.setPhoto(photo);
                            data.setPath(path);
                            data.setStatus(status);
                            data.setApprovalName(approvalName);
                            data.setShipNumber(shipNumber);
                            data.setCatatan(notes);
                            data.setGrandTotal(grandTotal);

                            list.add(data);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + totalData + " data";
                            txtCounter.setText(counter);

                            if (totalData == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_approval_sp.notifyDataSetChanged();
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
                hashMap.put("salesarea", salesarea);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSearchDataApproval(final String salesarea, final String key, final String start) {
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLSEARCHDATA + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 1;

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            String noSp = object.getString("no_sp");
                            String tgl  = object.getString("date");
                            String type_sp     = object.getString("type_sp");
                            String sales     = object.getString("sales");
                            String customer_name = object.getString("customer_name");
                            String address = object.getString("address");
                            String city  = object.getString("city");
                            String order_via   = object.getString("order_via");
                            String down_payment   = object.getString("down_payment");
                            String discount     = object.getString("discount");
                            String discount_val = object.getString("discount_val");
                            String conditions  = object.getString("conditions");
                            String installment = object.getString("installment");
                            String start_installment = object.getString("start_installment");
                            String shipping_address = object.getString("shipping_address");
                            String photo = object.getString("photo");
                            String path = object.getString("path");
                            String status = object.getString("status");
                            String approvalName = object.getString("approval_name");
                            String shipNumber = object.getString("ship_number");
                            String notes = object.getString("notes");
                            int grandTotal = object.getInt("grandTotal");
                            total_item   = object.getInt("totalrow");

                            Data_sp_header data = new Data_sp_header();
                            data.setNoSp(noSp);
                            data.setDate(tgl);
                            data.setTipeSp(type_sp);
                            data.setSales(sales);
                            data.setCustomerName(customer_name);
                            data.setAddress(address);
                            data.setCity(city);
                            data.setOrderVia(order_via);
                            data.setDownPayment(down_payment);
                            data.setDiscount(discount);
                            data.setDiscountValue(discount_val);
                            data.setConditions(conditions);
                            data.setInstallment(installment);
                            data.setStartInstallment(start_installment);
                            data.setShippingAddress(shipping_address);
                            data.setPhoto(photo);
                            data.setPath(path);
                            data.setStatus(status);
                            data.setApprovalName(approvalName);
                            data.setShipNumber(shipNumber);
                            data.setCatatan(notes);
                            data.setGrandTotal(grandTotal);

                            list.add(data);
                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + total_item + " data";
                            txtCounter.setText(counter);

                            if (total_item == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (start == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_approval_sp.notifyDataSetChanged();
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
                hashMap.put("salesarea", salesarea);
                hashMap.put("key", key);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getItemSp(final String idsp) {
        itemSp.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLITEMSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            showErrorImage();
                        }
                        else
                        {
                            Data_item_sp data = new Data_item_sp();
                            data.setLineItem(object.getInt("line_number"));
                            data.setAmount(object.getInt("amount"));
                            data.setQty(object.getInt("qty"));
                            data.setDefaulPrice(object.getInt("unit_standard_price"));
                            data.setDiscount(object.getDouble("discount"));
                            data.setDescription(object.getString("description"));
                            data.setItemCode(object.getString("item_code"));
                            data.setItemId(object.getString("item_id"));
                            data.setUmoCode(object.getString("umo_code"));

                            itemSp.add(data);
                        }
                    }

                    adapter_detail_sp.notifyDataSetChanged();
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
                hashMap.put("id_sp", idsp);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateApprovalSp(final String noSp, final String salesarea){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLAPPROVESP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.success(getApplicationContext(), "Sp sudah disetujui", Toast.LENGTH_SHORT).show();

                        getDataApproval(areacode, String.valueOf(counter));
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
                map.put("no_sp", noSp);
                map.put("salesarea", salesarea);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updatRejectSp(final String noSp, final String reason, final String salesarea){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLREJECTSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.warning(getApplicationContext(), "Sp sudah ditolak", Toast.LENGTH_SHORT).show();

                        getDataApproval(areacode, String.valueOf(counter));
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
                map.put("no_sp", noSp);
                map.put("reason", reason);
                map.put("salesarea", salesarea);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}