package com.zww.youquan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zww.youquan.R;
import com.zww.youquan.adapter.PersonalAdapter;
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
        View inflate = inflater.inflate(R.layout.fragment_personal, container, false);
        ImageView coverImage = inflate.findViewById(R.id.coverImage);
        Glide.with(this).load(R.drawable.empty_image).into(coverImage);
        TextView nickName = inflate.findViewById(R.id.nickName);
        nickName.setText("NickName");
        RecyclerView recycler = inflate.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(activity));
        PersonalAdapter adapter = new PersonalAdapter();
        recycler.setAdapter(adapter);
        return inflate;
    }
}
