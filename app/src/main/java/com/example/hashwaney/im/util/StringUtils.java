package com.example.hashwaney.im.util;

import android.text.TextUtils;

/**

    进行字符串的操作

 */

public class StringUtils {

    public static boolean checkUserName(String username){
        if (TextUtils.isEmpty(username)){
            return false;

        }

        return username.matches("^[a-zA-Z]\\w{2,19}$");
    }
    public static boolean checkPwd(String pwd){
        if (TextUtils.isEmpty(pwd)){
        return false;

    }
    return pwd.matches("^[0-9]\\d{2,19}$");


    }
}
