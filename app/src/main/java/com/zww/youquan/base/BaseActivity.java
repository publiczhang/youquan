package com.zww.youquan.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.zww.youquan.R;

/**
 * BaseActivity
 * @author zww
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static int CURRENT_STATUE;

    protected static final int STATUE_CONTENT = 0;

    protected static final int STATUE_LOADING = 1;

    protected static final int STATUE_EMPTY = 2;

    protected static final int STATUE_ERROR = 3;

    View content;
    FrameLayout contentHolder;
    SpinKitView loading;
    RelativeLayout emptyLayout;
    RelativeLayout errorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        contentHolder = findViewById(R.id.contentHolder);
        loading = findViewById(R.id.loading);
        emptyLayout = findViewById(R.id.emptyLayout);
        content = initContent(contentHolder);
        contentHolder.removeAllViews();
        contentHolder.addView(content);
        errorLayout = findViewById(R.id.errorLayout);
        initViewModel();
    }

    /**
     * initContent
     *
     * @param contentHolder contentHolder
     * @return View
     */
    protected abstract View initContent(ViewGroup contentHolder);

    /**
     * initViewModel
     */
    protected abstract void initViewModel();

    protected void showContentLayout(boolean showContent) {
        contentHolder.setVisibility(showContent ? View.VISIBLE : View.GONE);
    }

    protected int getCurrentStatue() {
        return CURRENT_STATUE;
    }

    protected void showLoadingStatue(boolean showLoading) {
        loading.setVisibility(showLoading ? View.VISIBLE : View.GONE);
    }

    protected void showEmptyStatue(boolean showEmpty) {
        emptyLayout.setVisibility(showEmpty ? View.VISIBLE : View.GONE);
    }

    protected void showErrorStatue(boolean showError) {
        errorLayout.setVisibility(showError ? View.VISIBLE : View.GONE);
    }
}
