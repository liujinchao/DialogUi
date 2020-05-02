package com.liujc.dialogui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.liujc.dialogui.R;
import com.widget.jcdialog.XDialog;
import com.widget.jcdialog.widget.pswKeyBoard.NormalKeyBoardActivity;
import com.widget.jcdialog.widget.pswKeyBoard.OnPasswordInputFinish;
import com.widget.jcdialog.widget.pswKeyBoard.widget.PopEnterPassword;

public class KeyBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);
    }

    public void toNormalKeyBoard(View view) {
        startActivity(new Intent(this, NormalKeyBoardActivity.class));
    }

    public void toPayKeyBoard(View view) {
        final PopEnterPassword popEnterPassword = XDialog.showPayKeyBoard(this,view);
        popEnterPassword.getPwdView().setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String password) {
                popEnterPassword.dismiss();
                Toast.makeText(KeyBoardActivity.this, "支付成功，密码为：" + password, Toast.LENGTH_SHORT).show();
            }
        });
//        PopEnterPassword popEnterPassword = new PopEnterPassword(this);
//
//        // 显示窗口
//        popEnterPassword.showAtLocation(view,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
    }
}
