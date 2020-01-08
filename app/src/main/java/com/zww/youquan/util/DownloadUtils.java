package com.zww.youquan.util;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;


import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zww.youquan.callback.DownloadCallback;

import java.io.File;
import java.io.IOException;

import io.reactivex.disposables.Disposable;

/**
 * DownloadUtils
 *
 * @author Administrator
 */
public class DownloadUtils {
    private DownloadManager downloadManager;
    private DownloadCallback downloadCallback;
    private Activity activity;
    /**
     * 下载的ID
     */
    private long downloadId;
    private String name;
    private String path;
    private RxPermissions permissions;
    private String url;

    public DownloadUtils(FragmentActivity activity, String url, String name) {
        this.activity = activity;
        this.name = name;
        this.url = url;
        checkReadWritePermission(activity);
    }

    private void checkReadWritePermission(Activity activity) {
        if (null == permissions) {
            permissions = new RxPermissions((FragmentActivity) activity);
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
                                    Toast.makeText(activity, "没有写入存储权限", Toast.LENGTH_SHORT).show();
                                    break;
                                case Manifest.permission.READ_EXTERNAL_STORAGE:
                                    Toast.makeText(activity, "没有读取存储权限", Toast.LENGTH_SHORT).show();
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
                            downloadApk(url, name);
                        }
                    }
                });
    }


    public void addDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    /**
     * 下载apk
     *
     * @param url  url
     * @param name name
     */
    private void downloadApk(String url, String name) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("美术蛙");
        request.setDescription("新版本正在下载中...");
        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        File file = new File(Environment.getExternalStorageDirectory(), name);
        if (file.exists()) {
            file.delete();
        }
        request.setDestinationUri(Uri.fromFile(file));
        path = file.getAbsolutePath();
        //获取DownloadManager
        if (downloadManager == null) {
            downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        }
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            downloadId = downloadManager.enqueue(request);
        }
        //注册广播接收者，监听下载状态
        activity.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 广播监听下载的各个状态
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };

    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installApk();
                    cursor.close();
                    activity.unregisterReceiver(receiver);
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(activity, "下载失败,请退出软件重新下载升级", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    activity.unregisterReceiver(receiver);
                    if (downloadCallback != null) {
                        downloadCallback.onDownloadError();
                    }
                    break;
                default:
                    break;
            }
        } else {
            if (downloadCallback != null) {
                downloadCallback.onDownloadCancel();
            }
            Toast.makeText(activity, "下载已取消", Toast.LENGTH_SHORT).show();
        }
    }

    public int getDownloadPercent() {
        if (downloadManager == null) {
            return 0;
        }
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (null != c && c.moveToFirst()) {
            int downloadBytesIdx = c.getColumnIndexOrThrow(
                    DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            int totalBytesIdx = c.getColumnIndexOrThrow(
                    DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            long totalBytes = c.getLong(totalBytesIdx);
            long downloadBytes = c.getLong(downloadBytesIdx);
            c.close();
            if (totalBytes != 0) {
                return (int) (downloadBytes * 100 / totalBytes);
            } else {
                return 0;
            }
        }
        return 0;
    }

    public void installApk() {
        setPermission(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Android 7.0以上要使用FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            File file = new File(path);
            Uri apkUri = FileProvider.getUriForFile(activity, "com.zww.youquan", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), name)), "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
    }

    /**
     * 修改文件权限
     *
     * @param absolutePath absolutePath
     */
    private void setPermission(String absolutePath) {
        String command = "chmod " + "777" + " " + absolutePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
