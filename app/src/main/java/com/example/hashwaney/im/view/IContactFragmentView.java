package com.example.hashwaney.im.view;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/21.
 */

public interface IContactFragmentView {
    //初始化联系人
    void onInitContact(List<String> contactLists);
    //更新联系人
    void updateContacts(boolean b, String message);
}
