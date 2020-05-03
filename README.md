# DialogUi
关于toast、等待框、对话框、选择框、下拉刷新等工具的封装。

在代码中build中集成`compile 'com.liujc.util:jcdialog:1.0.1'`.

## 主界面
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/功能页.png)

## Toast 工具   主要设置toast的显示位置及自定义toast的显示布局
在使用ToastUtil的时候需要调用XDialog.init(getApplicationContext())传入上下文;
 1. 顶部toast

 ToastUtil.showToastTop("顶部的Toast");

 2. 中部toast

 ToastUtil.showToastCenter("中部的Toast");

 3. 底部toast

 ToastUtil.showToast("默认的Toast");
 4. 显示带图片的toast

 ToastUtil.showToastWithImg("image Toast",R.drawable.ic_success);

 5. 自定义布局和显示位置的toast

 ToastUtil.showToastLayout(toastLayout, gravity)

  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/toast.png)
 
## 对话框工具
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog.png)
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog2.png)
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog3.png)
  1. popuwindow弹出框

  ```
  XDialog.showPopupWindow(mContext, btnPopu,new TdataListener() {
          @Override
          public void initPopupData(List<PopuBean> lists) {
              for (int i = 0; i < 5; i++) {
                  PopuBean popu = new PopuBean();
                  popu.setTitle("item"+i);
                  popu.setId(i);
                  lists.add(popu);
              }
          }

          @Override
          public void onItemClick(int position, PopupWindowView popupWindowView) {
              showToast("标题："+popupWindowView.getTitle(position));
              popupWindowView.dismiss();
          }
      });
  ```
  2. 各种等待框

  ```
  XDialog.showLoadingVertical(this, "加载中...", false).show();
  XDialog.showLoadingHorizontal(this, "加载中...", false).show();
  XDialog.showMdLoadingVertical(this, "加载中...").show();
  XDialog.showMdLoadingHorizontal(this, "加载中...").show();
  ```
  3. 各种对话框

  ```
  XDialog.showMdAlert(mActivity, "标题", msg, new DialogUIListener() {
          @Override
          public void onPositive() {
              showToast("onPositive");
          }

          @Override
          public void onNegative() {
              showToast("onNegative");
          }

      }).show();
  ```
  4. 各种列表选择框

  ```
  //单选
  String[] words2 = new String[]{"1", "2", "3"};
  XDialog.showSingleChoose(mActivity, "单选", 0, words2, new DialogUIItemListener() {
      @Override
      public void onItemClick(CharSequence text, int position) {
          showToast(text + "--" + position);
      }
  }).show();

  //多选
  String[] words = new String[]{"1", "2", "3"};
  boolean[] choseDefault = new boolean[]{false, false, false};
  XDialog.showMdMultiChoose(mActivity, "标题", words, choseDefault, new DialogUIListener() {
      @Override
      public void onPositive() {

      }

      @Override
      public void onNegative() {

      }
  }).show();
  ```
  5. 时间选择框

  ```
  XDialog.showDatePick(mActivity, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMM, 0, new DialogUIDateTimeSaveListener() {
          @Override
          public void onSaveSelectedDate(int tag, String selectedDate) {
              showToast(selectedDate);
          }
      }).show();

  或者

  XDialog.showTimePickView(mActivity,"选择日期",TimePickerView.Type.ALL,new TimePickerView.OnTimeSelectListener() {
         @Override
         public void onTimeSelect(Date date) {
             ToastUtil.showToast(getTime(date));
         }
     });
  ```
  
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/date1.png)
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/date2.png)
  
  6. 地址联动选择框
  
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/address.png)
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/jdaddress.png)

  ```
  DefaultAddressProvider addressProvider = new DefaultAddressProvider(mActivity);
  XDialog.showAddrPickView(mActivity, "选择地址",addressProvider, new OptionsPickerView.OnOptionsSelectListener() {
      @Override
      public void onOptionsSelect(int options1, int option2, int options3) {
          String tx = addressProvider.provideProvince().get(options1)
                  + addressProvider.provideCities().get(options1).get(option2)
                  + addressProvider.provideCounties().get(options1).get(option2).get(options3).getPickerViewText()
                  +addressProvider.provideZipCode().get(addressProvider.provideCounties().get(options1).get(option2).get(options3).getPickerViewText());
          ToastUtil.showToast(tx);
      }
  });

  // 或
  BottomDialog dialog1 = XDialog.showAddressDialog(mActivity);
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
  ```

## 内嵌加载等待框

## 支付键盘
一般键盘
 
 ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/一般键盘.png)
 
 支付键盘
 
 ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/支付键盘.png)

 ## 升级提示弹框

  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/升级弹框.png)
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/升级弹框下载中.png)

  ```
    UpdateAppBean updateAppBean = new UpdateAppBean.Builder()
          .setUpdate(true)
          .setNewVersion("2.0.20")
          .setApkFileUrl("http://106.54.169.177/file/app_dialog.apk")
          .setUpdateDesc("1，赏个脸支持(star)一下。\r\n2，用扯淡的方式，分享技术的内涵。\r\n3，谈的是技术，更是我们的人生。")
          .setApkSize("5.20M")
          .setNewMd5("B5A7C226C5D10C3734D2090282DF3FBD")
          .setForceUpdate(false)
          .create();
    UpdateAppManager updateAppManager = new UpdateAppManager.Builder(this)
    //                .showIgnoreVersion()
          .handleException(new ExceptionHandler() {
              @Override
              public void onException(Exception e) {
                  ToastUtil.showToast(e.getLocalizedMessage());
              }
          })
          .dismissNotificationProgress()
          // 监听更新提示框相关事件
          .setUpdateDialogFragmentListener(new IUpdateDialogFragmentListener() {
              @Override
              public void startDownloadApk(UpdateAppBean updateApp, final IDownloadCallBack downloadCallback) {
                  ToastUtil.showToast("开始下载");
                  doDownLoadAction(downloadCallback);
              }

              @Override
              public void cancelDownloadApk(UpdateAppBean updateApp) {
                  if(updateApp.isForceUpdate()){
                      // 处理强制更新，被用户cancel的情况
                  }
                  ToastUtil.showToast("取消更新");
              }
          })
          .create();
    XDialog.showUpdateDialog(updateAppManager, updateAppBean, new IUpdateCallback() {
      @Override
      public void hasNewApp(UpdateDialogFragment updateDialogFragment) {
          ToastUtil.showToast("有更新包");
      }

      @Override
      public void noNewApp() {
          ToastUtil.showToast("当前已是最新包");
      }
    });
  ```

