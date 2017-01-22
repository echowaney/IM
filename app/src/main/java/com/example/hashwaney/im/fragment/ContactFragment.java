package com.example.hashwaney.im.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.ContactFragmentAdapter;
import com.example.hashwaney.im.base.BaseFragment;
import com.example.hashwaney.im.presenter.impl.ContactFragmentPresenter;
import com.example.hashwaney.im.view.IContactFragmentView;
import com.example.hashwaney.im.widget.ContactView;

import java.util.List;

/**
 * Created by HashWaney on 2017/1/21.
 */

public class ContactFragment
        extends BaseFragment
        implements IContactFragmentView
{

    private ContactView mContactView;
    private ContactFragmentPresenter mContactFragmentPresenter;
    private ContactFragmentAdapter mAdapter;

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
        mContactView = (ContactView) view.findViewById(R.id.contactview);
        //进行联系人的初始化 ---- 交给p层
        mContactFragmentPresenter=new ContactFragmentPresenter(this);
        mContactFragmentPresenter.initContact();


    }
    //数据源 已经拿到了,接下来就是视图的加载问题了.
    @Override
    public void onInitContact(List<String> contactLists) {
        //数据传输到了adapter
        mAdapter = new ContactFragmentAdapter(contactLists);
        //接下来就是将数据绑定到视图上
//        setadapter -----将父容器进行一次包装,父容器中有了孩子recycleview
        //相应的将此功能包装到父容器中

        mContactView.setAdapter(mAdapter);

    }

    @Override
    public void updateContacts(boolean b, String message) {
       //更新adapter
        mAdapter.notifyDataSetChanged();

    }
}
