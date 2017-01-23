package com.example.hashwaney.im.presenter.impl;

import android.util.Log;

import com.example.hashwaney.im.db.DBUtils;
import com.example.hashwaney.im.presenter.IContactFragmetPresenter;
import com.example.hashwaney.im.util.ThreadUtils;
import com.example.hashwaney.im.view.IContactFragmentView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactFragmentPresenter
        implements IContactFragmetPresenter
{
    private IContactFragmentView mIContactFragmentView; //接受一个消息,是否更新成功,
    private List<String>                  contactLists =new ArrayList<>();

    public ContactFragmentPresenter(IContactFragmentView iContactFragmentView) {
        mIContactFragmentView = iContactFragmentView;
    }

    /**
     * /88888888888 策略1 888888888888/
     * 先从环信的服务器去获取数据,
     * 然后将数据缓存到本地
     * /88888888888888888888888888888/
     *
     *  /1111111111111111 策略2 111111111111111/
     *
     *  先从本地获取缓存数据,-----数据库 -- getcontact----然后将数据给view层,让view 进行更新.
     *  再去环信的服务器去拿数据 ---服务器
     *  将数据添加到缓存中,然后进行ui的刷新 --数据库 --update
     *  /1111111111111111111111111111111/
     *
     */
    @Override
    public void initContact() {
        //从服务器获取当前正在使用的用户
        String currentname =EMClient.getInstance().getCurrentUser();
        Log.e("result", "initContact: " +currentname );

        //从此用户获取所有的联系人
        List<String> contact = DBUtils.getContact(currentname);

        //清空该集合,添加数据到集合中
        contactLists.clear();;
        contactLists.addAll(contact);

        //view层进行初始化
        mIContactFragmentView.onInitContact(contactLists);
       //更新联系人
        updateContacts(currentname);

    }


    private void updateContacts(final String currentname) {
        //开启子线程去环信服务器中去取数据
//        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                try {
        ////                    List<EMContact> fromServer = EMClient.getInstance()
        ////                                                               .getRobotsFromServer();
        //                    List<String> fromServer = EMClient.getInstance()
        //                                                                 .contactManager()
        //                                                                 .getAllContactsFromServer();
        //                    //清空集合
        //                    //添加到集合中
        //                    //数据库进行数据的更新
        //                    //通知view 主线程进行ui的更新
        //
        //
        //
        //                } catch (HyphenateException e) {
        //                    e.printStackTrace();
        //                }
        //
        //            }
        //        }).start();

        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance()
                                                                 .contactManager()
                                                                 .getAllContactsFromServer();

                    Log.e("result", "run: "+allContactsFromServer.toString() );
                    //添加之前进行排序
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return o1.compareTo(o2);
                        }
                    });




                    //通知view 层去更新数据并且进行ui的刷新---注意这是有异常的,所以需要分情况
                    //将原先到的集合清空,
                    contactLists.clear();

                    //将从服务器获取的数据添加到集合中,
                    contactLists.addAll(allContactsFromServer);
                    //更新数据库----因为从 1服务器去拿数据,势必导致数据库中的数据发生变化,因此就需要去更新数据库
                    DBUtils.updateContact(currentname,contactLists);

                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mIContactFragmentView.updateContacts(true,null);
                        }
                   });


                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            mIContactFragmentView.updateContacts(false,e.getMessage());
                        }
                    });

                }


            }
        });



    }

    /**
     * 更新用户联系人,
     */
    @Override
    public void updateUserContacts() {
        updateContacts(EMClient.getInstance().getCurrentUser());
    }


}
