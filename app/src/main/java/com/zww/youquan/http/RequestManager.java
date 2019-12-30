package com.zww.youquan.http;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.logger.Logger;
import com.zww.youquan.BuildConfig;
import com.zww.youquan.Config;
import com.zww.youquan.MyApp;
import com.zww.youquan.util.UMengUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager {
    private static RequestManager mInstance;
    private Retrofit retrofit;

    private RequestManager() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Config.HOST)
                .callFactory(creatClient().build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static RequestManager getIntance() {
        if (null == mInstance) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    public Retrofit entry() {
        return retrofit;
    }


    private OkHttpClient.Builder creatClient() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .sslSocketFactory(TrustAllCerts.createSSLSocketFactory())
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .readTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(15 * 1000, TimeUnit.MILLISECONDS);
        clientBuilder.addInterceptor(new HeaderInterceptor());
        clientBuilder.addInterceptor(new LoggerInterceptor());
        return clientBuilder;
    }


    private class HeaderInterceptor implements Interceptor {

        @SuppressWarnings("NullableProblems")
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            if (TextUtils.isEmpty(request.header("Accept"))) {
                builder.addHeader("Accept", "application/vnd.edusoho.v2+json");
            }
            if (TextUtils.isEmpty(request.header("Content-Type"))) {
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
            }
            Request cRequest = builder.build();
            return chain.proceed(cRequest);
        }
    }

    private class LoggerInterceptor implements Interceptor {

        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (BuildConfig.DEBUG) {
                ResponseBody responseBody = response.body();
                if (responseBody != null && responseBody.contentLength() != 0) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE);
                    Buffer buffer = source.buffer();
                    Charset charset = Charset.forName("UTF-8");
                    Logger.i("request url: %s%nheader: %s%n body: %s%n", request.url(), request.headers(), (request.body() == null) + ":requstbody is null");
                    Logger.json(buffer.clone().readString(charset));
                }
            }
            if (!response.isSuccessful()) {
                Log.e("response failed","url-->"+ request.url()+"-------------->code:"+response.code());
                UMengUtil.reportError(MyApp.getInstance(), "FailCode:%s \\n url:%s", response.code(), request.url());
            }
            return response;
        }
    }

    private static class TrustAllCerts implements X509TrustManager {

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        static SSLSocketFactory createSSLSocketFactory() {
            SSLSocketFactory ssfFactory = null;
            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

                ssfFactory = sc.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ssfFactory;
        }

        public static class TrustAllHostnameVerifier implements HostnameVerifier {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }

        }
    }

}
