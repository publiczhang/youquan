package com.zww.youquan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zww.youquan.R;
import com.zww.youquan.base.BaseFragment;

/**
 * PersonalCenterFragment
 *
 * @author Administrator
 */
public class PersonalCenterFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }
}
