package com.example.hashwaney.im.presenter.impl;

import android.util.Log;

import com.example.hashwaney.im.listener.HMCallbackListener;
import com.example.hashwaney.im.presenter.IChatPresenter;
import com.example.hashwaney.im.view.IChatView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by HashWaney on 2017/1/23.
 */

public class ChatPresenter
        implements IChatPresenter

{

    //声明一个集合 用来装消息
    private List<EMMessage> mEMMessagesLists = new ArrayList<>();
    private IChatView mIChatView;

    public ChatPresenter(IChatView iChatView) {mIChatView = iChatView;}

    @Override
    public void onInitConversation(String contact) {
        updateData(contact);


        //通知view层去初始化聊天视图
        mIChatView.onInitChatView(mEMMessagesLists);

    }

    private void updateData(String contact) {//获取到聊天记录
        EMConversation conversation = EMClient.getInstance()
                                              .chatManager()
                                              .getConversation(contact);
        Log.d("result", "onInitConversation: " + conversation);
        if (conversation != null) {

            //     conversation.getAllMessages();=----只能获取单条数据
            EMMessage lastMessage = conversation.getLastMessage();
            String    msgId       = lastMessage.getMsgId();
            //经过测试,发现当信息超过二十条,不能查看以前的聊天记录
            int count = 19;
            if (mEMMessagesLists.size() >= 19) {
                count = mEMMessagesLists.size();
            }


            List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(msgId, count);
            Log.d("result", "onInitConversation: " + emMessages.toString());
            Collections.reverse(emMessages);
            mEMMessagesLists.clear();
            mEMMessagesLists.add(lastMessage);
            mEMMessagesLists.addAll(emMessages);
            Collections.reverse(mEMMessagesLists);

            //            mEMMessagesLists.addAll(emMessages);
            //            mEMMessagesLists.add(lastMessage);
        } else {
            mEMMessagesLists.clear();
        }
    }

    //发送消息
    @Override
    public void onSendMessage(String content, String username) {
        //        //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        //给消息设置一个状态,就是为了用户的体验,当网络不好,或者没有网络,就需要给消息设置和一个状态,设置一个加载中的状态

        message.setStatus(Status.INPROGRESS);

        mEMMessagesLists.add(message);
        mIChatView.onUpdateChatView(mEMMessagesLists.size() - 1);

        message.setMessageStatusCallback(new HMCallbackListener() {
            @Override
            public void onMainSuccess() {
                //当消息状态是发送成功的,重新更新一下视图
                mIChatView.onUpdateChatView(mEMMessagesLists.size() - 1);
            }

            @Override
            public void onMaiError(int i, String s) {
                mIChatView.onUpdateChatView(mEMMessagesLists.size()-1);

            }
        });
        //
        //        //发送消息
        EMClient.getInstance()
                .chatManager()
                .sendMessage(message);
        //
        //view视图更新
            //当视图更新的时候,

    }

    //重新去从会话中去取数据
    @Override
    public void updateMessage(String contact) {
        updateData(contact);
        //通知view层更新
        mIChatView.onUpdateChatView(mEMMessagesLists.size() - 1);
    }
}
