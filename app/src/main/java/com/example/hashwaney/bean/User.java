package com.example.hashwaney.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by HashWaney on 2017/1/18.
 */
public class User extends BmobObject {


    private String username;
    private String pwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
