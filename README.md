# DialogUi
关于toast、等待框、对话框、选择框、下拉刷新等工具的封装。

在代码中build中集成`compile 'com.liujc.util:jcdialog:1.0.1'`.

## 主界面
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/功能页.png)

## Toast 工具   主要设置toast的显示位置及自定义toast的显示布局
在使用ToastUitl的时候需要调用DialogUtils.init(getApplicationContext())传入上下文;
 1. 顶部toast
 ToastUitl.showToastTop("顶部的Toast");
 2. 中部toast
 ToastUitl.showToastCenter("中部的Toast");
 3. 底部toast
 ToastUitl.showToast("默认的Toast");
 4. 显示带图片的toast
 ToastUitl.showToastWithImg("image Toast",R.drawable.ic_success);
 ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/toast.png)
 5. 自定义布局和显示位置的toast
 ToastUitl.showToastLayout(toastLayout, gravity)
 
## 对话框工具
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog.png)
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog2.png)
![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/dialog3.png)
  1. popuwindow弹出框
  2. 各种等待框
  3. 各种对话框
  4. 各种列表选择框
  5. 时间选择框
  
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/date1.png)
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/date2.png)
  
  6. 地址联动选择框
  
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/address.png)
  ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/jdaddress.png)
  

## 内嵌加载等待框
## 支付键盘
一般键盘
 
 ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/一般键盘.png)
 
 支付键盘
 
 ![image](https://github.com/liujinchao/DialogUi/blob/master/screenshot/支付键盘.png)
