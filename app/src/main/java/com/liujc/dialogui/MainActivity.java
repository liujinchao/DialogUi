package com.liujc.dialogui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.widget.jcdialog.DialogUtils;
import com.widget.jcdialog.adapter.TieAdapter;
import com.widget.jcdialog.bean.BuildBean;
import com.widget.jcdialog.bean.PopuBean;
import com.widget.jcdialog.bean.TieBean;
import com.widget.jcdialog.listener.DialogUIDateTimeSaveListener;
import com.widget.jcdialog.listener.DialogUIItemListener;
import com.widget.jcdialog.listener.DialogUIListener;
import com.widget.jcdialog.listener.TdataListener;
import com.widget.jcdialog.utils.ToastUitl;
import com.widget.jcdialog.widget.DateSelectorWheelView;
import com.widget.jcdialog.widget.LoadingTip;
import com.widget.jcdialog.widget.PopuWindowView;

import java.util.ArrayList;
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

    @OnClick({R.id.btn_toast_top, R.id.btn_dialog_about, R.id.btn_pull_to_refresh,R.id.btn_loading_tip_dialog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_toast_top:
                jumpTarget(ToastActivity.class);
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

        }
    }

    private void jumpTarget(Class target){
        Intent intent = new Intent(mContext,target);
        startActivity(intent);
    }
    public void showToast(CharSequence msg) {
        ToastUitl.showToastLong(msg.toString());
    }
}
