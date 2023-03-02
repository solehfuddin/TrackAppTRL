package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
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
import com.sofudev.trackapptrl.Info.InfoOrderHistoryActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ayar.oktay.advancedtextview.AdvancedTextView;
import es.dmoral.toasty.Toasty;

public class SpOtherFragment extends Fragment {
    Config config = new Config();
    DateFormat tglFormat = new DateFormat();
    String URLHOLD = config.Ip_address + config.ordersp_hold_frame;
    String URLREJECT = config.Ip_address + config.ordersp_reject_frame;
    String URLLASTSTATUS = config.Ip_address + config.ordersp_laststatus_frame;
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

    AdvancedTextView txt_status, txt_statusnull, txt_datestatus, txt_duration, txt_invoice;
    UniversalFontTextView lbl_duration;
    LinearLayout linear_invoice;
    Button btn_detail, btnDownload;

    Adapter_approval_sp adapter_approval_sp;
    Adapter_detail_sp adapter_detail_sp;
    List<Data_sp_header> list = new ArrayList<>();
    List<Data_item_sp> itemSp = new ArrayList<>();
    String sales, salesarea, title, startDate, endDate, status, date_in, totalDurr;
    int condition, counter = 0, totalData, total_item, flagCon;
    Context myContext;

    public SpOtherFragment() {
        // Required empty public constructor
    }

    public static SpOtherFragment newInstance(Data_spframe_filter item) {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putString("salesarea", item.getSalesarea());
        bundle.putString("title", item.getTitle());
        bundle.putInt("condition", item.getCondition());
        bundle.putString("startdate", item.getStDate());
        bundle.putString("enddate", item.getEdDate());

        SpOtherFragment fragment = new SpOtherFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        myContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sp_other, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearch = view.findViewById(R.id.fragment_othersp_rpsearch);
        btnPrev = view.findViewById(R.id.fragment_othersp_btnprev);
        btnNext = view.findViewById(R.id.fragment_othersp_btnnext);
        edSearch= view.findViewById(R.id.fragment_othersp_txtJobNumber);
        txtCounter = view.findViewById(R.id.fragment_othersp_txtCounter);
        recyclerView = view.findViewById(R.id.fragment_othersp_recycleview);
        rl_track    = view.findViewById(R.id.fragment_othersp_relativelayout);
        progressBar = view.findViewById(R.id.fragment_othersp_progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);

        setData();

        adapter_approval_sp = new Adapter_approval_sp(getContext(), list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
//                if (condition == 0)
//                {
//                    showBottomDetail(pos);
//                }
//                else
//                {
                    showDialogTrack(pos);
//                }
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
                        switch (condition) {
                            case 11:
                                getDataHold(salesarea,"0", key);
                                break;

                            case 12:
                                getDataReject(salesarea, "0", key);
                                break;

                            case 13:
                                getDataLastStatus(salesarea, "AM", "0", key);
                                break;

                            case 14:
                                getDataLastStatus(salesarea, "AR", "0", key);
                                break;

                            case 15:
                                getDataLastStatus(salesarea, "Warehouse", "0", key);
                                break;

                            case 16:
                                getDataLastStatus(salesarea, "CS", "0", key);
                                break;

                            case 17:
                                getDataLastStatus(salesarea, "Shipping", "0", key);
                                break;
                        }
                    }
                    else
                    {
                        switch (condition) {
                            case 11:
                                getDataHold(salesarea,"0", "");
                                break;

                            case 12:
                                getDataReject(salesarea, "0", "");
                                break;

                            case 13:
                                getDataLastStatus(salesarea, "AM", "0", "");
                                break;

                            case 14:
                                getDataLastStatus(salesarea, "AR", "0", "");
                                break;

                            case 15:
                                getDataLastStatus(salesarea, "Warehouse", "0", "");
                                break;

                            case 16:
                                getDataLastStatus(salesarea, "CS", "0", "");
                                break;

                            case 17:
                                getDataLastStatus(salesarea, "Shipping", "0", "");
                                break;
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
                    switch (condition) {
                        case 11:
                            getDataHold(salesarea,"0", key);
                            break;

                        case 12:
                            getDataReject(salesarea, "0", key);
                            break;

                        case 13:
                            getDataLastStatus(salesarea, "AM", "0", key);
                            break;

                        case 14:
                            getDataLastStatus(salesarea, "AR", "0", key);
                            break;

                        case 15:
                            getDataLastStatus(salesarea, "Warehouse", "0", key);
                            break;

                        case 16:
                            getDataLastStatus(salesarea, "CS", "0", key);
                            break;

                        case 17:
                            getDataLastStatus(salesarea, "Shipping", "0", key);
                            break;
                    }
                }
                else
                {
                    switch (condition) {
                        case 11:
                            getDataHold(salesarea,"0", "");
                            break;

                        case 12:
                            getDataReject(salesarea, "0", "");
                            break;

                        case 13:
                            getDataLastStatus(salesarea, "AM", "0", "");
                            break;

                        case 14:
                            getDataLastStatus(salesarea, "AR", "0", "");
                            break;

                        case 15:
                            getDataLastStatus(salesarea, "Warehouse", "0", "");
                            break;

                        case 16:
                            getDataLastStatus(salesarea, "CS", "0", "");
                            break;

                        case 17:
                            getDataLastStatus(salesarea, "Shipping", "0", "");
                            break;
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = String.valueOf(edSearch.getText());

                switch (flagCon) {
                    case 0:
                        counter = counter - 8;

                        if (key.length() > 0)
                        {
                            getDataHold(salesarea, String.valueOf(counter), key);
                        }
                        else
                        {
                            getDataHold(salesarea, String.valueOf(counter), "");
                        }

                        break;

                    case 1:
                        list.clear();
                        counter = counter - 8;

                        if (key.length() > 0)
                        {
                            getDataReject(salesarea, String.valueOf(counter), key);
                        }
                        else {
                            getDataReject(salesarea, String.valueOf(counter), "");
                        }
                        break;

                    case 2:
                        list.clear();
                        counter = counter - 8;

                        if (key.length() > 0)
                        {
                            switch (condition) {
                                case 13:
                                    getDataLastStatus(salesarea, "AM", String.valueOf(counter), key);
                                    break;

                                case 14:
                                    getDataLastStatus(salesarea, "AR", String.valueOf(counter), key);
                                    break;

                                case 15:
                                    getDataLastStatus(salesarea, "Warehouse", String.valueOf(counter), key);
                                    break;

                                case 16:
                                    getDataLastStatus(salesarea, "CS", String.valueOf(counter), key);
                                    break;

                                case 17:
                                    getDataLastStatus(salesarea, "Shipping", String.valueOf(counter), key);
                                    break;
                            }
                        }
                        else {
                            switch (condition) {
                                case 13:
                                    getDataLastStatus(salesarea, "AM", String.valueOf(counter), "");
                                    break;

                                case 14:
                                    getDataLastStatus(salesarea, "AR", String.valueOf(counter), "");
                                    break;

                                case 15:
                                    getDataLastStatus(salesarea, "Warehouse", String.valueOf(counter), "");
                                    break;

                                case 16:
                                    getDataLastStatus(salesarea, "CS", String.valueOf(counter), "");
                                    break;

                                case 17:
                                    getDataLastStatus(salesarea, "Shipping", String.valueOf(counter), "");
                                    break;
                            }
                        }
                        break;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = String.valueOf(edSearch.getText());

                switch (flagCon) {
                    case 0:
                        counter = counter + 8;

                        if (key.length() > 0)
                        {
                            getDataHold(salesarea, String.valueOf(counter), key);
                        }
                        else
                        {
                            getDataHold(salesarea, String.valueOf(counter), "");
                        }

                        break;

                    case 1:
                        list.clear();
                        counter = counter + 8;

                        if (key.length() > 0)
                        {
                            getDataReject(salesarea, String.valueOf(counter), key);
                        }
                        else
                        {
                            getDataReject(salesarea, String.valueOf(counter), "");
                        }
                        break;

                    case 2:
                        list.clear();
                        counter = counter + 8;

                        if (key.length() > 0)
                        {
                            switch (condition) {
                                case 13:
                                    getDataLastStatus(salesarea, "AM", String.valueOf(counter), key);
                                    break;

                                case 14:
                                    getDataLastStatus(salesarea, "AR", String.valueOf(counter), key);
                                    break;

                                case 15:
                                    getDataLastStatus(salesarea, "Warehouse", String.valueOf(counter), key);
                                    break;

                                case 16:
                                    getDataLastStatus(salesarea, "CS", String.valueOf(counter), key);
                                    break;

                                case 17:
                                    getDataLastStatus(salesarea, "Shipping", String.valueOf(counter), key);
                                    break;
                            }
                        }
                        else {
                            switch (condition) {
                                case 13:
                                    getDataLastStatus(salesarea, "AM", String.valueOf(counter), "");
                                    break;

                                case 14:
                                    getDataLastStatus(salesarea, "AR", String.valueOf(counter), "");
                                    break;

                                case 15:
                                    getDataLastStatus(salesarea, "Warehouse", String.valueOf(counter), "");
                                    break;

                                case 16:
                                    getDataLastStatus(salesarea, "CS", String.valueOf(counter), "");
                                    break;

                                case 17:
                                    getDataLastStatus(salesarea, "Shipping", String.valueOf(counter), "");
                                    break;
                            }
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(SpApprovalFragment.class.getSimpleName(), "Kondisi : " + condition + " di resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(SpApprovalFragment.class.getSimpleName(), "Kondisi : " + condition + " di pause");
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

            list.clear();

            switch (condition) {
                case 11:
                    getDataHold(salesarea,"0", "");
                    break;

                case 12:
                    getDataReject(salesarea, "0", "");
                    break;

                case 13:
                    getDataLastStatus(salesarea, "AM", "0", "");
                    break;

                case 14:
                    getDataLastStatus(salesarea, "AR", "0", "");
                    break;

                case 15:
                    getDataLastStatus(salesarea, "Warehouse", "0", "");
                    break;

                case 16:
                    getDataLastStatus(salesarea, "CS", "0", "");
                    break;

                case 17:
                    getDataLastStatus(salesarea, "Shipping", "0", "");
                    break;
            }
        }
    }

    private void showErrorImage()
    {
        img_track = new ImageView(myContext);
        img_track.setImageResource(R.drawable.notfound);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        img_track.setLayoutParams(lp);
        rl_track.addView(img_track);
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

    private void getDataHold(final String salesarea, final String start, final String key) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLHOLD + start, new Response.Listener<String>() {
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
                            String nama_user = object.getString("nama_user");
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
                            String laststatus = object.getString("laststatus");
                            String lastapprove = object.getString("lastapprove");
                            String lastreason = object.getString("lastreason");
                            int grandTotal = object.getInt("grandTotal");
                            double subtotal = object.getDouble("subtotal");
                            totalData   = object.getInt("totalrow");

                            Data_sp_header data = new Data_sp_header();
                            data.setNoSp(noSp);
                            data.setDate(tgl);
                            data.setTipeSp(type_sp);
                            data.setSales(sales);
                            data.setCustomerName(customer_name);
                            data.setNamaUser(nama_user);
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
                            data.setLastStatus(laststatus);
                            data.setLastApprove(lastapprove);
                            data.setLastReason(lastreason);

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

    private void getDataReject(final String salesarea, final String start, final String key) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLREJECT + start, new Response.Listener<String>() {
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
                            String nama_user = object.getString("nama_user");
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
                            String laststatus = object.getString("laststatus");
                            String lastapprove = object.getString("lastapprove");
                            String lastreason = object.getString("lastreason");
                            int grandTotal = object.getInt("grandTotal");
                            double subtotal = object.getDouble("subtotal");
                            totalData   = object.getInt("totalrow");

                            Data_sp_header data = new Data_sp_header();
                            data.setNoSp(noSp);
                            data.setDate(tgl);
                            data.setTipeSp(type_sp);
                            data.setSales(sales);
                            data.setCustomerName(customer_name);
                            data.setNamaUser(nama_user);
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
                            data.setLastStatus(laststatus);
                            data.setLastApprove(lastapprove);
                            data.setLastReason(lastreason);

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

    private void getDataLastStatus(final String salesarea, final String laststatus, final String start, final String key) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLLASTSTATUS + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);
                flagCon = 2;

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
                            String nama_user = object.getString("nama_user");
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
                            String laststatus = object.getString("laststatus");
                            String lastapprove = object.getString("lastapprove");
                            String lastreason = object.getString("lastreason");
                            int grandTotal = object.getInt("grandTotal");
                            double subtotal = object.getDouble("subtotal");
                            totalData   = object.getInt("totalrow");

                            Data_sp_header data = new Data_sp_header();
                            data.setNoSp(noSp);
                            data.setDate(tgl);
                            data.setTipeSp(type_sp);
                            data.setSales(sales);
                            data.setCustomerName(customer_name);
                            data.setNamaUser(nama_user);
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
                            data.setLastStatus(laststatus);
                            data.setLastApprove(lastapprove);
                            data.setLastReason(lastreason);

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
                hashMap.put("laststatus", laststatus);
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
//                        ripple_btnDetail.setEnabled(false);
//                        btn_detail.setBackgroundColor(Color.parseColor("#58595e"));
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