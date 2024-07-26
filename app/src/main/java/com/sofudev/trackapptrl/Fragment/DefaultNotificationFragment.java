package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.sofudev.trackapptrl.Adapter.Adapter_notification;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_notification_center;
import com.sofudev.trackapptrl.Data.Data_notification_item;
import com.sofudev.trackapptrl.Form.AssignApprovalActivity;
import com.sofudev.trackapptrl.Form.FormOrderHistoryActivity;
import com.sofudev.trackapptrl.Form.FormTrackingSpActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DefaultNotificationFragment extends Fragment {
    Config config = new Config();
    private String URLGETDATA = config.Ip_address + config.notification_getdata;
    private String URLSETISREAD = config.Ip_address + config.notification_updateIsRead;
    private String argTitle, argUsername, argLevel, argIdparty, argCustname;
    private int argCatType = 0;
    private String TAG = DefaultNotificationFragment.class.getSimpleName();

    LinearLayout linearNew, linearOld;
    RecyclerView recyclerViewNew, recyclerViewOld, recyclerViewType;
    RelativeLayout rl_track;
    ProgressBar progressBarNew, progressBarOld, progressBarType;
    ImageView imgNotFoundNew, imgNotFoundOld, imgNotFoundType;

    Context myContext;

    RecyclerView.LayoutManager recyclerLayoutManagerNew, recyclerLayoutManagerOld, recyclerLayoutManagerType;
    Adapter_notification adapter_notification_new;
    Adapter_notification adapter_notification_old;
    Adapter_notification adapter_notification_type;
    List<Data_notification_item> notificationListNew = new ArrayList<>();
    List<Data_notification_item> notificationListOld = new ArrayList<>();
    List<Data_notification_item> notificationListType= new ArrayList<>();

    public DefaultNotificationFragment() {
        // Required empty public constructor
    }

    public static DefaultNotificationFragment newInstance(Data_notification_center item) {
        Bundle args = new Bundle();
        args.putString("title", item.getTitle());
        args.putString("username", item.getUsername());
        args.putString("level", item.getLevel());
        args.putString("idparty", item.getIdparty());
        args.putString("custname", item.getCustname());
        args.putInt("catType", item.getCategoryType());

        DefaultNotificationFragment fragment = new DefaultNotificationFragment();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_default_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearNew = view.findViewById(R.id.fragment_defaultnotif_linearNew);
        linearOld = view.findViewById(R.id.fragment_defaultnotif_linearOld);
        recyclerViewNew = view.findViewById(R.id.fragment_defaultnotif_recyclerNew);
        recyclerViewOld = view.findViewById(R.id.fragment_defaultnotif_recyclerOld);
        recyclerViewType = view.findViewById(R.id.fragment_defaultnotif_recyclerType);
        progressBarNew = view.findViewById(R.id.fragment_defaultnotif_progressNew);
        progressBarOld = view.findViewById(R.id.fragment_defaultnotif_progressOld);
        progressBarType= view.findViewById(R.id.fragment_defaultnotif_progressType);
        imgNotFoundOld = view.findViewById(R.id.fragment_defaultnotif_imgErrorOld);
        imgNotFoundNew = view.findViewById(R.id.fragment_defaultnotif_imgErrorNew);
        imgNotFoundType = view.findViewById(R.id.fragment_defaultnotif_imgErrorType);

        recyclerViewNew.setHasFixedSize(true);
        recyclerViewOld.setHasFixedSize(true);
        recyclerViewType.setHasFixedSize(true);
        recyclerLayoutManagerNew = new LinearLayoutManager(getContext());
        recyclerLayoutManagerOld = new LinearLayoutManager(getContext());
        recyclerLayoutManagerType = new LinearLayoutManager(getContext());

        recyclerViewNew.setLayoutManager(recyclerLayoutManagerNew);
        recyclerViewOld.setLayoutManager(recyclerLayoutManagerOld);
        recyclerViewType.setLayoutManager(recyclerLayoutManagerType);

        adapter_notification_new = new Adapter_notification(myContext, notificationListNew, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d(TAG, "Notif Type : " + notificationListNew.get(pos).getType());
                Log.d(TAG, "Notif Redirect : " + notificationListNew.get(pos).getRedirectTo());
                redirectTo(notificationListNew.get(pos).getId(), notificationListNew.get(pos).getType(), notificationListNew.get(pos).getRedirectTo());

            }
        });

        adapter_notification_old = new Adapter_notification(myContext, notificationListOld, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d(TAG, "Notif Type : " + notificationListOld.get(pos).getType());
                Log.d(TAG, "Notif Redirect : " + notificationListOld.get(pos).getRedirectTo());
                redirectTo(notificationListOld.get(pos).getId(), notificationListOld.get(pos).getType(), notificationListOld.get(pos).getRedirectTo());

            }
        });

        adapter_notification_type = new Adapter_notification(myContext, notificationListType, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d(TAG, "Notif Type : " + notificationListType.get(pos).getType());
                Log.d(TAG, "Notif Redirect : " + notificationListType.get(pos).getRedirectTo());
                redirectTo(notificationListType.get(pos).getId(), notificationListType.get(pos).getType(), notificationListType.get(pos).getRedirectTo());
            }
        });

        recyclerViewNew.setAdapter(adapter_notification_new);
        recyclerViewOld.setAdapter(adapter_notification_old);
        recyclerViewType.setAdapter(adapter_notification_type);

        setData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(DefaultNotificationFragment.class.getSimpleName(), "Type : " + argCatType + " di resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(DefaultNotificationFragment.class.getSimpleName(), "Type : " + argCatType + " di pause");
    }

    private void setData(){
        Bundle data = this.getArguments();

        if (data != null)
        {
            argTitle = data.getString("title");
            argUsername = data.getString("username");
            argLevel = data.getString("level");
            argIdparty = data.getString("idparty");
            argCustname = data.getString("custname");
            argCatType = data.getInt("catType", 0);

            if (argCatType > 0)
            {
                linearNew.setVisibility(View.GONE);
                linearOld.setVisibility(View.GONE);

                recyclerViewType.setVisibility(View.VISIBLE);

                if (argCatType == 11)
                {
                    getNotificationType(argUsername, "Info");
                    progressBarType.setVisibility(View.VISIBLE);
                }
                else if (argCatType == 12)
                {
                    getNotificationType(argUsername, "Transaksi");
                    progressBarType.setVisibility(View.VISIBLE);
                }
                else
                {
                    getNotificationType(argUsername, "Promo");
                    progressBarType.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                linearNew.setVisibility(View.VISIBLE);
                linearOld.setVisibility(View.VISIBLE);
                progressBarNew.setVisibility(View.VISIBLE);
                progressBarOld.setVisibility(View.VISIBLE);

                getNotificationNew(argUsername);
                getNotificationOld(argUsername);

                recyclerViewType.setVisibility(View.GONE);
            }
        }
    }

    private void getNotificationNew(final String username){
        notificationListNew.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    progressBarNew.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
//                            showErrorImage();
                            imgNotFoundNew.setVisibility(View.VISIBLE);
                            recyclerViewNew.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_notification_item item = new Data_notification_item();
                            item.setId(object.getString("id"));
                            item.setUsername(object.getString("username"));
                            item.setTitle(object.getString("title"));
                            item.setMessage(object.getString("message"));
                            item.setImage(object.getString("image"));
                            item.setType(object.getString("type"));
                            item.setRedirectTo(object.getString("redirectTo"));
                            item.setIsClick(object.getString("isClick"));
                            item.setWaktu(object.getString("waktu"));

                            notificationListNew.add(item);

                            imgNotFoundNew.setVisibility(View.GONE);
                            recyclerViewNew.setVisibility(View.VISIBLE);
                        }
                    }

                    adapter_notification_new.notifyDataSetChanged();
                }catch (Exception e)
                {
                    e.printStackTrace();
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
                map.put("username", username);
                map.put("isLatest", "true");
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getNotificationOld(final String username){
        notificationListOld.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    progressBarOld.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
//                            showErrorImage();

                            imgNotFoundOld.setVisibility(View.VISIBLE);
                            recyclerViewOld.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_notification_item item = new Data_notification_item();
                            item.setId(object.getString("id"));
                            item.setUsername(object.getString("username"));
                            item.setTitle(object.getString("title"));
                            item.setMessage(object.getString("message"));
                            item.setImage(object.getString("image"));
                            item.setType(object.getString("type"));
                            item.setRedirectTo(object.getString("redirectTo"));
                            item.setIsClick(object.getString("isClick"));
                            item.setWaktu(object.getString("waktu"));

                            notificationListOld.add(item);

                            imgNotFoundOld.setVisibility(View.GONE);
                            recyclerViewOld.setVisibility(View.VISIBLE);
                        }
                    }

                    adapter_notification_old.notifyDataSetChanged();
                }catch (Exception e)
                {
                    e.printStackTrace();
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
                map.put("username", username);
                map.put("isLatest", "false");
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void getNotificationType(final String username, final String type){
        notificationListType.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLGETDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    progressBarType.setVisibility(View.GONE);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("Error"))
                        {
//                            showErrorImage();

                            imgNotFoundType.setVisibility(View.VISIBLE);
                            recyclerViewType.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_notification_item item = new Data_notification_item();
                            item.setId(object.getString("id"));
                            item.setUsername(object.getString("username"));
                            item.setTitle(object.getString("title"));
                            item.setMessage(object.getString("message"));
                            item.setImage(object.getString("image"));
                            item.setType(object.getString("type"));
                            item.setRedirectTo(object.getString("redirectTo"));
                            item.setIsClick(object.getString("isClick"));
                            item.setWaktu(object.getString("waktu"));

                            notificationListType.add(item);

                            imgNotFoundType.setVisibility(View.GONE);
                            recyclerViewType.setVisibility(View.VISIBLE);
                        }
                    }

                    adapter_notification_type.notifyDataSetChanged();
                }catch (Exception e)
                {
                    e.printStackTrace();
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
                map.put("username", username);
                map.put("type", type);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }

    private void redirectTo(final String notifId, final String notifType, final String notifRedirect)
    {
        if (notifRedirect == null)
        {
            //Update Is Read
            updateNotifIsRead(notifId);
        }
        else
        {
            //Update Is Read
            //Open another form
            updateNotifIsRead(notifId);
            if (notifType.equals("Transaksi"))
            {
                //Cek Kode Orderan
                if (notifRedirect.contains("SPAF"))
                {
                    if (Integer.parseInt(argLevel) == 1) {
                        Intent intent = new Intent(myContext, FormTrackingSpActivity.class);
                        intent.putExtra("username", argUsername);
                        intent.putExtra("level", 1);
                        intent.putExtra("spnumber", notifRedirect);

                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormTrackingSpActivity.class);
                        intent.putExtra("username", argCustname);
                        intent.putExtra("level", 0);
                        intent.putExtra("spnumber", notifRedirect);

                        startActivity(intent);
                    }
                }
                else if (notifRedirect.contains("ALO"))
                {
                    if (argLevel != null) {
                        if (Integer.parseInt(argLevel) == 0) {
                           Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                           intent.putExtra("idparty", argIdparty);
                           intent.putExtra("user_info", argUsername);
                           intent.putExtra("level", argLevel);
                           intent.putExtra("ordernumber", notifRedirect);
                           startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                            intent.putExtra("sales", argUsername);
                            intent.putExtra("level", argLevel);
                            intent.putExtra("ordernumber", notifRedirect);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(myContext, FormOrderHistoryActivity.class);
                        intent.putExtra("idparty", argIdparty);
                        intent.putExtra("user_info", argUsername);
                        intent.putExtra("level", argLevel);
                        intent.putExtra("ordernumber", notifRedirect);
                        startActivity(intent);
                    }
                }
            }
            else if (notifType.equals("Info"))
            {
                //Redirect Ke History
                if (notifRedirect.contains("BIN"))
                {
                    Intent intent = new Intent(myContext, AssignApprovalActivity.class);
                    intent.putExtra("username", argUsername);
                    intent.putExtra("custname", argCustname);
                    intent.putExtra("idparty", argIdparty);
                    intent.putExtra("noinv", notifRedirect);
                    startActivity(intent);
                }
            }
            else
            {
                //Redirect Ke detail Promosi
            }
        }
    }

    private void updateNotifIsRead(final String id)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URLSETISREAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success"))
                    {
                        Log.d(TAG, "Success Update");

                        if (argCatType > 0)
                        {
                            linearNew.setVisibility(View.GONE);
                            linearOld.setVisibility(View.GONE);

                            recyclerViewType.setVisibility(View.VISIBLE);

                            if (argCatType == 11)
                            {
                                getNotificationType(argUsername, "Info");
                                progressBarType.setVisibility(View.VISIBLE);
                            }
                            else if (argCatType == 12)
                            {
                                getNotificationType(argUsername, "Transaksi");
                                progressBarType.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                getNotificationType(argUsername, "Promo");
                                progressBarType.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            linearNew.setVisibility(View.VISIBLE);
                            linearOld.setVisibility(View.VISIBLE);
                            progressBarNew.setVisibility(View.VISIBLE);
                            progressBarOld.setVisibility(View.VISIBLE);

                            getNotificationNew(argUsername);
                            getNotificationOld(argUsername);

                            recyclerViewType.setVisibility(View.GONE);
                        }
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.warning(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("notifId", id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}