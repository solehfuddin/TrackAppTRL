package com.sofudev.trackapptrl.Form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_filter_deliverytrack;
import com.sofudev.trackapptrl.Fragment.DeliveryTrackingFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormDeliveryTrackingActivity extends AppCompatActivity {
    private Config config = new Config();
    private String URLGENERATETOKEN  = config.Ip_21express + config.generate21_token;
    private String URLGENERATETOKENTIKI = config.Ip_tiki + config.generatetiki_token;

    UniversalFontTextView txtCounter, txtOpticName;
    RippleView btnBack;
    SlidingTabLayout tabLayout;
    private ProgressBar progressBar;
    private ViewPager viewPager;
    private ViewStatePagerAdapter statePagerAdapter;
    View decorView;

    String username = "";

    int expired;
    String sActive, sPast, sTotal, token, tokenTiki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_delivery_tracking);

        btnBack   = findViewById(R.id.form_tracksp_ripplebtnback);
        tabLayout = findViewById(R.id.form_deliverytrack_slidingtab);
        txtCounter= findViewById(R.id.form_deliverytrack_txtcounter);
        txtOpticName = findViewById(R.id.form_deliverytrack_txtopticname);
        progressBar = findViewById(R.id.form_deliverytrack_progressbar);

        decorView = getWindow().getDecorView();
        viewPager = ViewFindUtils.find(decorView, R.id.form_deliverytrack_viewpager);

        statePagerAdapter = new ViewStatePagerAdapter(getSupportFragmentManager());

        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString("username");
            sActive = bundle.getString("activetitle");
            sPast = bundle.getString("pasttitle");
            sTotal = bundle.getString("totalprocess");

            generateToken();
//            generateTokenTiki();
            txtCounter.setText(sTotal);
            txtOpticName.setText(username);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void generateToken()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGENERATETOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    token = jsonObject.getString("access_token");
                    expired = jsonObject.getInt("expires_in");
                    Log.d("Generate Token", response);
                    Log.d("Token 21Express", token);
                    Log.d("Expired Token", String.valueOf(expired));

                    expired = expired * 1000;
                    checkToken(expired);
                    generateTokenTiki();
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
                map.put("username", config.Username21);
                map.put("password", config.Password21);
                map.put("client_id", config.ClientId);
                map.put("client_secret", config.ClientSecret);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void generateTokenTiki()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLGENERATETOKENTIKI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject res = object.getJSONObject("response");
                    tokenTiki = res.getString("token");

                    Log.d("Generate Token Tiki : ", response);
                    Log.d("Token Tiki", tokenTiki);
                    setupPager(token, tokenTiki);
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
                map.put("username", config.UsernameTiki);
                map.put("password", config.PasswordTiki);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void checkToken(final int expire)
    {
        new CountDownTimer(expire, 1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                generateToken();
//                generateTokenTiki();
            }
        }.start();
    }

    private void setupPager(final String mytoken, final String tikitoken)
    {
        statePagerAdapter.removeAllFragment();
        statePagerAdapter.addFragment(new DeliveryTrackingFragment(), new Data_filter_deliverytrack(sActive, username, "ACTIVE", mytoken, tikitoken));
        statePagerAdapter.addFragment(new DeliveryTrackingFragment(), new Data_filter_deliverytrack(sPast, username, "PAST", mytoken, tikitoken));
        viewPager.setAdapter(statePagerAdapter);
        tabLayout.setViewPager(viewPager);

        progressBar.setVisibility(View.GONE);

        if (viewPager != null && viewPager.getAdapter() != null)
        {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private class ViewStatePagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private List<Data_filter_deliverytrack> item = new ArrayList<>();

        private ViewStatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return DeliveryTrackingFragment.newInstance(item.get(i));
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return item.get(position).getTitle();
        }

        private void addFragment(Fragment fragment, Data_filter_deliverytrack data) {
            mFragmentList.add(fragment);
            item.add(data);
        }

        private void removeAllFragment() {
            mFragmentList.clear();
            item.clear();
        }
    }
}