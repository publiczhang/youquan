package com.zww.youquan.bean;

import com.google.gson.annotations.SerializedName;
import com.zww.youquan.base.BaseBean;

/**
 * OptimusMaterialBean
 * @author zww
 */
public class OptimusMaterialBean extends BaseBean {

    @SerializedName("tbk_dg_optimus_material_response")
    private OptimusMaterialResponseBean optimusMaterialResponse;

    public OptimusMaterialResponseBean getOptimusMaterialResponse() {
        return optimusMaterialResponse;
    }

    public void setOptimusMaterialResponse(OptimusMaterialResponseBean optimusMaterialResponse) {
        this.optimusMaterialResponse = optimusMaterialResponse;
    }

    public static class OptimusMaterialResponseBean {

        @SerializedName("is_default")
        private String isDefault;
        @SerializedName("result_list")
        private ResultListBean resultList;
        @SerializedName("request_id")
        private String requestId;

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        public ResultListBean getResultList() {
            return resultList;
        }

        public void setResultList(ResultListBean resultList) {
            this.resultList = resultList;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }


    }
}
