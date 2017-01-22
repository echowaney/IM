package com.example.hashwaney.im.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

public class ContactView
        extends RelativeLayout
{

    private RecyclerView mRecyclerView;
    private SlidBar mSlidBar;
    private TextView mTextView;

    //次构造是在代码中使用
    public ContactView(Context context) {
        this(context,null);
    }

    //此构造是在布局中使用
    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.contact_view, this, true);
        mSlidBar      =  (SlidBar) findViewById(R.id.slidbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        mTextView = (TextView) findViewById(R.id.tv_name);


    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setAdapter(ContactFragmentAdapter adapter){

        //recycleview的特殊性
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
    }
}
