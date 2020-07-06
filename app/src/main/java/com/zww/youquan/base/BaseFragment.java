package com.zww.youquan.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * BaseFragment
 *
 * @author Administrator
 */
public abstract class BaseFragment extends Fragment {

    protected Activity activity;
    protected Context context;
    protected boolean autoLoadData;
    private boolean isFirstVisibleToUser;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isFirstVisibleToUser&&autoLoadData){
//            initViewModel();
//        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    /**
     * initViewModel
     */
    protected abstract void initViewModel();

    protected void setAutoLoadData(boolean autoLoadData) {
        this.autoLoadData = autoLoadData;
    }

    protected void showInput(View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    protected void hideInput(View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
