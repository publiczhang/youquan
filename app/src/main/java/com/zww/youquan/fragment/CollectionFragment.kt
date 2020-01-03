package com.zww.youquan.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.module.LoadMoreModuleConfig
import com.zww.youquan.R
import com.zww.youquan.adapter.CollectionAdapater
import com.zww.youquan.base.BaseFragment
import com.zww.youquan.bean.Bean
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 收藏
 *
 * @author Administrator
 */

 class CollectionFragment : BaseFragment() {

    private var rv_list: RecyclerView? =  null
    private var swipeLayout: SwipeRefreshLayout? =  null
    private var mAdapter: CollectionAdapater? = null
    private var beans = ArrayList<Bean>()
    private var mDisposable: Disposable? = null
    private var pager = 5


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val  view  = inflater.inflate(R.layout.fragment_collection, container, false)

        initView(view)
        rv_list?.layoutManager = LinearLayoutManager(context)

        initAdapter()
        initRefreshLayout()
        addHeadView()
        initLoadMore()
        return view
    }


    override fun onResume() {
        super.onResume()
        refresh()
    }


    fun initView(view:View){
        rv_list = view.findViewById(R.id.rv_list)
        swipeLayout = view.findViewById(R.id.swipeLayout)
    }

    private fun initAdapter() {
        mAdapter = CollectionAdapater()
        mAdapter?.animationEnable = true
        mAdapter?.loadMoreModule?.loadMoreView = LoadMoreModuleConfig.defLoadMoreView
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(context, "收藏${position+1}", Toast.LENGTH_SHORT).show()
        }
        rv_list?.adapter = mAdapter
    }

    private fun addHeadView() {
        val headView: View = layoutInflater.inflate(R.layout.collection_header_view, rv_list?.parent as ViewGroup, false)
        mAdapter!!.addHeaderView(headView)
    }


    private fun initRefreshLayout() {

        swipeLayout?.setColorSchemeColors(Color.rgb(47, 223, 189))
        swipeLayout?.setOnRefreshListener{
            refresh()
        }
    }


    private fun initLoadMore() {
        mAdapter?.loadMoreModule?.setOnLoadMoreListener{
            startFollow()
        }
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        mAdapter?.loadMoreModule?.isEnableLoadMoreIfNotFullPage = false
    }

    private fun refresh() { // 这里的作用是防止下拉刷新的时候还可以上拉加载
        pager = 5
        mAdapter?.setNewData(null)
        mAdapter?.loadMoreModule?.isEnableLoadMore = false
        swipeLayout?.isRefreshing = true
        startFollow()
        mAdapter?.loadMoreModule?.isEnableLoadMore =true
    }



    //假数据
    private fun getdata() {
        Log.d("getdata","loadingdata")
        var beans = ArrayList<Bean>()
        for(i in 0..5){
            beans.add(Bean(i,"死胖子","东西好的不得了"))
        }
        swipeLayout?.isRefreshing = false

        if(beans.size>0){
            mAdapter?.addData(beans)
            mAdapter?.loadMoreModule?.loadMoreComplete()
        }
    }


    private fun startFollow() {

        Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Long> {
                    override fun onSubscribe(d: Disposable) {
                        Log.d("CollectionFragment","onSubscribe")
                        mDisposable = d
                    }

                    override fun onNext(aLong: Long) {
                        Log.d("CollectionFragment","onNext:$aLong")
                        if (aLong == 0.toLong()) {
                            if(pager>0){
                                pager--
                                getdata()
                            }else{
                                mAdapter?.loadMoreModule?.loadMoreEnd()
                            }

                            mDisposable!!.dispose()
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.d("CollectionFragment","onError:${e.localizedMessage}")
                        if (mDisposable != null && !mDisposable!!.isDisposed) {
                            mDisposable!!.isDisposed
                        }
                    }

                    override fun onComplete() {
                        Log.d("CollectionFragment","onComplete")
                        if (mDisposable != null && !mDisposable!!.isDisposed) {
                            mDisposable!!.isDisposed
                        }

                    }
                })
    }

}