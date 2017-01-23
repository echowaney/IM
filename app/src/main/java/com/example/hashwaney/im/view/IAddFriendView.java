package com.example.hashwaney.im.view;

import com.example.hashwaney.bean.User;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/23.
 */

public interface IAddFriendView {
    void onSearchSuccess(String currentUser, boolean b, String msg, List<User> list);

    void onAddFriendFormService(String username, boolean b,  String msg);
}
