package com.widget.jcdialog.listener;

/**
 * 类名称：DialogUIListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:29
 * 描述：TODO
 */
public abstract class DialogUIListener {

    /**
     * 确定
     */
    public abstract void onPositive();

    /**
     * 否定
     */
    public abstract void onNegative();

    /**
     * 中立
     */
    public void onNeutral() {
    }

    /**
     * 取消
     */
    public void onCancle() {
    }

    /**
     * 获取输入内容
     */
    public void onGetInput(CharSequence input1, CharSequence input2) {
    }

    /**
     * 获取选择集合
     */
    public void onGetChoose(boolean[] states) {

    }


}
