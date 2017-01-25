package com.example.hashwaney.im.view;
import com.hyphenate.chat.EMConversation;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/25.
 */

public interface IConversationView {

    //初始化我们的聊天视图
    void initConversationView(List<EMConversation> emConversationList);

}
