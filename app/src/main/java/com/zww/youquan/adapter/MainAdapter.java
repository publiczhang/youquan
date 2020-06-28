package com.zww.youquan.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.zww.youquan.base.BaseFragment;

import com.zww.youquan.fragment.CollectionFragment;
import com.zww.youquan.fragment.CommonHomeFragment;
import com.zww.youquan.fragment.FreeShippingFragment;
import com.zww.youquan.fragment.HomeFragment;
import com.zww.youquan.fragment.PersonalCenterFragment;

/**
 *MainAdapter
 * @author Administrator
 */
public class MainAdapter extends FragmentStateAdapter {

    public MainAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FreeShippingFragment();
            case 2:
                return new CollectionFragment();
            case 3:
                return new PersonalCenterFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
