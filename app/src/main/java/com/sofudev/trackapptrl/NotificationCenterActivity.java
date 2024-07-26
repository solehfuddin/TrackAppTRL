package com.sofudev.trackapptrl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.andexert.library.RippleView;
import com.dueeeke.tablayout.SlidingTabLayout;
import com.sofudev.trackapptrl.Data.Data_notification_center;
import com.sofudev.trackapptrl.Fragment.DefaultNotificationFragment;
import com.sofudev.trackapptrl.Util.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificationCenterActivity extends AppCompatActivity {

    private String TAG = NotificationCenterActivity.class.getSimpleName();
    private String intentUsername, intentIdparty, intentUserlevel, intentCustname;

    SlidingTabLayout tabLayout;
    ViewPager viewPager;
    RippleView rippleBtnBack, rippleBtnSetting;
    private ViewPagerAdapter pagerAdapter;
    View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_center);

        rippleBtnBack = findViewById(R.id.notification_center_ripplebtnback);
        rippleBtnSetting = findViewById(R.id.notification_center_ripplebtnsetting);
        tabLayout = findViewById(R.id.notification_center_slidingtab);
        viewPager = findViewById(R.id.notification_center_viewpager);

        decorView = getWindow().getDecorView();
        viewPager = ViewFindUtils.find(decorView, R.id.notification_center_viewpager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        getData();

        rippleBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rippleBtnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                finish();
                startActivity(i);
            }
        });
    }

    private void getData()
    {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            intentUsername  = bundle.getString("username");
            intentIdparty   = bundle.getString("idparty");
            intentUserlevel = bundle.getString("level");
            intentCustname  = bundle.getString("custname");

            setupPager();

            Log.d(TAG, "Print Username : " + intentUsername);
        }
    }

    private void setupPager(){
        pagerAdapter.removeAllFragment();

        pagerAdapter.addFragment(new DefaultNotificationFragment(), new Data_notification_center("Utama", intentUsername, intentUserlevel, intentIdparty, intentCustname, 0));
        pagerAdapter.addFragment(new DefaultNotificationFragment(), new Data_notification_center("Informasi", intentUsername, intentUserlevel, intentIdparty, intentCustname, 11));
        pagerAdapter.addFragment(new DefaultNotificationFragment(), new Data_notification_center("Transaksi", intentUsername, intentUserlevel, intentIdparty, intentCustname, 12));
        pagerAdapter.addFragment(new DefaultNotificationFragment(), new Data_notification_center("Promosi", intentUsername, intentUserlevel, intentIdparty, intentCustname, 13));

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
        private List<Data_notification_center> item = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return DefaultNotificationFragment.newInstance(item.get(position));
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

        private void addFragment(Fragment fragment, Data_notification_center list){
            fragmentList.add(fragment);
            item.add(list);
        }

        private void removeAllFragment(){
            fragmentList.clear();
            item.clear();
        }
    }
}