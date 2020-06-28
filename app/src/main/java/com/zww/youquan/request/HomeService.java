package com.zww.youquan.request;


import com.zww.youquan.bean.OptimusMaterialBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface HomeService {

    /**
     *
     */
//    @GET("/tb/TaobaoClient")
//    Observable<Object> getHomeTab(@Query("parentId") String parentId, @Query("page") int page, @Query("rows") int rows);
    @GET("tb/TaobaoClient?parentId=9660&page=1&rows=1")
    Observable<OptimusMaterialBean> getHomeTab();
}
