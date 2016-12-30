package com.widget.jcdialog.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.widget.jcdialog.DialogUtils;
import com.widget.jcdialog.bean.BuildBean;
import com.widget.jcdialog.config.CommonConfig;

import java.io.UnsupportedEncodingException;

/**
 * 类名称：ToolUtils
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:28
 * 描述：TODO
 */
public class ToolUtils {

    /**
     * 统一显示
     * @param dialog
     */
    public static void showDialog(Dialog dialog) {
        try {
            dialog.show();
        } catch (Exception e) {
        }
    }


    /**
     * 混合上下文
     */
    public static BuildBean fixContext(BuildBean bean) {
        if (bean.mContext == null) {
            bean.mContext = DialogUtils.appContext;
        } else if (bean.mContext instanceof Activity) {
            Activity activity = (Activity) bean.mContext;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity.isDestroyed()) {
                    bean.mContext = DialogUtils.appContext;
                }
            }
        }
        return bean;
    }


    public static BuildBean setCancelable(BuildBean bean) {
        if (bean.alertDialog != null) {
            bean.alertDialog.setCancelable(bean.cancelable);
            bean.alertDialog.setCanceledOnTouchOutside(bean.outsideTouchable);
        } else if (bean.dialog != null) {
            bean.dialog.setCancelable(bean.cancelable);
            bean.dialog.setCanceledOnTouchOutside(bean.outsideTouchable);
        }
        return bean;
    }

    public static void setDialogStyle(BuildBean bean) {
        if (bean.alertDialog != null) {
            setMdBtnStytle(bean);
        } else {
            setDialogStyle(bean.mContext, bean.dialog, bean.viewHeight, bean);
        }

    }

    /**
     * 设置MD风格样式
     */
    public static void setMdBtnStytle(BuildBean bean) {
        Button btnPositive =
                bean.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button btnNegative =
                bean.alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button btnNatural =
                bean.alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        if (btnPositive != null && btnNegative != null) {
            btnPositive.setTextSize(bean.btnTxtSize);
            btnNegative.setTextSize(bean.btnTxtSize);
            btnNatural.setTextSize(bean.btnTxtSize);
            if (bean.btn1Color != 0)
                btnPositive.setTextColor(getColor(null, bean.btn1Color));
            if (bean.btn2Color != 0)
                btnNegative.setTextColor(getColor(null, bean.btn2Color));
            if (bean.btn3Color != 0)
                btnNatural.setTextColor(getColor(null, bean.btn3Color));
        }
        Window window = bean.alertDialog.getWindow();
        window.setGravity(bean.gravity);
    }

    public static void setDialogStyle(Context context, Dialog dialog, int measuredHeight, BuildBean bean) {
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(bean.gravity);
        WindowManager.LayoutParams wl = window.getAttributes();
        // 以下这两句是为了保证按钮可以水平满屏
        int width = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = (int) (((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight() * 0.9);
        if (bean.type != CommonConfig.TYPE_MD_LOADING_VERTICAL) {
            wl.width = (int) (width * 0.94);  // todo keycode to keep gap
        } else {
            wl.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;  //TODO  一般情况下为wrapcontent,最大值为height*0.9
        if (measuredHeight > height) {
            wl.height = height;
        }
        if (context instanceof Activity) {
            Activity activity1 = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity1.isDestroyed()) {
                    context = DialogUtils.appContext;
                }
            }
        } else {
            wl.type = WindowManager.LayoutParams.TYPE_TOAST;
            //todo keycode to improve window level,同时要让它的后面半透明背景也拦截事件,不要传递到下面去
            //todo 单例化,不然连续弹出两次,只能关掉第二次的
        }
        dialog.onWindowAttributesChanged(wl);

    }

    /**
     * 测量View
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    ,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int lpHeight = p.height;
        int lpWidth = p.width;
        int childHeightSpec;
        int childWidthSpec;
        if (lpHeight > 0) {   //如果Height是一个定值，那么我们测量的时候就使用这个定值
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {  // 否则，我们将mode设置为不指定，size设置为0
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        if (lpWidth > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(lpHeight,
                    View.MeasureSpec.EXACTLY);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 测量高度
     *
     * @param root
     * @param id   height为0,weight为1的scrollview包裹的view的id,如果没有,传0或负数即可
     * @return
     */
    public static int mesureHeight(View root, int id) {
        measureView(root);
        int height = root.getMeasuredHeight();
        int heightExtra = 0;
        if (id > 0) {
            View view = root.findViewById(id);
            if (view != null) {
                measureView(view);
                heightExtra = view.getMeasuredHeight();
            }

        }
        return height + heightExtra;
    }

    /**
     * 测量高度
     */
    public static int mesureHeight(View root, View... subViews) {
        measureView(root);
        int height = root.getMeasuredHeight();
        int heightExtra = 0;
        if (subViews != null && subViews.length > 0) {
            for (View view : subViews) {
                measureView(view);
                heightExtra += view.getMeasuredHeight();
            }

        }
        return height + heightExtra;
    }

    /**
     * 获取资源颜色
     */
    public static int getColor(Context context, int colorRes) {
        if (context == null) {
            context = DialogUtils.appContext;
        }
        return context.getResources().getColor(colorRes);

    }


    /**
     * 获取文字
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 获取屏幕宽度(像素)
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    static final int[] secPosValueList = {1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600};
    // 存放国标一级汉字不同读音的起始区位码对应读音
    static final char[] firstLetter = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z'};
    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        if ((ch >> 7) == 0)
        {
            return  ch;
        }
        byte[] uniCode = null;

        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
    /**
     *
     * Description: dp 转换 px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    private static Point deviceSize = null;
    /**
     * @author: liujc
     * @Title: getDeviceSize
     * @Description: 获取手机屏幕宽高
     */
    @SuppressLint("NewApi")
    public static Point getDeviceSize(Context context) {
        if (deviceSize == null) {
            deviceSize = new Point(0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                ((WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay().getSize(deviceSize);
            } else {
                Display display = ((WindowManager) context
                        .getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();
                deviceSize.x = display.getWidth();
                deviceSize.y = display.getHeight();
                display = null;
            }
        }
        return deviceSize;
    }

    public static double getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < s.length(); i++) {
            String temp = s.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 1;
            } else {
                valueLength += 0.5;
            }
        }
        return Math.ceil(valueLength);
    }
}
