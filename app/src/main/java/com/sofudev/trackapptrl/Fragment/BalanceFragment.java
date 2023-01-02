package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.raizlabs.universalfontcomponents.UniversalFontComponents;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Form.DetailBalanceActivity;
import com.sofudev.trackapptrl.Form.DetailDepositActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class BalanceFragment extends Fragment {

    Config config = new Config();
    private String URLCHECKDEPOSIT= config.Ip_address + config.depo_getsaldo;
    String URLCHECKBPRKS = config.Ip_address + config.check_balance_loanbprks;

    UniversalFontTextView txtDeposit, txtBprks;
    ImageView imgTrl, imgBprks;
    LinearLayout linearTrl, linearBprks;
    RelativeLayout relativeTrl, relativeBprks;

    Context myContext;

    String ACTIVITY_TAG, nominalDeposit, nominalBprks, userInfo, username;

    public BalanceFragment() {
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
        UniversalFontComponents.init(myContext);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_balance, container, false);
        txtDeposit = view.findViewById(R.id.layout_custom_balance_txtDeposit);
        txtBprks = view.findViewById(R.id.layout_custom_balance_txtBprks);
        imgTrl = view.findViewById(R.id.fragment_custombalance_imgtrl);
        imgBprks = view.findViewById(R.id.fragment_custombalance_imgbprks);
        linearTrl = view.findViewById(R.id.fragment_custombalance_lineartrl);
        linearBprks = view.findViewById(R.id.fragment_custombalance_linearbprks);
        relativeTrl = view.findViewById(R.id.fragment_custombalance_relativetrl);
        relativeBprks = view.findViewById(R.id.fragment_custombalance_relativebprks);

        hideSaldoTrl();
        hideSaldoBprks();

        imgTrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeposit();
            }
        });

        linearTrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeposit();
            }
        });

        imgBprks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBprks();
            }
        });

        linearBprks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleBprks();
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
//            nominal = bundle.getString("nominal");
            userInfo = bundle.getString("user_info");
            username = bundle.getString("username");

            getSaldoDepo(username);
            getSaldoBprks(username);

            Log.d("Balance Fragment", "User info : " + userInfo);
            Log.d("Balance Fragment", "Username : " + username);
        }
    }

    private String CurencyFormat(String Rp){
        if (Rp.contentEquals("0") | Rp.equals("0"))
        {
            return "0";
        }

        Double money = Double.valueOf(Rp);
        String strFormat ="#,###";
        DecimalFormat df = new DecimalFormat(strFormat,new DecimalFormatSymbols(Locale.GERMAN));
        return df.format(money);
    }

    private void showSaldoTrl()
    {
        relativeTrl.setVisibility(View.GONE);
        linearTrl.setVisibility(View.VISIBLE);
    }

    private void hideSaldoTrl()
    {
        relativeTrl.setVisibility(View.VISIBLE);
        linearTrl.setVisibility(View.GONE);
    }

    private void showSaldoBprks()
    {
        relativeBprks.setVisibility(View.GONE);
        linearBprks.setVisibility(View.VISIBLE);
    }

    private void hideSaldoBprks()
    {
        relativeBprks.setVisibility(View.VISIBLE);
        linearBprks.setVisibility(View.GONE);
    }

    private void handleDeposit()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, DetailDepositActivity.class);
            intent.putExtra("nominal", nominalDeposit);
            intent.putExtra("user_info", userInfo);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    private void handleBprks()
    {
        if (ACTIVITY_TAG.equals("main"))
        {
            Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(myContext, DetailBalanceActivity.class);
            intent.putExtra("nominal", nominalBprks);
            intent.putExtra("user_info", userInfo);
            intent.putExtra("username", username);
            startActivity(intent);
        }
    }

    private void getSaldoDepo(final String shipNumber){
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKDEPOSIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    showSaldoTrl();

                    nominalDeposit = jsonObject.getString("sisa_saldo");
                    String val;
                    val = "IDR " + CurencyFormat(nominalDeposit);

                    Log.d("Balance Fragment", "Nominal : " + nominalDeposit);
                    txtDeposit.setText(val);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("ship_number", shipNumber);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getSaldoBprks(final String username)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLCHECKBPRKS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    showSaldoBprks();

                    nominalBprks= jsonObject.getString("saldoLoan");
                    String nominal =  "IDR " + CurencyFormat(nominalBprks);

                    Log.d("Balance Fragment", "Nominal BPRKS : " + nominalBprks);

                    txtBprks.setText(nominal);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                return hashMap;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}