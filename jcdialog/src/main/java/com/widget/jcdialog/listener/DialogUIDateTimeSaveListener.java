package com.widget.jcdialog.listener;

/**
 * 类名称：DialogUIDateTimeSaveListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:27
 * 描述：选择日期回调
 */
public interface DialogUIDateTimeSaveListener {

    /**
     * 点击保存返回的日期
     */
    void onSaveSelectedDate(int tag, String selectedDate);

}