package com.widget.jcdialog.bean;

import android.support.annotation.DrawableRes;

/**
 * 类名称：BottomSheetBean
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:24
 * 描述：底部样式
 */
public class BottomSheetBean {
    public @DrawableRes
    int icon;
    public String text;

    public BottomSheetBean(){

    }

    public BottomSheetBean(int icon, String text) {
        this.icon = icon;
        this.text = text;
    }
}
