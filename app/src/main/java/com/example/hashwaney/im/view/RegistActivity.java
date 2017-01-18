package com.example.hashwaney.im.view;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.base.BaseActivity;
import com.example.hashwaney.im.presenter.RegistPresenter;
import com.example.hashwaney.im.util.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegistActivity
        extends BaseActivity
        implements IRegistView
{

    @InjectView(R.id.et_regist_user)
    EditText        mEtRegistUser;
    @InjectView(R.id.til_regist_user)
    TextInputLayout mTilRegistUser;
    @InjectView(R.id.et_regist_pwd)
    EditText        mEtRegistPwd;
    @InjectView(R.id.til_regist_pwd)
    TextInputLayout mTilRegistPwd;
    @InjectView(R.id.btn_regist)
    Button          mBtnRegist;
    private RegistPresenter mMRegistPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        //初始化bombsdk
        mMRegistPre = new RegistPresenter(this);

    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        registed();

    }

    private void registed() {
        String registPwd = mEtRegistPwd.getText()
                                  .toString()
                                  .trim();
        String registUser = mEtRegistUser.getText()
                                   .toString()
                                   .trim();

        if (!StringUtils.checkPwd(registPwd)){
            mTilRegistPwd.setErrorEnabled(true);
            mTilRegistPwd.setError("密码格式不正确");
            mEtRegistPwd.requestFocus(View.FOCUS_RIGHT);//这样做是为了让用户可以获取文本输入框的焦点并且焦点在右侧,便于用户重新输入
            return;
        }else{
            mTilRegistPwd.setErrorEnabled(false);
        }
        if (!StringUtils.checkUserName(registUser)){
            mTilRegistUser.setErrorEnabled(true);
            mTilRegistUser.setError("该账号已经被注册了");
            mEtRegistUser.requestFocus(View.FOCUS_RIGHT);
            return;

        }else {
            mTilRegistUser.setErrorEnabled(true);
        }
        //显示进度条
        showDialog("正在注册中...");

        //此时可以检验注册用户的信息
        mMRegistPre.onRegist(registUser,registPwd);


    }
    //注册之后的回调
    @Override
    public void regist(String username, String pwd, boolean isRegistForOk, String registCallBack) {
        //隐藏进度条,当回调就有结果了,因此就需要将进度条隐藏
        hideDialog();
        if (isRegistForOk){

            //1.保存数据
            saveUser(username,pwd);

            //2.跳转到登录界面,
            startActivity(LoginActivity.class,true);

        }else {
            //注册失败,弹出一个吐司
            Toast.makeText(this, "注册失败"+registCallBack, Toast.LENGTH_SHORT)
                 .show();


        }


    }
}
