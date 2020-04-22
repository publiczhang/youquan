package com.zww.youquan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.viewpager2.widget.ViewPager2;

import com.zww.youquan.adapter.MainAdapter;
import com.zww.youquan.base.BaseActivity;
import com.zww.youquan.util.AliUtil;

import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.alibaba.baichuan.android.trade.model.OpenType.Native;
import static com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType.AlibcNativeFailModeJumpH5;

/**
 * MainActivity
 *
 * @author zww
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    Disposable mDisposable;
    int counter = 2;
    ViewPager2 viewpager2;
    @Override
    protected View initContent(ViewGroup content) {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_main, content, false);
        viewpager2 = view.findViewById(R.id.viewpager2);
        viewpager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        MainAdapter homeAdapter = new MainAdapter(this);
        viewpager2.setAdapter(homeAdapter);
        view.findViewById(R.id.homeButton).setOnClickListener(this);
        view.findViewById(R.id.freeShipping).setOnClickListener(this);
        view.findViewById(R.id.collection).setOnClickListener(this);
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
                viewpager2.setCurrentItem(0,false);
                Toast.makeText(this, "首页", Toast.LENGTH_SHORT).show();
                break;
            case R.id.freeShipping:
                viewpager2.setCurrentItem(1,false);
                Toast.makeText(this, "9.9包邮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.collection:
                viewpager2.setCurrentItem(2,false);
                Toast.makeText(this, "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.personalCenter:
                viewpager2.setCurrentItem(3,false);
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
                            AliUtil.openDeatailPage(MainActivity.this,"",Native,"taobao","",AlibcNativeFailModeJumpH5);
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
