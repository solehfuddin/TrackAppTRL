package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sofudev.trackapptrl.Data.Data_item_sp;
import com.sofudev.trackapptrl.Data.Data_sp_header;
import com.sofudev.trackapptrl.Data.Data_spframe_filter;
import com.sofudev.trackapptrl.Form.FormTrackingSpActivity;
import com.sofudev.trackapptrl.Form.SpApprovalActivity;
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

import ayar.oktay.advancedtextview.AdvancedTextView;
import es.dmoral.toasty.Toasty;

public class SpApprovalFragment extends Fragment {
    Config config = new Config();
    DateFormat tglFormat = new DateFormat();
    String URLUNPROCESS = config.Ip_address + config.ordersp_unprocess_frame;
    String URLPROCESS   = config.Ip_address + config.ordersp_process_frame;
    String FINDUNPROCESS= config.Ip_address + config.ordersp_unprocess_frame_search;
    String FINDPROCESSS = config.Ip_address + config.ordersp_process_frame_search;
    String URLITEMSP  = config.Ip_address + config.ordersp_item_detailsp;
    String URLAPPROVESP = config.Ip_address + config.ordersp_update_approvalsp;
    String URLREJECTSP = config.Ip_address + config.ordersp_update_rejectsp;
    String URL_GETINV      = config.Ip_address + config.ordersp_get_inv;
    String URL_GETSTATUSSP = config.Ip_address + config.ordersp_get_statusSp;

    RippleView btnSearch, ripple_btnDetail, ripple_btndownload;
    BootstrapButton btnPrev, btnNext;
    MaterialEditText edSearch;
    UniversalFontTextView txtCounter;
    RecyclerView recyclerView;
    CircleProgressBar progressBar;
    RelativeLayout rl_track;
    ImageView img_track;
    RecyclerView.LayoutManager recyclerLayoutManager;
    View custom;

    AdvancedTextView  txt_status, txt_statusnull, txt_datestatus, txt_duration, txt_invoice;
    UniversalFontTextView lbl_duration;
    LinearLayout linear_invoice;
    Button btn_detail, btnDownload;

    Adapter_approval_sp adapter_approval_sp;
    Adapter_detail_sp adapter_detail_sp;
    List<Data_sp_header> list = new ArrayList<>();
    List<Data_item_sp> itemSp = new ArrayList<>();
    String sales, salesarea, startDate, endDate, status, date_in, totalDurr;
    int condition, counter = 0, totalData, total_item, flagCon;

    public SpApprovalFragment() {
        // Required empty public constructor
    }

    public static SpApprovalFragment newInstance(Data_spframe_filter item) {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putString("salesarea", item.getSalesarea());
        bundle.putInt("condition", item.getCondition());
        bundle.putString("startdate", item.getStDate());
        bundle.putString("enddate", item.getEdDate());

        SpApprovalFragment fragment = new SpApprovalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sp_approval, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearch = view.findViewById(R.id.fragment_approvalsp_rpsearch);
        btnPrev = view.findViewById(R.id.fragment_approvalsp_btnprev);
        btnNext = view.findViewById(R.id.fragment_approvalsp_btnnext);
        edSearch= view.findViewById(R.id.fragment_approvalsp_txtJobNumber);
        txtCounter = view.findViewById(R.id.fragment_approvalsp_txtCounter);
        recyclerView = view.findViewById(R.id.fragment_approvalsp_recycleview);
        rl_track    = view.findViewById(R.id.fragment_approvalsp_relativelayout);
        progressBar = view.findViewById(R.id.fragment_approvalsp_progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);

        setData();

        adapter_approval_sp = new Adapter_approval_sp(getContext(), list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                if (!list.get(pos).getStatus().equals("AM"))
                {
                    if (condition == 0)
                    {
                        showBottomDetail(pos);
                    }
                }
                else
                {
                    showDialogTrack(pos);
                }
            }
        });

        recyclerView.setAdapter(adapter_approval_sp);

        edSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER))
                {
                    String key = String.valueOf(edSearch.getText());

                    if (key.length() > 0)
                    {
                        if (condition == 0)
                        {
                            findDataUnprocess(salesarea, key, "0");
                        }
                        else
                        {
                            findDataProcess(salesarea, key, "0");
                        }
                    }
                    else
                    {
                        if (condition == 0)
                        {
                            getDataUnprocess(salesarea, "0");
                        }
                        else
                        {
                            getDataProcess(salesarea, "0");
                        }
                    }
                }
                return true;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                String key = String.valueOf(edSearch.getText());

                if (key.length() > 0)
                {
                    if (condition == 0)
                    {
                        findDataUnprocess(salesarea, key, "0");
                    }
                    else
                    {
                        findDataProcess(salesarea, key, "0");
                    }
                }
                else
                {
                    if (condition == 0)
                    {
                        getDataUnprocess(salesarea, "0");
                    }
                    else
                    {
                        getDataProcess(salesarea, "0");
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (flagCon) {
                    case 0:
                        counter = counter - 8;

                        if (condition == 0)
                        {
                            getDataUnprocess(salesarea, String.valueOf(counter));
                        }
                        else
                        {
                            getDataProcess(salesarea, String.valueOf(counter));
                        }

                        break;

                    case 1:
                        list.clear();
                        counter = counter - 1;

                        if (counter <= total_item) {
                            if (condition == 0)
                            {
                                findDataUnprocess(salesarea, edSearch.getText().toString(), String.valueOf(counter));
                            }
                            else
                            {
                                findDataProcess(salesarea, edSearch.getText().toString(), String.valueOf(counter));
                            }
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

                        if (condition == 0)
                        {
                            getDataUnprocess(salesarea, String.valueOf(counter));
                        }
                        else
                        {
                            getDataProcess(salesarea, String.valueOf(counter));
                        }

                        break;

                    case 1:
                        list.clear();
                        counter = counter + 1;

                        if (counter <= total_item) {
                            if (condition == 0)
                            {
                                findDataUnprocess(salesarea, edSearch.getText().toString(), String.valueOf(counter));
                            }
                            else
                            {
                                findDataProcess(salesarea, edSearch.getText().toString(), String.valueOf(counter));
                            }
                        }
                        break;
                }
            }
        });
    }

    private void setData()
    {
        Bundle data = this.getArguments();
        if (data != null)
        {
            sales     = data.getString("username");
            salesarea = data.getString("salesarea");
            condition = data.getInt("condition", 0);
            startDate = data.getString("startdate");
            endDate   = data.getString("enddate");

            Log.d(CourierTrackingFragment.class.getSimpleName(), "Salesarea : " + salesarea);

            if (condition == 0)
            {
                getDataUnprocess(salesarea, "0");
            }
            else
            {
                getDataProcess(salesarea, "0");
            }
        }
    }

    private void showDialogTrack(int pos) {
        final UniversalFontTextView txt_jobNumber;
        final AdvancedTextView txt_tipe, txt_nama, txt_kondisi, txt_via;

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_dialog_tracksp);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height= dm.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWidth = (int) (width * 0.98f);
        int dialogHeight= (int) (height * 0.65f);
        layoutParams.width = dialogWidth;
        layoutParams.height= dialogHeight;
        dialog.getWindow().setAttributes(layoutParams);

        linear_invoice  = dialog.findViewById(R.id.form_dialogtracksp_linearinvoice);
        txt_invoice     = dialog.findViewById(R.id.form_dialogtracksp_txtinvoice);
        txt_jobNumber   = dialog.findViewById(R.id.form_dialogtracksp_txtspnumber);
        txt_tipe        = dialog.findViewById(R.id.form_dialogtracksp_txttipe);
        txt_nama        = dialog.findViewById(R.id.form_dialogtracksp_txtnama);
        txt_kondisi     = dialog.findViewById(R.id.form_dialogtracksp_txtkondisi);
        txt_via         = dialog.findViewById(R.id.form_dialogtracksp_txtvia);
        txt_status      = dialog.findViewById(R.id.form_dialogtracksp_txtorderstatus);
        txt_statusnull  = dialog.findViewById(R.id.form_dialogtracksp_txtorderstatusnull);
        txt_datestatus  = dialog.findViewById(R.id.form_dialogtracksp_txtdatestatus);
        lbl_duration    = dialog.findViewById(R.id.form_dialogtracksp_lblduration);
        txt_duration    = dialog.findViewById(R.id.form_dialogtracksp_txtduration);

        btn_detail      = dialog.findViewById(R.id.form_dialogtracksp_btnDetail);
        ripple_btnDetail= dialog.findViewById(R.id.form_dialogtracksp_rippleBtnDetail);
        ripple_btndownload = dialog.findViewById(R.id.form_dialogtracksp_rippleBtnDownload);
        btnDownload     = dialog.findViewById(R.id.form_dialogtracksp_btnDownload);
        final String noSp = list.get(pos).getNoSp();
        final String tipe = list.get(pos).getTipeSp();

        getStatusSp(noSp);

        txt_jobNumber.setText("Job Number #" + noSp);
        txt_tipe.setText(tipe);
        txt_nama.setText(list.get(pos).getCustomerName());
        txt_kondisi.setText(list.get(pos).getConditions());
        txt_via.setText(list.get(pos).getOrderVia());

        getInv(noSp);

        txt_invoice.setTextColor(Color.parseColor("#0275d8"));
        txt_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://180.250.96.154/trl-webs/index.php/PrintReceipt/invdetail/"
                        + txt_invoice.getText().toString()));
                startActivity(openBrowser);
            }
        });

        ripple_btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), InfoOrderHistoryActivity.class);
                intent.putExtra("order_number", noSp);
                intent.putExtra("is_sp", 1);
                startActivity(intent);
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

        if (!getActivity().isFinishing()){
            dialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showBottomDetail(final int pos) {
        custom = LayoutInflater.from(getContext()).inflate(R.layout.bottom_dialog_approvalsp, null);
        final BottomDialog bottomDialog = new BottomDialog.Builder(requireContext())
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
        UniversalFontTextView txtSubtotal = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtbiayaitem);
        UniversalFontTextView txtDiskon   = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtbiayadiskon);
        UniversalFontTextView txtTotal    = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtbiayatotal);
        TextView txtCicil = (TextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtcicilan);
        UniversalFontTextView txtAlamatKirim = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtalamatkirim);
        UniversalFontTextView txtCatatan = (UniversalFontTextView) custom.findViewById(R.id.bottomdialog_approvalsp_txtcatatan);
        RecyclerView rvItem = (RecyclerView) custom.findViewById(R.id.bottomdialog_approvalsp_rvItem);
        RippleView btnApprove = (RippleView) custom.findViewById(R.id.bottomdialog_approvalsp_rippleBtnApprove);
        RippleView btnReject  = (RippleView) custom.findViewById(R.id.bottomdialog_approvalsp_rippleBtnReject);

        rvItem.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        rvItem.setLayoutManager(recyclerLayoutManager);

        getItemSp(list.get(pos).getNoSp());

        adapter_detail_sp = new Adapter_detail_sp(getContext(), itemSp);
        rvItem.setAdapter(adapter_detail_sp);

        String disc = "";
        double discVal = 0;
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
        if (Double.parseDouble(list.get(pos).getDiscount()) > 0)
        {
            disc = list.get(pos).getDiscount() + " (%)";
            discVal = Double.parseDouble(list.get(pos).getDiscount()) / 100 * list.get(pos).getSubTotal();

        }
        else
        {
            disc = "Rp. " + CurencyFormat(list.get(pos).getDiscountValue().replace(",", "."));
            discVal = Double.parseDouble(list.get(pos).getDiscountValue());
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
        txtSubtotal.setText("Rp. " + CurencyFormat(String.valueOf(list.get(pos).getSubTotal())).replace(",", "."));
        txtDiskon.setText("Rp. - " + CurencyFormat(String.valueOf(discVal)).replace(",", "."));
        txtDiskon.setTextColor(Color.parseColor("#f90606"));
        txtTotal.setText("Rp. " + CurencyFormat(String.valueOf(list.get(pos).getGrandTotal())).replace(",", "."));
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

    private void showDialogReason(final String spNumber) {
        final BootstrapEditText edReason;
        final RippleView btnSave, btnCancel;

        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.form_reason_rejectsp);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
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

        dialog.show();
    }

    private void updateApprovalSp(final String noSp, final String salesname){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLAPPROVESP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.success(getContext(), "Sp sudah disetujui", Toast.LENGTH_SHORT).show();

                        if (condition == 0)
                        {
                            getDataUnprocess(salesarea, "0");
                        }
                        else
                        {
                            getDataProcess(salesarea, "0");
                        }
                    }
                    else
                    {
                        Toasty.warning(getContext(), "Gagal menyimpan data, harap coba kembali !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toasty.error(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_sp", noSp);
                map.put("salesarea", salesname);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void updatRejectSp(final String noSp, final String reason, final String salesname){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLREJECTSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Toasty.warning(getContext(), "Sp sudah ditolak", Toast.LENGTH_SHORT).show();

                        if (condition == 0)
                        {
                            getDataUnprocess(salesarea, "0");
                        }
                        else
                        {
                            getDataProcess(salesarea, "0");
                        }
                    }
                    else
                    {
                        Toasty.warning(getContext(), "Gagal menyimpan data, harap coba kembali !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toasty.error(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_sp", noSp);
                map.put("reason", reason);
                map.put("salesarea", salesname);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest);
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

    private void showErrorImage()
    {
        img_track = new ImageView(getContext());
        img_track.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_track.setLayoutParams(lp);
        rl_track.addView(img_track);
    }

    private void getDataUnprocess(final String salesarea, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLUNPROCESS + start, new Response.Listener<String>() {
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
                            double subtotal = object.getDouble("subtotal");
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
                            data.setSubTotal(subtotal);

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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void findDataUnprocess(final String salesarea, final String key, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, FINDUNPROCESS + start, new Response.Listener<String>() {
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
                            double subtotal = object.getDouble("subtotal");
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
                            data.setSubTotal(subtotal);

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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getDataProcess(final String salesarea, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLPROCESS + start, new Response.Listener<String>() {
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
                            double subtotal = object.getDouble("subtotal");
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
                            data.setSubTotal(subtotal);

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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void findDataProcess(final String salesarea, final String key, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, FINDPROCESSS + start, new Response.Listener<String>() {
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
                            double subtotal = object.getDouble("subtotal");
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
                            data.setSubTotal(subtotal);

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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getStatusSp(final String noSp)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETSTATUSSP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    status = object.getString("status");
                    date_in= object.getString("date_in");
                    totalDurr = object.getString("total_durr");

                    Log.d(FormTrackingSpActivity.class.getSimpleName(), "Last Status : " + status);

                    if (date_in.equals("null") || totalDurr.equals("Dalam proses"))
                    {
                        if (status.contains("REJECT AM"))
                        {
                            txt_statusnull.setTextColor(Color.parseColor("#f90606"));
                        }

                        txt_statusnull.setText(status);

                        txt_statusnull.setVisibility(View.VISIBLE);
                        txt_status.setVisibility(View.GONE);
                        txt_datestatus.setVisibility(View.GONE);
                        lbl_duration.setVisibility(View.GONE);
                        txt_duration.setVisibility(View.GONE);
                        ripple_btnDetail.setEnabled(false);
                        btn_detail.setBackgroundColor(Color.parseColor("#58595e"));
//                        ripple_btndownload.setEnabled(false);
//                        btnDownload.setBackgroundColor(Color.parseColor("#58595e"));
                    }
                    else {
                        if (status.equals("AR")){
                            String approval = object.getString("approve");
                            if (!approval.equals("null"))
                            {
                                status = approval + " BY " + status;

                                if(approval.contains("APPROVE"))
                                {
                                    txt_status.setBackgroundColor(Color.parseColor("#45ac2d"));
                                    txt_status.setTextColor(Color.WHITE);
//                                    ripple_btndownload.setEnabled(true);
//                                    btnDownload.setBackgroundColor(Color.parseColor("#45ac2d"));
//                                    txt_status.setHintTextColor(Color.parseColor("#fff"));
                                }
                                else
                                {
                                    txt_status.setBackgroundColor(Color.parseColor("#f90606"));
                                    txt_status.setTextColor(Color.WHITE);
//                                    ripple_btndownload.setEnabled(false);
//                                    btnDownload.setBackgroundColor(Color.parseColor("#58595e"));
//                                    txt_status.setHintTextColor(Color.parseColor("#fff"));
                                }
                            }
                        }

                        txt_status.setText(status);
                        txt_datestatus.setText(tglFormat.Indotime(date_in));
                        txt_duration.setText(totalDurr);

                        txt_status.setVisibility(View.VISIBLE);
                        txt_statusnull.setVisibility(View.GONE);
                        txt_datestatus.setVisibility(View.VISIBLE);
                        lbl_duration.setVisibility(View.VISIBLE);
                        txt_duration.setVisibility(View.VISIBLE);
                        ripple_btnDetail.setEnabled(true);
                        btn_detail.setBackgroundColor(Color.parseColor("#ff9100"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("no_sp", noSp);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getInv(final String noSp)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETINV, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error")){
                        linear_invoice.setVisibility(View.GONE);
                    }
                    else
                    {
                        linear_invoice.setVisibility(View.VISIBLE);
                        txt_invoice.setText(jsonObject.getString("no_inv"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    linear_invoice.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id_sp", noSp);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}