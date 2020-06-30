package com.zww.youquan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.indicator.enums.IndicatorSlideMode;
import com.zhpan.indicator.enums.IndicatorStyle;
import com.zww.youquan.R;
import com.zww.youquan.bean.OptimusMaterialBean;
import com.zww.youquan.widget.RecyclerViewAtViewPager2;

import java.util.ArrayList;

/**
 * TopAdapter
 *
 * @author Administrator
 */
public class TopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_ACTIVITY = 1;
    private static final int TYPE_RECOMMEND = 2;

    private Context context;
    private OptimusMaterialBean recommendData;

    public TopAdapter(OptimusMaterialBean recommendData, Context context) {
        this.context = context;
        this.recommendData = recommendData;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_BANNER;
            case 1:
                return TYPE_ACTIVITY;
            case 2:
                return TYPE_RECOMMEND;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BANNER:
                View banner = LayoutInflater.from(context).inflate(R.layout.item_top_banner, parent, false);
                return new BannerHolder(banner);
            case TYPE_ACTIVITY:
                View activity = LayoutInflater.from(context).inflate(R.layout.item_top_activity, parent, false);
                return new ActivityHolder(activity);
            case TYPE_RECOMMEND:
                View recommend = LayoutInflater.from(context).inflate(R.layout.item_top_recycler, parent, false);
                return new RecommendHolder(recommend);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerHolder) {
            ArrayList<Integer> arrayList = new ArrayList<>(4);
            arrayList.add(R.drawable.empty_image);
            arrayList.add(R.drawable.empty_image);
            arrayList.add(R.drawable.empty_image);
            arrayList.add(R.drawable.empty_image);
            ((TopAdapter.BannerHolder) holder).bannerView
                    .setAutoPlay(true)
                    .setScrollDuration(500)
                    .setIndicatorStyle(IndicatorStyle.ROUND_RECT)
                    .setIndicatorSlideMode(IndicatorSlideMode.SCALE)
                    .setIndicatorSliderGap(context.getResources().getDimensionPixelOffset(R.dimen.dp_4))
                    .setIndicatorSliderWidth(context.getResources().getDimensionPixelOffset(R.dimen.dp_4),
                            context.getResources().getDimensionPixelOffset(R.dimen.dp_10))
                    .setIndicatorSliderColor(context.getResources().getColor(R.color.red_normal_color),
                            context.getResources().getColor(R.color.red_checked_color))
                    .setOrientation(ViewPager2.ORIENTATION_VERTICAL)
                    .setInterval(2000)
                    .setAdapter(new ImageResourceAdapter(20))
                    .setOnPageClickListener(new BannerViewPager.OnPageClickListener() {
                        @Override
                        public void onPageClick(int position) {
                            Toast.makeText(context, "Page OnClick " + position, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create(arrayList);
        } else if (holder instanceof ActivityHolder) {
            ((ActivityHolder) holder).activityOne.setOnClickListener(this);
            ((ActivityHolder) holder).activityTwo.setOnClickListener(this);
            ((ActivityHolder) holder).activityThree.setOnClickListener(this);
            ((ActivityHolder) holder).activityFour.setOnClickListener(this);
        } else if (holder instanceof RecommendHolder) {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            GoodGoodsAdapter topAdapter = new GoodGoodsAdapter(recommendData, context);
            ((RecommendHolder) holder).recommendRecycler.setLayoutManager(manager);
            ((RecommendHolder) holder).recommendRecycler.setAdapter(topAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activityOne:
                Toast.makeText(context, "活动1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activityTwo:
                Toast.makeText(context, "活动2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activityThree:
                Toast.makeText(context, "活动3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activityFour:
                Toast.makeText(context, "活动4", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;

        }
    }

    static class BannerHolder extends RecyclerView.ViewHolder {

        BannerViewPager<Integer, ImageResourceViewHolder> bannerView;

        BannerHolder(@NonNull View itemView) {
            super(itemView);
            bannerView = itemView.findViewById(R.id.bannerView);
        }
    }

    static class ActivityHolder extends RecyclerView.ViewHolder {

        TextView activityOne, activityTwo, activityThree, activityFour;

        ActivityHolder(@NonNull View itemView) {
            super(itemView);
            activityOne = itemView.findViewById(R.id.activityOne);
            activityTwo = itemView.findViewById(R.id.activityTwo);
            activityThree = itemView.findViewById(R.id.activityThree);
            activityFour = itemView.findViewById(R.id.activityFour);
        }
    }

    static class RecommendHolder extends RecyclerView.ViewHolder {
//        RecyclerViewAtViewPager2 recommendRecycler;
        RecyclerView recommendRecycler;
        RecommendHolder(@NonNull View itemView) {
            super(itemView);
            recommendRecycler = itemView.findViewById(R.id.recommendRecycler);
        }
    }
}
