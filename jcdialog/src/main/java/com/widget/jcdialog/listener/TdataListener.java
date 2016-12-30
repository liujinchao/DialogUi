package com.widget.jcdialog.listener;

import android.view.View;
import android.widget.AdapterView;

import com.widget.jcdialog.bean.PopuBean;

import java.util.List;

/**
 * 类名称：TdataListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：动态设置下拉框的数据
 */
public interface TdataListener {
    /**
     * 初始化数据
     */
    void initPupoData(List<PopuBean> lists);

    /**
     * 点击事件
     */
    void onItemClick(AdapterView<?> adapterView, View view, int position);
}