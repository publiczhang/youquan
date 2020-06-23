package com.zww.youquan.request;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HomeService {

    /**
     * 美术菌 老师上课
     */
    @FormUrlEncoded
    @POST("test")
    Observable<Object> getHomeTab(@Field("type") String type);
}
