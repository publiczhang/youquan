package com.zww.youquan.adapter

import com.chad.library.adapter.base.BaseQuickAdapter

import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zww.youquan.R
import com.zww.youquan.bean.Bean


/**
 *    author : Nihuajun
 *    date   : 2020-01-02 16:26
 */

class CollectionAdapter :
        BaseQuickAdapter<Bean?, BaseViewHolder>(R.layout.layout_animation),
        LoadMoreModule {

    override fun convert(helper: BaseViewHolder, item: Bean?) {
        helper.setText(R.id.saleName,item?.name)
        helper.setText(R.id.salePrice,item?.price.toString())
        helper.setText(R.id.saleName,item?.name)

    }


}