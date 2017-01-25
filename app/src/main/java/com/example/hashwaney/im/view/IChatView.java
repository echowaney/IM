package com.example.hashwaney.im.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/23.
 */

public interface IChatView {

    //接收到环信服务器返回的聊天记录
    void onInitChatView(List<EMMessage> emMessagesLists);
    //更新从服务器上接收的数据
    void onUpdateChatView(int position);

}
