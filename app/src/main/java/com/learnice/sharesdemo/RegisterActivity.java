package com.learnice.sharesdemo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.learnice.sharesdemo.MyServerHttp.IMyServerDataResult;
import com.learnice.sharesdemo.MyServerHttp.MyServerHttpRequestImpl;
import com.learnice.sharesdemo.MyServerHttp.MyServerHttpResponseStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements IMyServerDataResult {

    @BindView(R.id.register_tv_account)
    ImageView registerTvAccount;
    @BindView(R.id.register_et_username)
    EditText registerEtUsername;
    @BindView(R.id.register_usernamelayout)
    RelativeLayout registerUsernamelayout;
    @BindView(R.id.register_tv_secret)
    ImageView registerTvSecret;
    @BindView(R.id.register_et_password)
    EditText registerEtPassword;
    @BindView(R.id.register_passwordlayout)
    RelativeLayout registerPasswordlayout;
    @BindView(R.id.register_tv_secret2)
    ImageView registerTvSecret2;
    @BindView(R.id.register_et_password2)
    EditText registerEtPassword2;
    @BindView(R.id.register_passwordlayout2)
    RelativeLayout registerPasswordlayout2;
    @BindView(R.id.main_btn_register)
    Button mainBtnRegister;
    @BindView(R.id.register_progress)
    ProgressBar registerProgress;
    @BindView(R.id.register_progress_layout)
    RelativeLayout registerProgressLayout;
    netReceiver netReceiver;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setAlertDialog();
        isNoNetwork();
        netReceiver=new netReceiver();
        IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(netReceiver,intentFilter);
    }

    @OnClick(R.id.main_btn_register)
    public void onClick() {
        String name=registerEtUsername.getText().toString().trim();
        String pass1=registerEtPassword.getText().toString().trim();
        String pass2=registerEtPassword2.getText().toString().trim();
        if (name.length()==0||name.equals("null")){
            Toast.makeText(this,"用户名为空",Toast.LENGTH_SHORT).show();
        }
        else if (!pass1.equals(pass2)){
            Toast.makeText(this,"输入的两次密码不一致请重新输入",Toast.LENGTH_SHORT).show();
        }
        else{
            new MyServerHttpRequestImpl().checkUser("101",name,pass1,new MyServerHttpResponseStatus(this));
        }
    }

    @Override
    public void resultString(Object data) {
        if (data.toString().trim().equals("1")){
            Toast.makeText(this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void resultSayList(String list) {

    }
    public void setAlertDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        alertDialog= builder.setTitle("未联网")
                .setMessage("请连接网络")
                .setCancelable(false)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create();
    }
    public void isNoNetwork(){
        ConnectivityManager connectivityManager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null&&networkInfo.isAvailable()){
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
        else {
            alertDialog.show();
        }
    }
    public class netReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isNoNetwork();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netReceiver);
    }
}