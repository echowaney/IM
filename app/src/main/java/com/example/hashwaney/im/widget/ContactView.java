package com.example.hashwaney.im.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hashwaney.im.R;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactView
        extends RelativeLayout
{

    //次构造是在代码中使用
    public ContactView(Context context) {
        super(context);
    }

    //此构造是在布局中使用
    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View contactView = LayoutInflater.from(context)
                                         .inflate(R.layout.contact_view, this, true);
        SlidBar      slidBar      = (SlidBar) contactView.findViewById(R.id.slidbar);
        RecyclerView recyclerView = (RecyclerView) contactView.findViewById(R.id.recycleview);
        TextView     textView     = (TextView) contactView.findViewById(R.id.tv_name);


    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
