package com.widget.jcdialog.widget.updateDialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.widget.jcdialog.widget.updateDialog.listener.ExceptionHandler;
import com.widget.jcdialog.widget.updateDialog.listener.ExceptionHandlerHelper;
import com.widget.jcdialog.widget.updateDialog.listener.IUpdateCallback;
import com.widget.jcdialog.widget.updateDialog.listener.IUpdateDialogFragmentListener;
import com.widget.jcdialog.widget.updateDialog.utils.AppUpdateUtils;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 版本更新管理器
 */
public class UpdateAppManager {
    private static final String TAG = UpdateAppManager.class.getSimpleName();

    static final String INTENT_KEY = "update_dialog_values";
    final static String THEME_KEY = "theme_color";
    final static String TOP_IMAGE_KEY = "top_resId";
    private Activity mActivity;
    private int mThemeColor;
    private
    @DrawableRes
    int mTopPic;
    private UpdateAppBean mUpdateApp;
    private String mTargetPath;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;
    //自定义参数
    private IUpdateDialogFragmentListener mUpdateDialogFragmentListener;

    private UpdateAppManager(Builder builder) {
        mActivity = builder.mActivity;
        mThemeColor = builder.mThemeColor;
        mTopPic = builder.mTopPic;

        mTargetPath = builder.mTargetPath;
        mHideDialog = builder.mHideDialog;
        mShowIgnoreVersion = builder.mShowIgnoreVersion;
        mDismissNotificationProgress = builder.dismissNotificationProgress;
        mOnlyWifi = builder.mOnlyWifi;
        mUpdateDialogFragmentListener = builder.mUpdateDialogFragmentListener;
    }

    public Context getContext() {
        return mActivity;
    }

    /**
     * @return 补充新版本apk相关信息
     */
    public UpdateAppBean fillUpdateAppData() {
        if (mUpdateApp != null) {
            mUpdateApp.setTargetPath(mTargetPath);
            mUpdateApp.setHideDialog(mHideDialog);
            mUpdateApp.showIgnoreVersion(mShowIgnoreVersion);
            mUpdateApp.dismissNotificationProgress(mDismissNotificationProgress);
            mUpdateApp.setOnlyWifi(mOnlyWifi);
            return mUpdateApp;
        }

        return null;
    }

    private boolean verify() {
        //版本忽略
        if (mShowIgnoreVersion && AppUpdateUtils.isNeedIgnore(mActivity, mUpdateApp.getNewVersion())) {
            return true;
        }
        if (TextUtils.isEmpty(mTargetPath)) {
            Log.e(TAG, "下载路径不能为空");
            return true;
        }
        return mUpdateApp == null;
    }

    /**
     * 跳转到更新页面
     */
    public UpdateDialogFragment showDialogFragment() {
        //校验
        if (verify()) {
            return null;
        }
        if (mActivity != null && !mActivity.isFinishing()) {
            Bundle bundle = new Bundle();
            fillUpdateAppData();
            bundle.putSerializable(INTENT_KEY, mUpdateApp);
            if (mThemeColor != 0) {
                bundle.putInt(THEME_KEY, mThemeColor);
            }
            if (mTopPic != 0) {
                bundle.putInt(TOP_IMAGE_KEY, mTopPic);
            }
            UpdateDialogFragment updateDialogFragment = UpdateDialogFragment.newInstance(bundle);
            updateDialogFragment.setUpdateDialogFragmentListener(mUpdateDialogFragmentListener)
                    .show(((FragmentActivity) mActivity).getSupportFragmentManager(), "dialog");
            return updateDialogFragment;
        }
        return null;
    }

    public void checkUpdate(UpdateAppBean updateAppBean, IUpdateCallback updateCallback) {
        checkNewApp(updateCallback, updateAppBean);
    }

    /**
     * 检测是否有新版本
     *
     * @param callback 更新回调
     */
    public void checkNewApp(final IUpdateCallback callback, UpdateAppBean updateInfo) {
        if (callback == null) {
            return;
        }
        if (UpdateDialogFragment.isShow) {
            return;
        }
        mUpdateApp = updateInfo;
        if (mUpdateApp.isUpdate()) {
            callback.hasNewApp(showDialogFragment());
        } else {
            callback.noNewApp();
        }
    }

    public static class Builder {
        private Activity mActivity;
        //1，设置按钮，进度条的颜色
        private int mThemeColor = 0;
        //2，顶部的图片
        private
        @DrawableRes
        int mTopPic = 0;
        private String mTargetPath;
        private boolean mHideDialog;
        private boolean mShowIgnoreVersion;
        private boolean dismissNotificationProgress;
        private boolean mOnlyWifi;
        private IUpdateDialogFragmentListener mUpdateDialogFragmentListener;

        public Builder(Activity mActivity) {
            this.mActivity = mActivity;
        }

        /**
         * @param targetPath apk的下载存储路径，
         */
        public Builder setTargetPath(String targetPath) {
            mTargetPath = targetPath;
            return this;
        }

        /**
         * @param themeColor 设置按钮，进度条的颜色
         */
        public Builder setThemeColor(int themeColor) {
            mThemeColor = themeColor;
            return this;
        }

        /**
         * @param topPic 顶部的图片
         */
        public Builder setTopPic(int topPic) {
            mTopPic = topPic;
            return this;
        }

        /**
         *  设置默认的UpdateDialogFragment监听器
         */
        public Builder setUpdateDialogFragmentListener(IUpdateDialogFragmentListener updateDialogFragmentListener) {
            this.mUpdateDialogFragmentListener = updateDialogFragmentListener;
            return this;
        }

        /**
         * @return 生成app管理器
         */
        public UpdateAppManager create() {
            //校验
            if (mActivity == null) {
                throw new NullPointerException("必要参数不能为空");
            }
            if (TextUtils.isEmpty(mTargetPath)) {
                //sd卡是否存在
                String path = "";
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                        || !Environment.isExternalStorageRemovable()) {
                    try {
                        path = mActivity.getExternalCacheDir().getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (TextUtils.isEmpty(path)) {
                        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    }
                } else {
                    path = mActivity.getCacheDir().getAbsolutePath();
                }
                setTargetPath(path);
            }
            return new UpdateAppManager(this);
        }

        /**
         * @param b 是否隐藏对话框下载进度条
         */
        public Builder hideDialogOnDownloading(boolean b) {
            mHideDialog = b;
            return this;
        }

        /***
         * @return 是否显示忽略版本按钮
         */
        public Builder showIgnoreVersion() {
            mShowIgnoreVersion = true;
            return this;
        }

        /**
         * @return 是否显示通知栏进度条
         */
        public Builder dismissNotificationProgress() {
            dismissNotificationProgress = true;
            return this;
        }

        public Builder setOnlyWifi() {
            mOnlyWifi = true;
            return this;
        }

        public Builder handleException(ExceptionHandler exceptionHandler) {
            ExceptionHandlerHelper.init(exceptionHandler);
            return this;
        }
    }
}

