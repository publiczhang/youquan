package com.zww.youquan.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.zww.youquan.base.BaseViewModel;
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

    private MutableLiveData<Integer> homeTabResult;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeTabResult = new MutableLiveData<>();
    }

    private void getHomeTabInfo() {
        RequestManager.getIntance().entry().create(HomeService.class)
                .getHomeTab("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

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
