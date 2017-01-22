package com.example.hashwaney.im.listener;

import com.example.hashwaney.im.util.ThreadUtils;
import com.hyphenate.EMCallBack;

/**
 * Created by HashWaney on 2017/1/22.
 */

public abstract class HMCallbackListener
        implements EMCallBack
{
    //定义两个方法,包装这个接口,使得这个接口能够在主线程中更新ui
    public abstract void onMainSuccess();

    public abstract void onMaiError(int i,String s);


    @Override
    public void onSuccess() {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                onMainSuccess();
            }
        });

    }

    @Override
    public void onError(final int i, final String s) {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
               onMaiError(i,s);
            }
        });
    }

    @Override
    public void onProgress(int i, String s) {

    }
}
