package com.widget.jcdialog.widget.bottomMenu;

import android.app.Activity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;


import com.widget.jcdialog.R;
import com.widget.jcdialog.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * <dl>
 * <dt>BottomButtonMenu.java</dt>
 * <dd>Description:底部按钮菜单</dd>
 * </dl>
 *
 * @author liujc
 */
public class BottomButtonMenu extends BottomMenuWindow {

	private final static String TAG="BottomButtonMenu";
	private List<Button> dispButtons = new ArrayList<Button>();
	private List<View> dispViews = new ArrayList<View>();
	public LinearLayout dialogLayout;
	public LinearLayout cancelLayout;
	private List<String> contentItems;
	public Activity activity;
	public View contentView;

	public BottomButtonMenu(Activity activity) {
		super(activity);
		this.activity=activity;
		setContentView(R.layout.bottom_menu_dialog);
		((Button) getContentView().findViewById(R.id.bottom_menu_btn_cancel))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
		dialogLayout=(LinearLayout)getContentView().findViewById(R.id.bottom_menu_layout);
		cancelLayout=(LinearLayout)getContentView().findViewById(R.id.bottom_menu_cancel_layout);
		cancelLayout.setVisibility(View.VISIBLE);
//		initScrollView();
	}

	public void setData(List<String> contentItems){
		this.contentItems=contentItems;
		for(int i=0;i<contentItems.size();i++){
			Log.d(TAG, i + "=" + contentItems.get(i));
		}
	}
//	/**
//	 * 该方法根据需要显示的行数设置Dialog取消按钮以上的部分的高度
//	 * @param  显示行数
//	 */
//	public void initScrollViewHeight(int lineNum){
//		ScrollView view=(ScrollView) getContentView().findViewById(R.id.scroll_view);
//		int viewHeight= AndroidUtil.dp2px(activity, 48)*lineNum+ AndroidUtil.dp2px(activity, lineNum+1);
//		LayoutParams lp= view.getLayoutParams();
//		lp.height=viewHeight;
//	}
	public void addButtonFirst(final MenuClickedListener listener,
							   String textContent) {
		// 普通按钮1
		Button button = ((Button) getContentView().findViewById(R.id.bottom_menu_button1));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	public void addButtonSecond(final MenuClickedListener listener,
								String textContent) {
		// 普通按钮2
		Button button = ((Button) getContentView().findViewById(R.id.bottom_menu_button2));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	/**
	 * @author: liujc
	 * @Title: addButtonThird
	 * @Description:
	 * @param listener
	 * @param textContent
	 */
	public void addButtonThird(final MenuClickedListener listener,
							   String textContent) {
		// 普通按钮3
		Button button = ((Button) getContentView().findViewById(R.id.bottom_menu_button3));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	public void addButtonFourth(final MenuClickedListener listener,
								String textContent) {
		// 普通按钮4
		Button button = ((Button) getContentView().findViewById(R.id.bottom_menu_button4));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	public void addButtonChangeNumber(final MenuClickedListener listener,
									  String textContent) {
		// 红色按钮
		Button button = ((Button) getContentView().findViewById(R.id.bottom_menu_btn_exit));
		button.setText(textContent);
		button.setVisibility(View.VISIBLE);
		dispButtons.add(button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (listener != null) {
					listener.onMenuClicked();
				}
			}
		});
	}

	public void showDialog(){
		super.show();
	}
	public void show() {
		if (dispButtons.size() > 0) {
			for (int i = 0; i < dispButtons.size(); i++) {
				Button dispButton = dispButtons.get(i);
				View lineView = getContentView().findViewWithTag("line" + (String) dispButton.getTag());
				if (lineView != null) {
					lineView.setVisibility(View.VISIBLE);
				}
			}
			super.show();
		}
	}

	public void setSelected(int res) {
		((Button) getContentView().findViewById(res)).setPressed(true);
	}

	/**
	 * 根据给定的items为dialog画出相应的行
	 */
	public void generateBtnList(final DialogItemClickedListener listener) {
		if (null == contentItems) {
			Log.d(TAG, "contentItems=null ; return;");
			return;
		}
		for (int i = 0; i < contentItems.size(); i++) {
			Button btn = new Button(activity);
			setBtnAttribute(btn, i);
			btn.setVisibility(View.VISIBLE);
			btn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
					if(listener!=null){
						int index= Integer.parseInt(v.getTag().toString());
						listener.ondialogItemClicked(index);
					}
				}
			});
			View divider = new View(activity);
			setViewAttribute(divider,i);
			dispButtons.add(btn);
			dispViews.add(divider);
			dialogLayout.addView(btn);
			dialogLayout.addView(divider);
		}
	}

	/**
	 * 设置button 属性
	 *
	 * @param btn
	 * @param index
	 */
	public void setBtnAttribute(Button btn, int index) {
		if (btn == null) {
			return;
		}
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, ToolUtils.dp2px(activity, 48));
		btn.setTag(index+"");
		btn.setLayoutParams(lp);
		btn.setBackgroundResource(R.drawable.bg_bottom_menu_common_btn);
		btn.setTextColor(activity.getResources().getColor(
				R.color.bottom_menu_text_color));
		btn.setText(contentItems.get(index));
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		btn.setSingleLine(true);
	}

	/**
	 * 设置分隔线属性
	 *
	 * @param divider
	 * @param index
	 */
	public void setViewAttribute(View divider, int index) {
		divider.setBackgroundColor(activity.getResources().getColor(
				R.color.dialog_divider_color));
		divider.setTag("line" + index);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, ToolUtils.dp2px(activity, 1));
		int left = ToolUtils.dp2px(activity, 23);
		lp.setMargins(left, 0, left, 0);
		divider.setLayoutParams(lp);
//		divider.setTag("line"+index);
		divider.setVisibility(View.VISIBLE);

	}
	/**
	 * 如果通过数组创建dialog，则点击时，传递一个index以便识别
	 *
	 */
	public interface DialogItemClickedListener {
		public void ondialogItemClicked(int index);
	}
	public Button getButtonByIndex(int index){
		return dispButtons.get(index);
	}
	public Button getCancelButton(){
		return ((Button) getContentView().findViewById(R.id.bottom_menu_btn_cancel));
	}
}
