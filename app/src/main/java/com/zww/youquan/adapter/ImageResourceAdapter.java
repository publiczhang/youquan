package com.zww.youquan.adapter;

import android.view.View;

import com.zhpan.bannerview.BaseBannerAdapter;
import com.zww.youquan.R;

/**
 * <pre>
 *
 * @author zhpan
 * @date 2020/4/5
 *   Description:
 * </pre>
 */
public class ImageResourceAdapter extends BaseBannerAdapter<Integer, ImageResourceViewHolder> {

    private int roundCorner;

    public ImageResourceAdapter(int roundCorner) {
        this.roundCorner = roundCorner;
    }


    @Override
    protected void onBind(ImageResourceViewHolder holder, Integer data, int position, int pageSize) {
        holder.bindData(data, position, pageSize);
    }

    @Override
    public ImageResourceViewHolder createViewHolder(View itemView, int viewType) {
        return new ImageResourceViewHolder(itemView, roundCorner);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_slide_mode;
    }
}
