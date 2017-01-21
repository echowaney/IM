package com.example.hashwaney.im.presenter.impl;

import android.os.Handler;

import com.example.hashwaney.bean.User;
import com.example.hashwaney.im.presenter.IRegistPresenter;
import com.example.hashwaney.im.view.IRegistView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by HashWaney on 2017/1/18.
 */

public class RegistPresenter implements IRegistPresenter {
    private static final String TAG = "registpresenter";
    private IRegistView mIRegistView;

    public RegistPresenter(IRegistView IRegistView) {
        mIRegistView = IRegistView;
    }
    private Handler mHandler =new Handler();

    @Override
    public void onRegist(final String username, final String pwd) {
        /**
         * 1. 先注册Bmob云数据库
         * 2. 如果Bmob成功了再去注册环信平台
         * 3. 如果Bmob成功了，环信失败了，则再去把Bmob上的数据给删除掉
         */

        final User user =new User();
        user.setPwd(pwd);
        user.setUsername(username);
        user.save(new SaveListener<String>() {
          @Override
             public void done(String s, BmobException e) {
              //如果没有出现异常,就说明注册Bomb成功
              if (e==null){
                  //去注册环信,开启异步任务
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              //注册环信成功
                              EMClient.getInstance().createAccount(username,pwd);
                              //回调给view层--主线程
                              mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {
                                    mIRegistView.regist(username,pwd,true,null);
                                  }
                              });


                          } catch (final HyphenateException e1) {
                              e1.printStackTrace();
                              //移除bomb上注册的用户,保持和环信用户一致
                              user.delete();
                              mHandler.post(new Runnable() {
                                  @Override
                                  public void run() {
                                    mIRegistView.regist(username,pwd,false,e1.getMessage());
                                  }
                              });
                          }
                      }
                  }).start();



              }else {

                  //说明注册失败,将失败原因告诉用户
                  mIRegistView.regist(username,pwd,false,e.getMessage());


              }


          }
      });






    }
}
