package com.sofudev.trackapptrl.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_news;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_news;
import com.sofudev.trackapptrl.Data.Data_news_category;
import com.sofudev.trackapptrl.Form.DetailNewsActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsCategoryFragment extends Fragment {
    Config config = new Config();
    String URLNEWSCATEGORY = config.Ip_address + config.news_getbycategorylimit;

    ShimmerRecyclerView shimmerRecyclerView;
    RecyclerView recyclerView;
    LinearLayout linearControl;
    ImageView imgNotFound;
    UniversalFontTextView txtCounter;
    BootstrapButton btnPrev, btnNext;

    Adapter_news adapter_news;
    LinearLayoutManager layoutManagerVer;
    List<Data_news> listNews = new ArrayList<>();
    Context myContext;
    String CATEGORY_ID, CATEGORY_NEWS;

    int totalData = 0, counter = 0;

    public NewsCategoryFragment() {
        // Required empty public constructor
    }

    public static NewsCategoryFragment newInstance(Data_news_category item) {
        Bundle bundle = new Bundle();
        bundle.putString("categoryId", item.getId());
        bundle.putString("categoryName", item.getTitle());

        NewsCategoryFragment fragment = new NewsCategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_news_category, container, false);
        shimmerRecyclerView = view.findViewById(R.id.fragment_newscategory_shimmer);
        recyclerView = view.findViewById(R.id.fragment_newscategory_recyclerview);
        imgNotFound  = view.findViewById(R.id.fragment_newscategory_imgError);
        linearControl= view.findViewById(R.id.fragment_newscategory_rl_tool);
        btnPrev      = view.findViewById(R.id.fragment_newscategory_btnprev);
        btnNext      = view.findViewById(R.id.fragment_newscategory_btnnext);
        txtCounter   = view.findViewById(R.id.fragment_newscategory_txtCounter);

        getData();

        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        imgNotFound.setVisibility(View.GONE);

        layoutManagerVer = new LinearLayoutManager(myContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManagerVer);

        adapter_news = new Adapter_news(myContext, listNews, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d("ID click News : ", id);

                Intent intent = new Intent(getContext(), DetailNewsActivity.class);
                intent.putExtra("title", listNews.get(pos).getTitle());
                intent.putExtra("subtitle", listNews.get(pos).getCreate_by_user() + " - " + listNews.get(pos).getCreate_date());
                intent.putExtra("image", listNews.get(pos).getImages());
                intent.putExtra("descr", listNews.get(pos).getDescription());

                startActivity(intent);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter + 5;

                showData(CATEGORY_ID, String.valueOf(counter));
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter - 5;

                showData(CATEGORY_ID, String.valueOf(counter));
            }
        });

        recyclerView.setAdapter(adapter_news);
        return view;
    }

    private void getData(){
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            CATEGORY_ID = bundle.getString("categoryId");
            CATEGORY_NEWS  = bundle.getString("categoryName");

            Log.d("CATEGORY ID : ", CATEGORY_ID);
            Log.d("CATEGORY NEWS : ", CATEGORY_NEWS);

            showData(CATEGORY_ID, "0");
        }
    }

    private void showData(final String id, final String start) {
        listNews.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLNEWSCATEGORY + start, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int start, until;
                btnNext.setEnabled(true);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        if (object.names().get(0).equals("invalid"))
                        {
                            imgNotFound.setVisibility(View.VISIBLE);
                            linearControl.setVisibility(View.GONE);
                            shimmerRecyclerView.setVisibility(View.GONE);
                        }
                        else
                        {
                            Data_news item = new Data_news();
                            item.setId(object.getString("id"));
                            item.setTitle(object.getString("title"));
                            item.setSlug(object.getString("slug"));
                            item.setDescription(object.getString("description"));
                            item.setImages(object.getString("images"));
                            item.setType(object.getString("type"));
                            item.setCategory(object.getString("category"));
                            item.setCreate_by_user(object.getString("create_by_user"));
                            item.setCreate_date(object.getString("create_date"));
                            totalData = object.getInt("total_output");

                            listNews.add(item);

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

                            imgNotFound.setVisibility(View.GONE);
                            linearControl.setVisibility(View.VISIBLE);
                            shimmerRecyclerView.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    adapter_news.notifyDataSetChanged();
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
                map.put("id_category", id);
                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(request);
    }
}