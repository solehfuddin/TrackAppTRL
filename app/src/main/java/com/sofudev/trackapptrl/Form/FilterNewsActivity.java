package com.sofudev.trackapptrl.Form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andexert.library.RippleView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.sofudev.trackapptrl.App.AppController;
import com.sofudev.trackapptrl.Custom.Config;
import com.sofudev.trackapptrl.Data.Data_news_category;
import com.sofudev.trackapptrl.Fragment.NewsCategoryFragment;
import com.sofudev.trackapptrl.Fragment.NewsFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterNewsActivity extends AppCompatActivity {
    Config config = new Config();
    String URLCATEGORIES = config.Ip_address + config.news_getcategory;
    List<Data_news_category> categoryList = new ArrayList<>();;

    private ViewPagerNewsAdapter pagerAdapter;
    View decorView;
    RippleView rippleBtnBack;
    SlidingTabLayout tabLayout;
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_news);

        rippleBtnBack = findViewById(R.id.filter_news_ripplebtnback);
        tabLayout = (SlidingTabLayout) findViewById(R.id.filter_news_slidingtab);
        pager = (ViewPager) findViewById(R.id.filter_news_viewpager);

        decorView = getWindow().getDecorView();
        pager = ViewFindUtils.find(decorView, R.id.filter_news_viewpager);
        pagerAdapter = new ViewPagerNewsAdapter(getSupportFragmentManager());

        rippleBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getCategory();
    }

    private void getCategory() {
        categoryList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, URLCATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    Log.d("NEWS FILTER", response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);

                        Data_news_category item = new Data_news_category();
                        item.setId(object.getString("id"));
                        item.setSlug(object.getString("slug"));
                        item.setTitle(object.getString("title"));
                        item.setType(object.getString("type"));

                        categoryList.add(item);
                    }

                    setupPager();
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

    private void setupPager(){
        pagerAdapter.removeAllFragment();

        for(int j = 0; j < categoryList.size(); j++)
        {
            Data_news_category cat = categoryList.get(j);
            pagerAdapter.addFragment(new NewsCategoryFragment(), new Data_news_category(cat.getId(), cat.getTitle(), cat.getSlug(), cat.getType()));
        }

        pager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(pager);

        if (pager != null) {
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    pagerAdapter.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private static class ViewPagerNewsAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private List<Data_news_category> item = new ArrayList<>();

        public ViewPagerNewsAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return NewsCategoryFragment.newInstance(item.get(position));
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return item.get(position).getTitle();
        }

        private void addFragment(Fragment fragment, Data_news_category list){
            fragmentList.add(fragment);
            item.add(list);
        }

        private void removeAllFragment(){
            fragmentList.clear();
            item.clear();
        }
    }
}