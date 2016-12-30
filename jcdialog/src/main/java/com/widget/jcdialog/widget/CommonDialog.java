package com.widget.jcdialog.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.widget.jcdialog.R;
import com.widget.jcdialog.utils.ToolUtils;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <dl>
 * <dt>CommonDialog.java</dt>
 * <dd>Description:自定义对话框</dd>
 * </dl>

 */
public class CommonDialog {

    public static final int DIALOG_TYPE_NETWORK = 1;

    private View contentView;
    private View selfDifineView;
    private Dialog dialog;

    private boolean stillShow = false;
    private boolean canceledOnTouchOutside = false;

    private Context context;
    private SparseArray<Button> btns = null;

    private DialogInterface.OnDismissListener dismissListener = null;

    private String activityName;

    private static Map<String, ActivityDialogStack> activityDlgStkMap = new ConcurrentHashMap<String, ActivityDialogStack>();

    public static void clearStkByActivityName(String activityName) {
        activityDlgStkMap.remove(activityName);
    }

    public CommonDialog(Context context, final String activityName, int mtype) {
//        LogUtil.begin("activityName:" + activityName + "|mtype:" + mtype);
        this.context = context;
        this.activityName = activityName;

        btns = new SparseArray<Button>();
        dialog = new Dialog(context, R.style.CustomProgressDialog);
        contentView = LayoutInflater.from(context).inflate(
                R.layout.common_dialog_layout, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.addContentView(contentView, params);
        setTitle(null);
        dialog.setCanceledOnTouchOutside(false);
        dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_BACK));
        dialog.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                KeyEvent.KEYCODE_BACK));
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dismissListener != null) {
                    dismissListener.onDismiss(dialog);
                }

                ActivityDialogStack actDlgStack = activityDlgStkMap
                        .get(activityName);
                if (actDlgStack != null) {
                    actDlgStack.disDialogType = -1;
                    activityDlgStkMap.put(activityName, actDlgStack);

                    showDialog();
                }
            }
        });

        ActivityDialogStack actDlgStack = activityDlgStkMap.get(activityName);
        if (actDlgStack == null) {
            actDlgStack = new ActivityDialogStack();
        }
        if (actDlgStack.disDialogType != mtype
                && !actDlgStack.typeStack.contains(mtype + "")) {
            actDlgStack.dialogStack.push(this);
            actDlgStack.typeStack.push(mtype + "");
        }
        activityDlgStkMap.put(activityName, actDlgStack);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public SparseArray<Button> getBtns() {
        return btns;
    }

    public View getContentView() {
        return contentView;
    }

    public void setCancelable(boolean cancelable) {
        if (dialog != null) {
            dialog.setCancelable(cancelable);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener _linstener) {
        dismissListener = _linstener;
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        } else {
            return false;
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    public void showBtns(boolean flag) {
        LinearLayout btnLayout = (LinearLayout) contentView.findViewById(R.id.operation_layout);
        setBtnLine(flag);
        setContentLine(flag);
        if (!flag) {
            btnLayout.setVisibility(View.GONE);
        } else {
            btnLayout.setVisibility(View.VISIBLE);
        }
    }
    private void setContentLine(boolean flag){
        View line = (View) contentView.findViewById(R.id.line2);
        if (flag){
            line.setVisibility(View.VISIBLE);
        }else {
            line.setVisibility(View.GONE);
        }
    }
    public void showTitle(boolean flag) {
        TextView titleTv = (TextView) contentView
                .findViewById(R.id.tv_title);
        View line = (View) contentView
                .findViewById(R.id.line1);
        if (!flag) {
            titleTv.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            titleTv.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }
    }
    public void setTitle(int id) {
        if (id < 0) {
            // contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
            // contentView.findViewById(R.id.line1).setVisibility(View.GONE);
            TextView titleTv = (TextView) contentView
                    .findViewById(R.id.tv_title);
            titleTv.setText("提示");
        } else {
            TextView titleTv = (TextView) contentView
                    .findViewById(R.id.tv_title);
            titleTv.setText(id);
            titleTv.setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.line1).setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            // contentView.findViewById(R.id.tv_title).setVisibility(View.GONE);
            // contentView.findViewById(R.id.line1).setVisibility(View.GONE);
            TextView titleTv = (TextView) contentView
                    .findViewById(R.id.tv_title);
            titleTv.setText("提示");
        } else {
            TextView titleTv = (TextView) contentView
                    .findViewById(R.id.tv_title);
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
            contentView.findViewById(R.id.line1).setVisibility(View.VISIBLE);
        }
    }

    public void setTilteGravity(int gravity) {
        TextView titleTv = (TextView) contentView.findViewById(R.id.tv_title);
        titleTv.setGravity(gravity);
    }

    public void setMessage(int id) {
        if (id < 0) {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.GONE);
        } else {

            contentView.findViewById(R.id.lt_difine)
                    .setVisibility(View.VISIBLE);
            ((TextView) contentView.findViewById(R.id.tv_message)).setText(id);
            updateMsgTextViewGravity();
        }
    }

    public void setMessage(String msg) {
        if (TextUtils.isEmpty(msg)) {
            contentView.findViewById(R.id.lt_difine).setVisibility(View.GONE);
        } else {

            contentView.findViewById(R.id.lt_difine)
                    .setVisibility(View.VISIBLE);
            ((TextView) contentView.findViewById(R.id.tv_message)).setText(msg);
            updateMsgTextViewGravity();
        }
    }


    private void updateMsgTextViewGravity(){
        String txt = ((TextView) contentView.findViewById(R.id.tv_message)).getText().toString();
        int length = (int) ToolUtils.getLength(txt);
//    	int lineCount = ((TextView) contentView.findViewById(R.id.tv_message))
//                .getLineCount();
        if(length<=14){
            ((TextView) contentView.findViewById(R.id.tv_message))
                    .setGravity(Gravity.CENTER);
        } else {
            ((TextView) contentView.findViewById(R.id.tv_message))
                    .setGravity(Gravity.LEFT);
        }
    }

    public void setMessageTxtSize(int txtSize) {

        ((TextView) contentView.findViewById(R.id.tv_message))
                .setTextSize(txtSize);
    }

//    public void setMessageForPrivate(int id) {
//        contentView.findViewById(R.id.lt_difine).setVisibility(View.VISIBLE);
//        ((TextView) contentView.findViewById(R.id.tv_message)).setText(id);
//        contentView.findViewById(R.id.et_verify).setVisibility(View.VISIBLE);
//
//    }

//    public void setMessageForPrivate(String msg) {
//        contentView.findViewById(R.id.lt_difine).setVisibility(View.VISIBLE);
//        ((TextView) contentView.findViewById(R.id.tv_message)).setText(msg);
//        contentView.findViewById(R.id.et_verify).setVisibility(View.VISIBLE);
//    }

    public void setPositiveButton(final BtnClickedListener btnOk, String text) {

        Button sureBtn = (Button) contentView.findViewById(R.id.confirm_btn);
        sureBtn.setVisibility(View.VISIBLE);
        sureBtn.setText(text);
        btns.put(1, sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOk != null) {
                    btnOk.onBtnClicked();
                }
                if (dialog != null && !stillShow) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

    public void setPositiveButton(final BtnClickedRespListener btnLstner,
                                  String text) {

        Button sureBtn = (Button) contentView.findViewById(R.id.confirm_btn);
        sureBtn.setVisibility(View.VISIBLE);
        sureBtn.setText(text);
        btns.put(1, sureBtn);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLstner != null) {
                    if (btnLstner.onBtnClicked()) {
                        if (dialog != null && !stillShow) {
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                }
            }
        });
    }

    public void setPositiveButton(final BtnClickedListener btnOk, int id) {
        setPositiveButton(btnOk, context.getResources().getString(id));
    }

    public void setCancleButton(final BtnClickedListener cancleOk, String text) {
        Button canBtn = (Button) contentView.findViewById(R.id.cancel_btn);
        canBtn.setVisibility(View.VISIBLE);
        canBtn.setText(text);
        btns.put(0, canBtn);
        canBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (cancleOk != null) {
                    cancleOk.onBtnClicked();
                }
            }
        });
    }

    public void setCancleButton(final BtnClickedListener cancleOk, int id) {
        setCancleButton(cancleOk, context.getResources().getString(id));
    }

    public void addView(View view) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView
                    .findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = view;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            addLt.addView(selfDifineView, params);
        }
    }

    public void addSpecialView(View view) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView
                    .findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = view;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 0);
            addLt.setLayoutParams(lp);
            addLt.addView(selfDifineView);
        }
    }

    public void addSpecialView(int viewId) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView
                    .findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = LayoutInflater.from(context).inflate(viewId, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 100, 0, 100);
            addLt.setLayoutParams(lp);
            addLt.addView(selfDifineView);
        }
    }

    public void addView(int viewId) {
        if (contentView != null) {
            LinearLayout addLt = (LinearLayout) contentView
                    .findViewById(R.id.lt_difine);
            addLt.removeAllViews();
            selfDifineView = LayoutInflater.from(context).inflate(viewId, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            addLt.addView(selfDifineView,params);
        }
    }

    public View getSelfDifineView() {
        return selfDifineView;
    }

    public void showDialog() {
        ActivityDialogStack actDlgStk = activityDlgStkMap.get(activityName);
        if (actDlgStk != null) {
            if (!actDlgStk.dialogStack.empty()) {
                if (actDlgStk.disDialogType > -1) {
                    return;
                }
                CommonDialog dlg = actDlgStk.dialogStack.pop();
                if (dlg.getBtns().size() > 0) {
                    if (dlg.getBtns().size() == 2) {
                        dlg.getContentView().findViewById(R.id.line3)
                                .setVisibility(View.VISIBLE);
                    } else if (dlg.getBtns().size() == 1) {
                        Button cancelBtn = dlg.getBtns().get(0);
                        if (cancelBtn != null) {
                            cancelBtn.setBackgroundResource(R.drawable.bg_common_dialog_btn);
                        }
                    }
                }
                dlg.getDialog().setCanceledOnTouchOutside(
                        canceledOnTouchOutside);
                dlg.getDialog().show();

                if (!actDlgStk.typeStack.isEmpty()) {
                    actDlgStk.disDialogType = Integer
                            .parseInt(actDlgStk.typeStack.pop());
                } else {
                    actDlgStk.disDialogType = -1;
                }
            }

            activityDlgStkMap.put(activityName, actDlgStk);
        }
    }

    public void setCloseButton(final BtnClickedListener listener) {

        RelativeLayout closeBtn = (RelativeLayout) contentView
                .findViewById(R.id.lt_close);
        closeBtn.setVisibility(View.VISIBLE);
        TextView tv = (TextView) contentView.findViewById(R.id.tv_title);
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                if (listener != null) {
                    listener.onBtnClicked();
                }
            }
        });
    }

    public interface BtnClickedListener {
        public void onBtnClicked();
    }

    public interface BtnClickedRespListener {
        public boolean onBtnClicked();
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener) {
        if (dialog != null) {
            dialog.setOnCancelListener(listener);
        }
    }

    public class ActivityDialogStack {
        public int disDialogType = -1;
        public Stack<CommonDialog> dialogStack = new Stack<CommonDialog>();
        public Stack<String> typeStack = new Stack<String>();
    }

    public boolean isStillShow() {
        return stillShow;
    }

    public void setStillShow(boolean stillShow) {
        this.stillShow = stillShow;
    }
    //底部显示一个按钮时，隐藏中间分割线
    public  void setBtnLine(boolean isVisible)
    {
        View line = (View) contentView
                .findViewById(R.id.line3);
        if (!isVisible){
            line.setVisibility(View.GONE);
        }
    }

    /**
     *
     * Description:
     *
     * @param tag
     *            ture:
     */
    public void setCanceledOnTouchOutside(boolean tag) {
        this.canceledOnTouchOutside = tag;
    }


    public void setCommondialogDrawal() {
        ImageView tv = (ImageView) contentView.findViewById(R.id.tv_close);
        tv.setVisibility(View.VISIBLE);
        tv.setFocusable(true);
        tv.setClickable(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        });
    }

}
