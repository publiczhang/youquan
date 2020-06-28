package com.zww.youquan.viewmodel;

import android.app.Application;
import android.util.Log;

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

    private MutableLiveData<Integer> homeTabResult;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeTabResult = new MutableLiveData<>();
    }

    public void getHomeTabInfo() {
        Log.e("test","getHomeTabInfo");
        RequestManager.getInstance().entry().create(HomeService.class)
                .getHomeTab()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OptimusMaterialBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("test","onSubscribe");
                    }

                    @Override
                    public void onNext(OptimusMaterialBean o) {
                        Log.e("test","onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("test","onError:"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("test","onComplete");
                    }
                });
    }

}
