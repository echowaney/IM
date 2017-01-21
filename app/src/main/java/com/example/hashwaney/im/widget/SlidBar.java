package com.example.hashwaney.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.hyphenate.util.DensityUtil;


/**
 * Created by HashWaney on 2017/1/21.
 */

public class SlidBar
        extends TextView
{
    private String[] queryTitles ={
            "搜",
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I",
            "J",
            "K",
            "L",
            "M",
            "N",
            "O",
            "P",
            "Q",
            "R",
            "S",
            "T",
            "U",
            "V",
            "W",
            "X",
            "Y",
            "Z",
                };

    private int mSlidViewW;
    private int mSlidViewH;
    private Paint mPaint;

    public SlidBar(Context context) {
        super(context);
    }

    public SlidBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化一个画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔的颜色
        mPaint.setColor(getResources().getColor(R.color.text_query_color));
        //注意一下,代码中指定的是像素,也就是说s转化为px 在屏幕适配中会出现问题,因此在这里需要进行转换



        mPaint.setTextSize(DensityUtil.sp2px(getContext(),10));
        mPaint.setTextAlign(Paint.Align.CENTER);//指定位置,如果不指定,则为默认的以文字的左上角为起始坐标


    }

    public SlidBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlidBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//   TODO
        /**
         * 1.去测量控件的宽高
         * 2.根据测量得到的宽高去计算每一个视图的坐标
         * 3.
         */
        mSlidViewH = getMeasuredHeight();
        mSlidViewW = getMeasuredWidth();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//   TODO,将27字符画在我们的自定义view上
        float x =mSlidViewW/2;  //x的平局值
        float y =mSlidViewH/queryTitles.length;//y的平局值
        for (int i = 0; i <queryTitles.length ; i++) {
            canvas.drawText(queryTitles[i],x,y+y*i,mPaint);
        }


    }
}
