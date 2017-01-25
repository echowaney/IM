package com.example.hashwaney.im;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.example.hashwaney.im.activity.ChatActivity;
import com.example.hashwaney.im.adapter.MessageListenerAdapter;
import com.example.hashwaney.im.db.DBUtils;
import com.example.hashwaney.im.event.OnContactEvent;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
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

    private int mYuluSound;
    private int mDuanSound;
    private SoundPool mSoundPool;

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
        //初始化音乐池
        initSoundPool();
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
                        //给应用加上一个音效,如果在前台就发出一个短声音,在后台发出一个长声音
                      if (isRuninBackground()){
                          //播放长声音
                          /**
                           * 如果在后台,就发送一个通知
                           *
                           */
                          EMMessage    emMessage = list.get(0);


                          sendNotification(emMessage);
                          mSoundPool.play(mYuluSound,1,1,0,0,1);
                      }else {

                          //播放短声音
                          mSoundPool.play(mDuanSound,1,1,0,0,1);
                      }
                        if (list !=null && list.size()>0)
                            EventBus.getDefault().post(list.get(0));
                    }
                });
    }
    //发送一个通知
    private void sendNotification(EMMessage emMessage) {
        EMTextMessageBody body      = (EMTextMessageBody) emMessage.getBody();
     NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //意图数组 顺序为先加入的意图放在栈底,就意味着后执行
        Intent   chatIntent =new Intent(this, ChatActivity.class);
        Intent   mainIntent =new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        chatIntent.putExtra("username",emMessage.getUserName());
//        startActivity(chatIntent); ----因为是延时意图,这里不能startactivity
        Intent[] intents ={mainIntent, chatIntent};


        PendingIntent            intent =PendingIntent.getActivities(this, 1, intents, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =new Notification.Builder(this)
                .setContentText(body.getMessage())
                .setContentTitle("您有一条新消息")
                .setContentInfo(emMessage.getFrom())    //设置通知来自谁
                .setContentIntent(intent)   //设置通知的意图
                .setSmallIcon(R.mipmap.message) //设置小图标 一定要设置
                .setAutoCancel(true)        //点击了通知可以移除通知状态栏
                .setPriority(Notification.PRIORITY_MAX) //设置优先级,单锁屏了也可以接收到通知
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.default_avatar))
                .build();
        notificationManager.notify(1, notification);


    }

    private boolean isRuninBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        boolean equals = runningTaskInfo.topActivity.getPackageName()
                                                    .equals(getPackageName());
        if (equals){
            //说明是前台
            return false;
        }else {
            //后台
            return true;
        }

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

    private void initSoundPool() {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        //加载两个音乐
        mDuanSound = mSoundPool.load(this, R.raw.duan, 1);
        mYuluSound = mSoundPool.load(this, R.raw.yulu, 1);
    }

}
