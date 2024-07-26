package com.sofudev.trackapptrl.Form;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.andexert.library.RippleView;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.sofudev.trackapptrl.Data.Data_assignbin_filter;
import com.sofudev.trackapptrl.Fragment.AssignbinFragment;
import com.sofudev.trackapptrl.Fragment.ReturnbinFragment;
import com.sofudev.trackapptrl.Fragment.SpApprovalFragment;
import com.sofudev.trackapptrl.Fragment.SpOtherFragment;
import com.sofudev.trackapptrl.R;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

public class AssignApprovalActivity extends AppCompatActivity {
    SlidingTabLayout tabLayout;
    ViewPager viewPager;
    RippleView btnBack;

    private ViewPagerAdapter pagerAdapter;
    View decorView;

    String username, custname, partysiteid, noinv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_approval);

        btnBack     = findViewById(R.id.form_assignbin_ripplebtnback);

        tabLayout  = findViewById(R.id.form_assignbin_slidingtab);
        viewPager  = findViewById(R.id.form_assignbin_viewpager);

        decorView  = getWindow().getDecorView();
        viewPager  = ViewFindUtils.find(decorView, R.id.form_assignbin_viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        setData();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setData(){
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            username = bundle.getString("username");
            custname = bundle.getString("custname");
            partysiteid = bundle.getString("idparty");
            noinv = bundle.getString("noinv");

            setupPager();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pagerAdapter.notifyDataSetChanged();
    }

    private void setupPager(){
        pagerAdapter.removeAllFragment();
        pagerAdapter.addFragment(new AssignbinFragment(), new Data_assignbin_filter(username, "Belum Dikonfirmasi", custname, partysiteid, noinv, 1));
        pagerAdapter.addFragment(new AssignbinFragment(), new Data_assignbin_filter(username, "Sudah Dikonfirmasi", custname, partysiteid, noinv, 2));
        pagerAdapter.addFragment(new ReturnbinFragment(), new Data_assignbin_filter(username, "Kembalikan Stok", custname, partysiteid, noinv, 3));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setViewPager(viewPager);

        if (viewPager != null) {
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private List<Data_assignbin_filter> item = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                case 1 :
                    return AssignbinFragment.newInstance(item.get(position));
                default:
                    return ReturnbinFragment.newInstance(item.get(position));
            }
//            return AssignbinFragment.newInstance(item.get(position));
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

        private void addFragment(Fragment fragment, Data_assignbin_filter list){
            fragmentList.add(fragment);
            item.add(list);
        }

        private void removeAllFragment(){
            fragmentList.clear();
            item.clear();
        }
    }
}