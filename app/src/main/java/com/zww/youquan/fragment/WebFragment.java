package com.zww.youquan.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zww.youquan.R;
import com.zww.youquan.activity.WebActivity;
import com.zww.youquan.base.BaseFragment;
import com.zww.youquan.util.AppUtil;
import com.zww.youquan.util.BitmapUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * WebFragment
 *
 * @author ChenLang
 */
public class WebFragment extends BaseFragment {

    public final static String KEY_URL = "URL";
    private static final int PROGRESS_MAX_VALUE = 100;

    private ValueCallback<Uri[]> mFilePathCallback;
    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private final static int FILECHOOSER_RESULTCODE = 2;
    private boolean isPermissionsOk = false;
    private int chooserMode = 0;
    private int goRequestCode = 0;
    BridgeWebView webView;
    RelativeLayout emptyLayout;

    private boolean isFirst = true;

    private String url;

    private RelativeLayout titleLayout;
    private ImageView ivBack;
    private TextView topTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_web, container, false);
        initView(savedInstanceState,inflate);
        return inflate;
    }


    private void initView(Bundle instanceState, View v) {
        emptyLayout = v.findViewById(R.id.emptyLayout);
        webView = v.findViewById(R.id.web);
        initTitle(v);
        initWeb();
        initJBHandle();
        initWebView();
    }

    private void initTitle(View view) {
        titleLayout = view.findViewById(R.id.titleLayout);
        ivBack = view.findViewById(R.id.iv_back);
        topTitle = view.findViewById(R.id.top_title);
    }

    public void backClick(View v) {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void initWebView() {
        if (TextUtils.isEmpty(url) && getArguments() != null) {
            url = getArguments().getString(KEY_URL);
        }
        if (TextUtils.isEmpty(url)) {
            emptyLayout.setVisibility(View.VISIBLE);
            return;
        }
        if (emptyLayout.getVisibility() == View.VISIBLE) {
            emptyLayout.setVisibility(View.GONE);
        }
        webView.loadUrl(url);
    }

    public void setUrl(String url) {
        this.url = url;
        initWebView();
    }

    public void setShowTitle(boolean showTitle, String title, boolean showBack) {
        if (showTitle) {
            titleLayout.setVisibility(View.VISIBLE);
        } else {
            titleLayout.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            topTitle.setText(title);
        } else {
            topTitle.setText("");
        }
        if (showBack) {
            ivBack.setVisibility(View.VISIBLE);
        } else {
            ivBack.setVisibility(View.INVISIBLE);
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        boolean hasShowLoading = false;


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress != PROGRESS_MAX_VALUE) {
                if (!hasShowLoading) {
//                    showLoading(true);
                    hasShowLoading = true;
                }
            } else {
//                dismissLoading();
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (getActivity() instanceof WebActivity) {
                ((WebActivity) getActivity()).setTitle(title);
            }
        }


        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(Objects.requireNonNull(getContext()));
            frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            //全屏回调
            if (getActivity() != null) {
                showCustomView(view, callback);
            }
        }

        @Override
        public void onHideCustomView() {
            //取消全屏
            if (getActivity() != null) {
                hideCustomView();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
            }
            mFilePathCallback = filePathCallback;
            chooserMode = fileChooserParams.getMode();
            return true;
        }
    };

    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
            fullscreenContainer = new FullscreenHolder(getContext());
            fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
            decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
            customView = view;
            setStatusBarVisibility(false);
        }
        customViewCallback = callback;
    }

    @Override
    protected void initViewModel() {

    }

    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (getActivity() != null) {
            getActivity().getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setStatusBarVisibility(true);
            FrameLayout decor = (FrameLayout) getActivity().getWindow().getDecorView();
            decor.removeView(fullscreenContainer);
        }
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
    }


    /*** 下面代码是为了处理webview上传文件用, 只供参考 ***/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            if (mFilePathCallback != null) {
                mFilePathCallback.onReceiveValue(null);
                mFilePathCallback = null;
            }
            return;
        }
    }

    private void initWeb() {
        webView.requestFocusFromTouch();//支持获取手势焦点，便于用户输入
        WebSettings settings = webView.getSettings();
        String dir = getContext().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        settings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        settings.setGeolocationDatabasePath(dir);
//        settings.setDomStorageEnabled(true);//是否开启本地DOM存储
        //启用支持javascript
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //优先不使用缓存
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //当webview调用requestFocus时为webview设置节点
        settings.setNeedInitialFocus(true);
        //支持自动加载图片
        settings.setLoadsImagesAutomatically(true);
        //不显示缩放控制按钮（3.0以上有效）
        settings.setDisplayZoomControls(false);
        //支持内容重新布局
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // 关键点
        settings.setUseWideViewPort(true);
        // 允许访问文件
        settings.setAllowFileAccess(true);
        // 支持缩放
        settings.setSupportZoom(true);
        settings.setUserAgentString(settings.getUserAgentString() + "AiYiOnline");
        //自动播放
        settings.setMediaPlaybackRequiresUserGesture(false);
        String ua = settings.getUserAgentString();
        settings.setUserAgentString(String.format("%s %s/%s", ua, "MeiShuWa", AppUtil.getVersion(activity)));
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(new BridgeWebViewClient(webView) {

            @Override
            public void onPageFinished(WebView view, String webUrl) {
//                dismissLoading();
                super.onPageFinished(view, webUrl);
            }


            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.setDefaultHandler(new DefaultHandler());
        webView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final WebView.HitTestResult webViewHitTestResult = webView.getHitTestResult();
        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            if (null == permissions) {
                permissions = new RxPermissions(this);
            }
            permissions.requestEach(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new io.reactivex.Observer<Permission>() {
                        boolean result = true;

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Permission permission) {
                            if (!permission.granted) {
                                result = false;
                                switch (permission.name) {
                                    case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                        Toast.makeText(activity, "没有存储权限", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Manifest.permission.RECORD_AUDIO:
                                        Toast.makeText(activity, "没有麦克风权限", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            if (result) {
                                //menu.setHeaderTitle("网页中下载图片");
                                menu.add(0, 1, 0, "点击保存")
                                        .setOnMenuItemClickListener(menuItem -> {
                                            String downloadImageUrl = webViewHitTestResult.getExtra();
                                            Log.e("imgUrl:%s", downloadImageUrl);
                                            if (URLUtil.isValidUrl(downloadImageUrl)) {
                                                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadImageUrl));
                                                request.allowScanningByMediaScanner();
                                                //设置图片的保存路径
                                                if (isFolderExist()) {
                                                    request.setDestinationInExternalPublicDir(DIRECTORY_PICTURES, System.currentTimeMillis() + ".png");
                                                } else {
                                                    request.setDestinationInExternalFilesDir(getContext(), "/img", System.currentTimeMillis() + ".png");
                                                }
                                                request.allowScanningByMediaScanner();
                                                DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                                                downloadManager.enqueue(request);
                                                Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                                            } else if (downloadImageUrl.startsWith("data:image/")) {
                                                if (!checkPermission()) {
                                                    Toast.makeText(activity, "请开启存储权限", Toast.LENGTH_SHORT).show();
                                                }
                                                if (BitmapUtil.savePicture(getContext(), downloadImageUrl)) {
                                                    Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                                            }
                                            return false;
                                        });
                            }
                        }
                    });

        }
    }

    private boolean isFolderExist() {
        File folder = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    RxPermissions permissions;



    private void initJBHandle() {
        webView.registerHandler("appToH5Action", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    appToH5Action(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        webView.registerHandler("h5ToAppAction", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void appToH5Action(JSONObject json) {

    }


    /**
     * 跳转预约页面
     *
     * @param killedSelf killedSelf
     * @param source     source
     */
    private void goAppointment(boolean killedSelf, String source) {
        Intent it = new Intent("com.edusoho.aiyioto.student.book.ENTER");
        it.putExtra("source", source);
        getActivity().startActivity(it);
        if (getActivity() instanceof WebActivity && killedSelf) {
            getActivity().finish();
        }
    }

    RxPermissions rxPermission;
    boolean result = false;

    private boolean checkPermission() {
        if (null == rxPermission) {
            rxPermission = new RxPermissions(this);
        }
        Disposable subscribe = rxPermission.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> result = true);
        return result;
    }

    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener listener;

    @Override
    public void onResume() {
        if (audioManager != null) {
            audioManager.abandonAudioFocus(listener);
            audioManager = null;
        }

        super.onResume();
    }

    @Override
    public void onPause() {
        audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        listener = focusChange -> {
        };
        int result = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

        }
        super.onPause();
    }

}
