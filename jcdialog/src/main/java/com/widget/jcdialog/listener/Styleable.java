package com.widget.jcdialog.listener;

import android.app.Dialog;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;

import com.widget.jcdialog.bean.BuildBean;

import java.util.Map;

/**
 * 类名称：Styleable
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：TODO
 */
public interface Styleable {

    BuildBean setBtnColor(@ColorRes int btn1Color, @ColorRes int btn2Color, @ColorRes int btn3Color);

    BuildBean setListItemColor(@ColorRes int lvItemTxtColor, Map<Integer, Integer> colorOfPosition);

    BuildBean setTitleColor(@ColorRes int colorRes);
    BuildBean setMsgColor(@ColorRes int colorRes);
    BuildBean seInputColor(@ColorRes int colorRes);

    BuildBean setTitleSize(int sizeInSp);
    BuildBean setMsgSize(int sizeInSp);
    BuildBean setBtnSize(int sizeInSp);
    BuildBean setLvItemSize(int sizeInSp);
    BuildBean setInputSize(int sizeInSp);

    Dialog show();

    //内容设置
    BuildBean setBtnText(CharSequence btn1Text, @Nullable CharSequence btn2Text, @Nullable CharSequence btn3Text);

    BuildBean setBtnText(CharSequence positiveTxt, @Nullable CharSequence negtiveText);

    BuildBean setListener(DialogUIListener listener);

    BuildBean setCancelable(boolean cancelable, boolean outsideCancelable);

}
