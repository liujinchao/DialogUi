package com.widget.jcdialog.widget.updateDialog.listener;
import com.widget.jcdialog.widget.updateDialog.UpdateAppBean;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 监听升级弹框事件回调
 */
public interface IUpdateDialogFragmentListener {
    /**
     * 开始下载最新apk
     * @param updateApp
     */
    void startDownloadApk(UpdateAppBean updateApp, IDownloadCallBack downloadCallback);

    /**
     * 取消下载apk
     * @param updateApp
     */
    void cancelDownloadApk(UpdateAppBean updateApp);
}
