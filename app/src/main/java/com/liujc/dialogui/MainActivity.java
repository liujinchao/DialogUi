package com.liujc.dialogui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.liujc.dialogui.activity.DialogActivity;
import com.liujc.dialogui.activity.KeyBoardActivity;
import com.liujc.dialogui.activity.LoadingTipActivity;
import com.widget.jcdialog.XDialog;
import com.widget.jcdialog.utils.ToastUtil;
import com.widget.jcdialog.widget.updateDialog.UpdateDialogFragment;
import com.widget.jcdialog.widget.updateDialog.listener.IDownloadCallBack;
import com.widget.jcdialog.widget.updateDialog.listener.IUpdateCallback;
import com.widget.jcdialog.widget.updateDialog.UpdateAppBean;
import com.widget.jcdialog.widget.updateDialog.UpdateAppManager;
import com.widget.jcdialog.widget.updateDialog.listener.ExceptionHandler;
import com.widget.jcdialog.widget.updateDialog.listener.IUpdateDialogFragmentListener;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    Activity mActivity;
    Context mContext;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    IDownloadCallBack mDownloadCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivity = this;
        mContext = getApplication();
        XDialog.init(mContext);
        initView();
    }

    private void initView() {

    }

    //模拟网络请求
    int progress;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (progress == 0){
                mDownloadCallback.onStart();
            }
            progress = progress+5;
            if (mDownloadCallback != null){
                mDownloadCallback.onProgress(progress/100f);
                if (progress >= 100){
                    mDownloadCallback.onFinish(new File("test"));
                }
            }
            if (progress<100){
                handler.sendEmptyMessageDelayed(0, 200);
            }else {
                progress = 0;
            }
        }
    };

    @OnClick({R.id.btn_toast_top, R.id.btn_dialog_about, R.id.btn_pull_to_refresh,
            R.id.btn_loading_tip_dialog,R.id.btn_pay_keyboard, R.id.btn_update_dialog})
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
//                jumpTarget(RecycleviewActivity.class);
                break;
            case R.id.btn_loading_tip_dialog:
                jumpTarget(LoadingTipActivity.class);
                break;
            case R.id.btn_pay_keyboard:
                jumpTarget(KeyBoardActivity.class);
                break;
            case R.id.btn_update_dialog:
                showUpdateDialog();
                break;

        }
    }

    private void showUpdateDialog() {
        UpdateAppBean updateAppBean = new UpdateAppBean.Builder()
                .setUpdate(true)
                .setNewVersion("2.0.20")
                .setApkFileUrl("http://106.54.169.177/file/app_dialog.apk")
                .setUpdateDesc("1，赏个脸支持(star)一下。\r\n2，用扯淡的方式，分享技术的内涵。\r\n3，谈的是技术，更是我们的人生。")
                .setApkSize("5.20M")
                .setNewMd5("B5A7C226C5D10C3734D2090282DF3FBD")
                .setForceUpdate(false)
                .create();
        UpdateAppManager updateAppManager = new UpdateAppManager.Builder(this)
//                .showIgnoreVersion()
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        ToastUtil.showToast(e.getLocalizedMessage());
                    }
                })
                .dismissNotificationProgress()
                // 监听更新提示框相关事件
                .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
                    @Override
                    public void startDownloadApk(UpdateAppBean updateApp, final IDownloadCallBack downloadCallback) {
                        ToastUtil.showToast("开始下载");
                        doDownLoadAction(downloadCallback);
                    }

                    @Override
                    public void cancelDownloadApk(UpdateAppBean updateApp) {
                        if(updateApp.isForceUpdate()){
                            // 处理强制更新，被用户cancel的情况
                        }
                        ToastUtil.showToast("取消更新");
                    }
                })
                .create();
        XDialog.showUpdateDialog(updateAppManager, updateAppBean, new IUpdateCallback() {
            @Override
            public void hasNewApp(UpdateDialogFragment updateDialogFragment) {
                ToastUtil.showToast("有更新包");
            }

            @Override
            public void noNewApp() {
                ToastUtil.showToast("当前已是最新包");
            }
        });
    }
    private void doDownLoadAction(IDownloadCallBack downloadCallback) {
        mDownloadCallback = downloadCallback;
        handler.sendEmptyMessageDelayed(0, 200);
    }

    private void jumpTarget(Class target){
        Intent intent = new Intent(mContext,target);
        startActivity(intent);
    }
    public void showToast(CharSequence msg) {
        ToastUtil.showToastLong(msg.toString());
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
