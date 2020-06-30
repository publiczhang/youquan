package com.zww.youquan.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zww.youquan.R;
import com.zww.youquan.bean.OptimusMaterialBean;


/**
 * GoodGoodsAdapter
 *
 * @author zww
 */
public class GoodGoodsAdapter extends RecyclerView.Adapter<GoodGoodsAdapter.GoodGoodsHolder> {
    private OptimusMaterialBean data;
    private Context context;

    GoodGoodsAdapter(OptimusMaterialBean data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public GoodGoodsAdapter.GoodGoodsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_good_goods, parent, false);
        return new GoodGoodsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoodGoodsHolder holder, int position) {
        holder.goodGoodsItemRoot.setOnClickListener(v -> {
            Toast.makeText(context, "有好货" + position, Toast.LENGTH_SHORT).show();
            Log.e("test", "有好货" + position);
        });
        holder.price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        if (data == null || data.getOptimusMaterialResponse() == null || data.getOptimusMaterialResponse().getResultList() == null) {
            return 0;
        }
        return data.getOptimusMaterialResponse().getResultList().getMapData() == null ? 0 : data.getOptimusMaterialResponse().getResultList().getMapData().size();
    }

    static class GoodGoodsHolder extends RecyclerView.ViewHolder {

        RelativeLayout goodGoodsItemRoot;
        ImageView goodPicture;
        TextView goodName, couponInfo, couponPrice, price, sellCount;

        GoodGoodsHolder(@NonNull View itemView) {
            super(itemView);
            goodGoodsItemRoot = itemView.findViewById(R.id.goodGoodsItemRoot);
            goodPicture = itemView.findViewById(R.id.goodPicture);
            goodName = itemView.findViewById(R.id.goodName);
            couponInfo = itemView.findViewById(R.id.couponInfo);
            couponPrice = itemView.findViewById(R.id.couponPrice);
            price = itemView.findViewById(R.id.price);
            sellCount = itemView.findViewById(R.id.sellCount);
        }
    }
}
