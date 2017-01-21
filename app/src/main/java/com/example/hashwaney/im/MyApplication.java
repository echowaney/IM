package com.example.hashwaney.im;

import android.app.ActivityManager;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.hashwaney.im.db.DBUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

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

    private void initDb() {
        DBUtils.initDB(this);



    }

    private void initBomb() {
        //第一：默认初始化
        Bmob.initialize(this, "ca81373740bd7f2cc8af4d0f5fbe851c");

    }

    //初始化环信
    private void initHuanxin() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }


        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);


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
}
