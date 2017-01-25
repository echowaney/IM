package com.example.hashwaney.im.presenter;

/**
 * Created by HashWaney on 2017/1/23.
 */

public interface IChatPresenter {
    //初始化
    void onInitConversation(String contact);


    //更新消息
    void updateMessage(String contact);


    void onSendMessage(String content, String username);
}
