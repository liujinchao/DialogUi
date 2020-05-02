package com.widget.jcdialog.widget.updateDialog.listener;

import com.widget.jcdialog.widget.updateDialog.UpdateDialogFragment;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 版本检查回调
 */
public interface IUpdateCallback {
    /**
     * 有新版本提示
     * @param updateDialogFragment 升级弹框，可能为null
     */
    void hasNewApp(UpdateDialogFragment updateDialogFragment);

    /**
     * 没有新版本
     */
    void noNewApp();
}
