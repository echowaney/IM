package com.example.hashwaney.im.presenter;

import android.os.Handler;

import com.example.hashwaney.im.view.ILoginView;
import com.hyphenate.EMCallBack;
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
        EMClient.getInstance().login(username, pwd, new EMCallBack() {
            @Override
            public void onSuccess() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mILoginView.loginCheck(username,pwd,true,null);
                            }
                        });


                    }
                }).start();
                //校验通过,成功登录

            }

            @Override
            public void onError(int i, final String s) {
                //校验失败,登录失败
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mILoginView.loginCheck(username,pwd,false,s);
                            }
                        });

                    }
                }).start();


            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }
}
