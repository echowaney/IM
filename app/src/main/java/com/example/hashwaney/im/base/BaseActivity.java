package com.example.hashwaney.im.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.hashwaney.im.util.Constans;

/**
 * Created by HashWaney on 2017/1/18.
 */

public class BaseActivity
        extends AppCompatActivity
{


    private SharedPreferences mSp;
    public ProgressDialog    mDialog;
    private Toast sToast;
    private Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化sp
        mSp = getSharedPreferences("config", Context.MODE_PRIVATE);
        //初始化进度条
        mDialog = new ProgressDialog(this);

    }

    //进行用户账号和密码的保存
    public void saveUser(String username, String pwd) {
        mSp.edit()
           .putString(Constans.KEY_USERNAME, username)
           .putString(Constans.KEY_PWD, pwd)
           .commit();


    }


    //  getPwd()getUser())
    //跳转界面,当页面跳转完之后,可以根据是否要将之前的activity给销毁掉,前提是跳转完成之后
    public void startActivity(Class clazz, boolean isFinish) {
       startActivity(clazz,isFinish,null);

    }
    //跳转页面 需要进行数据的传递
    public void startActivity(Class clazz,boolean isFinish,String contact){
        Intent intent = new Intent(this, clazz);
        if (!TextUtils.isEmpty(contact)){
            intent.putExtra("username",contact);
        }
        startActivity(intent);
        if (isFinish) {
            finish();
        }
    }

    //进行密码的获取 ---z
    public String getPwd() {

        return mSp.getString(Constans.KEY_PWD, "");

    }

    //进行账号的获取
    public String getUser() {

        return mSp.getString(Constans.KEY_USERNAME, "");
    }

    //进度条的显示
    public void showDialog(String msg) {

        mDialog.show();
        mDialog.setMessage(msg);

    }

    //进度条的隐藏
    public void hideDialog() {
        mDialog.hide();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //界面销毁的时候进行界面的销毁
      mDialog.dismiss();
    }
    public void showToast(String msg){
        if (mToast==null){
            mToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        mToast.setText(msg);
        mToast.show();

    }

}
