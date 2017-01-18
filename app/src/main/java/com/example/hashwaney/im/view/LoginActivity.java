package com.example.hashwaney.im.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hashwaney.im.MainActivity;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.base.BaseActivity;
import com.example.hashwaney.im.presenter.LoginPresenter;
import com.example.hashwaney.im.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity
        extends BaseActivity
        implements TextView.OnEditorActionListener, ILoginView
{


    @InjectView(R.id.et_user)
    EditText        mEtUser;
    @InjectView(R.id.til_user)
    TextInputLayout mTilUser;
    @InjectView(R.id.et_pwd)
    EditText        mEtPwd;
    @InjectView(R.id.til_pwd)
    TextInputLayout mTilPwd;
    @InjectView(R.id.btn_login)
    Button          mBtnLogin;
    @InjectView(R.id.tv_new_user)
    TextView        mTvNewUser;
    private LoginPresenter mMLoginPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        //给ui绑定事件
        mEtPwd.setOnEditorActionListener(this);

        mMLoginPre = new LoginPresenter(this);
        /**
         *
         * 数据的回显
         */
        mEtPwd.setText(getPwd());
        mEtUser.setText(getUser());

    }

   @Override
   protected void onNewIntent(Intent intent) {
       super.onNewIntent(intent);

       mEtPwd.setText(getPwd());
       mEtUser.setText(getUser());

   }

    @OnClick({R.id.btn_login,
              R.id.tv_new_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_new_user:
                startActivity(RegistActivity.class, false);
                break;

        }
    }

    /**
     * 登录需要判断登录的状态-----p
     * 文本框中有用户输入的账号和密码,验证账号和密码的合法性
     * 验证之后进行数据的数据保存,保存到本地,下次回来可以进行数据的回显 sp进行数据的保存
     */
    private void login() {
        //  startActivity(MainActivity.class,true);
        String pwd = mEtPwd.getText()
                           .toString()
                           .trim();
        String username = mEtUser.getText()
                                 .toString()
                                 .trim();

        //首先对密码和账号进行为空判断,还有就是密码和账号的匹配规则

        //如果不合法的账户和密码,直接return 不让其走下去了
        if (!StringUtils.checkUserName(username)) {

            //不合理的话给用户友好提示,并且让用户可以进行修改,重新输入
            //新特性:TextInputLayout ,可以设置错误信息
            mTilUser.setError("用户名不合法");
            mTilUser.setErrorEnabled(true);
            //同时edittext需要重新请求焦点
            mEtUser.requestFocus(View.FOCUS_RIGHT);
            return;
        } else {
            //
            mTilUser.setErrorEnabled(false);
        }
        if (!StringUtils.checkPwd(pwd)) {
            //给用户友好提示,并且让用户可以进行修改,重新输入
            mTilPwd.setErrorEnabled(true);

            mTilPwd.setError("密码不合法");
            mEtPwd.requestFocus(View.FOCUS_RIGHT);

            return;
        } else {
            mTilPwd.setErrorEnabled(false);

        }

        //进度条的显示

        showDialog("正在登录中.....");
        //点击登录按钮,进行登录的校验
        mMLoginPre.onLogin(username, pwd);

    }

    //调取键盘,在输入密码完成之后,监听键盘事件,
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_pwd) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {

                login();
            }
            return true; //消费此事件
        }
        return false;
    }


    @Override
    public void loginCheck(String username, String pwd, boolean isLogin, String msg) {
       //隐藏进度条
        hideDialog();
        if (isLogin) {
            //成功登录,
            /**
             * 1. 保存用户,
             * 2,跳转主界面
             */

            saveUser(username, pwd);
            startActivity(MainActivity.class, true);

        } else {

            //登录失败
            Toast.makeText(this, "登录失败:" + msg, Toast.LENGTH_LONG)
                 .show();
        }
    }


}
