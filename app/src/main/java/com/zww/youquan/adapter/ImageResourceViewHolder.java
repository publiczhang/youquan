package com.zww.youquan.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.zhpan.bannerview.BaseViewHolder;
import com.zww.youquan.R;
import com.zww.youquan.widget.CornerImageView;

/**
 * <pre>
 *
 * @author zhangpan
 * @date 2019-08-14
 *   Description:
 * </pre>
 */
public class ImageResourceViewHolder extends BaseViewHolder<Integer> {

    public ImageResourceViewHolder(@NonNull View itemView, int roundCorner) {
        super(itemView);
        CornerImageView imageView = findView(R.id.bannerImage);
        imageView.setRoundCorner(roundCorner);
    }

    @Override
    public void bindData(Integer data, int position, int pageSize) {
        setImageResource(R.id.bannerImage, data);
    }
}
