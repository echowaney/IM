package com.example.hashwaney.im.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.hashwaney.im.MainActivity;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.AnimatorListenerAdapter;
import com.example.hashwaney.im.base.BaseActivity;
import com.example.hashwaney.im.presenter.SplashPresenter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity
        extends BaseActivity implements ISplashView
{
    private static final long DURATION = 2000;
    private SplashPresenter mSplashPresenter;
    @InjectView(R.id.iv_splash)
    ImageView mIvSplash;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        context =this;

        //1，判断是否登陆，。
        mSplashPresenter =new SplashPresenter(this);
        mSplashPresenter.onCheckLogin();
    }

    @Override
    public void onCheckLogined(boolean isLogined) {
        if (isLogined){
            //2.如果登陆 直接进入到mainactivity
            Intent intent
                    =new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();


        }else{
            //3.没有登陆，闪屏2秒，
            //  闪屏可以加上一个渐变动画
            // 进入到loginactivity
            ObjectAnimator alpha =ObjectAnimator.ofFloat(mIvSplash,"alpha",0.0f,1.0f);
            alpha.setDuration(DURATION);
            alpha.start();
            alpha.addListener(new AnimatorListenerAdapter(){
                @Override
                public void onAnimationEnd(Animator animation) {
                    //动画结束后，进入到一个登陆界面
//                    Intent intent =new Intent(context,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                    startActivity(LoginActivity.class,true);

                }
            });


        }


    }
}
