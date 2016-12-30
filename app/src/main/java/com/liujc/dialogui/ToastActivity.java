package com.liujc.dialogui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.widget.jcdialog.utils.ToastUitl;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名称：ToastActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/29 14:08
 * 描述：TODO
 * 最近修改时间：2016/12/29 14:08
 * 修改人：Modify by liujc
 */
public class ToastActivity extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        context = this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_toast_top, R.id.btn_toast_center, R.id.btn_toast,R.id.btn_toast_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_toast_top:
                ToastUitl.showToastCenter("顶部的Toast");
//                ToastUitl.showToastWithImg("顶部的Toast", R.drawable.ic_success);
                break;
            case R.id.btn_toast_center:
                ToastUitl.showToastCenter("中部的Toast");
                break;
            case R.id.btn_toast:
                ToastUitl.showToast("默认的Toast");
                break;
            case R.id.btn_toast_img:
                ToastUitl.showToastWithImg("image Toast",R.drawable.ic_success);
                break;
        }
    }



}
