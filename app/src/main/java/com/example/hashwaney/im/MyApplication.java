package com.example.hashwaney.im;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.hashwaney.im.adapter.MessageListenerAdapter;
import com.example.hashwaney.im.db.DBUtils;
import com.example.hashwaney.im.event.OnContactEvent;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * 进行数据的初始化，定义在全局
 */

public class MyApplication
        extends Application
{

    @Override
    public void onCreate() {
        super.onCreate();

        initHuanxin();
        initBomb();
        //初始化数据库
        initDb();


    }


    //初始化环信
    private void initHuanxin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int    pid            = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }


        //初始化
        EMClient.getInstance()
                .init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance()
                .setDebugMode(true);


        //初始化一个好友管理事件监听
        initContactManageListener();
        //初始化消息接收事件
        initMessageReceive();


    }


    private void initContactManageListener() {

        EMClient.getInstance()
                .contactManager()
                .setContactListener(new EMContactListener() {
                    @Override
                    public void onContactAdded(String username) {
                        EventBus.getDefault()
                                .post(new OnContactEvent(username, true));
                    }

                    @Override
                    public void onContactDeleted(String username) {
                        EventBus.getDefault()
                                .post(new OnContactEvent(username, false));

                    }

                    @Override
                    public void onContactInvited(String username, String reason) {
                        //接收好友邀请 通过环信服务器
                        try {
                            EMClient.getInstance()
                                    .contactManager()
                                    .acceptInvitation(username);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onContactAgreed(String username) {

                    }

                    @Override
                    public void onContactRefused(String username) {

                    }
                });


    }

    //该方法用来初始化环信接收的信息
    private void initMessageReceive() {
        EMClient.getInstance()
                .chatManager()
                .addMessageListener(new MessageListenerAdapter(){
                    @Override
                    public void onMessageReceived(List<EMMessage> list) {
                        super.onMessageReceived(list);

                        //环信将接收到的消息推送给应用
                        Log.d(TAG, "onMessageReceived: "+list.get(0));
                        if (list !=null && list.size()>0)
                            EventBus.getDefault().post(list.get(0));

                    }
                });

    }


    private String getAppName(int pID) {
        String          processName = null;
        ActivityManager am          = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List            l           = am.getRunningAppProcesses();
        Iterator        i           = l.iterator();
        PackageManager  pm          = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initDb() {
        DBUtils.initDB(this);

    }

    private void initBomb() {
        //第一：默认初始化
        Bmob.initialize(this, "ca81373740bd7f2cc8af4d0f5fbe851c");

    }

}
