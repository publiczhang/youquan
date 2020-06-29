package com.zww.youquan.request;


import com.zww.youquan.bean.OptimusMaterialBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * HomeService
 *
 * @author zww
 */
public interface HomeService {

    /**
     * 获取首页
     *
     * @param parentId 物料Id
     * @param page     page
     * @param rows     rows
     * @return OptimusMaterialBean
     */
    @GET("/tb/TaobaoClient")
    Observable<OptimusMaterialBean> getHomeTab(@Query("parentId") String parentId, @Query("page") int page, @Query("rows") int rows);
}
