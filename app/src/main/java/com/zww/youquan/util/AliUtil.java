package com.zww.youquan.util;

import android.app.Activity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.common.utils.AlibcLogger;

import java.util.HashMap;
import java.util.Map;

public class AliUtil {

    private static final String TAG = "test";

    /**
     * @param itemId
     * @param openType   OpenType（页面打开方式）： 枚举值（Auto和Native），Native表示唤端，Auto表示不做设置
     * @param clientType clientType表示唤端类型：taobao---唤起淘宝客户端；tmall---唤起天猫客户端
     * @param backUrl    BACK_URL（小把手）：唤端返回的scheme
     *                   (如果不传默认将不展示小把手；如果想展示小把手，可以自己传入自定义的scheme，
     *                   或者传入百川提供的默认scheme："alisdk://")
     * @param modeType   AlibcFailModeType（唤端失败模式）： 枚举值如下
     *                   AlibcNativeFailModeNONE：不做处理；
     *                   AlibcNativeFailModeJumpBROWER：跳转浏览器；
     *                   AlibcNativeFailModeJumpDOWNLOAD：跳转下载页；
     *                   AlibcNativeFailModeJumpH5：应用内webview打开）
     *                   （注：AlibcNativeFailModeJumpBROWER不推荐使用）
     */
    public static void openDeatailPage(Activity activity, String itemId, OpenType openType, String clientType, String backUrl, AlibcFailModeType modeType) {
        // 页面实例
        AlibcBasePage page = new AlibcDetailPage(itemId);
        //展示参数配置
        AlibcShowParams showParams = new AlibcShowParams();
        showParams.setOpenType(openType);
        showParams.setClientType(clientType);
        showParams.setBackUrl(backUrl);
        showParams.setNativeOpenFailedMode(modeType);
        // taokeParams（淘客）参数配置：配置aid或pid的方式分佣
        // 参数说明：pid   unionId    subPid  adzoneId    extraParams
        //（注：1、如果走adzoneId的方式分佣打点，需要在extraParams中显式传入taokeAppkey，否则打点失败；
        //      2、如果是打开店铺页面(shop)，需要在extraParams中显式传入sellerId，否则同步打点转链失败）
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams.setPid("mm_112883640_11584347_72287650277");
        taokeParams.setAdzoneid("72287650277");
        //adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
//        taokeParams.extraParams.put("taokeAppkey", "xxxxx");
//        taokeParams.extraParams.put("sellerId", "xxxxx");
        //自定义参数
        Map<String, String> trackParams = new HashMap<>();
        AlibcTrade.openByBizCode(activity, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", showParams, taokeParams, trackParams, new AlibcTradeCallback() {
                    @Override
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        // 交易成功回调（其他情形不回调）
                        AlibcLogger.i(TAG, "open detail page success");
                        Log.e("test","open detail page success");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // 失败回调信息
                        Log.e("test","open detail page onFailure");
                        Log.e("test", "code=" + code + ", msg=" + msg);
                        AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
                        if (code == -1) {
                            Toast.makeText(activity, "唤端失败，失败模式为none", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // 以显示传入url的方式打开页面（第二个参数是套件名称）
//        AlibcTrade.openByUrl(activity, "", url, null, new WebViewClient(), new WebChromeClient(), showParams,
//                taokeParams, trackParams, new AlibcTradeCallback() {
//                    @Override
//                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
//                        AlibcLogger.i(TAG, "request success");
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        AlibcLogger.e(TAG, "code=" + code + ", msg=" + msg);
//                        if (code == -1) {
//                            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    public static void openShopPage(String shopId, OpenType openType, String clientType, String backUrl, AlibcFailModeType modeType) {
        // 页面实例
        AlibcBasePage shopPage = new AlibcShopPage(shopId);
        //展示参数配置
        AlibcShowParams showParams = new AlibcShowParams();
        showParams.setOpenType(openType);
        showParams.setClientType(clientType);
        showParams.setBackUrl(backUrl);
        showParams.setNativeOpenFailedMode(modeType);

    }
}
