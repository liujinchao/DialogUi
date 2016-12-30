package com.widget.jcdialog.adapter;

/**
 * 类名称：StrericWheelAdapter
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/26 16:22
 * 描述：自定义wheel适配器
 */
public class StrericWheelAdapter implements WheelAdapter {
	
	private String[] strContents;
	/**
	 * 构造方法
	 * @param strContents
	 */
	public StrericWheelAdapter(String[] strContents){
		this.strContents=strContents;
	}
	
	
	public String[] getStrContents() {
		return strContents;
	}


	public void setStrContents(String[] strContents) {
		this.strContents = strContents;
	}


	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return strContents[index];
		}
		return null;
	}
	
	public int getItemsCount() {
		return strContents.length;
	}
	/**
	 * 设置最大的宽度
	 */
	public int getMaximumLength() {
		int maxLen=7;
		return maxLen;
	}


	@Override
	public String getCurrentId(int index) {
		// TODO Auto-generated method stub
		return null;
	}
}
