package com.example.hashwaney.im.presenter.impl;

import android.os.Handler;

import com.example.hashwaney.im.listener.HMCallbackListener;
import com.example.hashwaney.im.presenter.ILoginPresenter;
import com.example.hashwaney.im.view.ILoginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by HashWaney on 2017/1/18.
 */

public class LoginPresenter
        implements ILoginPresenter
{
    private ILoginView mILoginView;

    public LoginPresenter(ILoginView iLoginView) {mILoginView = iLoginView;}

    private Handler mHandler =new Handler();

    @Override
    public void onLogin(final String username, final String pwd) {
        //环信服务器校验用户登录账号和密码
        EMClient.getInstance().login(username, pwd, new HMCallbackListener() {
            @Override
            public void onMainSuccess() {
                mILoginView.loginCheck(username,pwd,true,null);
            }

            @Override
            public void onMaiError(int i, String s) {
                mILoginView.loginCheck(username,pwd,false,s);
            }
        });

    }
}
