package com.sofudev.trackapptrl.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.Form.FormDeliveryTrackingActivity;
import com.sofudev.trackapptrl.Form.FormFilterOpticnameActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryActivity;
import com.sofudev.trackapptrl.Form.FormOrderLensActivity;
import com.sofudev.trackapptrl.Form.FormTrackOrderActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AssitsFragment extends Fragment {
    private Config config = new Config();
    private String URLPARENTINFO  = config.Ip_address + config.getparent_info;
    private String URLGETUSERBYCUSTNAME = config.Ip_address + config.get_user_bycustname;
    private String URLDELIVERYCOUNTER = config.Ip_address + config.deliverytrack_counter;

    LinearLayout linearAssitsLensorder, linearAssitsHistoryLens, linearAssitsOrdertrack, linearAssitsDeliverytrack;
    RippleView rippleAssitsLensorder, rippleAssitsHistoryLens, rippleAssitsOrderTrack, rippleAssitsDeliveryTrack;
    Context myContext;
    String ACTIVITY_TAG, PARTYSITEID, USERNAME, CUSTNAME, STATUS, LEVEL, ADDRESS, CITY, PROVINCE, EMAIL, MOBNUMBER, MEMBERFLAG, SALESAREA, ISMANAGER;
    String sActive, sPast;
    int sTotal;
    Boolean isHavingChild = false;
    int customerId = 0;

    public AssitsFragment() {

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
        View view = inflater.inflate(R.layout.fragment_assits, container, false);

        linearAssitsLensorder   = view.findViewById(R.id.layout_assits_lensorder);
        rippleAssitsLensorder   = view.findViewById(R.id.layout_custom_rplensorder);
        linearAssitsHistoryLens = view.findViewById(R.id.layout_assits_historylens);
        rippleAssitsHistoryLens = view.findViewById(R.id.layout_custom_rphistorylens);
        linearAssitsOrdertrack  = view.findViewById(R.id.layout_assits_ordertrack);
        rippleAssitsOrderTrack  = view.findViewById(R.id.layout_custom_rpordertrack);
        linearAssitsDeliverytrack = view.findViewById(R.id.layout_assits_deliverytracking);
        rippleAssitsDeliveryTrack = view.findViewById(R.id.layout_custom_rpdeliverytracking);

        getData();

        linearAssitsLensorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
            }
        });
        rippleAssitsLensorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
            }
        });

        linearAssitsHistoryLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensTrack();
            }
        });
        rippleAssitsHistoryLens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensTrack();
            }
        });

        linearAssitsOrdertrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
            }
        });
        rippleAssitsOrderTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
            }
        });

        linearAssitsDeliverytrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
            }
        });
        rippleAssitsDeliveryTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
            }
        });

        return view;
    }

    private void getData()
    {
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");

            assert ACTIVITY_TAG != null;
            if (!ACTIVITY_TAG.equals("main"))
            {
                PARTYSITEID = bundle.getString("partySiteId");
                CUSTNAME = bundle.getString("username");

                Log.d(AssitsFragment.class.getSimpleName(), "custname : " + CUSTNAME);
                Log.d(AssitsFragment.class.getSimpleName(), "partyId : " + PARTYSITEID);

                getUserByCustname(CUSTNAME);
                countData(CUSTNAME.replace("(STAFF)", "").trim());
                getParentInfo(PARTYSITEID);
            }
        }
    }

    private void showAccessDenied()
    {
        BootstrapButton btn_close;

        final Dialog dialog = new Dialog(myContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_access_denied);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        btn_close  = dialog.findViewById(R.id.dialog_accessdenied_btnsave);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getParentInfo(final String key){
        StringRequest request = new StringRequest(Request.Method.POST, URLPARENTINFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("code") == 200){
                        JSONObject dataObj = jsonObject.getJSONObject("data");
                        String idFlag = dataObj.getString("id_flag");
                        customerId = dataObj.getInt("customer_id");

                        Log.d(CategoryFragment.class.getSimpleName(), "FLAG ID : " + idFlag);
                        Log.d(CategoryFragment.class.getSimpleName(), "CUST ID : " + customerId);

                        if (idFlag.equals("Y") && customerId > 0)
                        {
                            isHavingChild = true;
                        }
                    }
                    else
                    {
                        isHavingChild = false;
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
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("key", key);
                return map;
            }
        };

        request.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(request);
    }

    private void getUserByCustname(final String custname)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETUSERBYCUSTNAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.d("Error : ", "Data tidak ditemukan");
                    }
                    else
                    {
                        USERNAME = jsonObject.getString("username");
                        CUSTNAME = jsonObject.getString("custname");
                        STATUS = jsonObject.getString("status");
                        LEVEL = jsonObject.getString("level");
                        ADDRESS = jsonObject.getString("address");
                        CITY = jsonObject.getString("city");
                        PROVINCE = jsonObject.getString("province");
                        EMAIL = jsonObject.getString("email");
                        MOBNUMBER = jsonObject.getString("mobnumber");
                        MEMBERFLAG = jsonObject.getString("memberflag");
                        SALESAREA  = jsonObject.getString("salesarea");
                        ISMANAGER  = jsonObject.getString("ismanager");
//                        countData(CUSTNAME);
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
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("custname", custname);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void countData(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLDELIVERYCOUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Log.d(CategoryFragment.class.getSimpleName(), "Output : " + response);

                    sActive = "Active ("+ jsonObject.getInt("countactive") +")";
                    sPast = "Past (" + jsonObject.getInt("countpast") +")";
                    sTotal = jsonObject.getInt("countactive") + jsonObject.getInt("countpast");

                    Log.d(CategoryFragment.class.getSimpleName(), "Active : " + sActive);
                    Log.d(CategoryFragment.class.getSimpleName(), "Past : " + sPast);
                    Log.d(CategoryFragment.class.getSimpleName(), "Total : " + sTotal);
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

    private void handlerLensOrder()
    {
        // Get total date from range
        DateFormat testDf = new DateFormat();
        try {
            long dayDiff = testDf.daysBetween(testDf.getDate("2021-11-13"), testDf.getDate("2021-12-15"));
            Log.d(CategoryFragment.class.getSimpleName(), "Diff two days = " + dayDiff);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d(CategoryFragment.class.getSimpleName(), "Party site id : " + PARTYSITEID);
            if (STATUS.contains("a") || STATUS.equals("A"))
            {
                Log.d(CategoryFragment.class.getSimpleName(), "Eksukusi lensorder");
                if (LEVEL != null)
                {
                    if (Integer.parseInt(LEVEL) == 0 || Integer.parseInt(LEVEL) == 3)
                    {
                        if (isHavingChild)
                        {
                            Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                            intent.putExtra("cond", "LENSSALES");
                            intent.putExtra("sales", USERNAME);
                            intent.putExtra("havingChild", isHavingChild);
                            intent.putExtra("customerId", customerId);
                            startActivity(intent);

                            Log.d(CategoryFragment.class.getSimpleName(),"Sales Name : " + USERNAME);
                        }
                        else
                        {
                            Intent intent = new Intent(myContext, FormOrderLensActivity.class);
                            intent.putExtra("idparty", PARTYSITEID);
                            intent.putExtra("opticname", CUSTNAME);
                            intent.putExtra("province", PROVINCE);
                            intent.putExtra("usernameInfo", USERNAME);
                            intent.putExtra("city", CITY);
                            intent.putExtra("level", LEVEL);
                            intent.putExtra("flag", MEMBERFLAG);
                            intent.putExtra("idSp", "0");
                            intent.putExtra("isSp", "0");
                            intent.putExtra("noHp", "0");
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "LENSSALES");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);

                        Log.d(CategoryFragment.class.getSimpleName(),"Sales Name : " + USERNAME);
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormOrderLensActivity.class);
                    intent.putExtra("idparty", PARTYSITEID);
                    intent.putExtra("opticname", CUSTNAME);
                    intent.putExtra("province", PROVINCE);
                    intent.putExtra("usernameInfo", USERNAME);
                    intent.putExtra("city", CITY);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("flag", MEMBERFLAG);
                    intent.putExtra("idSp", "0");
                    intent.putExtra("isSp", "0");
                    intent.putExtra("noHp", "0");
                    startActivity(intent);
                }
            }
            else
            {
                showAccessDenied();
            }
        }
    }

    private void handlerLensTrack()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null) {
                if (Integer.parseInt(LEVEL) == 0 || Integer.parseInt(LEVEL) == 3) {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("level", "1");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                        intent.putExtra("idparty", PARTYSITEID);
                        intent.putExtra("user_info", USERNAME);
                        intent.putExtra("level", LEVEL);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("level", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("user_info", USERNAME);
                intent.putExtra("level", LEVEL);
                startActivity(intent);
            }
        }
    }

    private void handlerOrdertrack()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null)
            {
                if (Integer.parseInt(LEVEL) == 0 || Integer.parseInt(LEVEL) == 3)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "OTRACK");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormTrackOrderActivity.class);
                        intent.putExtra("idparty", PARTYSITEID);
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "OTRACK");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormTrackOrderActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                startActivity(intent);
            }
        }
    }

    private void handlerDeliverytrack()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null)
            {
                if (Integer.parseInt(LEVEL) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "DELIVTRACK");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormDeliveryTrackingActivity.class);
                        intent.putExtra("username", CUSTNAME);
                        intent.putExtra("activetitle", sActive);
                        intent.putExtra("pasttitle", sPast);
                        intent.putExtra("totalprocess", String.valueOf(sTotal));
                        startActivity(intent);
                    }
                }
                else if (Integer.parseInt(LEVEL) == 3)
                {
                    Intent intent = new Intent(myContext, FormDeliveryTrackingActivity.class);
                    intent.putExtra("username", CUSTNAME.replace("(STAFF)", "").trim());
                    intent.putExtra("activetitle", sActive);
                    intent.putExtra("pasttitle", sPast);
                    intent.putExtra("totalprocess", String.valueOf(sTotal));
                    startActivity(intent);
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "DELIVTRACK");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormDeliveryTrackingActivity.class);
                intent.putExtra("username", CUSTNAME);
                intent.putExtra("activetitle", sActive);
                intent.putExtra("pasttitle", sPast);
                intent.putExtra("totalprocess", String.valueOf(sTotal));
                startActivity(intent);
            }
        }
    }
}