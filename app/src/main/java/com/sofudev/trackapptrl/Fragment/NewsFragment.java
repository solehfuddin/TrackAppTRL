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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.raizlabs.universalfontcomponents.widget.UniversalFontTextView;
import com.sofudev.trackapptrl.Adapter.Adapter_news_featured;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Custom.RecyclerViewOnClickListener;
import com.sofudev.trackapptrl.Data.Data_news;
import com.sofudev.trackapptrl.Form.DetailNewsActivity;
import com.sofudev.trackapptrl.Form.FilterNewsActivity;
import com.sofudev.trackapptrl.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class NewsFragment extends Fragment {

    Config config = new Config();
    String URLNEWS = config.Ip_address + config.news_getdatawithlimit;

    ShimmerRecyclerView shimmerRecyclerView;
    RecyclerView recyclerView;
    UniversalFontTextView txtMore, txtTitle;

    Adapter_news_featured adapter_news_featured;
    LinearLayoutManager layoutManager;
    List<Data_news> listNews = new ArrayList<>();
    Context myContext;
    String ACTIVITY_TAG, ACCESS_FROM;

    public NewsFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_news, container, false);
        shimmerRecyclerView = view.findViewById(R.id.fragment_news_shimmer);
        recyclerView = view.findViewById(R.id.fragment_news_recyclerview);
        txtMore = view.findViewById(R.id.fragment_news_txtMore);
        txtTitle = view.findViewById(R.id.fragment_news_txtTitle);

        shimmerRecyclerView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

        getData();

        layoutManager = new LinearLayoutManager(myContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter_news_featured = new Adapter_news_featured(myContext, listNews, new RecyclerViewOnClickListener() {
            @Override
            public void onItemClick(View view, int pos, String id) {
                Log.d("ID click News : ", id);

                if (ACCESS_FROM.equals("main"))
                {
                    Toasty.warning(myContext, "Silahkan login terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(getContext(), DetailNewsActivity.class);
                    intent.putExtra("title", listNews.get(pos).getTitle());
                    intent.putExtra("subtitle", listNews.get(pos).getCreate_by_user() + " - " + listNews.get(pos).getCreate_date());
                    intent.putExtra("image", listNews.get(pos).getImages());
                    intent.putExtra("descr", listNews.get(pos).getDescription());

                    startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(adapter_news_featured);

        txtMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FilterNewsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getData(){
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            ACTIVITY_TAG = bundle.getString("activity");
            ACCESS_FROM  = bundle.getString("access");

            showData();
        }
    }

    private void showData() {
        listNews.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLNEWS + "0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

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

                        listNews.add(item);
                    }

                    shimmerRecyclerView.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                    adapter_news_featured.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }
}