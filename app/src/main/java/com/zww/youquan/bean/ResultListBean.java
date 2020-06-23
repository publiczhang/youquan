package com.zww.youquan.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ResultListBean
 *
 * @author zww
 */
public class ResultListBean {
    @SerializedName("map_data")
    private List<MapDataBean> mapData;

    public List<MapDataBean> getMapData() {
        return mapData;
    }

    public void setMapData(List<MapDataBean> mapData) {
        this.mapData = mapData;
    }

}