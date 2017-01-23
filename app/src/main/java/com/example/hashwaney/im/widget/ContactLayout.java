package com.example.hashwaney.im.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.ContactFragmentAdapter;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactLayout
        extends RelativeLayout
{

    private RecyclerView mRecyclerView;
    private SlidBar mSlidBar;
    private TextView mTextView;
    private SwipeRefreshLayout mSwipeRefresh;

    //次构造是在代码中使用
    public ContactLayout(Context context) {
        this(context,null);
    }

    //此构造是在布局中使用
    public ContactLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.contact_view, this, true);
        mSlidBar      =  (SlidBar) findViewById(R.id.slidbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mTextView = (TextView) findViewById(R.id.tv_floate);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        //设置下来刷新的颜色
        mSwipeRefresh.setColorSchemeColors(Color.RED,Color.GRAY,Color.GREEN);



    }

    public ContactLayout(Context  context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setAdapter(ContactFragmentAdapter adapter){

        //recycleview的特殊性
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }

    //代理思想- 进行刷新的监听
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener){
        mSwipeRefresh.setOnRefreshListener(listener);

    }
    //进度条的显示和隐藏
    public void setRefreshing(boolean isRefresh){

        mSwipeRefresh.setRefreshing(isRefresh);

    }
}
