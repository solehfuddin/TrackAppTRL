package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.sofudev.trackapptrl.Adapter.Adapter_assign_bin;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_assignbin;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_assignbin_filter;
import com.sofudev.trackapptrl.Data.Data_assignbin_header;
import com.sofudev.trackapptrl.Data.Data_assignbin_line;
import com.sofudev.trackapptrl.Form.ReturnBinActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ReturnbinFragment extends Fragment implements View.OnClickListener {
    Config config = new Config();
    String URL_GETHEADERRETUR = config.Ip_address + config.assignbin_getdataretur;
    String URL_GETLINERETUR   = config.Ip_address + config.assignbin_getlineretur;

    View custom;
    RippleView btnSearch;
    MaterialEditText edSearch;
    RecyclerView recyclerView, rvItem;
    BootstrapEditText edNote;
    BootstrapButton btnPrev, btnNext;
    UniversalFontTextView txtCounter, txtFlag, txtTotalQty, txtNote;
    ProgressBar progressBarItem;
    CircleProgressBar progressBar;
    RelativeLayout rl_track;
    LinearLayout lin_action;
    ImageView img_track, imgItemNotFound;
    Switch swFlag;

    RecyclerView.LayoutManager recyclerLayoutManager;
    Adapter_assign_bin adapter_assign_bin;
    Adapter_detail_assignbin adapter_detail_assignbin;
    List<Data_assignbin_header> listHeader = new ArrayList<>();
    List<Data_assignbin_line> listLine = new ArrayList<>();

    Context myContext;
    String username, custname, partysiteid, noinv, flag;
    int status, counter = 0, totalData;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public ReturnbinFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReturnbinFragment newInstance(Data_assignbin_filter item) {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putString("custname", item.getCustname());
        bundle.putString("title", item.getTitle());
        bundle.putInt("condition", item.getCondition());
        bundle.putString("partysiteid", item.getPartysiteid());
        bundle.putString("noinv", item.getNoinv());

        ReturnbinFragment fragment = new ReturnbinFragment();
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
        return inflater.inflate(R.layout.fragment_returnbin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fabAdd = view.findViewById(R.id.fragment_returbin_fabAdd);
        txtCounter  = view.findViewById(R.id.fragment_returbin_txtCounter);
        btnNext     = view.findViewById(R.id.fragment_returbin_btnnext);
        btnPrev     = view.findViewById(R.id.fragment_returbin_btnprev);
        btnSearch   = view.findViewById(R.id.fragment_returbin_rpsearch);
        edSearch    = view.findViewById(R.id.fragment_returbin_txtJobNumber);
        recyclerView= view.findViewById(R.id.fragment_returbin_recycleview);
        progressBar = view.findViewById(R.id.fragment_returbin_progressBar);
        rl_track    = view.findViewById(R.id.fragment_returbin_relativelayout);
        lin_action   = view.findViewById(R.id.fragment_returbin_rl_tool);
        txtFlag     = view.findViewById(R.id.fragment_returbin_txtflag);
        swFlag      = view.findViewById(R.id.fragment_returbin_swflag);

        setData();

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        adapter_assign_bin = new Adapter_assign_bin(myContext, listHeader, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                showBottomDetail(pos);
            }
        });
        recyclerView.setAdapter(adapter_assign_bin);

        btnSearch.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        fabAdd.setOnClickListener(this);

        swFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b)
                {
                    counter = 0;
                    txtFlag.setText("Selesai");
                    flag = "Selesai";
                    storingPref();

                    getDataHeader(partysiteid, "COMPLETE", String.valueOf(edSearch.getText()), String.valueOf(counter));
                }
                else
                {
                    counter = 0;
                    txtFlag.setText("Menunggu");
                    flag = "Menunggu";
                    storingPref();

                    getDataHeader(partysiteid, "RETUR", String.valueOf(edSearch.getText()), String.valueOf(counter));
                }
            }
        });

        edSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    counter = 0;

                    if (counter <= totalData) {
                        if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu"))
                        {
                            getDataHeader(partysiteid, "RETUR", String.valueOf(edSearch.getText()), String.valueOf(counter));
                        }
                        else
                        {
                            getDataHeader(partysiteid, "COMPLETE", String.valueOf(edSearch.getText()), String.valueOf(counter));
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_returbin_btnnext:
                counter = counter + 5;

                if (counter <= totalData) {
                    if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu"))
                    {
                        getDataHeader(partysiteid, "RETUR", String.valueOf(edSearch.getText()), String.valueOf(counter));
                    }
                    else
                    {
                        getDataHeader(partysiteid, "COMPLETE", String.valueOf(edSearch.getText()), String.valueOf(counter));;
                    }
                }
                break;
            case R.id.fragment_returbin_btnprev:
                counter = counter - 5;

                if (counter <= totalData) {
                    if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu"))
                    {
                        getDataHeader(partysiteid, "RETUR", String.valueOf(edSearch.getText()), String.valueOf(counter));
                    }
                    else
                    {
                        getDataHeader(partysiteid, "COMPLETE", String.valueOf(edSearch.getText()), String.valueOf(counter));;
                    }
                }
                break;
            case R.id.fragment_returbin_rpsearch:
                counter = 0;

                if (counter <= totalData) {
                    if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu"))
                    {
                        getDataHeader(partysiteid, "RETUR", String.valueOf(edSearch.getText()), String.valueOf(counter));
                    }
                    else
                    {
                        getDataHeader(partysiteid, "COMPLETE", String.valueOf(edSearch.getText()), String.valueOf(counter));;
                    }
                }
                break;
            case R.id.fragment_returbin_fabAdd:
                Intent intent = new Intent(getContext(), ReturnBinActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("custname", custname);
                intent.putExtra("partysiteid", partysiteid);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData()
    {
        Bundle data = this.getArguments();
        if (data != null)
        {
            username  = data.getString("username");
            custname  = data.getString("custname");
            status    = data.getInt("condition", 1);
            noinv     = data.getString("noinv");
            partysiteid  = data.getString("partysiteid");

            initialisePref();

            if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu"))
            {
                swFlag.setChecked(false);
                txtFlag.setText("Menunggu");
                getDataHeader(partysiteid, "RETUR", "", "0");
            }
            else
            {
                swFlag.setChecked(true);
                txtFlag.setText("Selesai");
                getDataHeader(partysiteid, "COMPLETE", "", "0");
            }

            Log.d(ReturnbinFragment.class.getSimpleName(), "Status : " + pref.getString("STATUSRETUR", "Menunggu"));
            Log.d(ReturnBinActivity.class.getSimpleName(), "username : " + username);
        }
    }

    private void initialisePref(){
        pref = myContext.getSharedPreferences("PrefReturBin", 0);
    }

    private void storingPref(){
        editor = pref.edit();
        editor.putString("STATUSRETUR", flag);
        editor.apply();
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

    @SuppressLint("SetTextI18n")
    private void showBottomDetail(final int pos) {
        custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_assignbin, null);
        final BottomDialog bottomDialog = new BottomDialog.Builder(myContext)
                .setTitle("Informasi Detail")
                .setCustomView(custom)
                .build();

        RecyclerView.LayoutManager recyclerLayoutManager;

        UniversalFontTextView txtId         = custom.findViewById(R.id.bottomdialog_assignbin_txtinvnumber);
        UniversalFontTextView txtDate       = custom.findViewById(R.id.bottomdialog_assignbin_txttglsp);
        progressBarItem                     = custom.findViewById(R.id.bottomdialog_assignbin_progress);
        txtTotalQty                         = custom.findViewById(R.id.bottomdialog_assignbin_txtqtytotal);
        rvItem                              = custom.findViewById(R.id.bottomdialog_assignbin_rvItem);
        imgItemNotFound                     = custom.findViewById(R.id.bottomdialog_assignbin_imgNotFound);
        edNote                              = custom.findViewById(R.id.bottomdialog_assignbin_edtNote);
        txtNote                             = custom.findViewById(R.id.bottomdialog_assignbin_txtnote);
        RippleView btnCancel                = custom.findViewById(R.id.bottomdialog_assignbin_btncancel);
        RippleView btnSave                  = custom.findViewById(R.id.bottomdialog_assignbin_btnsave);
        ImageView imageView                 = custom.findViewById(R.id.bottomdialog_assignbin_icpackage);

        txtId.setText(listHeader.get(pos).getAssignId());
        txtDate.setText(listHeader.get(pos).getAssignDate());

        rvItem.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(myContext);
        rvItem.setLayoutManager(recyclerLayoutManager);

        if (pref.getString("STATUSRETUR", "Menunggu").equals("Menunggu")){
            getDataLine(partysiteid, "RETUR", listHeader.get(pos).getAssignId());
        }
        else
        {
            getDataLine(partysiteid, "COMPLETE", listHeader.get(pos).getAssignId());
        }

        adapter_detail_assignbin = new Adapter_detail_assignbin(myContext, listLine);
        rvItem.setAdapter(adapter_detail_assignbin);
        imageView.setImageResource(R.drawable.return_bin);
        edNote.setVisibility(View.GONE);
        txtNote.setVisibility(View.VISIBLE);

        btnSave.setVisibility(View.GONE);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show();
    }

    private void getDataHeader(final String idSales, final String status, final String noinv, final String start){
        progressBar.setVisibility(View.VISIBLE);
        listHeader.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETHEADERRETUR + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                int from, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error")){
                            showErrorImage();
                            lin_action.setVisibility(View.GONE);
                        }
                        else
                        {
                            totalData = object.getInt("totalRow");

                            Data_assignbin_header item = new Data_assignbin_header();
                            item.setAssignId(object.getString("assignId"));
                            item.setAssignDate(object.getString("assignDate"));
                            item.setAssignSales(object.getString("assignSales"));
                            item.setAssignBy(object.getString("assignBy"));
                            item.setAssignFlag(object.getString("assignFlag"));
                            item.setTotalRow(object.getString("totalRow"));

                            listHeader.add(item);

                            from  = counter + 1;
                            until = jsonArray.length() + counter;

                            String message = "show " + from + " - " + until + " from " + totalData + " data";
                            lin_action.setVisibility(View.VISIBLE);
                            txtCounter.setText(message);

                            if (totalData == until)
                            {
                                btnNext.setEnabled(false);
                            }

                            if (from == 1)
                            {
                                btnPrev.setEnabled(false);
                            }
                            else
                            {
                                btnPrev.setEnabled(true);
                            }
                        }
                    }

                    adapter_assign_bin.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(myContext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("idsales", idSales);
                map.put("status", status);
                map.put("noinv", noinv);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDataLine(final String idSales, final String status, final String noinv){
        listLine.clear();
        progressBarItem.setVisibility(View.VISIBLE);
        rvItem.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URL_GETLINERETUR, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String totalQty = "";
                    String note = "";

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
                            //Ekse ketika error
                            imgItemNotFound.setVisibility(View.VISIBLE);
                            rvItem.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_assignbin_line item = new Data_assignbin_line();
                            item.setItemId(object.getString("itemId"));
                            item.setItemQty(object.getString("itemQty"));
                            item.setItemCode(object.getString("itemCode"));
                            item.setItemCategory(object.getString("itemCategory"));
                            item.setItemName(object.getString("itemName"));
                            item.setItemDesc(object.getString("itemDesc"));
                            item.setItemOrg(object.getString("itemOrg"));
                            item.setItemNote(object.getString("itemNote"));
                            item.setTotalQty(object.getString("totalQty"));

                            totalQty = object.getString("totalQty");
                            note = object.getString("itemNote");

                            if (note.equals("null"))
                            {
                                note = "";
                            }

                            listLine.add(item);

                            imgItemNotFound.setVisibility(View.GONE);
                            rvItem.setVisibility(View.VISIBLE);
                        }
                    }

                    progressBarItem.setVisibility(View.GONE);
                    txtTotalQty.setText(totalQty + " Pcs");
                    edNote.setText(note);
                    edNote.setSelection(note.length());
                    txtNote.setText(note);
                    adapter_detail_assignbin.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(myContext, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("idsales", idSales);
                map.put("status", status);
                map.put("noinv", noinv);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}