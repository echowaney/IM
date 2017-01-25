package com.example.hashwaney.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hashwaney.im.R;
import com.example.hashwaney.im.adapter.ChatAdapter;
import com.example.hashwaney.im.presenter.IChatPresenter;
import com.example.hashwaney.im.presenter.impl.ChatPresenter;
import com.example.hashwaney.im.view.IChatView;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity
        extends AppCompatActivity
        implements TextWatcher, IChatView
{


    private IChatPresenter mIChatPresenter;


    @InjectView(R.id.tv_title)
    TextView     mTvTitle;
    @InjectView(R.id.toolbar)
    Toolbar      mToolbar;
    @InjectView(R.id.recycleview)
    RecyclerView mRecycleview;
    @InjectView(R.id.et_msg)
    EditText     mEtMsg;
    @InjectView(R.id.btn_send)
    Button       mBtnSend;
    private String mUsername;
    private ChatAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        //初始化toolbar
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //初始化标题栏
        mUsername = getIntent().getStringExtra("username");
        if (TextUtils.isEmpty(mUsername)) {
            Toast.makeText(this, "竟然传空数据...", Toast.LENGTH_SHORT)
                 .show();

            System.exit(0); //退出虚拟机
            //            finish();
            return;

        }
        mTvTitle.setText("与" + mUsername + "聊天中");

        //初始化button的状态,如果有文本,就显示可用,并没有文本显示不可用

        String msg = mEtMsg.getText()
                           .toString();
        if (msg.length() == 0) {
            mBtnSend.setEnabled(false);
        } else {
            mBtnSend.setEnabled(true);
        }
        //对文本进行监听
        mEtMsg.addTextChangedListener(this);


        /**
         * 显示最近最多20条数据
         *
         */
         mIChatPresenter = new ChatPresenter(this);
         mIChatPresenter.onInitConversation(mUsername);


        //一定要注册eventbus
        EventBus.getDefault().register(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }


        return true;

    }

    @OnClick(R.id.btn_send)
    public void onClick() {
        //TODO 点击发送 ,将消息发送出去
        String content =mEtMsg.getText().toString().trim();
        if (TextUtils.isEmpty(content)){
            return ;

        }
        mIChatPresenter.onSendMessage(content,mUsername);
        //清空edittext上的文本
        mEtMsg.getText().clear();



    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //需要通知我的p层,,这里不需要传入参数,因为此时数据已经插入到数据库中
        String from = emMessage.getFrom();
        //判断消息是否来自正在和我聊天的用户,不是就不执行以下方法
        if (from.equals(mUsername))
                mIChatPresenter.updateMessage(mUsername);

    }

    //文本变化的时候之前
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //文本变化
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //文本变化之后,button可用,
    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString()
             .length() == 0)
        {
            //说明没有文本
            mBtnSend.setEnabled(false);

        } else {
            mBtnSend.setEnabled(true);
        }

    }

    @Override
    public void onInitChatView(List<EMMessage> emMessagesLists) {
        //将数据设置给recycleview
        //Log.d("result", "onInitConversation: "+emMessagesLists.toString());
        mAdapter = new ChatAdapter(emMessagesLists);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mRecycleview.setAdapter(mAdapter);
        if(emMessagesLists.size()!=0){
        //让用户一进入聊天界面就可以看到最近的聊天记录,所以让adapter滚动到最后一条
            //TODO 直接显示最后一条数据 ,体验更好
//        mRecycleview.smoothScrollToPosition(emMessagesLists.size()-1);
            mRecycleview.scrollToPosition(emMessagesLists.size()-1);

        }

    }

    @Override
    public void onUpdateChatView(int position) {
        //让Adapter 更新一下
        mAdapter.notifyDataSetChanged();
        //并且让recycleview滚动最后一条
        mRecycleview.smoothScrollToPosition(position);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
