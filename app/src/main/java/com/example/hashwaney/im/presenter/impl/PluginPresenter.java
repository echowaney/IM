package com.example.hashwaney.im.presenter.impl;

import com.example.hashwaney.im.listener.HMCallbackListener;
import com.example.hashwaney.im.presenter.IPluginPresenter;
import com.example.hashwaney.im.view.IPluginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by HashWaney on 2017/1/22.
 */

public class PluginPresenter
        implements IPluginPresenter
{

    private IPluginView mIPluginView;

    public PluginPresenter(IPluginView iPluginView) {
        mIPluginView = iPluginView;
    }


    @Override
    public void logOut() {
        /**
         * 参数1.解除所有的推送,为true,就不在接收推送,为false,退出后仍然接收推送
         * 参数2.退出的回调,这里是在子线程中执行的,
         */
//        EMClient.getInstance().logout(true, new EMCallBack() {
//            @Override
//            public void onSuccess() {
//                ThreadUtils.runOnMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//
//            }
//
//            @Override
//            public void onProgress(int i, String s) {
//
//            }
//        });
        EMClient.getInstance()
                .logout(true, new HMCallbackListener() {
                    @Override
                    public void onMainSuccess() {
                        mIPluginView.onLogout(true,null);
                    }

                    @Override
                    public void onMaiError(int i, String s) {
                        mIPluginView.onLogout(false,s);

                    }
                });

    }
}
