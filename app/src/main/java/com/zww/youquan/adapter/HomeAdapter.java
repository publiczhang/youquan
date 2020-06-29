package com.zww.youquan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zww.youquan.R;
import com.zww.youquan.bean.OptimusMaterialBean;

import java.util.List;


/**
 * HomeAdapter
 *
 * @author Administrator
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOP = 0;
    private static final int TYPE_FUNCTION = 1;
    private static final int TYPE_GOOD = 2;
    private static final int TYPE_HOT = 3;
    private static final int TYPE_DEFAULT = 4;

    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_TOP;
            case 1:
                return TYPE_FUNCTION;
            case 2:
                return TYPE_GOOD;
            case 3:
                return TYPE_HOT;
            default:
                return TYPE_DEFAULT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_top, parent, false);
                return new DefaultHolder(view);
            case TYPE_FUNCTION:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_function, parent, false);
                return new DefaultHolder(view);
            case TYPE_GOOD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_good, parent, false);
                return new GoodGoodsHolder(view);
            case TYPE_HOT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_hot, parent, false);
                return new DefaultHolder(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_default, parent, false);
                return new DefaultHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (position) {
            case 0:
                Log.e("test", "BindViewHolder 0");
                break;
            case 1:
                Log.e("test", "BindViewHolder 1");
                break;
            case 2:
                bindGoodGoods(holder);
                break;
            case 3:
                Log.e("test", "BindViewHolder 3");
                break;
            default:
                break;
        }
    }

    /**
     * 有好货
     *
     * @param holder GoodGoodSHolder
     */
    private void bindGoodGoods(RecyclerView.ViewHolder holder) {
        if (holder instanceof GoodGoodsHolder) {
            GridLayoutManager manager = new GridLayoutManager(context, 2);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            ((GoodGoodsHolder) holder).goodRecycler.setLayoutManager(manager);
        }
    }

    private void setGoodGoodsData(List<OptimusMaterialBean> goodGoodsData, GoodGoodsHolder holder) {
        GoodGoodsAdapter adapter = new GoodGoodsAdapter(goodGoodsData, context);
         holder.goodRecycler.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class GoodGoodsHolder extends RecyclerView.ViewHolder {
        TextView title, more;
        RecyclerView goodRecycler;

        GoodGoodsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            more = itemView.findViewById(R.id.more);
            goodRecycler = itemView.findViewById(R.id.goodRecycler);
        }
    }

    static class DefaultHolder extends RecyclerView.ViewHolder {

        RecyclerView materialRecycler;

        DefaultHolder(@NonNull View itemView) {
            super(itemView);
            materialRecycler = itemView.findViewById(R.id.materialRecycler);
        }
    }
}
