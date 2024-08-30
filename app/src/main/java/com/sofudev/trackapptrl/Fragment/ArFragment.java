package com.sofudev.trackapptrl.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.github.javiersantos.bottomdialogs.BottomDialog;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.DateFormat;
import com.sofudev.trackapptrl.FanpageActivity;
import com.sofudev.trackapptrl.Form.AssignApprovalActivity;
import com.sofudev.trackapptrl.Form.CheckBalanceActivity;
import com.sofudev.trackapptrl.Form.CourierHistoryActivity;
import com.sofudev.trackapptrl.Form.CourierTaskActivity;
import com.sofudev.trackapptrl.Form.EinvoiceActivity;
import com.sofudev.trackapptrl.Form.EstatementActivity;
import com.sofudev.trackapptrl.Form.EwarrantyActivity;
import com.sofudev.trackapptrl.Form.FormBatchOrderActivity;
import com.sofudev.trackapptrl.Form.FormCourierTrackingActivity;
import com.sofudev.trackapptrl.Form.FormDeliveryTrackingActivity;
import com.sofudev.trackapptrl.Form.FormFilterOpticnameActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryFrameActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryPartaiActivity;
import com.sofudev.trackapptrl.Form.FormOrderLensActivity;
import com.sofudev.trackapptrl.Form.FormOrderSpActivity;
import com.sofudev.trackapptrl.Form.FormTrackOrderActivity;
import com.sofudev.trackapptrl.Form.FormTrackingSpActivity;
import com.sofudev.trackapptrl.Form.ProductCompareActivity;
import com.sofudev.trackapptrl.Form.SpApprovalActivity;
import com.sofudev.trackapptrl.OnHandActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ArFragment extends Fragment {

    private Config config = new Config();
    private String URLGETUSERBYID  = config.Ip_address + config.get_user_byid;
    private String URLGETUSERBYCUSTNAME = config.Ip_address + config.get_user_bycustname;
    private String URLDELIVERYCOUNTER = config.Ip_address + config.deliverytrack_counter;
    private String URLGETSICCODE  = config.Ip_address + config.get_sic_code;
    private String URLSETSICCODE  = config.Ip_address + config.set_sic_code;
    private String URLVERIFYSICCODE = config.Ip_address + config.verify_siccode;
    private String URLPARENTINFO  = config.Ip_address + config.getparent_info;

    View custom;
    LinearLayout linearLensorder, linearStockorder, linearSporder, linearOnhand, linearOrdertrack, linearDeliverytrack,
            linearSpApproval, linearMore;
    RippleView rpLensorder, rpStockorder, rpSpOrder, rpOnHand, rpOrderTrack, rpDeliveryTrack, rpSpApproval, rpMore;
    Context myContext;

    BottomDialog bottomDialogInput;
    ImageView imgAction;
    CardView cardHide1, cardHide2, cardHide3, cardHide4, cardHide5, cardHide6;
    CardView cardShow1, cardShow2, cardShow3, cardShow4, cardShow5, cardShow6;
    LinearLayout linKode1, linKode2, linKode3, linKode4, linKode5, linKode6;
    UniversalFontTextView txtKode1, txtKode2, txtKode3, txtKode4, txtKode5, txtKode6;
    RippleView pinBtn1, pinBtn2, pinBtn3, pinBtn4, pinBtn5, pinBtn6, pinBtn7, pinBtn8, pinBtn9, pinBtn0;
    List<Integer> pinnumber = new ArrayList<>();

    String ACTIVITY_TAG, PARTYSITEID, USERNAME, CUSTNAME, STATUS, LEVEL, ADDRESS, CITY, PROVINCE, EMAIL, MOBNUMBER, MEMBERFLAG, SALESAREA, ISMANAGER;
    String sActive, sPast, sic_code;
    int sTotal, cTotal;
    int customerId = 0;
    Boolean isHide = true;
    Boolean isHavingChild = false;

    public ArFragment() {
        // Required empty public constructor
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ar, container, false);
        TypefaceProvider.registerDefaultIconSets();
        linearLensorder = view.findViewById(R.id.layout_custom_lensorder);
        rpLensorder     = view.findViewById(R.id.layout_custom_rplensorder);
        linearStockorder= view.findViewById(R.id.layout_custom_stockorder);
        rpStockorder    = view.findViewById(R.id.layout_custom_rpstockorder);
        linearSporder   = view.findViewById(R.id.layout_custom_sporder);
        rpSpOrder       = view.findViewById(R.id.layout_custom_rpsporder);
        linearOnhand    = view.findViewById(R.id.layout_custom_onhand);
        rpOnHand        = view.findViewById(R.id.layout_custom_rponhand);
        linearOrdertrack= view.findViewById(R.id.layout_custom_ordertrack);
        rpOrderTrack    = view.findViewById(R.id.layout_custom_rpordertrack);
        linearDeliverytrack = view.findViewById(R.id.layout_custom_deliverytracking);
        rpDeliveryTrack     = view.findViewById(R.id.layout_custom_rpdeliverytracking);
        linearSpApproval   = view.findViewById(R.id.layout_custom_spapproval);
        rpSpApproval       = view.findViewById(R.id.layout_custom_rpspapproval);
        linearMore      = view.findViewById(R.id.layout_custom_more);
        rpMore          = view.findViewById(R.id.layout_custom_rpmore);

        getData();

        linearLensorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
            }
        });
        rpLensorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
            }
        });

        linearStockorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockOrder();
            }
        });
        rpStockorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockOrder();
            }
        });

        linearSporder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpOrder();
            }
        });
        rpSpOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpOrder();
            }
        });

        linearOnhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOnhand();
            }
        });
        rpOnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOnhand();
            }
        });

        linearOrdertrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
            }
        });
        rpOrderTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
            }
        });

        linearDeliverytrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
            }
        });
        rpDeliveryTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
            }
        });

        linearSpApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpApproval();
            }
        });
        rpSpApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpApproval();
            }
        });

        linearMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDialog();
            }
        });
        rpMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDialog();
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
                LEVEL = bundle.getString("level");

                Log.d(CategoryFragment.class.getSimpleName(), "custname : " + CUSTNAME);
//                getUserById(PARTYSITEID);
                getUserByCustname(CUSTNAME);
                countData(CUSTNAME);
                getParentInfo(PARTYSITEID);
            }
        }
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
                    if (Integer.parseInt(LEVEL) == 0)
                    {
                        if (isHavingChild)
                        {
                            Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                            intent.putExtra("cond", "LENSSALES");
                            intent.putExtra("sales", USERNAME);
                            intent.putExtra("havingChild", isHavingChild);
                            intent.putExtra("customerId", customerId);
                            intent.putExtra("ispaid", false);
                            intent.putExtra("leveluser", LEVEL);
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
                            intent.putExtra("flag", MEMBERFLAG);
                            intent.putExtra("level", LEVEL);
                            intent.putExtra("isSp", "0");
                            intent.putExtra("idSp", "0");
                            intent.putExtra("noHp", "0");
                            intent.putExtra("sales", USERNAME);

                            //Area Sp
                            intent.putExtra("header_nosp", "");
                            intent.putExtra("header_tipesp", "");
                            intent.putExtra("header_sales", "");
                            intent.putExtra("header_shipnumber", "");
                            intent.putExtra("header_custname", "");
                            intent.putExtra("header_address", "");
                            intent.putExtra("header_city", "");
                            intent.putExtra("header_ordervia", "");
                            intent.putExtra("header_dp", 0);
                            intent.putExtra("header_disc", "");
                            intent.putExtra("header_condition", "");
                            intent.putExtra("header_installment", "");
                            intent.putExtra("header_startinstallment", "");
                            intent.putExtra("header_shippingaddress", "");
                            intent.putExtra("header_status", "");
                            intent.putExtra("header_image", "");
                            intent.putExtra("header_signedpath", "");
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
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
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
                    intent.putExtra("flag", MEMBERFLAG);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("isSp", "0");
                    intent.putExtra("idSp", "0");
                    intent.putExtra("noHp", "0");
                    intent.putExtra("sales", USERNAME);

                    //Area Sp
                    intent.putExtra("header_nosp", "");
                    intent.putExtra("header_tipesp", "");
                    intent.putExtra("header_sales", "");
                    intent.putExtra("header_shipnumber", "");
                    intent.putExtra("header_custname", "");
                    intent.putExtra("header_address", "");
                    intent.putExtra("header_city", "");
                    intent.putExtra("header_ordervia", "");
                    intent.putExtra("header_dp", 0);
                    intent.putExtra("header_disc", "");
                    intent.putExtra("header_condition", "");
                    intent.putExtra("header_installment", "");
                    intent.putExtra("header_startinstallment", "");
                    intent.putExtra("header_shippingaddress", "");
                    intent.putExtra("header_status", "");
                    intent.putExtra("header_image", "");
                    intent.putExtra("header_signedpath", "");
                    startActivity(intent);
                }
            }
            else
            {
                showAccessDenied();
            }
        }
    }

    private void handlerStockOrder()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Integer.parseInt(LEVEL) == 0)
            {
                if (isHavingChild)
                {
                    Intent intent = new Intent(getContext(), FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "BATCHSALES");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(myContext, FormBatchOrderActivity.class);

                    intent.putExtra("idparty", PARTYSITEID);
                    intent.putExtra("opticname", CUSTNAME);
                    intent.putExtra("province", PROVINCE);
                    intent.putExtra("usernameInfo", USERNAME);
                    intent.putExtra("province_address", ADDRESS);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("city", CITY);
                    intent.putExtra("idSp", "0");
                    intent.putExtra("isSp", 0);
                    intent.putExtra("flag", MEMBERFLAG);


                    intent.putExtra("header_nosp", "");
                    intent.putExtra("header_tipesp", "");
                    intent.putExtra("header_sales", "");
                    intent.putExtra("header_shipnumber", "");
                    intent.putExtra("header_custname", "");
                    intent.putExtra("header_address", "");
                    intent.putExtra("header_city", "");
                    intent.putExtra("header_ordervia", "");
                    intent.putExtra("header_dp", 0);
                    intent.putExtra("header_disc", "");
                    intent.putExtra("header_condition", "");
                    intent.putExtra("header_installment", "");
                    intent.putExtra("header_startinstallment", "");
                    intent.putExtra("header_shippingaddress", "");
                    intent.putExtra("header_status", "");
                    intent.putExtra("header_image", "");
                    intent.putExtra("header_signedpath", "");
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                intent.putExtra("cond", "BATCHSALES");
                intent.putExtra("sales", USERNAME);
                intent.putExtra("havingChild", isHavingChild);
                intent.putExtra("customerId", customerId);
                intent.putExtra("ispaid", false);
                intent.putExtra("leveluser", LEVEL);
                startActivity(intent);
            }
        }
    }

    private void handlerSpOrder()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Integer.parseInt(LEVEL) == 1)
            {
                Intent intent = new Intent(myContext, FormOrderSpActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                intent.putExtra("ismanager", ISMANAGER);
                startActivity(intent);
            }
            else
            {
                showAccessDenied();
            }
        }
    }

    private void handlerSpApproval()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Integer.parseInt(ISMANAGER) == 1 || Integer.parseInt(LEVEL) == 5)
            {
                Intent intent = new Intent(myContext, SpApprovalActivity.class);
                intent.putExtra("username", USERNAME);
                intent.putExtra("salesarea", SALESAREA);
                intent.putExtra("level", LEVEL);
                startActivity(intent);
            }
            else
            {
                showAccessDenied();
            }
        }
    }

    private void handlerOnhand()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (Integer.parseInt(LEVEL) == 1)
            {
                Intent intent = new Intent(myContext, OnHandActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
//                intent.putExtra("username", USERNAME);
//                intent.putExtra("level", LEVEL);
                startActivity(intent);
            }
            else
            {
                showAccessDenied();
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
                if (Integer.parseInt(LEVEL) == 0)
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                        intent.putExtra("cond", "OTRACK");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormTrackOrderActivity.class);
                        intent.putExtra("idparty", PARTYSITEID);
                        intent.putExtra("leveluser", LEVEL);
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
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormTrackOrderActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("leveluser", LEVEL);
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
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
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
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "DELIVTRACK");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
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

    private void handlerCourierPending()
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
                        showAccessDeniedCustom("Mohon maaf fitur ini hanya dapat digunakan untuk user kurir");
                    }
                    else
                    {
                        showAccessDeniedCustom("Mohon maaf fitur ini hanya dapat digunakan untuk user kurir");
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, CourierTaskActivity.class);
                    intent.putExtra("idparty", PARTYSITEID);
                    intent.putExtra("username", USERNAME);
                    intent.putExtra("level", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, CourierTaskActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                intent.putExtra("level", LEVEL);
                startActivity(intent);
            }
        }
    }

    private void handlerCourierHistory()
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
                        showAccessDeniedCustom("Mohon maaf fitur ini hanya dapat digunakan untuk user kurir");
                    }
                    else
                    {
                        showAccessDeniedCustom("Mohon maaf fitur ini hanya dapat digunakan untuk user kurir");
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, CourierHistoryActivity.class);
                    intent.putExtra("idparty", PARTYSITEID);
                    intent.putExtra("username", USERNAME);
//                    intent.putExtra("isadmin", isAdmin);
                    intent.putExtra("isadmin", false);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, CourierHistoryActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                intent.putExtra("isadmin", false);
                startActivity(intent);
            }
        }
    }

    private void handlerCouriertrack()
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
                        intent.putExtra("cond", "COURIERTRACK");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormCourierTrackingActivity.class);
                        intent.putExtra("username", USERNAME);
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "COURIERTRACK");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormCourierTrackingActivity.class);
                intent.putExtra("username", USERNAME);

                Log.d(CategoryFragment.class.getSimpleName(), "Username : " + USERNAME);
                startActivity(intent);
            }
        }
    }

    private void handlerCourierprogress()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null)
            {
                if (Integer.parseInt(LEVEL) == 1)
                {
                    Intent intent = new Intent(myContext, CourierHistoryActivity.class);
                    intent.putExtra("idparty", PARTYSITEID);
                    intent.putExtra("username", USERNAME);
                    intent.putExtra("isadmin", true);
                    startActivity(intent);
                }
                else
                {
                    showAccessDeniedCustom("Mohon maaf fitur ini hanya dapat digunakan untuk user admin");
                }
            }
            else
            {
                Intent intent = new Intent(myContext, CourierHistoryActivity.class);
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                intent.putExtra("isadmin", true);
                startActivity(intent);
            }
        }
    }

    private void handlerSptrack()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null) {
                if (Integer.parseInt(LEVEL) == 1) {
                    Intent intent = new Intent(myContext, FormTrackingSpActivity.class);
                    intent.putExtra("username", USERNAME);
                    intent.putExtra("level", 1);
                    intent.putExtra("spnumber", "");

                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(myContext, FormTrackingSpActivity.class);
                    intent.putExtra("username", CUSTNAME);
                    intent.putExtra("level", 0);
                    intent.putExtra("spnumber", "");

                    startActivity(intent);
                }
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
                if (Integer.parseInt(LEVEL) == 0) {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                        intent.putExtra("level", "1");
                        intent.putExtra("ordernumber", "");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("idparty", "");
                        intent.putExtra("user_info", "");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                        intent.putExtra("level", LEVEL);
                        intent.putExtra("ordernumber", "");
                        intent.putExtra("sales", "");
                        intent.putExtra("idparty", PARTYSITEID);
                        intent.putExtra("user_info", USERNAME);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("ordernumber", "");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("idparty", "");
                    intent.putExtra("user_info", "");
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                intent.putExtra("level", LEVEL);
                intent.putExtra("ordernumber", "");
                intent.putExtra("sales", "");
                intent.putExtra("idparty", PARTYSITEID);
                intent.putExtra("user_info", USERNAME);
                startActivity(intent);
            }
        }
    }

    private void handlerFrameTrack()
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
                        Intent intent = new Intent(myContext, FormOrderHistoryFrameActivity.class);
                        intent.putExtra("level", "1");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("user_info", "");
                        intent.putExtra("username", "");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryFrameActivity.class);
                        intent.putExtra("level", LEVEL);
                        intent.putExtra("sales", "");
                        intent.putExtra("user_info", PARTYSITEID);
                        intent.putExtra("username", USERNAME);
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormOrderHistoryFrameActivity.class);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("user_info", "");
                    intent.putExtra("username", "");
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormOrderHistoryFrameActivity.class);
                intent.putExtra("level", LEVEL);
                intent.putExtra("sales", "");
                intent.putExtra("user_info", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                startActivity(intent);
            }
        }
    }

    private void handlerStockTrack()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (LEVEL != null)
            {
                if (LEVEL.equals("0"))
                {
                    if (isHavingChild)
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryPartaiActivity.class);
                        intent.putExtra("level", "1");
                        intent.putExtra("user_info", "");
                        intent.putExtra("username", "");
                        intent.putExtra("sales", USERNAME);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryPartaiActivity.class);
                        intent.putExtra("level", LEVEL);
                        intent.putExtra("user_info", PARTYSITEID);
                        intent.putExtra("username", USERNAME);
                        intent.putExtra("sales", "");
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormOrderHistoryPartaiActivity.class);
                    intent.putExtra("level", LEVEL);
                    intent.putExtra("user_info", "");
                    intent.putExtra("username", "");
                    intent.putExtra("sales", USERNAME);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, FormOrderHistoryPartaiActivity.class);
                intent.putExtra("level", LEVEL);
                intent.putExtra("user_info", PARTYSITEID);
                intent.putExtra("username", USERNAME);
                intent.putExtra("sales", "");
                startActivity(intent);
            }
        }
    }

    private void handlerCheckBalance()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, CheckBalanceActivity.class);
            intent.putExtra("user_info", PARTYSITEID);
            intent.putExtra("username", USERNAME);
            startActivity(intent);
        }
    }

    private void handlerEstatement()
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
                        intent.putExtra("cond", "ESTMENT");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
                        startActivity(intent);
                    }
                    else
                    {
                        String prefix = CUSTNAME.substring(CUSTNAME.length() - 7);
                        Log.d("Prefix : ", prefix);

                        if (prefix.equals("(STAFF)"))
                        {
                            showAccessDenied();
                        }
                        else
                        {
                            getSicCode(USERNAME);
                        }
                    }
                }
                else
                {
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "ESTMENT");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                String prefix = CUSTNAME.substring(CUSTNAME.length() - 7);
                Log.d("Prefix : ", prefix);

                if (prefix.equals("(STAFF)"))
                {
                    showAccessDenied();
                }
                else
                {
                    getSicCode(USERNAME);
                }
            }
        }
    }

    private void handlerEWarranty()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, EwarrantyActivity.class);
            startActivity(intent);
        }
    }

    private void handlerEInvoice()
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
                        intent.putExtra("cond", "EINVOICE");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        intent.putExtra("ispaid", false);
                        intent.putExtra("leveluser", LEVEL);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, EinvoiceActivity.class);
                        intent.putExtra("username", USERNAME);
                        intent.putExtra("custname", CUSTNAME);
                        intent.putExtra("ispaid", false);
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "EINVOICE");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", false);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, EinvoiceActivity.class);
                intent.putExtra("username", USERNAME);
                intent.putExtra("custname", CUSTNAME);
                intent.putExtra("ispaid", false);
                startActivity(intent);
            }
        }
    }

    @SuppressLint("InflateParams")
    private void handlerProductCompared()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, ProductCompareActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("InflateParams")
    private void handlerAssignBin()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, AssignApprovalActivity.class);
            intent.putExtra("username", USERNAME);
            intent.putExtra("custname", CUSTNAME);
            intent.putExtra("idparty", PARTYSITEID);
            intent.putExtra("noinv", "");
            startActivity(intent);
        }
    }

    private void handlerInvoicePaid()
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
                        intent.putExtra("cond", "EINVOICE");
                        intent.putExtra("sales", USERNAME);
                        intent.putExtra("havingChild", isHavingChild);
                        intent.putExtra("customerId", customerId);
                        intent.putExtra("ispaid", true);
                        intent.putExtra("leveluser", LEVEL);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, EinvoiceActivity.class);
                        intent.putExtra("username", USERNAME);
                        intent.putExtra("custname", CUSTNAME);
                        intent.putExtra("ispaid", true);
                        startActivity(intent);
                    }
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), "INI ADMINISTRATOR", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(myContext, FormFilterOpticnameActivity.class);
                    intent.putExtra("cond", "EINVOICE");
                    intent.putExtra("sales", USERNAME);
                    intent.putExtra("havingChild", isHavingChild);
                    intent.putExtra("customerId", customerId);
                    intent.putExtra("ispaid", true);
                    intent.putExtra("leveluser", LEVEL);
                    startActivity(intent);
                }
            }
            else
            {
                Intent intent = new Intent(myContext, EinvoiceActivity.class);
                intent.putExtra("username", USERNAME);
                intent.putExtra("custname", CUSTNAME);
                intent.putExtra("ispaid", true);
                startActivity(intent);
            }
        }
    }

    private void handlerCustomerCare()
    {
        Intent intent = new Intent(myContext, FanpageActivity.class);
        intent.putExtra("data", "http://www.timurrayalab.com/custommer_care");
        startActivity(intent);
    }

    @SuppressLint("InflateParams")
    private void showMoreDialog()
    {
        custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_moremenu, null);

        LinearLayout linLensOrder = custom.findViewById(R.id.layout_custom_lensorder);
        RippleView ripLensOrder   = custom.findViewById(R.id.layout_custom_rplensorder);
        LinearLayout linStockOrder = custom.findViewById(R.id.layout_custom_stockorder);
        RippleView ripStockOrder  = custom.findViewById(R.id.layout_custom_rpstockorder);
        LinearLayout linSpOrder = custom.findViewById(R.id.layout_custom_sporder);
        RippleView ripSpOrder   = custom.findViewById(R.id.layout_custom_rpsporder);
        LinearLayout linSpApproval = custom.findViewById(R.id.layout_custom_spapproval);
        RippleView ripSpApproval   = custom.findViewById(R.id.layout_custom_rpspapproval);
        LinearLayout linTrackOrder = custom.findViewById(R.id.layout_custom_ordertrack);
        RippleView ripTrackOrder   = custom.findViewById(R.id.layout_custom_rpordertrack);
        LinearLayout linTrackDelivery = custom.findViewById(R.id.layout_custom_deliverytracking);
        RippleView ripTrackDevliery = custom.findViewById(R.id.layout_custom_rpdeliverytracking);
        LinearLayout linTrackSp = custom.findViewById(R.id.layout_custom_sptracking);
        RippleView ripTrackSp   = custom.findViewById(R.id.layout_custom_rpsptracking);
        LinearLayout linPendingCourier = custom.findViewById(R.id.layout_custom_couriertask);
        RippleView ripPendingCourier   = custom.findViewById(R.id.layout_custom_rpcouriertask);
        LinearLayout linHistoryCourier = custom.findViewById(R.id.layout_custom_courierhistory);
        RippleView ripHistoryCourier   = custom.findViewById(R.id.layout_custom_rpcourierhistory);
        LinearLayout linTrackCourier = custom.findViewById(R.id.layout_custom_couriertracking);
        RippleView ripTrackCourier   = custom.findViewById(R.id.layout_custom_rpcouriertracking);
        LinearLayout linProgressCourier = custom.findViewById(R.id.layout_custom_courierprogress);
        RippleView ripProgressCourier = custom.findViewById(R.id.layout_custom_rpcourierprogress);
        LinearLayout linLensTrack = custom.findViewById(R.id.layout_custom_historylens);
        RippleView ripLensTrack   = custom.findViewById(R.id.layout_custom_rphistorylens);
        LinearLayout linFrameTrack = custom.findViewById(R.id.layout_custom_historyframe);
        RippleView ripFrameTrack   = custom.findViewById(R.id.layout_custom_rphistoryframe);
        LinearLayout linStockTrack = custom.findViewById(R.id.layout_custom_historystock);
        RippleView ripStockTrack   = custom.findViewById(R.id.layout_custom_rphistorystock);
        LinearLayout linCheckBalance = custom.findViewById(R.id.layout_custom_featurebalance);
        RippleView ripCheckBalance = custom.findViewById(R.id.layout_custom_rpfeaturebalance);
        LinearLayout linMasterOnHand = custom.findViewById(R.id.layout_custom_masteronhand);
        RippleView ripMasterOnHand   = custom.findViewById(R.id.layout_custom_rpmasteronhand);
        LinearLayout linStatement = custom.findViewById(R.id.layout_custom_estatement);
        RippleView ripStatement   = custom.findViewById(R.id.layout_custom_rpestatement);
        LinearLayout linWarranty = custom.findViewById(R.id.layout_custom_ewarranty);
        RippleView ripWarranty  = custom.findViewById(R.id.layout_custom_rpewarranty);
        LinearLayout linInvoice = custom.findViewById(R.id.layout_custom_featureinvoice);
        RippleView ripInvoice   = custom.findViewById(R.id.layout_custom_rpfeatureinvoice);
        LinearLayout linInvoicePaid = custom.findViewById(R.id.layout_custom_featureinvoicepaid);
        RippleView ripInvoicePaid   = custom.findViewById(R.id.layout_custom_rpfeatureinvoicepaid);
        LinearLayout linProductCompare = custom.findViewById(R.id.layout_custom_featureproductcompare);
        RippleView ripProductCompare = custom.findViewById(R.id.layout_custom_rpfeatureproductcompare);
        LinearLayout linAssignBin    = custom.findViewById(R.id.layout_custom_featureassignbin);
        RippleView ripAssignBin      = custom.findViewById(R.id.layout_custom_rpfeatureassignbin);
        LinearLayout linCustomercare = custom.findViewById(R.id.layout_custom_customercare);
        RippleView ripCustomercare   = custom.findViewById(R.id.layout_custom_rpcustomercare);

        final BottomDialog bottomDialog = new BottomDialog.Builder(myContext)
                .setTitle("More Menu")
                .setCustomView(custom)
                .build();

        linLensOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
                bottomDialog.dismiss();
            }
        });
        ripLensOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensOrder();
                bottomDialog.dismiss();
            }
        });

        linStockOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockOrder();
                bottomDialog.dismiss();
            }
        });
        ripStockOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockOrder();
                bottomDialog.dismiss();
            }
        });

        linSpOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpOrder();
                bottomDialog.dismiss();
            }
        });
        ripSpOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpOrder();
                bottomDialog.dismiss();
            }
        });

        linSpApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpApproval();
                bottomDialog.dismiss();
            }
        });
        ripSpApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSpApproval();
                bottomDialog.dismiss();
            }
        });

        linTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
                bottomDialog.dismiss();
            }
        });
        ripTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOrdertrack();
                bottomDialog.dismiss();
            }
        });

        linTrackDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
                bottomDialog.dismiss();
            }
        });
        ripTrackDevliery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerDeliverytrack();
                bottomDialog.dismiss();
            }
        });

        linPendingCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierPending();
                bottomDialog.dismiss();
            }
        });
        ripPendingCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierPending();
                bottomDialog.dismiss();
            }
        });

        linHistoryCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierHistory();
                bottomDialog.dismiss();
            }
        });
        ripHistoryCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierHistory();
                bottomDialog.dismiss();
            }
        });

        linTrackCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCouriertrack();
                bottomDialog.dismiss();
            }
        });
        ripTrackCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCouriertrack();
                bottomDialog.dismiss();
            }
        });

        linProgressCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierprogress();
                bottomDialog.dismiss();
            }
        });
        ripProgressCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCourierprogress();
                bottomDialog.dismiss();
            }
        });

        linTrackSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSptrack();
                bottomDialog.dismiss();
            }
        });
        ripTrackSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerSptrack();
                bottomDialog.dismiss();
            }
        });

        linLensTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensTrack();
                bottomDialog.dismiss();
            }
        });
        ripLensTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerLensTrack();
                bottomDialog.dismiss();
            }
        });

        linFrameTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerFrameTrack();
                bottomDialog.dismiss();
            }
        });
        ripFrameTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerFrameTrack();
                bottomDialog.dismiss();
            }
        });

        linStockTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockTrack();
                bottomDialog.dismiss();
            }
        });
        ripStockTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerStockTrack();
                bottomDialog.dismiss();
            }
        });

        linCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCheckBalance();
                bottomDialog.dismiss();
            }
        });
        ripCheckBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCheckBalance();
                bottomDialog.dismiss();
            }
        });

        linMasterOnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOnhand();
                bottomDialog.dismiss();
            }
        });
        ripMasterOnHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerOnhand();
                bottomDialog.dismiss();
            }
        });

        linStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEstatement();
                bottomDialog.dismiss();
            }
        });
        ripStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEstatement();
                bottomDialog.dismiss();
            }
        });

        linWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEWarranty();
                bottomDialog.dismiss();
            }
        });
        ripWarranty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEWarranty();
                bottomDialog.dismiss();
            }
        });

        linInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEInvoice();
                bottomDialog.dismiss();
            }
        });
        ripInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerEInvoice();
                bottomDialog.dismiss();
            }
        });

        linInvoicePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerInvoicePaid();
                bottomDialog.dismiss();
            }
        });
        ripInvoicePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerInvoicePaid();
                bottomDialog.dismiss();
            }
        });

        //DISABLE BEFORE UPLOAD
        linProductCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                handlerProductCompared();
                bottomDialog.dismiss();
            }
        });
        ripProductCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                handlerProductCompared();
                bottomDialog.dismiss();
            }
        });
        //DISABLE BEFORE UPLOAD

        linAssignBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerAssignBin();
                bottomDialog.dismiss();
            }
        });

        ripAssignBin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerAssignBin();
                bottomDialog.dismiss();
            }
        });

        linCustomercare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCustomerCare();
                bottomDialog.dismiss();
            }
        });
        ripCustomercare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlerCustomerCare();
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show();
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

    private void showAccessDeniedCustom(String caption)
    {
        BootstrapButton btn_close;
        UniversalFontTextView txt_title;

        final Dialog dialog = new Dialog(myContext);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_access_denied);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        txt_title  = dialog.findViewById(R.id.dialog_accessdenied_txtcaption);
        btn_close  = dialog.findViewById(R.id.dialog_accessdenied_btnsave);

        txt_title.setText(caption);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getUserById(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETUSERBYID, new Response.Listener<String>() {
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

                        countData(USERNAME);
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
                map.put("party_siteid", id);
                return map;
            }
        };

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

    private void getSicCode(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGETSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Log.d("Error : ", "Data tidak ditemukan");
                    }
                    else if (jsonObject.names().get(0).equals("invalid"))
                    {
                        Log.d("invalid : ", "Username tidak boleh kosong");
                    }
                    else
                    {
                        sic_code = jsonObject.getString("sic_code");
                        int act;
                        if (sic_code.isEmpty() || sic_code.equals("null"))
                        {
                            Log.d("Info : ", "Buat pin baru");
                            act = 0;
                            showCreatePin(act);
                        }
                        else
                        {
                            Log.d("Info : ", "Masukkan pin");
                            act = 1;
                            showInputPin(act);
                        }
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
                map.put("username", username);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void setSicCode(final String username, final String pin)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSETSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        //Gagal create pin
                        Log.d("Pin error : ", "Gagal membuat pin");
                    }
                    else
                    {
                        //Sukses create pin
                        Toasty.info(myContext, "Berhasil menambahkan pin", Toast.LENGTH_SHORT).show();
                        pinnumber.clear();
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
                map.put("pinnumber", pin);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void verifySicCode(final String username, final String pin)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLVERIFYSICCODE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.names().get(0).equals("error"))
                    {
                        Toasty.warning(myContext, "Pin yang anda masukkan tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        bottomDialogInput.dismiss();
                        pinnumber.clear();

                        Intent intent = new Intent(myContext, EstatementActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("opticname",CUSTNAME);
                        startActivity(intent);
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
                map.put("username", username);
                map.put("pinnumber", pin);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void addPin(int number)
    {
        if (pinnumber.size() + 1 > 6)
        {
            Toasty.info(myContext, "Pin maksimal 6 Karakter", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pinnumber.add(number);
        }
    }

    private void removePin()
    {
        if (pinnumber.size() < 1)
        {
            Toasty.info(myContext, "Anda belum memasukkan pin", Toast.LENGTH_SHORT).show();
        }
        else
        {
            pinnumber.remove(pinnumber.size() - 1);
        }
    }

    @SuppressLint("SetTextI18n")
    private void checkSelectedPin()
    {
        switch (pinnumber.size())
        {
            case 0 :
                linKode1.setBackgroundResource(R.drawable.pinview_unselected);
                linKode2.setBackgroundResource(R.drawable.pinview_unselected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);
                txtKode1.setText("");
                txtKode2.setText("");
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 1 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_unselected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText("");
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 2 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_unselected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText("");
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 3 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_unselected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText("");
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 4 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_unselected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText("");
                txtKode6.setText("");
                break;
            case 5 :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_selected);
                linKode6.setBackgroundResource(R.drawable.pinview_unselected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText(pinnumber.get(4).toString());
                txtKode6.setText("");
                break;
            default :
                linKode1.setBackgroundResource(R.drawable.pinview_selected);
                linKode2.setBackgroundResource(R.drawable.pinview_selected);
                linKode3.setBackgroundResource(R.drawable.pinview_selected);
                linKode4.setBackgroundResource(R.drawable.pinview_selected);
                linKode5.setBackgroundResource(R.drawable.pinview_selected);
                linKode6.setBackgroundResource(R.drawable.pinview_selected);

                txtKode1.setText(pinnumber.get(0).toString());
                txtKode2.setText(pinnumber.get(1).toString());
                txtKode3.setText(pinnumber.get(2).toString());
                txtKode4.setText(pinnumber.get(3).toString());
                txtKode5.setText(pinnumber.get(4).toString());
                txtKode6.setText(pinnumber.get(5).toString());
                break;
        }
    }

    private void positionShow()
    {
        cardHide1.setVisibility(View.GONE);
        cardHide2.setVisibility(View.GONE);
        cardHide3.setVisibility(View.GONE);
        cardHide4.setVisibility(View.GONE);
        cardHide5.setVisibility(View.GONE);
        cardHide6.setVisibility(View.GONE);
        cardShow1.setVisibility(View.VISIBLE);
        cardShow2.setVisibility(View.VISIBLE);
        cardShow3.setVisibility(View.VISIBLE);
        cardShow4.setVisibility(View.VISIBLE);
        cardShow5.setVisibility(View.VISIBLE);
        cardShow6.setVisibility(View.VISIBLE);
    }

    private void positionHide()
    {
        cardHide1.setVisibility(View.VISIBLE);
        cardHide2.setVisibility(View.VISIBLE);
        cardHide3.setVisibility(View.VISIBLE);
        cardHide4.setVisibility(View.VISIBLE);
        cardHide5.setVisibility(View.VISIBLE);
        cardHide6.setVisibility(View.VISIBLE);
        cardShow1.setVisibility(View.GONE);
        cardShow2.setVisibility(View.GONE);
        cardShow3.setVisibility(View.GONE);
        cardShow4.setVisibility(View.GONE);
        cardShow5.setVisibility(View.GONE);
        cardShow6.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showCreatePin(int action)
    {
        @SuppressLint("InflateParams")
        View custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_create_pin, null);
        final BottomDialog bottomDialog = new BottomDialog.Builder(myContext)
                .setTitle("Membuat pin baru")
                .setCustomView(custom)
                .setCancelable(false)
                .build();

        cardHide1 = custom.findViewById(R.id.bottom_dialog_createpin_hide1);
        cardHide2 = custom.findViewById(R.id.bottom_dialog_createpin_hide2);
        cardHide3 = custom.findViewById(R.id.bottom_dialog_createpin_hide3);
        cardHide4 = custom.findViewById(R.id.bottom_dialog_createpin_hide4);
        cardHide5 = custom.findViewById(R.id.bottom_dialog_createpin_hide5);
        cardHide6 = custom.findViewById(R.id.bottom_dialog_createpin_hide6);
        cardShow1 = custom.findViewById(R.id.bottom_dialog_createpin_show1);
        cardShow2 = custom.findViewById(R.id.bottom_dialog_createpin_show2);
        cardShow3 = custom.findViewById(R.id.bottom_dialog_createpin_show3);
        cardShow4 = custom.findViewById(R.id.bottom_dialog_createpin_show4);
        cardShow5 = custom.findViewById(R.id.bottom_dialog_createpin_show5);
        cardShow6 = custom.findViewById(R.id.bottom_dialog_createpin_show6);
        linKode1  = custom.findViewById(R.id.bottom_dialog_createpin_kode1);
        linKode2  = custom.findViewById(R.id.bottom_dialog_createpin_kode2);
        linKode3  = custom.findViewById(R.id.bottom_dialog_createpin_kode3);
        linKode4  = custom.findViewById(R.id.bottom_dialog_createpin_kode4);
        linKode5  = custom.findViewById(R.id.bottom_dialog_createpin_kode5);
        linKode6  = custom.findViewById(R.id.bottom_dialog_createpin_kode6);
        pinBtn0   = custom.findViewById(R.id.bottom_dialog_createpin_btn0);
        pinBtn1   = custom.findViewById(R.id.bottom_dialog_createpin_btn1);
        pinBtn2   = custom.findViewById(R.id.bottom_dialog_createpin_btn2);
        pinBtn3   = custom.findViewById(R.id.bottom_dialog_createpin_btn3);
        pinBtn4   = custom.findViewById(R.id.bottom_dialog_createpin_btn4);
        pinBtn5   = custom.findViewById(R.id.bottom_dialog_createpin_btn5);
        pinBtn6   = custom.findViewById(R.id.bottom_dialog_createpin_btn6);
        pinBtn7   = custom.findViewById(R.id.bottom_dialog_createpin_btn7);
        pinBtn8   = custom.findViewById(R.id.bottom_dialog_createpin_btn8);
        pinBtn9   = custom.findViewById(R.id.bottom_dialog_createpin_btn9);
        txtKode1  = custom.findViewById(R.id.bottom_dialog_createpin_txt1);
        txtKode2  = custom.findViewById(R.id.bottom_dialog_createpin_txt2);
        txtKode3  = custom.findViewById(R.id.bottom_dialog_createpin_txt3);
        txtKode4  = custom.findViewById(R.id.bottom_dialog_createpin_txt4);
        txtKode5  = custom.findViewById(R.id.bottom_dialog_createpin_txt5);
        txtKode6  = custom.findViewById(R.id.bottom_dialog_createpin_txt6);

        RippleView btnAction = custom.findViewById(R.id.bottom_dialog_createpin_btnact);
        RippleView btnClose  = custom.findViewById(R.id.bottom_dialog_createpin_btnclose);
        RippleView btnDelete = custom.findViewById(R.id.bottom_dialog_createpin_btndel);
        RippleView btnSimpan = custom.findViewById(R.id.bottom_dialog_createpin_btnsave);
        LinearLayout linearAction = custom.findViewById(R.id.bottom_dialog_createpin_linearaction);
        LinearLayout linearCheck  = custom.findViewById(R.id.bottom_dialog_createpin_linearcheck);
        imgAction  = custom.findViewById(R.id.press_act);

        if (action == 1)
        {
            linearAction.setVisibility(View.GONE);
            linearCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            linearAction.setVisibility(View.VISIBLE);
            linearCheck.setVisibility(View.GONE);
        }

        pinBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(0);
                checkSelectedPin();
            }
        });

        pinBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(1);
                checkSelectedPin();
            }
        });

        pinBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(2);
                checkSelectedPin();
            }
        });

        pinBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(3);
                checkSelectedPin();
            }
        });

        pinBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(4);
                checkSelectedPin();
            }
        });

        pinBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(5);
                checkSelectedPin();
            }
        });

        pinBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(6);
                checkSelectedPin();
            }
        });

        pinBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(7);
                checkSelectedPin();
            }
        });

        pinBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(8);
                checkSelectedPin();
            }
        });

        pinBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(9);
                checkSelectedPin();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide)
                {
                    isHide = false;
                    imgAction.setImageResource(R.drawable.view);

                    positionShow();
                }
                else
                {
                    isHide = true;
                    imgAction.setImageResource(R.drawable.invisible);

                    positionHide();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
                checkSelectedPin();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Total pin : ", String.valueOf(pinnumber.size()));

                if (String.valueOf(pinnumber.size()).equals("6"))
                {
                    StringBuilder listString = new StringBuilder();
                    for (int s : pinnumber)
                    {
                        listString.append(s);
                    }

                    Log.d("Output pin : ", listString.toString());
                    setSicCode(USERNAME, listString.toString());
                    bottomDialog.dismiss();
                }
                else
                {
                    Toasty.warning(myContext, "Harap melengkapi pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinnumber.clear();
                bottomDialog.dismiss();
                isHide = true;
            }
        });

        bottomDialog.show();
    }

    private void showInputPin(int action)
    {
        View custom = LayoutInflater.from(myContext).inflate(R.layout.bottom_dialog_create_pin, null);
        bottomDialogInput = new BottomDialog.Builder(myContext)
                .setTitle("Masukkan Pin")
                .setCustomView(custom)
                .build();
        isHide = true;

        cardHide1 = custom.findViewById(R.id.bottom_dialog_createpin_hide1);
        cardHide2 = custom.findViewById(R.id.bottom_dialog_createpin_hide2);
        cardHide3 = custom.findViewById(R.id.bottom_dialog_createpin_hide3);
        cardHide4 = custom.findViewById(R.id.bottom_dialog_createpin_hide4);
        cardHide5 = custom.findViewById(R.id.bottom_dialog_createpin_hide5);
        cardHide6 = custom.findViewById(R.id.bottom_dialog_createpin_hide6);
        cardShow1 = custom.findViewById(R.id.bottom_dialog_createpin_show1);
        cardShow2 = custom.findViewById(R.id.bottom_dialog_createpin_show2);
        cardShow3 = custom.findViewById(R.id.bottom_dialog_createpin_show3);
        cardShow4 = custom.findViewById(R.id.bottom_dialog_createpin_show4);
        cardShow5 = custom.findViewById(R.id.bottom_dialog_createpin_show5);
        cardShow6 = custom.findViewById(R.id.bottom_dialog_createpin_show6);
        linKode1  = custom.findViewById(R.id.bottom_dialog_createpin_kode1);
        linKode2  = custom.findViewById(R.id.bottom_dialog_createpin_kode2);
        linKode3  = custom.findViewById(R.id.bottom_dialog_createpin_kode3);
        linKode4  = custom.findViewById(R.id.bottom_dialog_createpin_kode4);
        linKode5  = custom.findViewById(R.id.bottom_dialog_createpin_kode5);
        linKode6  = custom.findViewById(R.id.bottom_dialog_createpin_kode6);
        pinBtn0   = custom.findViewById(R.id.bottom_dialog_createpin_btn0);
        pinBtn1   = custom.findViewById(R.id.bottom_dialog_createpin_btn1);
        pinBtn2   = custom.findViewById(R.id.bottom_dialog_createpin_btn2);
        pinBtn3   = custom.findViewById(R.id.bottom_dialog_createpin_btn3);
        pinBtn4   = custom.findViewById(R.id.bottom_dialog_createpin_btn4);
        pinBtn5   = custom.findViewById(R.id.bottom_dialog_createpin_btn5);
        pinBtn6   = custom.findViewById(R.id.bottom_dialog_createpin_btn6);
        pinBtn7   = custom.findViewById(R.id.bottom_dialog_createpin_btn7);
        pinBtn8   = custom.findViewById(R.id.bottom_dialog_createpin_btn8);
        pinBtn9   = custom.findViewById(R.id.bottom_dialog_createpin_btn9);
        txtKode1  = custom.findViewById(R.id.bottom_dialog_createpin_txt1);
        txtKode2  = custom.findViewById(R.id.bottom_dialog_createpin_txt2);
        txtKode3  = custom.findViewById(R.id.bottom_dialog_createpin_txt3);
        txtKode4  = custom.findViewById(R.id.bottom_dialog_createpin_txt4);
        txtKode5  = custom.findViewById(R.id.bottom_dialog_createpin_txt5);
        txtKode6  = custom.findViewById(R.id.bottom_dialog_createpin_txt6);

        RippleView btnAction = custom.findViewById(R.id.bottom_dialog_createpin_btnact);
        RippleView btnDelete = custom.findViewById(R.id.bottom_dialog_createpin_btndel);
        RippleView btnCheck  = custom.findViewById(R.id.bottom_dialog_createpin_btncheck);
        LinearLayout linearAction = custom.findViewById(R.id.bottom_dialog_createpin_linearaction);
        LinearLayout linearCheck  = custom.findViewById(R.id.bottom_dialog_createpin_linearcheck);
        imgAction  = custom.findViewById(R.id.press_act);

        if (action == 1)
        {
            linearAction.setVisibility(View.GONE);
            linearCheck.setVisibility(View.VISIBLE);
        }
        else
        {
            linearAction.setVisibility(View.VISIBLE);
            linearCheck.setVisibility(View.GONE);
        }

        pinBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(0);
                checkSelectedPin();
            }
        });

        pinBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(1);
                checkSelectedPin();
            }
        });

        pinBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(2);
                checkSelectedPin();
            }
        });

        pinBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(3);
                checkSelectedPin();
            }
        });

        pinBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(4);
                checkSelectedPin();
            }
        });

        pinBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(5);
                checkSelectedPin();
            }
        });

        pinBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(6);
                checkSelectedPin();
            }
        });

        pinBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(7);
                checkSelectedPin();
            }
        });

        pinBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(8);
                checkSelectedPin();
            }
        });

        pinBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPin(9);
                checkSelectedPin();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isHide)
                {
                    isHide = false;
                    imgAction.setImageResource(R.drawable.view);

                    positionShow();
                }
                else
                {
                    isHide = true;
                    imgAction.setImageResource(R.drawable.invisible);

                    positionHide();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePin();
                checkSelectedPin();
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Total pin : ", String.valueOf(pinnumber.size()));

                if (String.valueOf(pinnumber.size()).equals("6"))
                {
                    StringBuilder listString = new StringBuilder();
                    for (int s : pinnumber)
                    {
                        listString.append(s);
                    }

                    Log.d("Output pin : ", listString.toString());
                    verifySicCode(USERNAME, listString.toString());
                }
                else
                {
                    Toasty.warning(myContext, "Harap melengkapi pin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomDialogInput.show();
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
}