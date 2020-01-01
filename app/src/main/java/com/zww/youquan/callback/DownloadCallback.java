package com.zww.youquan.callback;

public interface DownloadCallback {

    /**
     * onDownloadSuccess
     */
    void onDownloadSuccess();

    /**
     * onCancel
     */
    void onDownloadCancel();

    /**
     * onDownloadError
     */
    void onDownloadError();
}
