package com.liujc.dialogui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.liujc.dialogui.R;
import com.widget.jcdialog.widget.LoadingTip;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名称：LoadingTipActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/11/18 17:17
 * 描述：TODO
 */
public class LoadingTipActivity extends Activity {
    @BindView(R.id.loadedTip)
    LoadingTip loadedTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_tip);
        initView();
        ButterKnife.bind(this);
    }

    private void initView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.loading_tip_loading,R.id.loading_tip_empty,R.id.loading_tip_no_net,R.id.loading_tip_reload,R.id.loading_tip_net_error})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loading_tip_loading:
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.loading);
                break;
            case R.id.loading_tip_empty:
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.empty);
                break;
            case R.id.loading_tip_no_net:
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.netError);
                break;
            case R.id.loading_tip_reload:
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.finish);
                break;
            case R.id.loading_tip_net_error:
                loadedTip.setLoadingTip(LoadingTip.LoadStatus.sereverError);
                break;
        }
    }
}
