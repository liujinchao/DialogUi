package com.widget.jcdialog.widget.updateDialog.listener;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 升级弹框展示异常帮助类
 */
public class ExceptionHandlerHelper {

    private static  ExceptionHandler instance;

    public static void init(ExceptionHandler exceptionHandler) {
        ExceptionHandler temp = instance;
        if (temp == null) {
            synchronized (ExceptionHandlerHelper.class) {
                temp = instance;
                if (temp == null) {
                    temp = exceptionHandler;
                    instance = temp;
                }
            }
        }
    }
    public static ExceptionHandler getInstance() {
        return instance;
    }
}
