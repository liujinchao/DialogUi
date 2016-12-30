package com.widget.jcdialog.holder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.widget.jcdialog.bean.BuildBean;


/**
 * 类名称：SuperHolder
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:27
 * 描述：TODO
 */
public abstract class SuperHolder {
    public View rootView;

    public SuperHolder(Context context) {
        rootView = View.inflate(context, setLayoutRes(), null);
        findViews();
    }

    protected abstract void findViews();

    protected abstract
    @LayoutRes
    int setLayoutRes();

    /**
     * 一般情况下，实现这个方法就足够了
     *
     * @param context
     * @param bean
     */
    public abstract void assingDatasAndEvents(Context context, BuildBean bean);


}
