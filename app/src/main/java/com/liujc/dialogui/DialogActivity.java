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
import com.widget.jcdialog.widget.CommonDialog;
import com.widget.jcdialog.widget.DateSelectorWheelView;
import com.widget.jcdialog.widget.JDAddressSeletor.BottomDialog;
import com.widget.jcdialog.widget.JDAddressSeletor.DefaultAddressProvider;
import com.widget.jcdialog.widget.JDAddressSeletor.OnAddressSelectedListener;
import com.widget.jcdialog.widget.PopuWindowView;
import com.widget.jcdialog.widget.pickerview.OptionsPickerView;
import com.widget.jcdialog.widget.pickerview.TimePickerView;
import com.widget.jcdialog.widget.pickerview.model.CityModel;
import com.widget.jcdialog.widget.pickerview.model.DistrictModel;
import com.widget.jcdialog.widget.pickerview.model.ProvinceModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.widget.jcdialog.utils.ToastUitl.showToast;

/**
 * 类名称：DialogActivity
 * 创建者：Create by liujc
 * 创建时间：Create on 2016/12/29 15:53
 * 描述：TODO
 * 最近修改时间：2016/12/29 15:53
 * 修改人：Modify by liujc
 */
public class DialogActivity extends AppCompatActivity implements View.OnClickListener{
    Context mContext;
    Activity mActivity;
    String msg = "别总是来日方长，这世上挥手之间的都是人走茶凉。";
    @Bind(R.id.btn_popu)
    Button btnPopu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
    }

    @OnClick({R.id.btn_loading_dialog,R.id.btn_dialog2,R.id.btn_select_dialog,R.id.btn_time_dialog,R.id.btn_address_dialog,
            R.id.btn_custom_bottom_alert, R.id.btn_popu, R.id.btn_select_ymd, R.id.btn_select_ymdhm, R.id.btn_select_ymdhms, R.id.btn_dialog, R.id.btn_loading_vertical, R.id.btn_loading_horizontal, R.id.btn_loading_vertical_gray, R.id.btn_loading_horizontal_gray, R.id.btn_md_loading_vertical, R.id.btn_md_loading_horizontal, R.id.btn_md_alert, R.id.btn_tie_alert, R.id.btn_alert_horizontal,
            R.id.btn_alert_vertical, R.id.btn_bottom_sheet_cancel, R.id.btn_center_sheet, R.id.btn_alert_input,
            R.id.btn_alert_multichoose, R.id.btn_alert_singlechoose, R.id.btn_md_bottom_vertical, R.id.btn_md_bottom_horizontal,
            R.id.btn_custom_alert,R.id.btn_select_address,R.id.btn_select_time,R.id.btn_select_time_ymdhms,R.id.btn_select_address_picker})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_loading_dialog:
                break;
            case R.id.btn_dialog2:
                break;
            case R.id.btn_select_dialog:
                break;
            case R.id.btn_time_dialog:
                break;
            case R.id.btn_address_dialog:
                break;
            case R.id.btn_popu:
                final PopuWindowView popuWindowView = new PopuWindowView(mContext, LinearLayout.LayoutParams.WRAP_CONTENT);
                popuWindowView.initPupoData(new TdataListener() {
                    @Override
                    public void initPupoData(List<PopuBean> lists) {
                        for (int i = 0; i < 5; i++) {
                            PopuBean popu = new PopuBean();
                            popu.setTitle("item"+i);
                            popu.setId(i);
                            lists.add(popu);
                        }
                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position) {
                        showToast(popuWindowView.getTitle(position));
                        popuWindowView.dismiss();
                    }
                });
                popuWindowView.showing(btnPopu);
                break;
            case R.id.btn_select_ymd: {
                DialogUtils.showDatePick(mActivity, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        showToast(selectedDate);
                    }
                }).show();
            }
            break;
            case R.id.btn_select_ymdhm: {
                DialogUtils.showDatePick(mActivity, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMM, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        showToast(selectedDate);
                    }
                }).show();
            }
            break;
            case R.id.btn_select_ymdhms: {
                DialogUtils.showDatePick(mActivity, Gravity.BOTTOM, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMMSS, 0, new DialogUIDateTimeSaveListener() {
                    @Override
                    public void onSaveSelectedDate(int tag, String selectedDate) {
                        showToast(selectedDate);
                    }
                }).show();
            }
            break;
            case R.id.btn_select_time_ymdhms:
                DialogUtils.showTimePickView(mActivity,"选择日期",TimePickerView.Type.ALL,new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date) {
                    ToastUitl.showToast(getTime(date));
                }
            });
            break;
            case R.id.btn_select_address_picker:
                final DefaultAddressProvider addressProvider = new DefaultAddressProvider(mActivity);
                DialogUtils.showAddrPickView(mActivity, "选择地址",addressProvider, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3) {
                        String tx = addressProvider.provideProvince().get(options1)
                                + addressProvider.provideCities().get(options1).get(option2)
                                + addressProvider.provideCounties().get(options1).get(option2).get(options3).getPickerViewText()
                                +addressProvider.provideZipCode().get(addressProvider.provideCounties().get(options1).get(option2).get(options3).getPickerViewText());
                        ToastUitl.showToast(tx);
                    }
                });
            break;
            case R.id.btn_custom_alert:
                View rootView = View.inflate(mActivity, R.layout.custom_dialog_layout, null);
                final Dialog dialog = DialogUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
                rootView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtils.dismiss(dialog);
                    }
                });
                break;
            case R.id.btn_custom_bottom_alert:
                View rootViewB = View.inflate(mActivity, R.layout.custom_dialog_layout, null);
                DialogUtils.showCustomBottomAlert(this, rootViewB).show();
                break;
            case R.id.btn_loading_vertical:
                DialogUtils.showLoadingVertical(this, "加载中...").show();
                break;
            case R.id.btn_loading_horizontal:
                DialogUtils.showLoadingHorizontal(this, "加载中...").show();
                break;
            case R.id.btn_loading_vertical_gray:
                DialogUtils.showLoadingVertical(this, "加载中...", false).show();
                break;
            case R.id.btn_loading_horizontal_gray:
                DialogUtils.showLoadingHorizontal(this, "加载中...", false).show();
                break;
            case R.id.btn_md_loading_vertical:
                DialogUtils.showMdLoadingVertical(this, "加载中...").show();
                break;
            case R.id.btn_md_loading_horizontal:
                DialogUtils.showMdLoadingHorizontal(this, "加载中...").show();
                break;
            case R.id.btn_md_alert:
                DialogUtils.showMdAlert(mActivity, "标题", msg, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        showToast("onPositive");
                    }

                    @Override
                    public void onNegative() {
                        showToast("onNegative");
                    }

                }).show();
                break;
            case R.id.btn_dialog:
                DialogUtils.showDialogTie(this, msg).show();
                break;
            case R.id.btn_tie_alert:
                DialogUtils.showAlert(mActivity, "标题", msg, "", "", "确定", "", true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        showToast("onPositive");
                    }

                    @Override
                    public void onNegative() {
                        showToast("onNegative");
                    }

                }).show();
                break;
            case R.id.btn_alert_horizontal:
                DialogUtils.showAlert(mActivity, "标题", msg, "", "", "确定", "取消", false, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        showToast("onPositive");
                    }

                    @Override
                    public void onNegative() {
                        showToast("onNegative");
                    }

                }).show();
                break;
            case R.id.btn_alert_vertical:
                DialogUtils.showAlert(this, "标题", msg, "", "", "确定", "取消", true, new DialogUIListener() {
                    @Override
                    public void onPositive() {
                        showToast("onPositive");
                    }

                    @Override
                    public void onNegative() {
                        showToast("onNegative");
                    }

                }).show();
                break;
            case R.id.btn_alert_input:
                DialogUtils.showAlert(mActivity, "登录", "", "请输入用户名", "请输入密码", "登录", "取消", false, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }

                    @Override
                    public void onNegative() {

                    }

                    @Override
                    public void onGetInput(CharSequence input1, CharSequence input2) {
                        super.onGetInput(input1, input2);
                        showToast("input1:" + input1 + "--input2:" + input2);
                    }
                }).show();
                break;
            case R.id.btn_alert_multichoose:
                String[] words = new String[]{"1", "2", "3"};
                boolean[] choseDefault = new boolean[]{false, false, false};
                DialogUtils.showMdMultiChoose(mActivity, "标题", words, choseDefault, new DialogUIListener() {
                    @Override
                    public void onPositive() {

                    }

                    @Override
                    public void onNegative() {

                    }
                }).show();
                break;
            case R.id.btn_alert_singlechoose:
                String[] words2 = new String[]{"1", "2", "3"};
                DialogUtils.showSingleChoose(mActivity, "单选", 0, words2, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        showToast(text + "--" + position);
                    }
                }).show();
                break;
            case R.id.btn_center_sheet: {
                List<TieBean> strings = new ArrayList<TieBean>();
                strings.add(new TieBean("1"));
                strings.add(new TieBean("2"));
                strings.add(new TieBean("3"));
                DialogUtils.showCenterSheet(mActivity, strings, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        showToast(text);
                    }

                    @Override
                    public void onBottomBtnClick() {
                        showToast("onItemClick");
                    }
                }).show();
            }
            break;
            case R.id.btn_bottom_sheet_cancel: {
                List<TieBean> strings = new ArrayList<TieBean>();
                strings.add(new TieBean("1"));
                strings.add(new TieBean("2"));
                strings.add(new TieBean("3"));
                DialogUtils.showMdBottomSheet(mActivity, true, "", strings, "取消", 0, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        showToast(text + "---" + position);
                    }
                }).show();
//                showBottomMenu();
            }
            break;
            case R.id.btn_md_bottom_vertical:
                List<TieBean> datas2 = new ArrayList<TieBean>();
                datas2.add(new TieBean("1"));
                datas2.add(new TieBean("2"));
                datas2.add(new TieBean("3"));
                datas2.add(new TieBean("4"));
                datas2.add(new TieBean("5"));
                datas2.add(new TieBean("6"));
                TieAdapter adapter = new TieAdapter(mContext, datas2);
                BuildBean buildBean = DialogUtils.showMdBottomSheet(mActivity, true, "", datas2, "", 0, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        showToast(text + "---" + position);
                    }
                });
                buildBean.mAdapter = adapter;
                buildBean.show();
                break;
            case R.id.btn_md_bottom_horizontal:
                List<TieBean> datas3 = new ArrayList<TieBean>();
                datas3.add(new TieBean("1"));
                datas3.add(new TieBean("2"));
                datas3.add(new TieBean("3"));
                datas3.add(new TieBean("4"));
                datas3.add(new TieBean("5"));
                datas3.add(new TieBean("6"));
                DialogUtils.showMdBottomSheet(mActivity, false, "标题", datas3, "", 4, new DialogUIItemListener() {
                    @Override
                    public void onItemClick(CharSequence text, int position) {
                        showToast(text + "---" + position);
                    }
                }).show();
                break;
            case R.id.btn_select_address:
//                jumpTarget(JDAddressSelectActivity.class);
                final BottomDialog dialog1 = DialogUtils.showAddressDialog(mActivity);
                dialog1.setOnAddressSelectedListener(new OnAddressSelectedListener() {
                    @Override
                    public void onAddressSelected(ProvinceModel province, CityModel city, DistrictModel county) {
                        String s =(province == null ? "" : province.getName()) +
                                (city == null ? "" : "\n" + city.getName()) +
                                (county == null ? "" : "\n" + county.getName());
                        showToast(s);
                        dialog1.dismiss();
                    }
                });
                break;
            case R.id.btn_select_time:
//                jumpTarget(PickerViewActivity.class);
                break;

        }
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    private void jumpTarget(Class target){
        Intent intent = new Intent(mContext,target);
        startActivity(intent);
    }

    private void showBottomMenu() {
        final CommonDialog commonDialog = new CommonDialog(DialogActivity.this,DialogActivity.class.getSimpleName(),1);
        commonDialog.setMessage("hello");
        commonDialog.setCancleButton(new CommonDialog.BtnClickedListener() {
            @Override
            public void onBtnClicked() {
                commonDialog.dismiss();
            }
        },"取消");
        commonDialog.setPositiveButton(new CommonDialog.BtnClickedListener() {
            @Override
            public void onBtnClicked() {
                commonDialog.dismiss();
            }
        },"queding");
        commonDialog.showDialog();
    }
}
