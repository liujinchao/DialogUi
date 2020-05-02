package com.widget.jcdialog.widget.updateDialog;

import java.io.Serializable;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 版本信息
 */
public class UpdateAppBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * update : true
     * new_version : xxxxx
     * apk_url : http://xxxxx/xx.apk
     * update_desc : xxxx
     * new_md5 : xxxxxxxxxxxxxx
     * apk_size : 601132
     */
    //是否有新版本
    private boolean update;
    //新版本号
    private String new_version;
    //新app下载地址
    private String apk_file_url;
    //更新文案
    private String update_desc;
    //标题
    private String update_def_dialog_title;
    //新app大小
    private String apk_size;
    //是否强制更新
    private boolean force_update;
    //md5
    private String new_md5;

    private String targetPath;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;

    public UpdateAppBean(Builder builder) {
        this.update = builder.update;
        this.force_update = builder.force_update;
        this.update_def_dialog_title = builder.update_def_dialog_title;
        this.update_desc = builder.update_desc;
        this.apk_file_url = builder.apk_file_url;
        this.apk_size = builder.apk_size;
        this.new_version = builder.new_version;
        this.new_md5 = builder.new_md5;
    }

    //是否隐藏对话框下载进度条,内部使用
    public boolean isHideDialog() {
        return mHideDialog;
    }

    public void setHideDialog(boolean hideDialog) {
        mHideDialog = hideDialog;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public UpdateAppBean setTargetPath(String targetPath) {
        this.targetPath = targetPath;
        return this;
    }

    public boolean isForceUpdate() {
        return force_update;
    }

    public boolean isUpdate() {
        return update;
    }

    public String getNewVersion() {
        return new_version;
    }

    public String getApkFileUrl() {
        return apk_file_url;
    }

    public String getUpdateDesc() {
        return update_desc;
    }

    public String getUpdateDefDialogTitle() {
        return update_def_dialog_title;
    }

    public String getNewMd5() {
        return new_md5;
    }

    public String getApkSize() {
        return apk_size;
    }

    public boolean isShowIgnoreVersion() {
        return mShowIgnoreVersion;
    }

    public void showIgnoreVersion(boolean showIgnoreVersion) {
        mShowIgnoreVersion = showIgnoreVersion;
    }

    public void dismissNotificationProgress(boolean dismissNotificationProgress) {
        mDismissNotificationProgress = dismissNotificationProgress;
    }

    public boolean isDismissNotificationProgress() {
        return mDismissNotificationProgress;
    }

    public boolean isOnlyWifi() {
        return mOnlyWifi;
    }

    public void setOnlyWifi(boolean onlyWifi) {
        mOnlyWifi = onlyWifi;
    }

    public static class Builder{
        //是否有新版本
        private boolean update;
        //新版本号
        private String new_version;
        //新app下载地址
        private String apk_file_url;
        //更新文案
        private String update_desc;
        //标题
        private String update_def_dialog_title;
        //新app大小
        private String apk_size;
        //是否强制更新
        private boolean force_update;
        //md5
        private String new_md5;

        public Builder setUpdate(boolean update) {
            this.update = update;
            return this;
        }

        public Builder setNewVersion(String new_version) {
            this.new_version = new_version;
            return this;
        }

        public Builder setApkFileUrl(String apk_file_url) {
            this.apk_file_url = apk_file_url;
            return this;
        }

        public Builder setUpdateDesc(String updateDesc) {
            this.update_desc = updateDesc;
            return this;
        }

        public Builder setUpdateDialogTitle(String dialogTitle) {
            this.update_def_dialog_title = dialogTitle;
            return this;
        }

        public Builder setApkSize(String apkSize) {
            this.apk_size = apkSize;
            return this;
        }

        public Builder setForceUpdate(boolean forceUpdate) {
            this.force_update = forceUpdate;
            return this;
        }

        public Builder setNewMd5(String newMd5) {
            this.new_md5 = newMd5;
            return this;
        }

        public UpdateAppBean create(){
            return new UpdateAppBean(this);
        }
    }

}
