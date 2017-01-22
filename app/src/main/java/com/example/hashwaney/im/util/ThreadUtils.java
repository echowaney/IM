package com.example.hashwaney.im.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 其实这个方法就是通过线程池来进行任务的执行
 */

public class ThreadUtils {

    private  static Handler  mHandler  =new Handler(Looper.getMainLooper());
    private static  Executor mExecutor = Executors.newSingleThreadExecutor();

    public static void runOnMainThread(Runnable runnable){


//                new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }
//
//        ).start();
//        mExecutor.execute(mHandler.getLooper());

        mHandler.post(runnable);

    }
    public static void runOnSubThread(Runnable runable){

        mExecutor.execute(runable);

    }

}
