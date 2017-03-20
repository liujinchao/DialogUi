package com.liujc.dialogui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.liujc.dialogui.activity.DialogActivity;
import com.liujc.dialogui.activity.KeyBoardActivity;
import com.liujc.dialogui.activity.LoadingTipActivity;
import com.liujc.dialogui.activity.RecycleviewActivity;
import com.liujc.dialogui.activity.ToastActivity;
import com.widget.jcdialog.DialogUtils;
import com.widget.jcdialog.utils.ToastUitl;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    Activity mActivity;
    Context mContext;
    @Bind(R.id.ll_main)
    LinearLayout llMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivity = this;
        mContext = getApplication();
        DialogUtils.init(mContext);
    }

    @OnClick({R.id.btn_toast_top, R.id.btn_dialog_about, R.id.btn_pull_to_refresh,R.id.btn_loading_tip_dialog,R.id.btn_pay_keyboard})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_toast_top:
                openRouterUri("app://dialog/toast");
//                jumpTarget(ToastActivity.class);
                break;
            case R.id.btn_dialog_about:
                jumpTarget(DialogActivity.class);
                break;
            case R.id.btn_pull_to_refresh:
                jumpTarget(RecycleviewActivity.class);
                break;
            case R.id.btn_loading_tip_dialog:
                jumpTarget(LoadingTipActivity.class);
                break;
            case R.id.btn_pay_keyboard:
                jumpTarget(KeyBoardActivity.class);
                break;

        }
    }

    private void jumpTarget(Class target){
        Intent intent = new Intent(mContext,target);
        startActivity(intent);
    }
    public void showToast(CharSequence msg) {
        ToastUitl.showToastLong(msg.toString());
    }

    /**
     * 通过uri跳转指定页面
     *
     * @param url
     */
    private void openRouterUri(String url) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isValid = !activities.isEmpty();
        if (isValid) {
            mContext.startActivity(intent);
        }
    }
}
