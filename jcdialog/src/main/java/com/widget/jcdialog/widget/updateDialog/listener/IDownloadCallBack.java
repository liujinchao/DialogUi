package com.widget.jcdialog.widget.updateDialog.listener;

import java.io.File;

/**
 * 进度条回调接口
 */
public interface IDownloadCallBack {
    /**
     * 开始
     */
    void onStart();

    /**
     * @param progress  进度 0.00 -> 1.00 ，总大小
     */
    void onProgress(float progress);


    /**
     * @param file 下载的app
     * @return true ：下载完自动跳到安装界面，false：则不进行安装
     */
    boolean onFinish(File file);

    /**
     * 下载异常
     */
    void onError(String msg);

    /**
     * 当应用处于前台，准备执行安装程序时候的回调，
     */
    boolean onInstallAppAndAppOnForeground(File file);
}
