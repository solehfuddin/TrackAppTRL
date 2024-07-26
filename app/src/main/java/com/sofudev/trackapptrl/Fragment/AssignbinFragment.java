package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.sofudev.trackapptrl.Adapter.Adapter_assign_bin;
import com.sofudev.trackapptrl.Adapter.Adapter_detail_assignbin;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_assignbin_filter;
import com.sofudev.trackapptrl.Data.Data_assignbin_header;
import com.sofudev.trackapptrl.Data.Data_assignbin_line;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AssignbinFragment extends Fragment {
    Config config = new Config();
    String URLGETHEADER = config.Ip_address + config.assignbin_getheader;
    String URLGETLINE   = config.Ip_address + config.assignbin_getline;
    String URLUPDATE    = config.Ip_address + config.assignbin_updateline;

    View custom;
    RippleView btnSearch;
    MaterialEditText edSearch;
    UniversalFontTextView txtCounter, txtTotalQty;
    BootstrapButton btnPrev, btnNext;
    RecyclerView recyclerView, rvItem;
    CircleProgressBar progressBar;
    RelativeLayout rl_track;
    ImageView img_track, imgItemNotFound;
    ProgressBar progressBarItem;
    BootstrapEditText edNote;
    LinearLayout linearAction;

    RecyclerView.LayoutManager recyclerLayoutManager;
    Adapter_assign_bin adapter_assign_bin;
    Adapter_detail_assignbin adapter_detail_assignbin;
    List<Data_assignbin_header> list = new ArrayList<>();
    List<Data_assignbin_line> listLine = new ArrayList<>();

    Context myContext;
    String username, custname, partysiteid, noinv;
    int counter = 0, totalData, status, condition;

    public AssignbinFragment() {
        // Required empty public constructor
    }

    public static AssignbinFragment newInstance(Data_assignbin_filter item) {
        Bundle bundle = new Bundle();
        bundle.putString("username", item.getUsername());
        bundle.putString("custname", item.getCustname());
        bundle.putString("title", item.getTitle());
        bundle.putInt("condition", item.getCondition());
        bundle.putString("partysiteid", item.getPartysiteid());
        bundle.putString("noinv", item.getNoinv());

        AssignbinFragment fragment = new AssignbinFragment();
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
        return inflater.inflate(R.layout.fragment_assignbin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSearch   = view.findViewById(R.id.fragment_assignbin_rpsearch);
        edSearch    = view.findViewById(R.id.fragment_assignbin_txtJobNumber);
        txtCounter  = view.findViewById(R.id.fragment_assignbin_txtCounter);
        btnPrev     = view.findViewById(R.id.fragment_assignbin_btnprev);
        btnNext     = view.findViewById(R.id.fragment_assignbin_btnnext);
        recyclerView= view.findViewById(R.id.fragment_assignbin_recycleview);
        progressBar = view.findViewById(R.id.fragment_assignbin_progressBar);
        rl_track    = view.findViewById(R.id.fragment_assignbin_relativelayout);
        linearAction= view.findViewById(R.id.fragment_assignbin_rl_tool);

        setData();

        recyclerView.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(myContext);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        adapter_assign_bin = new Adapter_assign_bin(myContext, list, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                showBottomDetail(pos);
            }
        });

        recyclerView.setAdapter(adapter_assign_bin);

        edSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    counter = 0;
                    list.clear();
                    String key = String.valueOf(edSearch.getText());

                    if (key.length() > 0)
                    {
                        condition = 1;
                        getDataHeader(partysiteid, key, String.valueOf(status), "0");
                    }
                    else
                    {
                        condition = 0;
                        getDataHeader(partysiteid, "", String.valueOf(status), "0");
                    }
                }
                return true;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = 0;
                list.clear();
                String key = String.valueOf(edSearch.getText());

                if (key.length() > 0)
                {
                    condition = 1;
                    getDataHeader(partysiteid, key, String.valueOf(status), "0");
                }
                else
                {
                    condition = 0;
                    getDataHeader(partysiteid, "", String.valueOf(status), "0");
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                counter = counter + 5;

                if (counter <= totalData) {
                    if (condition == 0)
                    {
                        getDataHeader(partysiteid, "", String.valueOf(status), String.valueOf(counter));
                    }
                    else
                    {
                        getDataHeader(partysiteid, String.valueOf(edSearch.getText()), String.valueOf(status), String.valueOf(counter));;
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                counter = counter - 5;

                if (counter <= totalData) {
                    if (condition == 0)
                    {
                        getDataHeader(partysiteid, "", String.valueOf(status), String.valueOf(counter));
                    }
                    else
                    {
                        getDataHeader(partysiteid, String.valueOf(edSearch.getText()), String.valueOf(status), String.valueOf(counter));
                    }
                }
            }
        });
    }

    private void setData()
    {
        Bundle data = this.getArguments();
        if (data != null)
        {
            username  = data.getString("username");
            custname  = data.getString("custname");
            //Status 1 => Data BIN Belum Dikonfirmasi
            //Status 2 => Data BIN Sudah Dikonfirmasi
            status    = data.getInt("condition", 1);
            noinv     = data.getString("noinv");
            partysiteid  = data.getString("partysiteid");

            Log.d(CourierTrackingFragment.class.getSimpleName(), "username : " + username);
            list.clear();

            assert noinv != null;
            if (!noinv.isEmpty())
            {
                edSearch.setText(noinv);
                getDataHeader(partysiteid, noinv, String.valueOf(status), "0");
            }
            else
            {
                getDataHeader(partysiteid, "", String.valueOf(status), "0");
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
        RippleView btnSave                  = custom.findViewById(R.id.bottomdialog_assignbin_btnsave);

        txtId.setText(list.get(pos).getAssignId());
        txtDate.setText(list.get(pos).getAssignDate());

        rvItem.setHasFixedSize(true);
        recyclerLayoutManager = new LinearLayoutManager(myContext);
        rvItem.setLayoutManager(recyclerLayoutManager);

        getDataLine(partysiteid, list.get(pos).getAssignId(), String.valueOf(status));

        adapter_detail_assignbin = new Adapter_detail_assignbin(myContext, listLine);
        rvItem.setAdapter(adapter_detail_assignbin);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(list.get(pos).getAssignId(), "SEEN", edNote.getText().toString());
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show();
    }

    private void getDataHeader(final String idsales, final String noinv, final String flag, final String start) {
        progressBar.setVisibility(View.VISIBLE);
        list.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETHEADER + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                int start, until;
                rl_track.removeView(img_track);
                btnNext.setEnabled(true);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
                            showErrorImage();
                            linearAction.setVisibility(View.GONE);
                        }
                        else
                        {
                            String assignId     = object.getString("assignId");
                            String assignDate   = object.getString("assignDate");
                            String assignSales  = object.getString("assignSales");
                            String assignBy     = object.getString("assignBy");
                            String assignFlag   = object.getString("assignFlag");
                            totalData           = object.getInt("totalRow");

                            Data_assignbin_header data = new Data_assignbin_header();
                            data.setAssignId(assignId);
                            data.setAssignDate(assignDate);
                            data.setAssignSales(assignSales);
                            data.setAssignBy(assignBy);
                            data.setAssignFlag(assignFlag);

                            list.add(data);

                            start = counter + 1;
                            until = jsonArray.length() + counter;

                            String counter = "show " + start + " - " + until + " from " + totalData + " data";
                            linearAction.setVisibility(View.VISIBLE);
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

                    adapter_assign_bin.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(myContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("idsales", idsales);
                hashMap.put("noinv", noinv);
                hashMap.put("status", flag);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getDataLine(final String idSales, final String noInv, final String flag) {
        listLine.clear();
        progressBarItem.setVisibility(View.VISIBLE);
        rvItem.setVisibility(View.INVISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLGETLINE, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String totalQty = "";
                    String note = "";

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        if (jsonObject.names().get(0).equals("Error"))
                        {
                            //Ekse ketika error
                            imgItemNotFound.setVisibility(View.VISIBLE);
                            rvItem.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_assignbin_line data = new Data_assignbin_line();
                            data.setItemId(jsonObject.getString("itemId"));
                            data.setItemQty(jsonObject.getString("itemQty"));
                            data.setItemCode(jsonObject.getString("itemCode"));
                            data.setItemCategory(jsonObject.getString("itemCategory"));
                            data.setItemName(jsonObject.getString("itemName"));
                            data.setItemDesc(jsonObject.getString("itemDesc"));
                            data.setItemOrg(jsonObject.getString("itemOrg"));
                            data.setItemNote(jsonObject.getString("itemNote"));
                            data.setTotalQty(jsonObject.getString("totalQty"));

                            totalQty = jsonObject.getString("totalQty");
                            note = jsonObject.getString("itemNote");

                            if (note.equals("null"))
                            {
                                note = "";
                            }

                            listLine.add(data);

                            imgItemNotFound.setVisibility(View.GONE);
                            rvItem.setVisibility(View.VISIBLE);
                        }
                    }

                    progressBarItem.setVisibility(View.GONE);
                    txtTotalQty.setText(totalQty + " Pcs");
                    edNote.setText(note);
                    edNote.setSelection(note.length());
                    adapter_detail_assignbin.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(myContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("idsales", idSales);
                map.put("noinv", noInv);
                map.put("status", flag);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void updateData(final String noinv, final String flag, final String note)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLUPDATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if (object.names().get(0).equals("Success"))
                    {
                        Toasty.success(myContext, "Data has been update", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toasty.warning(myContext, "Failed to update data", Toast.LENGTH_SHORT).show();
                    }

                    if (!noinv.isEmpty())
                    {
                        edSearch.setText(noinv);
//                        getDataHeader(partysiteid, String.valueOf(edSearch.getText()), "0");
                        getDataHeader(partysiteid, String.valueOf(edSearch.getText()), String.valueOf(status), "0");
                    }
                    else
                    {
//                        getDataHeader(partysiteid, "", "0");
                        getDataHeader(partysiteid, "", String.valueOf(status), "0");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(myContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("no_inv", noinv);
                map.put("flag", flag);
                map.put("note", note);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}