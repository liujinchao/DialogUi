package com.widget.jcdialog.listener;

import java.util.List;

/**
 * 类名称：RefreshableListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:30
 * 描述：TODO
 */
public interface RefreshableListener {
    public void refresh(List newData);

    public void addAll(List newData);

    public void clear();

    public void delete(int position);

    public void add(Object object);
}
