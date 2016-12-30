package com.widget.jcdialog.listener;


import com.widget.jcdialog.widget.WheelView;

/**
 * 类名称：OnWheelScrollListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：wheel滚动监听事件
 */
public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(WheelView wheel);
}
