package com.widget.jcdialog.listener;

/**
 * 类名称：DialogUIItemListener
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：TODO
 */
public abstract class DialogUIItemListener {

   /**item点击事件*/
   public abstract void onItemClick(CharSequence text, int position);

   /**底部最后一个按钮点击事件*/
   public void onBottomBtnClick(){}

}
