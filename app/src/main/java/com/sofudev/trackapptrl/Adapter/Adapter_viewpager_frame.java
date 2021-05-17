package com.sofudev.trackapptrl.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_viewpager_frame extends FragmentPagerAdapter {

    List <Fragment> fragmentList = new ArrayList<>();
    List <String> titleFragmentList = new ArrayList<>();

    public Adapter_viewpager_frame(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleFragmentList.get(position);
    }

    public void addFragment(Fragment fragment, String title)
    {
        fragmentList.add(fragment);
        titleFragmentList.add(title);
    }

    public void clearFragment()
    {
        fragmentList.clear();
        titleFragmentList.clear();
    }
}
