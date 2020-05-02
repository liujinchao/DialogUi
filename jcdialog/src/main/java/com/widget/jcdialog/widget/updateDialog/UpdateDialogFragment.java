package com.widget.jcdialog.widget.updateDialog;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.widget.jcdialog.R;
import com.widget.jcdialog.widget.updateDialog.listener.ExceptionHandler;
import com.widget.jcdialog.widget.updateDialog.listener.ExceptionHandlerHelper;
import com.widget.jcdialog.widget.updateDialog.listener.IDownloadCallBack;
import com.widget.jcdialog.widget.updateDialog.listener.IUpdateDialogFragmentListener;
import com.widget.jcdialog.widget.updateDialog.utils.AppUpdateUtils;
import com.widget.jcdialog.widget.updateDialog.utils.ColorUtil;
import com.widget.jcdialog.widget.updateDialog.utils.DrawableUtil;
import com.widget.jcdialog.widget.updateDialog.view.NumberProgressBar;

import java.io.File;

/**
 * @author :liujc
 * @date : 2020/5/1
 * @Description : 版本升级弹框
 */
public class UpdateDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String TIPS = "请授权访问存储空间权限，否则App无法更新";
    public static final int DEFAULT_VALUE = -1;
    public static boolean isShow = false;
    private View updateRootView;
    private TextView mContentTextView;
    private Button mUpdateOkButton;
    private UpdateAppBean mUpdateApp;
    private NumberProgressBar mNumberProgressBar;
    private ImageView mIvClose;
    private TextView mTitleTextView;
    private LinearLayout mLlClose;
    //默认色
    private int mDefaultColor = 0xffe94339;
    private int mDefaultPicResId = R.mipmap.lib_update_app_top_bg;
    private ImageView mTopIv;
    private TextView mIgnore;
    private IUpdateDialogFragmentListener mUpdateDialogFragmentListener;

    public UpdateDialogFragment setUpdateDialogFragmentListener(IUpdateDialogFragmentListener updateDialogFragmentListener) {
        this.mUpdateDialogFragmentListener = updateDialogFragmentListener;
        return this;
    }

    public static UpdateDialogFragment newInstance(Bundle args) {
        UpdateDialogFragment fragment = new UpdateDialogFragment();
        if (args != null) {
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShow = true;
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.UpdateAppDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //点击window外的区域 是否消失
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //禁用
                    if (mUpdateApp != null && mUpdateApp.isForceUpdate()) {
                        //返回桌面
                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
        });

        Window dialogWindow = getDialog().getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        lp.height = (int) (displayMetrics.heightPixels * 0.8f);
        dialogWindow.setAttributes(lp);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        updateRootView = inflater.inflate(R.layout.lib_update_app_dialog, container);
        return updateRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //提示内容
        mContentTextView = view.findViewById(R.id.tv_update_info);
        //标题
        mTitleTextView = view.findViewById(R.id.tv_title);
        //更新按钮
        mUpdateOkButton = view.findViewById(R.id.btn_ok);
        //进度条
        mNumberProgressBar = view.findViewById(R.id.npb);
        //关闭按钮
        mIvClose = view.findViewById(R.id.iv_close);
        //关闭按钮+线 的整个布局
        mLlClose = view.findViewById(R.id.ll_close);
        //顶部图片
        mTopIv = view.findViewById(R.id.iv_top);
        //忽略
        mIgnore = view.findViewById(R.id.tv_ignore);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData() {
        mUpdateApp = (UpdateAppBean) getArguments().getSerializable(UpdateAppManager.INTENT_KEY);
        //设置主题色
        initTheme();
        if (mUpdateApp != null) {
            //弹出对话框
            final String dialogTitle = mUpdateApp.getUpdateDefDialogTitle();
            final String newVersion = mUpdateApp.getNewVersion();
            final String targetSize = mUpdateApp.getApkSize();
            final String updateLog = mUpdateApp.getUpdateDesc();
            String msg = "";
            if (!TextUtils.isEmpty(targetSize)) {
                msg = "新版本大小：" + targetSize + "\n\n";
            }
            if (!TextUtils.isEmpty(updateLog)) {
                msg += updateLog;
            }
            //更新内容
            mContentTextView.setText(msg);
            //标题
            mTitleTextView.setText(TextUtils.isEmpty(dialogTitle) ? String.format("是否升级到%s版本？", newVersion) : dialogTitle);
            //强制更新
            if (mUpdateApp.isForceUpdate()) {
                mLlClose.setVisibility(View.GONE);
            } else {
                //不是强制更新时，才生效
                if (mUpdateApp.isShowIgnoreVersion()) {
                    mIgnore.setVisibility(View.VISIBLE);
                }
            }
            initEvents();
        }
    }

    public View getUpdateRootView() {
        return updateRootView;
    }

    /**
     * 初始化主题色
     */
    private void initTheme() {
        final int color = getArguments().getInt(UpdateAppManager.THEME_KEY, DEFAULT_VALUE);
        final int topResId = getArguments().getInt(UpdateAppManager.TOP_IMAGE_KEY, DEFAULT_VALUE);
        if (DEFAULT_VALUE == topResId) {
            if (DEFAULT_VALUE == color) {
                //默认红色
                setDialogTheme(mDefaultColor, mDefaultPicResId);
            } else {
                setDialogTheme(color, mDefaultPicResId);
            }
        } else {
            if (DEFAULT_VALUE == color) {
                //自动提色
                Palette.from(AppUpdateUtils.drawableToBitmap(this.getResources().getDrawable(topResId))).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int mDominantColor = palette.getDominantColor(mDefaultColor);
                        setDialogTheme(mDominantColor, topResId);
                    }
                });
//                setDialogTheme(mDefaultColor, topResId);
            } else {
                setDialogTheme(color, topResId);
            }
        }
    }

    /**
     * 设置
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mTopIv.setImageResource(topResId);
        mUpdateOkButton.setBackgroundDrawable(DrawableUtil.getDrawable(AppUpdateUtils.dip2px(4, getActivity()), color));
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mUpdateOkButton.setTextColor(ColorUtil.isTextColorDark(color) ? Color.BLACK : Color.WHITE);
    }

    private void initEvents() {
        mUpdateOkButton.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mIgnore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_ok) {
            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            } else {
                installApp();
                mUpdateOkButton.setVisibility(View.GONE);
            }

        } else if (i == R.id.iv_close) {
            if (mUpdateDialogFragmentListener != null) {
                mUpdateDialogFragmentListener.cancelDownloadApk(mUpdateApp);
            }
            dismiss();
        } else if (i == R.id.tv_ignore) {
            AppUpdateUtils.saveIgnoreVersion(getActivity(), mUpdateApp.getNewVersion());
            dismiss();
        }
    }

    private void installApp() {
        if (AppUpdateUtils.appIsDownloaded(mUpdateApp)) {
            AppUpdateUtils.installApp(UpdateDialogFragment.this, AppUpdateUtils.getAppFile(mUpdateApp));
            dismiss();
        } else {
            if (mUpdateDialogFragmentListener != null){
                mUpdateDialogFragmentListener.startDownloadApk(mUpdateApp, downloadCallback);
            }
            if (mUpdateApp.isHideDialog()) {
                dismiss();
            }
        }
    }

    IDownloadCallBack downloadCallback = new IDownloadCallBack() {
        @Override
        public void onStart() {
            if (!UpdateDialogFragment.this.isRemoving()) {
                mUpdateOkButton.setVisibility(View.INVISIBLE);
                mNumberProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onProgress(float progress) {
            if (!UpdateDialogFragment.this.isRemoving()) {
                mNumberProgressBar.setProgress(Math.round(progress * 100));
                mNumberProgressBar.setMax(100);
            }
        }


        @Override
        public boolean onFinish(final File file) {
            if (!UpdateDialogFragment.this.isRemoving()) {
                if (mUpdateApp.isForceUpdate()) {
                    mNumberProgressBar.setVisibility(View.INVISIBLE);
                    mUpdateOkButton.setText("安装");
                    mUpdateOkButton.setVisibility(View.VISIBLE);
                    mUpdateOkButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AppUpdateUtils.installApp(UpdateDialogFragment.this, file);
                            dismiss();
                        }
                    });
                } else {
                    dismissAllowingStateLoss();
                }
            }
            return true;
        }

        @Override
        public void onError(String msg) {
            if (!UpdateDialogFragment.this.isRemoving()) {
                dismissAllowingStateLoss();
            }
        }

        @Override
        public boolean onInstallAppAndAppOnForeground(File file) {
            // 如果应用处于前台，那么就自行处理应用安装
            AppUpdateUtils.installApp(UpdateDialogFragment.this.getActivity(), file);
            dismiss();
            return true;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                installApp();
                mUpdateOkButton.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), TIPS, Toast.LENGTH_LONG).show();
                dismiss();
            }
        }

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (manager.isDestroyed()) {
                return;
            }
        }
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            ExceptionHandler exceptionHandler = ExceptionHandlerHelper.getInstance();
            if (exceptionHandler != null) {
                exceptionHandler.onException(e);
            }
        }
    }

    @Override
    public void onDestroyView() {
        isShow = false;
        updateRootView = null;
        super.onDestroyView();
    }
}

