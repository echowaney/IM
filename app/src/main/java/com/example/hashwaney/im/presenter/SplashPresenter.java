package com.example.hashwaney.im.presenter;

import com.example.hashwaney.im.view.ISplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by HashWaney on 2017/1/17.
 */

public class SplashPresenter
        implements ISplashPresenter
{
    private ISplashView mISplashView;

    public SplashPresenter(ISplashView ISplashView) {
        mISplashView = ISplashView;
    }

    //此逻辑是进行登陆判断的处理，具体的回调是有view来执行的，至于为什么传递一个boolean类型的参数
    //就是因为，在SplashActivity中进行ui真正处理是时的区别

    @Override
    public void onCheckLogin() {
        if (EMClient.getInstance().isConnected()&&EMClient.getInstance().isLoggedInBefore()){
            //登陆--需要回调函数
            mISplashView.onCheckLogined(true);

        }else{
            //未登录
            mISplashView.onCheckLogined(false);

        }
    }
}
