package com.example.hashwaney.im.presenter.impl;

import com.example.hashwaney.bean.User;
import com.example.hashwaney.im.presenter.IAddFriendPresenter;
import com.example.hashwaney.im.util.ThreadUtils;
import com.example.hashwaney.im.view.IAddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HashWaney on 2017/1/23.
 */

public class AddFriendPresenter
        implements IAddFriendPresenter
{
    private IAddFriendView mIAddFriendView;

    public AddFriendPresenter(IAddFriendView iAddFriendView) {mIAddFriendView = iAddFriendView;}

    @Override
    public void onSearchFriend(String contact) {
        //搜索好友功能放在我们在bmon服务器上,环信不提供
        /**
         * 环信只是即时通讯的消息通道。环信本身不提供用户体系，环信既不保存任何 APP 业务数据，也不保存任何 APP 的用户信息。比如说，你的 APP 是一个婚恋交友 APP，
         * 那么你的 APP 用户的头像、昵称、身高、体重、三围、电话号码等信息是保存在你自己的 APP 业务服务器上，这些信息不需要告诉环信，环信也不想知道。         */
        BmobQuery<User> query       = new BmobQuery<>();
        final String    currentUser = EMClient.getInstance()
                                              .getCurrentUser();
        query.addWhereStartsWith("username", contact)
             .addWhereNotEqualTo("username", currentUser)
             .findObjects(new FindListener<User>() {
                 @Override
                 public void done(List<User> list, BmobException e) {
                     if (e == null && list != null && list.size() > 0) {
                         //成功
                         //获取数据
                         mIAddFriendView.onSearchSuccess(currentUser, true, null, list);
                     } else {

                         //失败
                         if (e == null) {
                             //无异常.但是集合有问题,没有数据
                             mIAddFriendView.onSearchSuccess(currentUser, false, null, null);

                         } else {
                             //有异常
                             String msg = e.getMessage();
                             mIAddFriendView.onSearchSuccess(currentUser, false, msg, list);

                         }
                     }
                 }
             });


    }

    @Override
    public void onAddFriend(final String username) {
        //添加好友通过环信服务器管理
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance()
                            .contactManager()
                            .addContact(username, "想一起去马尔代夫");
                    //仅仅只是发送添加好友请求成功,对方同不同意是另外一回事
                    //通知view层
                    onAddFriendForResult(username, true,  null);


                } catch (HyphenateException e) {
                    e.printStackTrace();
                    onAddFriendForResult(username, false,e.getMessage());
                }

            }
        });


    }

    public void onAddFriendForResult(final String username,
                                     final boolean success,

                                     final String msg)
    {
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                mIAddFriendView.onAddFriendFormService(username, success,  msg);
            }
        });

    }
}
