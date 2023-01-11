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
import com.dueeeke.tablayout.SlidingTabLayout;
import com.sofudev.trackapptrl.Data.Data_spframe_filter;
import com.sofudev.trackapptrl.Fragment.SpApprovalFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

public class SpApprovalActivity extends AppCompatActivity {
    SlidingTabLayout tabLayout;
    ViewPager viewPager;
    RippleView btnBack;
    private ViewPagerAdapter pagerAdapter;
    View decorView;

    String sales, areacode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_approval);

        btnBack = findViewById(R.id.form_approvalsp_ripplebtnback);
        tabLayout  = (SlidingTabLayout) findViewById(R.id.form_approvalsp_slidingtab);
        viewPager  = (ViewPager) findViewById(R.id.form_approvalsp_viewpager);

        decorView  = getWindow().getDecorView();
        viewPager  = ViewFindUtils.find(decorView, R.id.form_approvalsp_viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        getData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            sales     = bundle.getString("username");
            areacode  = bundle.getString("salesarea");

            Log.d("NAMA SALES", sales);

            setupPager();
        }
    }

    private void setupPager(){
        pagerAdapter.removeAllFragment();
        pagerAdapter.addFragment(new SpApprovalFragment(), new Data_spframe_filter(sales,"On process", areacode, "2022-12-01", "2023-01-02", 0));
        pagerAdapter.addFragment(new SpApprovalFragment(), new Data_spframe_filter(sales,"Tracking SP", areacode, "2022-12-01", "2023-01-02",1));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        if (viewPager != null && viewPager.getAdapter() != null)
        {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private List<Data_spframe_filter> item = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return SpApprovalFragment.newInstance(item.get(position));
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

        private void addFragment(Fragment fragment, Data_spframe_filter list){
            fragmentList.add(fragment);
            item.add(list);
        }

        private void removeAllFragment(){
            fragmentList.clear();
            item.clear();
        }
    }
}