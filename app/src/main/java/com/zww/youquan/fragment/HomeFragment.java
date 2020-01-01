package com.zww.youquan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zww.youquan.R;
import com.zww.youquan.adapter.HomeAdapter;
import com.zww.youquan.base.BaseFragment;

/**
 * HomeFragment
 *
 * @author Administrator
 */
public class HomeFragment extends BaseFragment {

    private ViewPager2 homeViewPager2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        initViewpager2(view);
        new TabLayoutMediator(tabLayout, homeViewPager2, false, (tab, position) -> {
            tab.setText("Tab" + position);
        }).attach();
        return view;
    }

    private void initViewpager2(View view) {
        homeViewPager2 = view.findViewById(R.id.homeViewPager2);
        homeViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        HomeAdapter homeAdapter = new HomeAdapter();
        homeViewPager2.setAdapter(homeAdapter);
    }

}
