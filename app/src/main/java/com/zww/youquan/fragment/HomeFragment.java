package com.zww.youquan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.zww.youquan.R;
import com.zww.youquan.adapter.HomeAdapter;
import com.zww.youquan.base.BaseFragment;
import com.zww.youquan.viewmodel.HomeViewModel;

/**
 * HomeFragment
 *
 * @author Administrator
 */
public class HomeFragment extends BaseFragment implements TextView.OnEditorActionListener {

    private ViewPager2 homeViewPager2;
    private EditText searchEdit;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchEdit = view.findViewById(R.id.searchEdit);
        searchEdit.setFocusable(true);
        searchEdit.setFocusableInTouchMode(true);
        searchEdit.requestFocus();
        searchEdit.setOnEditorActionListener(this);
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

    @Override
    protected void initViewModel() {
        Log.e("test","initViewModel");
        homeViewModel= ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getHomeTabInfo();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String trim = searchEdit.getText().toString().trim();
            if (TextUtils.isEmpty(trim)) {
                Toast.makeText(activity, "请输入您要搜索的内容", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }
        return false;
    }

}
