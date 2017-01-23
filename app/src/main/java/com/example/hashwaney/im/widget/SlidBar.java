package com.example.hashwaney.im.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.IContactAdapter;
import com.example.hashwaney.im.util.StringUtils;
import com.hyphenate.util.DensityUtil;

import java.util.List;


/**
 * Created by HashWaney on 2017/1/21.
 */

public class SlidBar
        extends TextView
{
    private String[] queryTitles = {"搜",
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
                                    "Z",};


    private Paint        mPaint;
    private int          mAvgWidth;
    private int          mAvgHeight;
    private SlidBar      mSlidBar;
    private TextView     mTvFloate;
    private RecyclerView mRecycle;

    public SlidBar(Context context) {
        super(context);
    }

    public SlidBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化一个画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置画笔的颜色
        mPaint.setColor(Color.parseColor("#ff0000")); //这里使用颜色解析,发现资源文件中定义的颜色不生效
        //注意一下,代码中指定的是像素,也就是说s转化为px 在屏幕适配中会出现问题,因此在这里需要进行转换
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
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

        /**
         * 1.去测量控件的宽高
         * 2.根据测量得到的宽高去计算每一个视图的坐标
         * 3.
         */
        int mSlidViewW = getMeasuredWidth();
        int mSlidViewH = getMeasuredHeight();

        //计算平均值
        mAvgWidth = mSlidViewW / 2;
        mAvgHeight = mSlidViewH / queryTitles.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < queryTitles.length; i++) {
            canvas.drawText(queryTitles[i], mAvgWidth, mAvgHeight * (i + 1), mPaint);
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                /**
                 * 1.换背景----灰色背景
                 * 2.显示悬浮的textview ---M
                 * 3.给TextVIew设置文本
                 */

            case MotionEvent.ACTION_MOVE:
                /**
                 * 1.显示TextVIew
                 * 2.给TextVIew设置文本
                 * 3.让recycleview回滚到slidbar指定的位置
                 */
                setBackgroundResource(R.drawable.slidebar_bg);
                //                mTvFloate.setVisibility(VISIBLE);
                showFloateTitleAndScrollRecycleView(event.getY());


                break;

            case MotionEvent.ACTION_UP:
                /**
                 * 1.换背景---原来的背景,
                 * 2.隐藏TextVIew
                 */
                setBackgroundColor(Color.TRANSPARENT);
                mTvFloate.setVisibility(GONE);

                break;
        }


        return true;
    }

    private void showFloateTitleAndScrollRecycleView(float y) {
        //通过判断高度来计算相应的角标
        //规定y的取值范围
        //通过y来获取哪一个角标
        int index = (int) (y / mAvgHeight);
        if (index < 0) {
            index = 0;
        } else if (index > queryTitles.length - 1) {
            index = queryTitles.length - 1;
        }
        //  Log.d("result", "showFloateTitleAndScrollRecycleView:  index "+index+"  y "+y);
        ViewGroup parent = (ViewGroup) getParent();

        if (mTvFloate == null || mRecycle == null) {

            mTvFloate = (TextView) parent.findViewById(R.id.tv_floate);
            mRecycle = (RecyclerView) parent.findViewById(R.id.recycleview);
        }
        mTvFloate.setVisibility(VISIBLE);

        String section = queryTitles[index];

        mTvFloate.setText(section);

        RecyclerView.Adapter adapter = mRecycle.getAdapter();//获取到一个adapter

        if (adapter instanceof IContactAdapter) {      //通过adapter去获取数据，面向接口编程，只要是这个接口的子类，都可以通过强转成接口对象
            //可以使用这个接口中定义好的方法。

            IContactAdapter contactAdapter = (IContactAdapter) adapter;

            List<String> data = contactAdapter.getData();
            for (int i = 0; i < data.size(); i++) {
                String contacts = data.get(i);
                if (section.equals(StringUtils.getSearchTitle(contacts))) {
                    mRecycle.smoothScrollToPosition(i);
                }
            }
        }
    }
}
