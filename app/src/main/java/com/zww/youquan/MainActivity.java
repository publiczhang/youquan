package com.zww.youquan;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.zww.youquan.adapter.HomeAdapter;
import com.zww.youquan.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    Disposable mDisposable;
    int counter = 2;

    protected View initContent(ViewGroup content) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, content, false);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab3"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab4"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab5"));
        ViewPager2 viewpager2 = view.findViewById(R.id.viewpager2);
        viewpager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        HomeAdapter homeAdapter = new HomeAdapter(this);
        viewpager2.setAdapter(homeAdapter);
        view.findViewById(R.id.homeButton).setOnClickListener(this);
        view.findViewById(R.id.tab).setOnClickListener(this);
        view.findViewById(R.id.personalCenter).setOnClickListener(this);
        return view;
    }

    @Override
    protected void initViewModel() {
        //模拟网络请求
        startFollow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tab:
                Toast.makeText(this, "Tab", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personalCenter:
                Toast.makeText(this, "我的", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void startFollow() {
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                        counter--;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == counter - 1) {
                            showContentLayout(true);
                            showLoadingStatue(false);
                            mDisposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDisposable != null && !mDisposable.isDisposed()) {
                            mDisposable.isDisposed();
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mDisposable != null && !mDisposable.isDisposed()) {
                            mDisposable.isDisposed();
                        }
                    }
                });

    }

}
