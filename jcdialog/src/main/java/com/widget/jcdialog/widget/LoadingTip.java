package com.widget.jcdialog.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.widget.jcdialog.DialogUtils;
import com.widget.jcdialog.R;

/**
 * 类名称：LoadingTip
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/13 10:48
 * 描述：加载页面内嵌提示
 */
public class LoadingTip extends LinearLayout {

    private ImageView img_tip_logo;
//    private ImageView img_loading_rotate;
    private TextView tv_tips;
    private Button bt_operate;
    private String errorMsg;
    private onReloadListener onReloadListener;


    public LoadingTip(Context context) {
        super(context);
        initView(context);
    }

    public LoadingTip(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingTip(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    //分为服务器失败，网络加载失败、数据为空、加载中、完成四种状态
    public static enum LoadStatus {
        sereverError,netError, empty, loading,finish
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.dialog_loading_tip, this);
        img_tip_logo = (ImageView) findViewById(R.id.img_tip_logo);
//        img_loading_rotate = (ImageView) findViewById(R.id.img_loading_rotate2);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        bt_operate = (Button) findViewById(R.id.bt_operate);
        //重新尝试
        bt_operate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onReloadListener!=null){
                    onReloadListener.reload();
                }
            }
        });
        setVisibility(View.GONE);
    }

    public void setTips(String tips){
        if(tv_tips!=null){
            tv_tips.setText(tips);
        }
    }

    /**
     * 根据状态显示不同的提示
     * @param loadStatus
     */
    public void setLoadingTip(LoadStatus loadStatus){
        switch (loadStatus){
            case empty:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                tv_tips.setText(getContext().getText(R.string.loading_tip_empty).toString());
                bt_operate.setVisibility(GONE);
                img_tip_logo.setImageResource(R.drawable.no_content_tip);
                img_tip_logo.clearAnimation();
                break;
            case sereverError:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(errorMsg)){
                    tv_tips.setText(getContext().getText(R.string.loading_tip_net_error).toString());
                }else {
                    tv_tips.setText(errorMsg);
                }
                bt_operate.setText("重新尝试");
                bt_operate.setVisibility(VISIBLE);
                img_tip_logo.setImageResource(R.drawable.loading_tip_server_wrong);
                img_tip_logo.clearAnimation();
                break;
            case netError:
                setVisibility(View.VISIBLE);
                img_tip_logo.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(errorMsg)){
                    tv_tips.setText(getContext().getText(R.string.loading_tip_no_net).toString());
                }else {
                    tv_tips.setText(errorMsg);
                }
                bt_operate.setText("设置网络");
                bt_operate.setVisibility(VISIBLE);
                img_tip_logo.setImageResource(R.drawable.loading_tip_wifi_off);
                img_tip_logo.clearAnimation();
                break;
            case loading:
                setVisibility(View.VISIBLE);
                bt_operate.setVisibility(GONE);
                img_tip_logo.setVisibility(View.VISIBLE);
                img_tip_logo.setImageResource(R.drawable.img_loading_rotate);
                // 旋转动画
                Animation animation = AnimationUtils.loadAnimation(DialogUtils.appContext,R.anim.loading_rotate);
                img_tip_logo.startAnimation(animation);
                tv_tips.setText(getContext().getText(R.string.loading_tip_loading).toString());
                break;
            case finish:
                setVisibility(View.GONE);
                break;
        }
    }


    public void setOnReloadListener(onReloadListener onReloadListener){
        this.onReloadListener=onReloadListener;
    }
    /**
     * 重新尝试接口
     */
    public interface onReloadListener{
        void reload();
    }


}

