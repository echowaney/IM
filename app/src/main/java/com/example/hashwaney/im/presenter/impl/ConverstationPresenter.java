package com.example.hashwaney.im.presenter.impl;

import com.example.hashwaney.im.presenter.IConversationPresenter;
import com.example.hashwaney.im.view.IConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 * Created by HashWaney on 2017/1/25.
 */

public class ConverstationPresenter
        implements IConversationPresenter

{

    private List<EMConversation> mEMConversationList =new ArrayList<>();
    private IConversationView mIConversationView;

    public ConverstationPresenter(IConversationView iConversationView) {
        mIConversationView = iConversationView;
    }


    @Override
    public void initConverstation() {
        //需要拿到会话
        /***
         * 参数1 :代表的是聊天的用户,并不是当前用户 查看了环信生成的数据库 ---有一个字段为username 收集了联系人(聊天对象)
         * 经过分析: 初始化会话,只需要拿到所有的会话即可,那么map中的string参数应用不到,那么就可以将map转换为list集合
         * 参数2: 会话: 与聊天用户的会话,这个可以干嘛 ---可以显示聊天的信息--聊天的时间--聊天的对象
         */

        Map<String,EMConversation> allConversations = EMClient.getInstance()
                                                                                  .chatManager()
                                                                                  .getAllConversations();

//        //username 是所有的联系人,---是从数据库获取的
//       EMConversation emConversation = allConversations.get("username");
//
      Collection<EMConversation> emConversations = allConversations.values();
//        Set<String>                strings         = allConversations.keySet();
        //定义一个集合 去装这个会话
     mEMConversationList.clear();//TODO 需不需先将集合清空呢  不清空的话是什么效果 以前的会话会进行保留, 清空的话就会去拿取最新的会话
       // mEMConversationList.add(emConversation);
        mEMConversationList.addAll(emConversations);
        /**
         *  将会话进行一个排序,将最近消息显示在最上面(倒序)
         */
        Collections.sort(mEMConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation o1,
                               EMConversation o2)
            {
                return (int) (o2.getLastMessage().getMsgTime()-o1.getLastMessage().getMsgTime());
            }
        });
       //通知view进行UI更新
        mIConversationView.initConversationView(mEMConversationList);



    }
}
