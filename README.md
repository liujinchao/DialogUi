# DialogUi
关于toast、等待框、对话框、选择框、下拉刷新等工具的封装
## Toast 工具   主要设置toast的显示位置及自定义toast的显示布局
 1. 顶部toast
 ToastUitl.showToastTop("顶部的Toast");
 2. 中部toast
 ToastUitl.showToastCenter("中部的Toast");
 3. 底部toast
 ToastUitl.showToast("默认的Toast");
 4. 显示带图片的toast
 ToastUitl.showToastWithImg("image Toast",R.drawable.ic_success);
 5. 自定义布局和显示位置的toast
 ToastUitl.showToastLayout(toastLayout, gravity)
 
## 对话框工具
  1. popuwindow弹出框
  2. 各种等待框
  3. 各种对话框
  4. 各种列表选择框
  5. 时间选择框
  6. 地址联动选择框
  
## 基于recycleview的下拉刷新 上拉加载列表

## 内嵌加载等待框
