package com.zww.youquan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zww.youquan.base.BaseViewModel;
import com.zww.youquan.bean.OptimusMaterialBean;
import com.zww.youquan.http.RequestManager;
import com.zww.youquan.request.HomeService;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * HomeViewModel
 *
 * @author Administrator
 */
public class HomeViewModel extends BaseViewModel {

    public MutableLiveData<OptimusMaterialBean> homeTabResult;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeTabResult = new MutableLiveData<>();
    }

    public void getHomeTabInfo() {
        RequestManager.getInstance().entry().create(HomeService.class)
                .getHomeTab("4092",1,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OptimusMaterialBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(OptimusMaterialBean bean) {
                        homeTabResult.setValue(bean);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
