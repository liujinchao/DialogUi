package com.widget.jcdialog.listener;

import com.widget.jcdialog.widget.WheelView;

/**
 * 类名称：OnWheelChangedListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：wheel改变监听事件
 */
public interface OnWheelChangedListener {
    /**
     * Callback method to be invoked when current item changed
     *
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
