package com.widget.jcdialog.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.widget.jcdialog.R;
import com.widget.jcdialog.adapter.PopuWindowAdapter;
import com.widget.jcdialog.bean.PopuBean;
import com.widget.jcdialog.listener.TdataListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：Create on 2016/12/26 16:29
 * 描述：PopupWindowView,下拉选择列表
 */
public class PopupWindowView {

    View viewItem = null;
    RecyclerView rcvPopupWindowList;
    PopupWindow pullDownView;// 弹出窗口
    private List<PopuBean> popuLists = new ArrayList<PopuBean>();
    private PopuWindowAdapter mPopuWindowAdapter;
    private Context mContext;
    private TdataListener mTdataListener;
    private int maxLine = 5;

    public PopupWindowView(Context mContext, int widthGravity) {
        this.mContext = mContext;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        viewItem = inflater.inflate(R.layout.dialogui_popu_options, null);
        rcvPopupWindowList =  viewItem.findViewById(R.id.rcv_popup_window_list);
        mPopuWindowAdapter = new PopuWindowAdapter(mContext, popuLists);
        rcvPopupWindowList.setLayoutManager(new LinearLayoutManager(mContext));
        rcvPopupWindowList.setAdapter(mPopuWindowAdapter);
        pullDownView = new PopupWindow(viewItem, widthGravity,
                LayoutParams.WRAP_CONTENT, true);
        pullDownView.setOutsideTouchable(true);
        pullDownView.setBackgroundDrawable(new BitmapDrawable());
        mPopuWindowAdapter.setClickListener(new PopuWindowAdapter.PopupWindowClickListener() {
            @Override
            public void setOnclickListener(int position, PopuBean popuBean) {
                if (mTdataListener != null) {
                    mTdataListener.onItemClick(position, PopupWindowView.this);
                }
            }
        });
    }

    /**
     * 设置下拉框的数据
     */
    public void initPopupData(TdataListener tdataListener) {
        mTdataListener = tdataListener;
        if (mTdataListener != null) {
            mTdataListener.initPopupData(popuLists);
        }
        if (popuLists != null && popuLists.size() > maxLine) {
            pullDownView.setHeight(dip2px(maxLine * 40));
        }
        mPopuWindowAdapter.notifyDataSetChanged();
    }

    private int dip2px(int dip) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * 设置最大行popupWindow
     */
    public void setMaxLines(int maxLines) {
        maxLine = maxLines;
    }

    /**
     * 显示popupWindow
     */
    public void showing(View v) {
        pullDownView.showAsDropDown(v, 0, 0);
    }

    public void showAsDropDown(View v, int xoff, int yoff ) {
        pullDownView.showAsDropDown(v, xoff, yoff);
    }

    /**
     * 关闭popupWindow
     */
    public void dismiss() {
        pullDownView.dismiss();
    }

    /**
     * 获取选择的PopuBean
     */
    public PopuBean getPopupBean(int popupPosition){
        return popuLists.get(popupPosition);
    }
    /**
     * 获取选择的名称
     */
    public String getTitle(int popupPosition) {
        return popuLists.get(popupPosition).getTitle();
    }

    /**
     * 获取选择的id
     */
    public int getId(int popupPosition) {
        return popuLists.get(popupPosition).getId();
    }

    /**
     * 获取选择的sid
     */
    public String getSid(int popupPosition) {
        return popuLists.get(popupPosition).getSid();
    }

}
