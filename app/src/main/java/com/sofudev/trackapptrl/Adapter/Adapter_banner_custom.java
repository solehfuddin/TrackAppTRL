package com.sofudev.trackapptrl.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.sofudev.trackapptrl.Fragment.CustomHomeFragment;

import java.util.List;

public class Adapter_banner_custom extends FragmentStatePagerAdapter {
    List<Fragment> mFrags;

    public Adapter_banner_custom(@NonNull FragmentManager fm, List<Fragment> mFrags) {
        super(fm);
        this.mFrags = mFrags;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        int index = position % mFrags.size();
        return CustomHomeFragment.newInstance(mFrags.get(index).getArguments().getString("imgSlider"));
    }

    @Override
    public int getCount() {
        return mFrags.size();
    }
}
