package com.zww.youquan.base;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * BaseFragment
 *
 * @author Administrator
 */
public class BaseFragment extends Fragment {

    protected Activity activity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}
