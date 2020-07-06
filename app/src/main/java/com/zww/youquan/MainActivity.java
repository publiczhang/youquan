package com.zww.youquan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zww.youquan.base.BaseActivity;
import com.zww.youquan.base.BaseFragment;
import com.zww.youquan.fragment.CollectionFragment;
import com.zww.youquan.fragment.FreeShippingFragment;
import com.zww.youquan.fragment.HomeFragment;
import com.zww.youquan.fragment.PersonalCenterFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * MainActivity
 *
 * @author zww
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    Disposable mDisposable;
    int counter = 2;

    private List<BaseFragment> fragmentList;
    private FragmentManager supportFragmentManager;

    @Override
    protected View initContent(ViewGroup content) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, content, false);
        HomeFragment homeFragment = new HomeFragment();
        FreeShippingFragment freeShippingFragment = new FreeShippingFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        PersonalCenterFragment personalCenterFragment = new PersonalCenterFragment();
        fragmentList = new ArrayList<>();
        fragmentList.add(homeFragment);
        fragmentList.add(freeShippingFragment);
        fragmentList.add(collectionFragment);
        fragmentList.add(personalCenterFragment);
        supportFragmentManager = getSupportFragmentManager();
        view.findViewById(R.id.homeButton).setOnClickListener(this);
        view.findViewById(R.id.freeShipping).setOnClickListener(this);
        view.findViewById(R.id.collection).setOnClickListener(this);
        view.findViewById(R.id.personalCenter).setOnClickListener(this);
        supportFragmentManager.beginTransaction()
                .add(R.id.container, freeShippingFragment)
                .add(R.id.container, homeFragment)
                .add(R.id.container, collectionFragment)
                .add(R.id.container, personalCenterFragment)
                .commit();
        showFragment(0);
        return view;
    }

    @Override
    protected void initViewModel() {
        //模拟网络请求
        startFollow();
    }

    private void showFragment(int index) {
        for (int i = 0; i < fragmentList.size(); i++) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if (index == i) {
                fragmentTransaction.show(fragmentList.get(i));
            } else {
                fragmentTransaction.hide(fragmentList.get(i));
            }
            fragmentTransaction.commitNow();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                showFragment(0);
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show();
                break;
            case R.id.freeShipping:
                showFragment(1);
                Toast.makeText(this, "9.9包邮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.collection:
                showFragment(2);
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personalCenter:
                showFragment(3);
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
