package com.example.hashwaney.im.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hashwaney.im.MainActivity;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.activity.ChatActivity;
import com.example.hashwaney.im.adapter.ConversationAdapter;
import com.example.hashwaney.im.base.BaseFragment;
import com.example.hashwaney.im.presenter.IConversationPresenter;
import com.example.hashwaney.im.presenter.impl.ConverstationPresenter;
import com.example.hashwaney.im.view.IConversationView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ConversationFragment
        extends BaseFragment
        implements IConversationView, View.OnClickListener, ConversationAdapter.OnItemClickListener
{

    private RecyclerView           mRecycleview;
    private FloatingActionButton   mFab;
    private IConversationPresenter mIConversationPresenter;
    private ConversationAdapter    mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycleview = (RecyclerView) view.findViewById(R.id.recycleview);
        mRecycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);

        /**
         * 1.初始化会话列表
         */
        mIConversationPresenter = new ConverstationPresenter(this);
        mIConversationPresenter.initConverstation();
        //2.给fab设置点击事件
        mFab.setOnClickListener(this);

        //通过eventbus 来接收消息----用户会话发生变化了 //TODO Fragment 和 Activity的级别是一样的
        EventBus.getDefault()
                .register(this);
        //  Log.d("result", "onViewCreated: this = "+ this  +     (this.equals(getActivity()))   +"  getActivity() =  "+getActivity().toString());

    }


    @Override
    public void onClick(View v) {
        //TODO 点击按钮将未读消息全部标记为已读
        //        mFab.setRotation();
        ObjectAnimator.ofFloat(mFab, "rotation", 0f, 360f)
                      .setDuration(500)
                      .start();
        //将消息标记为已
        // EMClient.
        EMClient.getInstance()
                .chatManager()
                .markAllConversationsAsRead();
        //通知Fragment界面刷新
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();

        }

        //通知MainActivity界面进行刷新
        MainActivity activity = (MainActivity) getActivity();
        activity.updateBadgeItemCount();


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage) {
        //接收到这个消息 ,然后进行更新数据
        //重新去环信的数据中找数据
        //在初始化的时候,就已经去数据库中找了数据
        //可以将初始化这个方法重新调用一下
        mIConversationPresenter.initConverstation();
        //TODO 这里如果直接调用  mAdapter.notifyDataSetChanged()是只对老的会话生效的, 如果又有新的用户给你发消息 是不会显示的.
      //  mAdapter = null;
        //        aaa
    }

    @Override
    public void onStart() {
        super.onStart();
        mIConversationPresenter.initConverstation();
    }

    @Override
    public void onResume() {
        super.onResume();
        //更新一下adapter
        if (mAdapter != null) {

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void initConversationView(List<EMConversation> emConversationList) {
       // if (mAdapter == null) {
            mAdapter = new ConversationAdapter(emConversationList);
            mRecycleview.setAdapter(mAdapter);
            mAdapter.setOnIntemClick(this);
        //} else {
           // mAdapter.notifyDataSetChanged();//那就更新一下数据 //TODO 这里有一个bug 就是把这里注释掉 就可以展示出来
        //}


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault()
                .unregister(this);
    }

    @Override
    public void onItemClick(String username) {
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(ChatActivity.class, false, username);

    }
}
