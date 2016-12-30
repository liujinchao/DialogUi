package com.liujc.dialogui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.liujc.dialogui.R;

import java.util.ArrayList;

/**
 * 类名称：${primary_type_name}
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/10/25 16:21
 * 描述：TODO
 * 最近修改时间：2016/10/25 16:21
 * 修改人：Modify by liujc
 */
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> list;
    private OnClickListener onClickListener;

    public RecycleAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card_view, parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.showMsg.setText(list.get(position).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null){
                    onClickListener.OnItemClick(position,list.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }
    
    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener{
        void OnItemClick(int postion, Object object);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView showMsg;
        public MyViewHolder(View itemView) {
            super(itemView);
            showMsg = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
