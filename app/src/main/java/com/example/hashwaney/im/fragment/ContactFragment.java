package com.example.hashwaney.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hashwaney.im.MainActivity;
import com.example.hashwaney.im.R;
import com.example.hashwaney.im.activity.ChatActivity;
import com.example.hashwaney.im.adapter.ContactFragmentAdapter;
import com.example.hashwaney.im.base.BaseFragment;
import com.example.hashwaney.im.event.OnContactEvent;
import com.example.hashwaney.im.presenter.IContactFragmetPresenter;
import com.example.hashwaney.im.presenter.impl.ContactFragmentPresenter;
import com.example.hashwaney.im.view.IContactFragmentView;
import com.example.hashwaney.im.widget.ContactLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactFragment
        extends BaseFragment
        implements IContactFragmentView, SwipeRefreshLayout.OnRefreshListener,
                   ContactFragmentAdapter.OnItemClickListener
{

    private ContactLayout            mContactView;
    private ContactFragmentAdapter   mAdapter;
    private IContactFragmetPresenter mIContactFragmetPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //找到我们这个控件 --肯定有作用的
        mContactView = (ContactLayout) view.findViewById(R.id.contactview);
        //进行联系人的初始化 ---- 交给p层
        mIContactFragmetPresenter  = new ContactFragmentPresenter(this);
        mIContactFragmetPresenter.initContact();
        mContactView.setOnRefreshListener(this);
        EventBus.getDefault().register(this);


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public  void  onEvent(OnContactEvent contactEvent){
        //重新从网上请求数据
        Log.d("result", "onEvent: 执行了吗");
        mIContactFragmetPresenter.updateUserContacts(); //接收事件

    }


    //数据源 已经拿到了,接下来就是视图的加载问题了.
    @Override
    public void onInitContact(List<String> contactLists) {
        //数据传输到了adapter
        mAdapter = new ContactFragmentAdapter(contactLists);
        mContactView.setAdapter(mAdapter);
        mAdapter.setOnItemLongClickListener(this);

    }

    @Override
    public void updateContacts(boolean b, String message) {
       //更新adapter
        mAdapter.notifyDataSetChanged();
        //隐藏下拉刷新
        mContactView.setRefreshing(false);
    }

    @Override
    public void onDeleteContact(String contact, String msg, boolean isDel) {

        if (isDel){
            Toast.makeText(getActivity(), "被干掉了", Toast.LENGTH_SHORT)
                 .show();

        }else {
            Toast.makeText(getActivity(),"么有",Toast.LENGTH_LONG).show();

        }



    }

    @Override
    public void onRefresh() {
        /**
         * 1.从网络上去拿数据
         * 2.将数据保存到本地数据库
         * 3.更新ui
         * 4.隐藏下拉刷新
         * 前面三步是我们去请求
         */
        mIContactFragmetPresenter.updateUserContacts();


    }

    //反注册eventbus

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onItemLongClick(final String contact, int position) {

        Snackbar
                .make(mContactView,"确定删除"+contact,Snackbar.LENGTH_LONG)
                .setAction("sure", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getActivity(), "你被干掉了", Toast.LENGTH_SHORT)
//                             .show();


                        //删除逻辑还是得p来执行

                    mIContactFragmetPresenter.deleteContacts(contact);

                    }
                })

                .show();


    }

    //点击通讯录,跳转到聊天界面
    @Override
    public void onItemClick(String contact, int position) {
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(ChatActivity.class,false,contact);

    }
}
